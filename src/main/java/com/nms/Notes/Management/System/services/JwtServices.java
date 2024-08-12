package com.nms.Notes.Management.System.services;


import com.google.api.services.drive.model.User;
import com.nms.Notes.Management.System.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


@Service
public class JwtServices {

    private static String SECRET = "";

    public static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public JwtServices(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secret = keyGenerator.generateKey();
            SECRET = Base64.getEncoder().encodeToString(secret.getEncoded());
        }catch (NoSuchAlgorithmException e){
            System.out.println(e.getMessage());;
        }
    }

    public String generateToken(Admin admin){

        Map<String, String> claims = new HashMap<>();
        claims.put("iss","https://rohanthapa.com.np/notes");

        return Jwts.builder()
                .claims(claims)
                .subject(admin.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateSecret())
                .compact();
    }

    private SecretKey generateSecret(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractEmail(String token) {
        // extract the email from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, AdminDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
