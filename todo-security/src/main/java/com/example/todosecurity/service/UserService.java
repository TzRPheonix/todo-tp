package com.example.todosecurity.service;

import com.example.todosecurity.config.jwt.JwtTokenProvider;
import com.example.todosecurity.model.User;
import com.example.todosecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        System.out.println("Input: "+input);
        if (input.contains("@")) {
            System.out.println("Email: "+input);
            return userRepository.findByEmail(input)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + input));
        } else {
            System.out.println("Username: "+input);
            return userRepository.findByUsername(input)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + input));
        }
    }

    public String generateToken(String email, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    public boolean verifyUser(String identifier, String password) {
        System.out.println("Identifier: "+identifier);
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier).map(user ->
                            passwordEncoder.matches(password, user.getPassword()))
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with " + identifier));
        } else {
            return userRepository.findByUsername(identifier).map(user ->
                            passwordEncoder.matches(password, user.getPassword()))
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with " + identifier));
        }
    }

    public boolean checkUserExists(String input) {
        if (input.contains("@")) {
            return userRepository.findByEmail(input).isPresent();
        } else {
            return userRepository.findByUsername(input).isPresent();
        }
    }

    public boolean createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }


}