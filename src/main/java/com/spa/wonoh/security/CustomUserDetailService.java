package com.spa.wonoh.security;

import com.spa.wonoh.model.Member;
import com.spa.wonoh.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService  implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member =memberRepository.findByEmail(email)
                        .orElseThrow(()->
                                    new UsernameNotFoundException("user not found!!"+email)
                        );
        return MemberPrinciple.build(member);
    }
}
