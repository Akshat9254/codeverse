package com.org.codeverse.repository;

import com.org.codeverse.dto.TopicDTO;
import com.org.codeverse.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findBySlug(String slug);
    @Query("SELECT NEW com.org.codeverse.dto.TopicDTO(t.tag, t.slug, t.id) FROM Topic t")
    List<TopicDTO> findAllTagSlugId();
}
