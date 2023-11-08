package com.user_manager_v1.usermanagement.rest_controllers;

import com.user_manager_v1.usermanagement.models.Login;
import com.user_manager_v1.usermanagement.models.User;
import com.user_manager_v1.usermanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DeleteController {

    @Autowired
    UserService userService;

    @DeleteMapping(path = "{email}")
    public ResponseEntity registerNewUser(@PathVariable("email") String email) {

        if(email.isEmpty()){
            return new ResponseEntity<>("Please complite all Fileds.", HttpStatus.BAD_REQUEST);
        }

        int result = userService.deleteUsers(email);

        if(result!=1) {
            return new ResponseEntity<>("unsuccessful", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
