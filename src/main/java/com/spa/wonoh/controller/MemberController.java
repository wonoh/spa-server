package com.spa.wonoh.controller;


import com.spa.wonoh.model.Member;
import com.spa.wonoh.model.Role;
import com.spa.wonoh.model.RoleName;
import com.spa.wonoh.model.request.LoginRequest;
import com.spa.wonoh.model.request.SignUpRequest;
import com.spa.wonoh.model.response.JwtResponse;
import com.spa.wonoh.repository.MemberRepository;
import com.spa.wonoh.repository.RoleRepository;
import com.spa.wonoh.security.jwt.JwtProvider;
import com.spa.wonoh.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        ResponseEntity<?> response = memberService.login(loginRequest);
        return response;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        ResponseEntity<?> response = memberService.signUp(signUpRequest);
        return response;
    }
}
