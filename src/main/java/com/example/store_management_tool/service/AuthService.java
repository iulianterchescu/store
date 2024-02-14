package com.example.store_management_tool.service;

import com.example.store_management_tool.exception.BadCredentialsException;
import com.example.store_management_tool.exception.UsernameAlreadyUsedException;
import com.example.store_management_tool.model.Roles;
import com.example.store_management_tool.model.UserEntity;
import com.example.store_management_tool.model.dtos.LoginDto;
import com.example.store_management_tool.model.dtos.RegisterDto;
import com.example.store_management_tool.repository.RoleRepository;
import com.example.store_management_tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.store_management_tool.model.RolesEnum.ADMIN;
import static com.example.store_management_tool.model.RolesEnum.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())){
            log.info("Username can't be used because there is another one registered in our services");
            throw new UsernameAlreadyUsedException("Username is already in use");
        }
        String roleType = registerDto.getIsAdmin() ? String.valueOf(ADMIN) : String.valueOf(USER);
        Roles role = roleRepository.findByName(roleType).get();
        UserEntity user = UserEntity.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Collections.singletonList(role))
                .build();

        userRepository.save(user);
        return "User registered";
    }

    public String login(LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "User signed in";
        }catch (Exception e){
            log.info("Username and password wrong");
            throw new BadCredentialsException("Username or password wrong");
        }
    }
}
