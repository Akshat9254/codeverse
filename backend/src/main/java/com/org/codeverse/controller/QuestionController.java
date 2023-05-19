package com.org.codeverse.controller;

import com.org.codeverse.dto.*;
import com.org.codeverse.model.Question;
import com.org.codeverse.model.Submission;
import com.org.codeverse.repository.SubmissionRepository;
import com.org.codeverse.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/question")
@Slf4j
@CrossOrigin("*")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/")
    public ResponseEntity<Question> createQuestion( @RequestBody Question question) {
        Question savedQuestion = questionService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

    @GetMapping("")
    public ResponseEntity<AllQuestionResponseDTO> getAllQuestions(
            @RequestParam(required = false) Long userId,
            @RequestParam(name = "search", required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        AllQuestionResponseDTO responseDTO = questionService.getAllQuestions(
                userId, keyword, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<QuestionDetailsDTO> getQuestion(@PathVariable String slug) {
        QuestionDetailsDTO questionDetailsDTO = questionService.getQuestionBySlug(slug);
        return ResponseEntity.status(HttpStatus.OK).body(questionDetailsDTO);
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmitQuestionResponseDTO> submitQuestion(@RequestBody SubmitQuestionRequestDTO requestDTO) {
        SubmitQuestionResponseDTO responseDTO = questionService.submitQuestion(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/run")
    public ResponseEntity<RunQuestionResponseDTO> runQuestion(@RequestBody RunQuestionRequestDTO requestDTO) throws IOException {
        RunQuestionResponseDTO responseDTO = questionService.runQuestion(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/run2")
    public ResponseEntity<RunQuestionResponseDTO> runQuestionV2(@RequestBody RunQuestionRequestDTO requestDTO)
            throws IOException, InterruptedException {
        return ResponseEntity.ok(questionService.runQuestionV2(requestDTO));
    }

    @GetMapping("/submission/result")
    public ResponseEntity<List<SubmissionRepository.SubmissionProjection>> getSubmissionResultByUserIdAndQuestionIds (
            @RequestParam(name = "userId") Long userId, @RequestParam(name = "questionIds") List<Long> questionIds) {
        List<SubmissionRepository.SubmissionProjection> submissions = questionService
                .getSubmissionResultByUserIdAndQuestionIds(userId, questionIds);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submission")
    public ResponseEntity<List<Submission>> getSubmissionsByUserIdAndQuestionId(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "questionId") Long questionId
    ) {
        List<Submission> submissions = questionService.getSubmissionsByUserIdAndQuestionId(userId, questionId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/user-reaction")
    public ResponseEntity<Map<String, Boolean>> findUserQuestionReactions(
            @RequestParam(name = "questionId") Long questionId,
            @RequestParam(name = "userId") Long userId
    ) {
        Map<String, Boolean> userReaction = questionService.findUserQuestionReactions(questionId, userId);
        return ResponseEntity.ok(userReaction);
    }

    @PostMapping("/like")
    public ResponseEntity<LikeQuestionResponseDTO> likeQuestion(@RequestBody LikeQuestionRequestDTO requestDTO) {
        LikeQuestionResponseDTO responseDTO =  questionService.likeQuestion(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/dislike")
    public ResponseEntity<DislikeQuestionResponseDTO> dislikeQuestion(@RequestBody DislikeQuestionRequestDTO requestDTO) {
        DislikeQuestionResponseDTO responseDTO = questionService.dislikeQuestion(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
