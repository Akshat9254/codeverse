package com.org.codeverse.controller;

import com.org.codeverse.dto.UserLoginRequestDTO;
import com.org.codeverse.dto.UserSubmissionResponseDTO;
import com.org.codeverse.dto.UserSubmissionStatResponseDTO;
import com.org.codeverse.dto.UserSubmissionsByDateResponseDTO;
import com.org.codeverse.model.User;
import com.org.codeverse.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUserByEmailAndPassword(@RequestBody User user) {
        userService.registerUserByEmailAndPassword(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<User> registerUserByEmailAndPassword(
            @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        User user = userService.loginUserByEmailAndPassword(userLoginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/submission-stat")
    public ResponseEntity<UserSubmissionStatResponseDTO> getUserSubmissionStat(@RequestParam Long userId) {
        UserSubmissionStatResponseDTO responseDTO = userService.getUserSubmissionStat(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/submission-date-counts")
    public ResponseEntity<UserSubmissionsByDateResponseDTO> countSubmissionsByDateForUser(
            @RequestParam Long userId) {
        UserSubmissionsByDateResponseDTO responseDTO = userService
                .countSubmissionsByDateForUser(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/submissions/all")
    public ResponseEntity<UserSubmissionResponseDTO> getAllSubmissions(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        UserSubmissionResponseDTO responseDTO = userService.getAllSubmissions(
                userId,
                pageNumber,
                pageSize);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(value = "/{userId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
        ) {
        userService.uploadProfileImage(userId, file);
    }


}
