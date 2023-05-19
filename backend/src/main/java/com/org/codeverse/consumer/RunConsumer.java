package com.org.codeverse.consumer;

import com.org.codeverse.dto.SubmissionQueueDTO;
import com.org.codeverse.enums.Language;
import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.enums.SubmissionStatus;
import com.org.codeverse.exception.CompilationError;
import com.org.codeverse.exception.ResourceNotFoundException;
import com.org.codeverse.exception.TimeLimitExceededException;
import com.org.codeverse.model.*;
import com.org.codeverse.repository.DriverCodeRepository;
import com.org.codeverse.repository.SubmissionRepository;
import com.org.codeverse.repository.TestCaseRepository;
import com.org.codeverse.utils.RunQuestionUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class RunConsumer {
    @Autowired
    private DriverCodeRepository driverCodeRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    SubmissionRepository submissionRepository;
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeJsonMessage(SubmissionQueueDTO submissionQueueDTO) throws IOException {
        System.out.println("Received submission " + submissionQueueDTO.getSubmission().getId());

        Long questionId = submissionQueueDTO.getQuestionId();
        Submission submission = submissionQueueDTO.getSubmission();

        String snippet = submission.getSnippet();
        Language language = submission.getLanguage();
        String submissionId = submission.getId();

        List<DriverCode> driverCodes = driverCodeRepository.findByQuestionId(questionId);
        List<TestCase> testCases = testCaseRepository.findByQuestionId(questionId);
        String inputFilename = RunQuestionUtil.generateInputFileWithTestCases(submissionId.toString(), testCases);
        String stdoutFilename = submissionId.toString() + "-stdout.txt";
        String outputFilename = submissionId.toString() + "-output.properties";

        String driverCode = driverCodes.get(0).getCode();

        driverCode = driverCode.replace("{inputFilename}", inputFilename)
                .replace("{stdoutFilename}", stdoutFilename)
                .replace("{outputFilename}", outputFilename);

        String code = driverCode + "\n\n" + snippet;

        String executableFilename = RunQuestionUtil.generateExecutableFile(submissionId.toString(), code, ".java");

        Submission savedSubmission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", "submissionId", submissionId));

        try {
            String response = RunQuestionUtil.runFile("java_run.sh", executableFilename, inputFilename);
            String[] parts = response.split("\n");
            FileInputStream fis = new FileInputStream(outputFilename);
            Properties prop = new Properties();
            prop.load(fis);

            savedSubmission.setNumAccepted(Long.parseLong(prop.getProperty("numAccepted")));
            savedSubmission.setNumTestCases(Long.parseLong(prop.getProperty("totalTestcases")));
            savedSubmission.setAvgRuntime(Double.parseDouble(prop.getProperty("avgTimeTaken")));
            savedSubmission.setAvgMemoryUsed(Double.parseDouble(prop.getProperty("avgMemoryUsed")));

            SubmissionResult submissionResult = null;

            if (savedSubmission.getNumAccepted() == savedSubmission.getNumTestCases()) {
                submissionResult = SubmissionResult.ACCEPTED;
            } else {
                submissionResult = SubmissionResult.WRONG_ANSWER;
            }


            savedSubmission.setResult(submissionResult);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeLimitExceededException e) {
            savedSubmission.setNumAccepted(0L);
            savedSubmission.setNumTestCases(0L);
            savedSubmission.setAvgRuntime(0.0);
            savedSubmission.setAvgMemoryUsed(0.0);
            savedSubmission.setResult(SubmissionResult.TIME_LIMIT_EXCEEDED);
        } catch (CompilationError e) {
            savedSubmission.setNumAccepted(0L);
            savedSubmission.setNumTestCases(0L);
            savedSubmission.setAvgRuntime(0.0);
            savedSubmission.setAvgMemoryUsed(0.0);
            savedSubmission.setResult(SubmissionResult.COMPILATION_ERROR);
        } finally {
            RunQuestionUtil.deleteFile(inputFilename);
            RunQuestionUtil.deleteFile(executableFilename);
            RunQuestionUtil.deleteFile(stdoutFilename);
            RunQuestionUtil.deleteFile(outputFilename);

            savedSubmission.setStatus(SubmissionStatus.COMPLETED);
            submissionRepository.save(savedSubmission);
        }
    }
}
