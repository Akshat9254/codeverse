package com.org.codeverse.service;

import com.org.codeverse.dto.*;
import com.org.codeverse.exception.ResourceNotFoundException;
import com.org.codeverse.model.*;
import com.org.codeverse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SubmissionRepository submissionRepository;
    @Autowired
    S3Service s3Service;

    public void registerUserByEmailAndPassword(User user) {
        if (user.getRoles() != null) {
            List<Role> existingRoles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                Optional<Role> existingRole = roleRepository.findByRoleType(role.getRoleType());
                if (existingRole.isPresent()) {
                    existingRole.get().getUsers().add(user);
                    existingRoles.add(role);
                } else {
                    role.getUsers().add(user);
                }
            }

            System.out.println(user.getPassword());

            user.getRoles().removeAll(existingRoles);
        }

        userRepository.save(user);
    }

    public User loginUserByEmailAndPassword(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userRepository.findUserByEmail(userLoginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userLoginRequestDTO.getEmail()));

        if(!user.getPassword().equals(userLoginRequestDTO.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public UserSubmissionStatResponseDTO getUserSubmissionStat(Long userId) {
        List<DifficultyCount> totalSubmissions = questionRepository.findDifficultyCount();
        List<DifficultyCount> acceptedSubmissions = submissionRepository
                .findDifficultyCountByUserAndAcceptedResult(userId);
        UserSubmissionStatResponseDTO responseDTO = new UserSubmissionStatResponseDTO();
        responseDTO.setTotalSubmissions(totalSubmissions);
        responseDTO.setAcceptedSubmissions(acceptedSubmissions);
        return responseDTO;
    }

    public UserSubmissionsByDateResponseDTO countSubmissionsByDateForUser(Long userId) {
        List<SubmissionDateCount> submissionDateCounts = submissionRepository.countSubmissionsByDateForUser(userId);
        UserSubmissionsByDateResponseDTO responseDTO = new UserSubmissionsByDateResponseDTO();
        responseDTO.setSubmissionDateCounts(submissionDateCounts);
        System.out.println(submissionDateCounts);
        return responseDTO;
    }

    public UserSubmissionResponseDTO getAllSubmissions(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("q.createdAt").descending());
        Page<UserSubmission> page = submissionRepository.findByUserId(userId, pageable);
        UserSubmissionResponseDTO responseDTO = new UserSubmissionResponseDTO();
        responseDTO.setAllSubmissions(page.getContent());
        responseDTO.setPageNumber(page.getNumber());
        responseDTO.setPageSize(page.getSize());
        responseDTO.setTotalElements(page.getTotalElements());
        responseDTO.setLast(page.isLast());
        responseDTO.setTotalPages(page.getTotalPages());
        return responseDTO;
    }

    public void uploadProfileImage(Long userId, MultipartFile file) {
        String avatarUrl = "profile-images/%s".formatted(userId);
        try {
            s3Service.putObject("aws-mydemo", avatarUrl, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }
}
