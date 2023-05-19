package com.org.codeverse.controller;

import com.org.codeverse.dto.TopicDTO;
import com.org.codeverse.service.TopicService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topic")
@CrossOrigin("*")
public class TopicController {
    @Autowired
    TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok().body(topics);
    }

}
