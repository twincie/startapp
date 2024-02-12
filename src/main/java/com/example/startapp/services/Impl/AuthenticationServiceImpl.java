package com.example.startapp.services.Impl;

import com.example.startapp.dto.JwtAuthenticationResponse;
import com.example.startapp.dto.RefreshTokenRequest;
import com.example.startapp.dto.SignUpRequest;
import com.example.startapp.dto.SigninRequest;
import com.example.startapp.entity.Role;
import com.example.startapp.entity.Users;
import com.example.startapp.repository.UsersRepository;
import com.example.startapp.services.AuthenticationService;
import com.example.startapp.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;


    public Users signup(SignUpRequest signUpRequest){
        Users users = new Users();

        users.setEmail(signUpRequest.getEmail());
        users.setName(signUpRequest.getName());
        users.setRole(Role.USER);
        users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return usersRepository.save(users);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword());
            System.out.println(usernamePasswordAuthenticationToken);
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            var users = usersRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid email or password."));
            var jwt = jwtService.generateToken(users);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), users);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            return jwtAuthenticationResponse;
        } catch (AuthenticationException e){
            throw new IllegalArgumentException("Authentication failed: "+ e.getMessage());
        }
    }

    public JwtAuthenticationResponse requestToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        Users user = usersRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
