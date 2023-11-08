package com.look4.demo.controllers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.security.JwtUtil;
import com.look4.demo.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    AppUserService appUserService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;

    @PostMapping()
    public AppUserDTO addUser(@RequestBody AppUserDTO user){
        return appUserService.addUser(user);
    }

     @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody AppUserDTO user){
        return  appUserService.registerUser(user);
    }
    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody AppUserDTO body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());

            return Collections.singletonMap("token", token);
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
    }


}
