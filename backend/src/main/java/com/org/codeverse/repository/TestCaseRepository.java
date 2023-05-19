package com.org.codeverse.repository;

import com.org.codeverse.model.DriverCode;
import com.org.codeverse.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByQuestionId(Long questionId);
}
