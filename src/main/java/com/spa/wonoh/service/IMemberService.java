package com.spa.wonoh.service;

import com.spa.wonoh.model.request.LoginRequest;
import com.spa.wonoh.model.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface IMemberService {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<String> signUp(SignUpRequest signUpRequest);


}
