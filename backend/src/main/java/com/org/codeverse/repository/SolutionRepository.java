package com.org.codeverse.repository;

import com.org.codeverse.enums.Language;
import com.org.codeverse.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    @Query("SELECT snippet FROM Solution s WHERE s.question.id = :questionId AND s.language = :language")
    Optional<String> findSnippetByQuestionIdAndLanguage(@Param("questionId") Long questionId, @Param("language") Language language);
//    Optional<List<String>> findSnippetByQuestionIdAndLanguage(Long questionId, Language language);
}
