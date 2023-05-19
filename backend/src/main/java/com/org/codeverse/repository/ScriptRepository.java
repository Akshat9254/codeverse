package com.org.codeverse.repository;

import com.org.codeverse.enums.Language;
import com.org.codeverse.model.Script;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    Optional<Script> findByLanguage(Language language);
}
