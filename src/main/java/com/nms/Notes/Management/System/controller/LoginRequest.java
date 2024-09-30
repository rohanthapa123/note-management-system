package com.nms.Notes.Management.System.controller;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
