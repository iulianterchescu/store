package com.example.store_management_tool.model.dtos;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private Boolean isAdmin;
}
