package com.yassine.users.service;

import com.yassine.users.entities.Role;
import com.yassine.users.entities.User;
import com.yassine.users.register.RegistrationRequest;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User saveUser(User user);
    User findUserByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    User registerUser(RegistrationRequest request);
    User validateToken(String code);
}
