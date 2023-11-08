package com.user_manager_v1.usermanagement.rest_controllers;

import com.user_manager_v1.usermanagement.models.Login;
import com.user_manager_v1.usermanagement.models.User;
import com.user_manager_v1.usermanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProfileApiController {

    @Autowired
    UserService userService;

    @GetMapping("/user/profile")
    public List<User> getStudents(){
        return userService.getStudents();
    }
}
