package com.nms.Notes.Management.System.services;

import com.nms.Notes.Management.System.model.Admin;
import com.nms.Notes.Management.System.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAdminDetailService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        Admin admin = adminRepository.findByEmail(email);
        if(admin == null){
            throw new UsernameNotFoundException("Admin not found");
        }
        return new AdminDetails(admin);
    }
}
