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
    AuthenticationManager authenticationManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateMember(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@RequestBody SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("fail -> useremail is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (memberRepository.existsByUserId(signUpRequest.getUserId())) {
            return new ResponseEntity<String>("fail -> userId is already in use!", HttpStatus.BAD_REQUEST);
        }
        Member member = new Member(signUpRequest.getEmail(), signUpRequest.getUserId(), passwordEncoder.encode(signUpRequest.getPassword()));
        Set<String> requestRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        requestRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("fail -> cause : user role not find"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("fail -> cause : user role not find"));
                    roles.add(pmRole);
                    break;
                default:
                    Role memberRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("fail -> cause : user role not find"));
                    roles.add(memberRole);
                    break;
            }
        });
        member.setRoles(roles);
        memberRepository.save(member);
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
