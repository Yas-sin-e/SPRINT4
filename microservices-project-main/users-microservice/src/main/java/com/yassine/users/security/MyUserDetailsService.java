package com.yassine.users.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yassine.users.entities.User;
import com.yassine.users.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

    final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable : " + username);
        }

        List<GrantedAuthority> auths = new ArrayList<>();
        user.getRoles().forEach(role -> {
            auths.add(new SimpleGrantedAuthority(role.getRole()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(), // ← MODIFIÉ : false = bloqué au login si email non vérifié
                true,             // accountNonExpired
                true,             // credentialsNonExpired
                true,             // accountNonLocked
                auths
        );
    }
}