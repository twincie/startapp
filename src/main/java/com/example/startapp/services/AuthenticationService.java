package com.example.startapp.services;

import com.example.startapp.dto.JwtAuthenticationResponse;
import com.example.startapp.dto.RefreshTokenRequest;
import com.example.startapp.dto.SignUpRequest;
import com.example.startapp.dto.SigninRequest;
import com.example.startapp.entity.Users;

public interface AuthenticationService {

    Users signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse requestToken(RefreshTokenRequest refreshTokenRequest);
}
