package com.nms.Notes.Management.System.controller;

import com.nms.Notes.Management.System.model.Admin;
import com.nms.Notes.Management.System.services.AdminServices;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    AdminServices adminservices;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String response  = adminservices.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (response == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        } else {
            ResponseCookie cookie = ResponseCookie.from("auth-token" , response)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Login Successful");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){

        ResponseCookie cookie = ResponseCookie.from("auth-token","")
                .httpOnly(true).
        secure(false).path("/").maxAge(Duration.ofHours(0)).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout Successful");
    }

}
