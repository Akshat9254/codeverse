package com.org.codeverse.repository;

import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    interface SubmissionProjection {
        Long getQuestionId();
        SubmissionResult getResult();
    }
    List<SubmissionProjection> findByUserIdAndQuestionIdIn(Long userId, List<Long> questionIds);

    List<Submission> findByUser_IdAndQuestion_Id(Long userId, Long questionId);

    @Query(value = "SELECT new com.org.codeverse.model.DifficultyCount(q.difficulty, " +
            "COALESCE(COUNT(DISTINCT q.id), 0)) "
            + "FROM Submission s "
            + "JOIN s.question q "
            + "WHERE s.user.id = :userId AND s.result = 'ACCEPTED' "
            + "GROUP BY q.difficulty, q.id")
    List<DifficultyCount> findDifficultyCountByUserAndAcceptedResult(@Param("userId") Long userId);


    @Query("SELECT NEW com.org.codeverse.model.SubmissionDateCount(DATE(s.createdAt), COUNT(s)) " +
            "FROM Submission s WHERE s.user.id = :userId GROUP BY DATE(s.createdAt)")
    List<SubmissionDateCount> countSubmissionsByDateForUser(@Param("userId") Long userId);

    @Query("SELECT NEW com.org.codeverse.model.UserSubmission(s.id, q.title, q.slug, s.result, DATE(s.createdAt), " +
            "s.snippet, s.avgRuntime, s.avgMemoryUsed, s.language) " +
            "FROM Submission s JOIN s.question q WHERE s.user.id = :userId")
    Page<UserSubmission> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
