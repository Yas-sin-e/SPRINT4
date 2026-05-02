package com.yassine.users.service;

//import org.springframework.beans.factory.annotation.Autowired;
import com.yassine.users.entities.VerificationToken;
import com.yassine.users.exception.EmailAlreadyExistsException;
import com.yassine.users.exception.ExpiredTokenException;
import com.yassine.users.exception.InvalidTokenException;
import com.yassine.users.register.RegistrationRequest;
import com.yassine.users.repos.VerificationTokenRepository;
import com.yassine.users.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yassine.users.entities.Role;
import com.yassine.users.entities.User;
import com.yassine.users.repos.RoleRepository;
import com.yassine.users.repos.UserRepository;

import java.util.*;

@Transactional//l'orsque modification on  commit automatique  ou valider la transaction
@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository userRep;

    final
    RoleRepository roleRep;
    @Autowired
    private VerificationTokenRepository verificationTokenRepo;
    final
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailSender emailSender;
    public UserServiceImpl(UserRepository userRep, RoleRepository roleRep, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRep = userRep;
        this.roleRep = roleRep;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRep.findAll();
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRep.save(user);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User usr = userRep.findByUsername(username);
        Role r = roleRep.findByRole(rolename);
        usr.getRoles().add(r);
        return usr;
    }

    @Override
    public Role addRole(Role role) {
        return roleRep.save(role);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRep.findByUsername(username);
    }
    @Override
    public User registerUser(RegistrationRequest request) {
        // Vérifier si l'email existe déjà
        Optional<User> existing = userRep.findByEmail(request.getEmail());
        if (existing.isPresent())
            throw new EmailAlreadyExistsException("Email déjà existant !");

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setEnabled(false); // ← désactivé jusqu'à validation email
        userRep.save(newUser);

        // Ajouter le rôle USER par défaut
        Role r = roleRep.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        newUser.setRoles(roles);
        userRep.save(newUser);

        // Générer et sauvegarder le code de vérification
        String code = generateCode();
        VerificationToken token = new VerificationToken(code, newUser);
        verificationTokenRepo.save(token);

        // Envoyer le code par email
        sendEmailUser(newUser, code);

        return newUser;
    }

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // code à 6 chiffres
        return String.valueOf(code);
    }

    public void sendEmailUser(User u, String code) {
        String body = "Bonjour <h1>" + u.getUsername() + "</h1>"
                + " Votre code de validation est : <h1>" + code + "</h1>";
        emailSender.sendEmail(u.getEmail(), body);
    }

    @Override
    public User validateToken(String code) {
        VerificationToken token = verificationTokenRepo.findByToken(code);
        if (token == null)
            throw new InvalidTokenException("Code invalide !");

        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(token);
            throw new ExpiredTokenException("Code expiré !");
        }

        User user = token.getUser();
        user.setEnabled(true); // ← activer l'utilisateur
        userRep.save(user);
        return user;
    }
}
