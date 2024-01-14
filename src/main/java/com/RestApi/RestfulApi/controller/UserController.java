package com.RestApi.RestfulApi.controller;

import com.RestApi.RestfulApi.dto.UserDto;
import com.RestApi.RestfulApi.model.Users;
import com.RestApi.RestfulApi.serviceImpl.UserServiceImpl;
import com.RestApi.RestfulApi.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class UserController {
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    @Autowired
    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto){
        Users user = userService.saveUser(userDto);
        UserDto userDto1 = new ObjectMapper().convertValue(user, UserDto.class);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logInUser (@RequestBody UserDto userDto){
        UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if(passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            String jwtToken = jwtUtils.createJwt.apply(user);
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Username and Password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/dashboard")
    public String dashBoard(){
        return "Welcome to your dashboard";
    }
}
