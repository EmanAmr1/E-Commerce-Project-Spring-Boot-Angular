package com.SpringBoot.ecom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    public static final String SECRET ="413F4428472B4B6250655368566D5970337336763979244226452948404D6351";

    public String generateToken(String userName){     //generate token to this user

        Map<String,Object> claims = new HashMap<>();  //initialize claim with empty hashmap

        return createToken(claims,userName);
    }

    private String createToken(Map<String,Object> claims,String userName ){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration( new Date(System.currentTimeMillis()+10000*60*30)) // 30 minutes in milliseconds, which equals 1,800,000 milliseconds.
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    private Key getSignKey(){
        byte [] keybytes = Decoders.BASE64.decode(SECRET);
        // decodes the SECRET_KEY string from Base64 encoding to a byte array
        //Decoders.BASE64 is a utility provided by the io.jsonwebtoken.impl library to handle Base64 decoding.
        return Keys.hmacShaKeyFor(keybytes);
        //It takes the byte array and creates an HmacKey object suitable
        // for HMAC (Hash-based Message Authentication Code) operations with the SHA algorithm.
        // This key is used for signing and verifying the JWT's integrity and authenticity.
    }

    public String extractUsername(String token){//extractUsername from token
        return extractClaim(token , Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims =extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){ //extractAllClaims
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token){

        return extractClaim(token,Claims::getExpiration);
    }


    public Boolean validateToken(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

}
