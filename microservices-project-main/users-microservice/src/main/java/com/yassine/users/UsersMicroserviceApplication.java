package com.yassine.users;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yassine.users.entities.Role;
import com.yassine.users.entities.User;
import com.yassine.users.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UsersMicroserviceApplication {

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(UsersMicroserviceApplication.class, args);
    }

    /**
     * IMPORTANT : Cette méthode s'exécute au démarrage pour insérer les données
     * initiales. Une fois les données bien insérées dans la BDD, commentez tout
     * le contenu de cette méthode pour éviter les doublons au prochain démarrage.
     */
//    @PostConstruct
//    void init_users() {
//        // --- Ajouter les rôles ---
//        userService.addRole(new Role(null, "ADMIN"));
//        userService.addRole(new Role(null, "USER"));
//
//        // --- Ajouter les utilisateurs ---
//        userService.saveUser(new User(null, "admin",   "123", true, new ArrayList<>()));
//        userService.saveUser(new User(null, "yassine", "123", true, new ArrayList<>()));
//
//        // --- Assigner les rôles aux utilisateurs ---
//        userService.addRoleToUser("admin",   "ADMIN");
//        userService.addRoleToUser("admin",   "USER");
//
//        userService.addRoleToUser("yassine", "USER");

}
