package com.nms.Notes.Management.System.services;

import com.nms.Notes.Management.System.controller.LoginRequest;
import com.nms.Notes.Management.System.model.Admin;
import com.nms.Notes.Management.System.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AdminServices {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServices jwtServices;

    public Admin getUserByEmail(String email){
        System.out.println(email);
        return adminRepo.findByEmail(email);
    }


    public String login(String email, String password) {
        try{
            System.out.println(email + password);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            System.out.println(authentication.getPrincipal());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if(userDetails instanceof AdminDetails){
                AdminDetails adminDetails = (AdminDetails) authentication.getPrincipal();
                Admin admin = adminDetails.getAdmin();
                return jwtServices.generateToken(admin);
            }else {
                throw new IllegalArgumentException("Unexpected user detail type");
            }

        }catch(AuthenticationException e){
            System.out.println(e.getMessage());
            return  null;
        }
    }


}
