package com.org.codeverse.repository;

import com.org.codeverse.enums.Difficulty;
import com.org.codeverse.model.DifficultyCount;
import com.org.codeverse.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    interface QuestionInfo {
        Long getId();
        String getTitle();
        String getSlug();
        Difficulty getDifficulty();
        Boolean getSolved();
        Double getAcceptanceRate();
    }
    @Query(value = "SELECT q.id, q.title, q.slug, q.difficulty,\n" +
            "    EXISTS(SELECT 1 FROM submission s WHERE s.question_id = q.id AND s.user_id = :userId AND s.result = 'ACCEPTED') as solved,\n" +
            "    CASE \n" +
            "        WHEN (SELECT COUNT(s3.id) FROM submission s3 WHERE s3.question_id = q.id) = 0 \n" +
            "            THEN 0.0 \n" +
            "        ELSE COALESCE((SELECT COUNT(s2.id) FILTER (WHERE s2.result = 'ACCEPTED') FROM submission s2 WHERE s2.question_id = q.id), 0.0) / \n" +
            "            NULLIF((SELECT COUNT(s3.id) FROM submission s3 WHERE s3.question_id = q.id), 0.0) * 100.0\n" +
            "    END AS acceptanceRate\n" +
            "FROM question q\n" +
            "WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%'))\n" +
            "GROUP BY q.id, q.title, q.slug, q.difficulty \n" +
            "OFFSET :offset \n" +
            "LIMIT :limit", nativeQuery = true)
    List<QuestionInfo> findQuestionInfoByUserId(@Param("userId") Long userId,
                                                @Param("keyword") String keyword,
                                                @Param("offset") Integer offset,
                                                @Param("limit") Integer limit);

    @Query("SELECT "
            + "(SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END "
            + "FROM Question q JOIN q.usersLiked ul WHERE q.id = :questionId AND ul.id = :userId) AS liked, "
            + "(SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END "
            + "FROM Question q JOIN q.usersDisLiked dl WHERE q.id = :questionId AND dl.id = :userId) AS disliked "
            + "FROM Question q WHERE q.id = :questionId")
    Map<String, Boolean> findUserQuestionReactions(@Param("questionId") Long questionId, @Param("userId") Long userId);

    Question findBySlug(String slug);


    @Query("SELECT new com.org.codeverse.model.DifficultyCount(q.difficulty, COUNT(q)) " +
            "FROM Question q GROUP BY q.difficulty")
    List<DifficultyCount> findDifficultyCount();

    @Query(value = "SELECT COUNT(q.id) FROM Question q")
    Long getQuestionCount();
}
