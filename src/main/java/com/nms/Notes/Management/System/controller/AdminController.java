package com.nms.Notes.Management.System.controller;

import com.nms.Notes.Management.System.services.AdminServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/api")
public class AdminController {

    private final AdminServices adminservices;

    public AdminController(AdminServices adminservices){
        this.adminservices = adminservices;
    }

    @Value("${cookie.secure}")
    private boolean secure;

    @Value("${cookie.sameSite}")
    private String sameSite;

    @Value("${cookie.maxAge}")
    private long maxAge;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String response  = adminservices.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (response == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        } else {
            ResponseCookie cookie = ResponseCookie.from("auth-token" , response)
                    .httpOnly(true)
                    .secure(secure)
                    .sameSite(sameSite)
                    .path("/")
                    .maxAge(Duration.ofHours(maxAge))
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
        secure(secure).path("/").sameSite(sameSite).maxAge(Duration.ofHours(0)).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout Successful");
    }

}
