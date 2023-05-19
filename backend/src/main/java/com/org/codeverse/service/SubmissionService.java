package com.org.codeverse.service;

import com.org.codeverse.dto.SubmitQuestionRequestDTO;
import com.org.codeverse.dto.SubmitQuestionResponseDTO;
import com.org.codeverse.enums.Language;
import com.org.codeverse.enums.SubmissionStatus;
import com.org.codeverse.exception.ResourceNotFoundException;
import com.org.codeverse.model.*;
import com.org.codeverse.producer.SubmissionProducer;
import com.org.codeverse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubmissionService {
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
    private SubmissionProducer submissionProducer;
    public SubmissionService(SubmissionProducer submissionProducer) {
        this.submissionProducer = submissionProducer;
    }

    public SubmitQuestionResponseDTO submitQuestion(SubmitQuestionRequestDTO requestDTO) {
        UUID id = UUID.randomUUID();
        Long questionId = requestDTO.getQuestionId();
        String snippet = requestDTO.getSnippet();
        Long userId = requestDTO.getUserId();
        Language language = requestDTO.getLanguage();

        Submission submission = new Submission();
        submission.setId(id.toString());
        submission.setStatus(SubmissionStatus.QUEUED);
        submission.setLanguage(language);
        submission.setSnippet(snippet);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId",
                        questionId.toString()));
        submission.setQuestion(question);
        question.getSubmissions().add(submission);

        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId",
                        questionId.toString()));

        submission.setUser(user);
        user.getSubmissions().add(submission);

        submissionProducer.sendJsonMessage(submission, questionId);
        submissionRepository.save(submission);

        SubmitQuestionResponseDTO responseDTO = new SubmitQuestionResponseDTO();
        responseDTO.setId(id.toString());

        return responseDTO;
    }

    public Submission submissionStatus(String submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", "submissionId", submissionId));
    }
}
