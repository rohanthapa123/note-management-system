package com.nms.Notes.Management.System.services;

import com.nms.Notes.Management.System.model.Admin;
import com.nms.Notes.Management.System.repo.AdminRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServices {


    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;

    public AdminServices(AdminRepository adminRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtServices jwtServices) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtServices = jwtServices;
    }

    public Admin getUserByEmail(String email){
        System.out.println(email);
        return adminRepo.findByEmail(email);
    }


    public String login(String email, String password) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

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
