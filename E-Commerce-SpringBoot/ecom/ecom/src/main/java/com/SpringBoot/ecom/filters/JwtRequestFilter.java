package com.SpringBoot.ecom.filters;

import com.SpringBoot.ecom.services.jwt.UserDetailsServiceImpl;
import com.SpringBoot.ecom.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter { //every request will first go there

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader =request.getHeader("Authorization");

        String token = null;  //variable to store token

        String username =null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);  //get token from request header
            username=jwtUtil.extractUsername(token);  // get username of user from token
        }


        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){ //ma7salo4 auth before
            UserDetails userDetails =userDetailsService.loadUserByUsername(username);

            if(jwtUtil.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(userDetails,null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //set the details of request
                SecurityContextHolder.getContext().setAuthentication(authToken);  //update context that this user already auth
            }
        }
        filterChain.doFilter(request,response); //go to the next filter


    }
}
