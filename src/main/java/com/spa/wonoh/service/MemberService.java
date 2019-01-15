package com.spa.wonoh.service;


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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MemberService implements  IMemberService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    /**
     * @author wonoh
     * @param loginRequest
     *  로그인 요청값을 받아 spring security 인증을 거쳐 성공하면 jwt 토큰을 발급한다.
     * @return jwt token
     */
    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
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

    /**
     * @author wonoh
     * @param signUpRequest
     *  회원가입 요청값을 받아 가입이 가능한 사용자라면
     * @return
     */
    @Override
    public ResponseEntity<String> signUp(SignUpRequest signUpRequest) {

        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("이미 등록되어 있는 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }
        if (memberRepository.existsByUserId(signUpRequest.getUserId())) {
            return new ResponseEntity<>("이미 등록되어 있는 아이디 입니다.", HttpStatus.BAD_REQUEST);
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
                default:
                    Role memberRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("fail -> cause : user role not find"));
                    roles.add(memberRole);
                    break;
            }
        });
        member.setRoles(roles);
        memberRepository.save(member);
        return ResponseEntity.ok().body("회원가입에 성공하였습니다.");
    }
}
