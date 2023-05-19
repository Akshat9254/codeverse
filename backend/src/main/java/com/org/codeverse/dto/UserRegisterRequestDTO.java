package com.org.codeverse.dto;

import com.org.codeverse.enums.RoleType;
import lombok.Data;

import java.util.List;

@Data
public class UserRegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private List<RoleType> roles;
}
