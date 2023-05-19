package com.org.codeverse.repository;

import com.org.codeverse.dto.CommentDetails;
import com.org.codeverse.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public interface CommentDetails {
        Long getId();
        String getTitle();
        String getContent();
        String getUserName();
        String getUserEmail();
        String getUserAvatarUrl();
        int getLikes();
        int getDislikes();
        LocalDateTime getCreatedAt();
    }

    @Query("SELECT c.id AS id, c.title AS title, c.content AS content, u.name AS userName, u.email AS userEmail, " +
            "u.avatarUrl AS userAvatarUrl, SIZE(c.usersLiked) AS likes, SIZE(c.usersDisLiked) AS dislikes, " +
            "c.createdAt AS createdAt FROM Comment c JOIN c.user u WHERE c.question.id = :questionId")
    Page<CommentDetails> findAllByQuestionId(@Param("questionId") Long questionId, Pageable pageable);
}
