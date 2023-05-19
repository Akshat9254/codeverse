package com.org.codeverse.repository;

import com.org.codeverse.model.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
}
