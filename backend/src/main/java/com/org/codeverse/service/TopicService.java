package com.org.codeverse.service;

import com.org.codeverse.dto.TopicDTO;
import com.org.codeverse.repository.TopicRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAllTagSlugId();
    }
}
