package com.nms.Notes.Management.System.config;

import com.nms.Notes.Management.System.services.AdminDetails;
import com.nms.Notes.Management.System.services.CustomAdminDetailService;
import com.nms.Notes.Management.System.services.JwtServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtServices jwtServices;

    @Autowired
    private CustomAdminDetailService customAdminDetailService;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/login",
            "/api/notes",
            "/api/notes/search"
    );  

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();

        // If the request URI matches any of the excluded URLs, proceed with the filter chain without JWT validation
        if (EXCLUDE_URLS.stream().anyMatch(path::matches)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String email = null;



        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    email = jwtServices.extractEmail(token);
                    break;
                }
            }
        }

        System.out.println("Token" + token);
        System.out.println("Email ;" + email);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            AdminDetails adminDetails = (AdminDetails) customAdminDetailService.loadUserByUsername(email);

            if(jwtServices.validateToken(token , adminDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        adminDetails , null , adminDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request , response);
    }
}
