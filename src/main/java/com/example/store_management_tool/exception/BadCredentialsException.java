package com.example.store_management_tool.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadCredentialsException extends RuntimeException{
    private String message;
}

