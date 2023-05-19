package com.org.codeverse.service;

import com.org.codeverse.dto.*;
import com.org.codeverse.enums.DriverCodeType;
import com.org.codeverse.enums.Language;
import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.enums.SubmissionStatus;
import com.org.codeverse.exception.ResourceNotFoundException;
import com.org.codeverse.model.*;
import com.org.codeverse.repository.*;
import com.org.codeverse.runner.QuestionRunner;
import com.org.codeverse.utils.RunQuestionUtil;
import com.org.codeverse.utils.SubmitQuestionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class QuestionService {
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    SubmissionRepository submissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private DriverCodeRepository driverCodeRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    public Question createQuestion(Question question) {
        if (question.getTestCases() != null) {
            question.getTestCases().forEach(testCase -> testCase.setQuestion(question));
        }

        if (question.getDriverCodes() != null) {
            question.getDriverCodes().forEach(driverCode -> driverCode.setQuestion(question));
        }

        if (question.getCodeSnippets() != null) {
            question.getCodeSnippets().forEach(codeSnippet -> codeSnippet.setQuestion(question));
        }

        if (question.getSolutions() != null) {
            question.getSolutions().forEach(solution -> solution.setQuestion(question));
        }

        if (question.getHints() != null) {
            question.getHints().forEach(hint -> hint.setQuestion(question));
        }

        if (question.getTopics() != null) {
            List<Topic> existingTopics = new ArrayList<>();
            for (Topic topic : question.getTopics()) {
                Optional<Topic> existingTopic = topicRepository.findBySlug(topic.getSlug());
                if (existingTopic.isPresent()) {
                    existingTopic.get().getQuestions().add(question);
                    existingTopics.add(topic);
                } else {
                    topic.getQuestions().add(question);
                }
            }
            question.getTopics().removeAll(existingTopics);
        }

        return questionRepository.save(question);
    }

    public AllQuestionResponseDTO getAllQuestions(
            Long userId, String keyword, Integer pageNumber, Integer pageSize) {
        List<QuestionRepository.QuestionInfo> allQuestions = questionRepository.findQuestionInfoByUserId(
                userId, keyword, pageNumber * pageSize, pageSize);
//        System.out.println("Offset " + pageNumber * pageSize);
        Long totalElements = questionRepository.getQuestionCount();

        AllQuestionResponseDTO responseDTO = new AllQuestionResponseDTO();
        responseDTO.setAllQuestions(allQuestions);
        responseDTO.setPageNumber(pageNumber);
        responseDTO.setPageSize(pageSize);
        responseDTO.setTotalElements(totalElements);
        responseDTO.setLast(Math.ceil(totalElements * 1.0 / pageSize) - 1 == pageNumber);
        return responseDTO;
    }

    public QuestionDetailsDTO getQuestionBySlug(String slug) {
        Question question = questionRepository.findBySlug(slug);
        if (question == null) {
            throw new ResourceNotFoundException("Question", "slug", slug);
        }

        int likes = question.getUsersLiked().size();
        int dislikes = question.getUsersDisLiked().size();
        int accepted = 0;
        int submissions = 0;
        double acceptanceRate = 0;

        for (Submission submission : question.getSubmissions()) {
            submissions++;
            if (submission.getResult() != null && submission.getResult().equals(SubmissionResult.ACCEPTED)) {
                accepted++;
            }
        }

        if (submissions > 0) {
            acceptanceRate = ((double) accepted / (double) submissions) * 100;
        }

        QuestionDetailsDTO questionDetailsDTO = new QuestionDetailsDTO();

        questionDetailsDTO.setId(question.getId());
        questionDetailsDTO.setTitle(question.getTitle());
        questionDetailsDTO.setSlug(question.getSlug());
        questionDetailsDTO.setDifficulty(question.getDifficulty());
        questionDetailsDTO.setLikes(likes);
        questionDetailsDTO.setDislikes(dislikes);
        questionDetailsDTO.setHints(question.getHints());
        questionDetailsDTO.setDescription(question.getDescription());
        questionDetailsDTO.setTestCases(question.getTestCases());
        questionDetailsDTO.setAccepted(accepted);
        questionDetailsDTO.setSubmissions(submissions);
        questionDetailsDTO.setAcceptanceRate(acceptanceRate);
        questionDetailsDTO.setTopics(question.getTopics());
        questionDetailsDTO.setCodeSnippets(question.getCodeSnippets());
        questionDetailsDTO.setSolutions(question.getSolutions());

        return questionDetailsDTO;

    }

    public SubmitQuestionResponseDTO submitQuestion(SubmitQuestionRequestDTO requestDTO) {
        UUID id = UUID.randomUUID();
        Long questionId = requestDTO.getQuestionId();
        String snippet = requestDTO.getSnippet();

        List<DriverCode> driverCodes = driverCodeRepository.findByQuestionId(questionId);
        List<TestCase> testCases = testCaseRepository.findByQuestionId(questionId);
        String inputFileName = SubmitQuestionUtil.writeTestCasesIntoFile(id.toString(), testCases);

        String fileName = SubmitQuestionUtil.makeFile(snippet, driverCodes.get(0).getCode(), inputFileName);
        String response = SubmitQuestionUtil.runFile(fileName);

        SubmitQuestionResponseDTO responseDTO = new SubmitQuestionResponseDTO();
        String[] parts = response.split("\n");

        responseDTO.setNumAccepted(Long.parseLong(parts[1]));
        responseDTO.setNumTestCases(Long.parseLong(parts[2]));
        responseDTO.setAvgRuntime(Double.parseDouble(parts[3]));
        responseDTO.setAvgMemoryUsed(Double.parseDouble(parts[4]));

        SubmissionResult submissionResult = null;
        if (parts[0].equals("Compilation Error")) {
            submissionResult = SubmissionResult.COMPILATION_ERROR;
        } else if (parts[0].equals("Runtime Error")) {
            submissionResult = SubmissionResult.RUNTIME_ERROR;
        } else {
            if (responseDTO.getNumAccepted() == responseDTO.getNumTestCases()) {
                submissionResult = SubmissionResult.ACCEPTED;
            } else {
                submissionResult = SubmissionResult.WRONG_ANSWER;
            }
        }

        responseDTO.setSubmissionResult(submissionResult);

        Submission submission = new Submission();
        submission.setStatus(SubmissionStatus.COMPLETED);
        submission.setResult(responseDTO.getSubmissionResult());
        submission.setLanguage(requestDTO.getLanguage());
        submission.setAvgRuntime(responseDTO.getAvgRuntime());
        submission.setAvgMemoryUsed(responseDTO.getAvgMemoryUsed());
        submission.setSnippet(requestDTO.getSnippet());

        Question question = questionRepository.findById(requestDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId",
                        requestDTO.getQuestionId().toString()));
        submission.setQuestion(question);
        question.getSubmissions().add(submission);

        User user = userRepository.findById(requestDTO.getUserId()).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId",
                        requestDTO.getQuestionId().toString()));

        submission.setUser(user);
        user.getSubmissions().add(submission);

        submissionRepository.save(submission);

        SubmitQuestionUtil.deleteFile(inputFileName);
        SubmitQuestionUtil.deleteFile(fileName);

        return responseDTO;
    }

    public RunQuestionResponseDTO runQuestion(RunQuestionRequestDTO requestDTO) throws IOException {
        List<String> inputs = requestDTO.getInputs();
        Long questionId = requestDTO.getQuestionId();
        Language language = requestDTO.getLanguage();
        String snippet = requestDTO.getSnippet();

        UUID id = UUID.randomUUID();
        String inputFileName = RunQuestionUtil.generateInputFile(id.toString(), inputs);

        String correctSnippet = solutionRepository.findSnippetByQuestionIdAndLanguage(questionId, language)
                .orElseThrow(() -> new ResourceNotFoundException("Solution", "questionId", questionId.toString()));
        String driverCode = driverCodeRepository.findCodeByQuestionIdAndLanguageAndType(questionId, language,
                        DriverCodeType.RUN)
                .orElseThrow(() -> new ResourceNotFoundException("DriverCode", "questionId", questionId.toString()));

        String fileName = RunQuestionUtil.generateJavaClassFile(driverCode, snippet, correctSnippet, inputFileName);

        String response = RunQuestionUtil.runJavaClassFile(fileName, inputs.size());

        RunQuestionResponseDTO responseDTO = new RunQuestionResponseDTO();
        String[] parts = response.split("\n");
        responseDTO.setResult(parts[0]);

        List<String> verdicts = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        List<String> expectedOutputs = new ArrayList<>();

        for (int i = 0; i < inputs.size(); i++) {
            verdicts.add(parts[3 * i + 1]);
            outputs.add(parts[3 * i + 2]);
            expectedOutputs.add(parts[3 * i + 3]);
        }

        responseDTO.setVerdicts(verdicts);
        responseDTO.setOutputs(outputs);
        responseDTO.setExpectedOutputs(expectedOutputs);

        responseDTO.setRuntime(Double.parseDouble(parts[parts.length - 1]));

        RunQuestionUtil.deleteFile(inputFileName);
        RunQuestionUtil.deleteFile(fileName);

        return responseDTO;

    }

    public RunQuestionResponseDTO runQuestionV2(RunQuestionRequestDTO requestDTO) throws IOException, InterruptedException {
        List<String> inputs = requestDTO.getInputs();
        Long questionId = requestDTO.getQuestionId();
        Language language = requestDTO.getLanguage();
        String snippet = requestDTO.getSnippet();

        Script script = scriptRepository.findByLanguage(language)
                .orElseThrow(() -> new ResourceNotFoundException("Script", "language", language.toString()));

        UUID id = UUID.randomUUID();
        QuestionRunner questionRunner = new QuestionRunner();
        String scriptFilename = RunQuestionUtil.generateScriptFile(script.getScript(), language);
        String inputFilename = questionRunner.generateInputFile(id, inputs);
        String outputFilename = questionRunner.generateOutputFile(id);
        String stdoutFilename = questionRunner.generateStdoutFile(id);

        String correctSnippet = solutionRepository.findSnippetByQuestionIdAndLanguage(questionId, language)
                .orElseThrow(() -> new ResourceNotFoundException("Solution", "questionId", questionId.toString()));
        String driverCode = driverCodeRepository.findCodeByQuestionIdAndLanguageAndType(questionId, language,
                        DriverCodeType.RUN)
                .orElseThrow(() -> new ResourceNotFoundException("DriverCode", "questionId", questionId.toString()));

        driverCode = driverCode
                .replace("{inputFilename}", inputFilename)
                .replace("{outputFilename}", outputFilename)
                .replace("{stdoutFilename}", stdoutFilename);
        correctSnippet = correctSnippet.replace("class Solution", "class CorrectSolution");
        String code = driverCode + "\n" + correctSnippet + "\n" + snippet;

        String executableFilename = questionRunner.generateExecutableFile(id, code);

        SubmissionResult submissionResult = questionRunner.runExecutableFile(scriptFilename, executableFilename);

        RunQuestionResponseDTO responseDTO = new RunQuestionResponseDTO();
        if(!submissionResult.equals(SubmissionResult.SUCCESSFUL)) {
            responseDTO.setResult(submissionResult.toString());
            questionRunner.deleteFile(inputFilename);
            questionRunner.deleteFile(outputFilename);
            questionRunner.deleteFile(stdoutFilename);
            questionRunner.deleteFile(executableFilename);
            return responseDTO;
        }

        BufferedReader reader = new BufferedReader(new FileReader(outputFilename));
        List<String> outputs = new ArrayList<>();
        List<String> expectedOutputs = new ArrayList<>();
        List<String> verdicts = new ArrayList<>();
        for(int i = 0; i < inputs.size(); i++) {
            verdicts.add(reader.readLine());
            outputs.add(reader.readLine());
            expectedOutputs.add(reader.readLine());
        }


        BufferedReader reader2 = new BufferedReader(new FileReader(stdoutFilename));
        List<String> stdout = new ArrayList<>();
        for(int i = 0; i < inputs.size(); i++) {
            stdout.add(reader2.readLine());
        }

        responseDTO.setStdout(stdout);


        responseDTO.setVerdicts(verdicts);
        responseDTO.setOutputs(outputs);
        responseDTO.setExpectedOutputs(expectedOutputs);
        responseDTO.setRuntime(Double.parseDouble(reader.readLine()));
        String result = String.valueOf(verdicts.stream().allMatch(verdict -> verdict.equals(SubmissionResult.ACCEPTED.toString()))
                ? SubmissionResult.ACCEPTED : SubmissionResult.WRONG_ANSWER);

        RunQuestionUtil.deleteFile(scriptFilename);
        questionRunner.deleteFile(inputFilename);
        questionRunner.deleteFile(outputFilename);
        questionRunner.deleteFile(stdoutFilename);
        questionRunner.deleteFile(executableFilename);

        responseDTO.setResult(result);
        return responseDTO;
    }

    public List<SubmissionRepository.SubmissionProjection> getSubmissionResultByUserIdAndQuestionIds(
            Long userId,
            List<Long> questionIds
    ) {
        return submissionRepository.findByUserIdAndQuestionIdIn(userId, questionIds);
    }

    public List<Submission> getSubmissionsByUserIdAndQuestionId(Long userId, Long questionId) {
        return submissionRepository.findByUser_IdAndQuestion_Id(userId, questionId);
    }

    public Map<String, Boolean> findUserQuestionReactions(Long questionId, Long userId) {
        Map<String, Boolean> userReaction = questionRepository.findUserQuestionReactions(questionId, userId);
        return userReaction;
    }

    public LikeQuestionResponseDTO likeQuestion(LikeQuestionRequestDTO requestDTO) {
        Long questionId = requestDTO.getQuestionId();
        Long userId = requestDTO.getUserId();

        boolean isLiked = false;

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId", questionId.toString()));
        List<User> usersLiked = question.getUsersLiked();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId.toString()));
        List<Question> likedQuestions = user.getLikedQuestions();

        if (usersLiked.contains(user)) {
            usersLiked.remove(user);
            likedQuestions.remove(question);

            isLiked = false;
        } else {
            usersLiked.add(user);
            likedQuestions.add(question);

            isLiked = true;
        }

        questionRepository.save(question);
        userRepository.save(user);

        LikeQuestionResponseDTO responseDTO = new LikeQuestionResponseDTO();
        responseDTO.setLiked(isLiked);
        responseDTO.setLikes(usersLiked.size());

        return responseDTO;
    }

    public DislikeQuestionResponseDTO dislikeQuestion(DislikeQuestionRequestDTO requestDTO) {
        Long questionId = requestDTO.getQuestionId();
        Long userId = requestDTO.getUserId();

        boolean disliked = false;

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId", questionId.toString()));
        List<User> usersDisLiked = question.getUsersDisLiked();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId.toString()));
        List<Question> disLikedQuestions = user.getDisLikedQuestions();

        if (usersDisLiked.contains(user)) {
            usersDisLiked.remove(user);
            disLikedQuestions.remove(question);

            disliked = false;
        } else {
            usersDisLiked.add(user);
            disLikedQuestions.add(question);

            disliked = true;
        }

        questionRepository.save(question);
        userRepository.save(user);

        DislikeQuestionResponseDTO responseDTO = new DislikeQuestionResponseDTO();
        responseDTO.setDisliked(disliked);
        responseDTO.setDislikes(usersDisLiked.size());

        return responseDTO;
    }

}
