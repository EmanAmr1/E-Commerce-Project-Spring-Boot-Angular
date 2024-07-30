package com.SpringBoot.ecom.services.auth;

import com.SpringBoot.ecom.dto.SignupRequest;
import com.SpringBoot.ecom.dto.UserDto;

public interface AuthService {

    UserDto CraeteUser(SignupRequest signupRequest);
   Boolean hasUserWithEmail(String email);

}
