package com.example.store_management_tool.controller;

import com.example.store_management_tool.model.Roles;
import com.example.store_management_tool.model.UserEntity;
import com.example.store_management_tool.model.dtos.LoginDto;
import com.example.store_management_tool.model.dtos.RegisterDto;
import com.example.store_management_tool.repository.RoleRepository;
import com.example.store_management_tool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

import static com.example.store_management_tool.model.RolesEnum.ADMIN;
import static com.example.store_management_tool.model.RolesEnum.USER;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) throws RoleNotFoundException {
        if (userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username is Taken", HttpStatus.BAD_REQUEST);
        }
        String roleType = registerDto.getIsAdmin() ? String.valueOf(ADMIN) : String.valueOf(USER);
        Roles role = roleRepository.findByName(roleType).get();
        UserEntity user = UserEntity.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Collections.singletonList(role))
                .build();

        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed in", HttpStatus.OK);
    }


}
