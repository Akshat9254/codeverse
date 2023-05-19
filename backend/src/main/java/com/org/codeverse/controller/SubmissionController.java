package com.org.codeverse.controller;

import com.org.codeverse.dto.SubmitQuestionRequestDTO;
import com.org.codeverse.dto.SubmitQuestionResponseDTO;
import com.org.codeverse.model.Submission;
import com.org.codeverse.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submission")
@Slf4j
@CrossOrigin("*")
public class SubmissionController {
    @Autowired
    SubmissionService submissionService;
    @PostMapping
    public ResponseEntity<SubmitQuestionResponseDTO> submitQuestion(@RequestBody SubmitQuestionRequestDTO requestDTO) {
        SubmitQuestionResponseDTO responseDTO = submissionService.submitQuestion(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/status")
    public ResponseEntity<Submission> submissionStatus(@RequestParam String submissionId) {
        Submission submission = submissionService.submissionStatus(submissionId);
        return ResponseEntity.ok().body(submission);
    }
}
