package com.org.codeverse.controller;

import com.org.codeverse.dto.PlaygroundRequestDTO;
import com.org.codeverse.dto.PlaygroundResponseDTO;
import com.org.codeverse.service.PlaygroundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/playground")
@Slf4j
@CrossOrigin("*")
public class PlaygroundController {
    @Autowired
    PlaygroundService playgroundService;
    @PostMapping("/run")
    public ResponseEntity<PlaygroundResponseDTO> runCode(@RequestBody PlaygroundRequestDTO requestDTO) throws IOException, InterruptedException {
        PlaygroundResponseDTO responseDTO = playgroundService.runCode(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
