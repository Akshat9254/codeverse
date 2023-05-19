package com.org.codeverse.controller;

import com.org.codeverse.dto.AllCommentsResponseDTO;
import com.org.codeverse.dto.CommentDetails;
import com.org.codeverse.dto.CommentRequestDTO;
import com.org.codeverse.model.Comment;
import com.org.codeverse.repository.CommentRepository;
import com.org.codeverse.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@Slf4j
@CrossOrigin("*")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("")
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequestDTO requestDTO) {
        Comment savedComment = commentService.addComment(requestDTO);
        if(savedComment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<AllCommentsResponseDTO> getCommentsByQuestionId(
            @RequestParam Long questionId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        AllCommentsResponseDTO responseDTO = commentService
                .getCommentsByQuestionId(questionId, pageNumber, pageSize);
        return ResponseEntity.ok(responseDTO);
    }
}
