package com.nms.Notes.Management.System.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ValidationController {

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(){

        return ResponseEntity.ok    ("Valid Token");
    }
}
