package com.yassine.users.restControllers;

import java.util.List;

import com.yassine.users.register.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yassine.users.entities.User;
import com.yassine.users.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    UserService userService;

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/verifyEmail/{token}")
    public User verifyEmail(@PathVariable("token") String token) {
        return userService.validateToken(token);
    }

}