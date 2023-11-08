package com.user_manager_v1.usermanagement.services;

import com.user_manager_v1.usermanagement.models.User;
import com.user_manager_v1.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public int registerNewUserServiceMethod(String fname, String lname, String email, String password) {
        return userRepository.registerNewUser(fname, lname, email, password);
    }

    // Check User Email Services Method:
    public List<String> checkUserEmail(String email) {
        return userRepository.checkUserEmail(email);
    }
    // End Of Check User Email Services Method

    // Check User Password Services Method:
    public String checkUserPasswordByEmail(String email) {
        return userRepository.checkUserPasswordByEmail(email);
    }
    // End Of Check User Password Services Method

    public User getUserDetailsByEmail(String email) {
        return userRepository.GetUserDetailsByEmail(email);
    }

    public List<User> getStudents(){
        return userRepository.findAllUsers();
    }

    public int deleteUsers(String email) { return userRepository.deleteUserByEmail(email);}
}
