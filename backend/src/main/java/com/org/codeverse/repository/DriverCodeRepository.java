package com.org.codeverse.repository;

import com.org.codeverse.model.DriverCode;
import com.org.codeverse.enums.DriverCodeType;
import com.org.codeverse.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverCodeRepository extends JpaRepository<DriverCode, Long> {
    List<DriverCode> findByQuestionId(Long questionId);

    @Query("SELECT dc.code FROM DriverCode dc WHERE dc.question.id = :questionId AND dc.language = :language AND dc.type = :type")
    Optional<String> findCodeByQuestionIdAndLanguageAndType(@Param("questionId") Long questionId, @Param("language") Language language, @Param("type") DriverCodeType type);
}
