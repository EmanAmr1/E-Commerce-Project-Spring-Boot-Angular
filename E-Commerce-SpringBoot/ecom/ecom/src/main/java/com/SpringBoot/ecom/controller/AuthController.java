package com.SpringBoot.ecom.controller;

import com.SpringBoot.ecom.dto.AuthenticationRequest;
import com.SpringBoot.ecom.dto.SignupRequest;
import com.SpringBoot.ecom.dto.UserDto;
import com.SpringBoot.ecom.entity.User;
import com.SpringBoot.ecom.enums.UserRole;
import com.SpringBoot.ecom.repository.UserRepository;
import com.SpringBoot.ecom.services.auth.AuthService;
import com.SpringBoot.ecom.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

   /* @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody
                                              AuthenticationRequest authenticationRequest,
                                              HttpServletResponse response)
            throws IOException, JSONException {



        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Incorrect username or password");
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        String jwt = jwtUtil.generateToken(userDetails.getUsername());  //generate token to this user

        if (optionalUser.isPresent()) {
            JSONObject jsonResponse = new JSONObject()

                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole().toString());

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        }
        /*response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Origin", "Authorization ,x-PINGOTHER ,origin ,"+
                "x-requested-with,Content-Type,Accept, X-Custom-Header");

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Correct header setup

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }

    */

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Incorrect username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            JSONObject jsonResponse = new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole().toString());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwt);
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jsonResponse.toString());
        }

        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
                .body("User not found");
    }



    @PostMapping("/sign-up")
    public ResponseEntity<?> SignupUser(@RequestBody SignupRequest signupRequest) {

      if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("User already exist" , HttpStatus.NOT_ACCEPTABLE);
      }

        UserDto userDto=authService.CraeteUser(signupRequest);
      return new ResponseEntity<>(userDto,HttpStatus.OK);

    }




    @PostConstruct
    public void createAdminAccount(){

        User adminAccount=userRepository.findByRole(UserRole.ADMIN);
        if(adminAccount==null){

           User user=new User();
           user.setEmail("Admin@gmail.com");
           user.setName("Admin");
           user.setRole(UserRole.ADMIN);
           user.setPassword(new BCryptPasswordEncoder().encode("admin"));
           userRepository.save(user);
        }
    }





}
