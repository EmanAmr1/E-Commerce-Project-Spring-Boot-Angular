package com.SpringBoot.ecom.services.auth;

import com.SpringBoot.ecom.dto.SignupRequest;
import com.SpringBoot.ecom.dto.UserDto;
import com.SpringBoot.ecom.entity.User;
import com.SpringBoot.ecom.enums.UserRole;
import com.SpringBoot.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public UserDto CraeteUser(SignupRequest  signupRequest) {

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword( new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
        user.setRole(UserRole.CUSTOMER);

        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

            return userDto;
    }



    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

}
