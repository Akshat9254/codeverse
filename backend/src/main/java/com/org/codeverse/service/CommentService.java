package com.org.codeverse.service;

import com.org.codeverse.dto.AllCommentsResponseDTO;
import com.org.codeverse.dto.CommentRequestDTO;
import com.org.codeverse.exception.ResourceNotFoundException;
import com.org.codeverse.model.Comment;
import com.org.codeverse.model.Question;
import com.org.codeverse.model.User;
import com.org.codeverse.repository.CommentRepository;
import com.org.codeverse.repository.QuestionRepository;
import com.org.codeverse.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository,
                          QuestionRepository questionRepository,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.commentRepository = commentRepository;
    }

    public Comment addComment(CommentRequestDTO requestDTO) {
        Long userId = requestDTO.getUserId();
        Long questionId = requestDTO.getQuestionId();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId.toString()));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId", questionId.toString()));

        Comment comment = new Comment();
        comment.setTitle(title);
        comment.setContent(content);
        comment.setUser(user);
        comment.setQuestion(question);

        Comment savedComment = commentRepository.save(comment);
        return savedComment;
    }

    public AllCommentsResponseDTO getCommentsByQuestionId(
            Long questionId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<CommentRepository.CommentDetails> page = commentRepository.findAllByQuestionId(questionId, pageable);

        AllCommentsResponseDTO responseDTO = new AllCommentsResponseDTO();
        responseDTO.setComments(page.getContent());
        responseDTO.setPageNumber(page.getNumber());
        responseDTO.setPageSize(page.getSize());
        responseDTO.setTotalElements(page.getTotalElements());
        responseDTO.setLast(page.isLast());

        return responseDTO;
    }
}
