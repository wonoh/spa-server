package com.spa.wonoh.security;

import com.spa.wonoh.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class MemberPrinciple implements UserDetails {

    private Long id;
    private String userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static MemberPrinciple build(Member member){
        List<GrantedAuthority> authorities = member.getRoles().stream().map(role ->
                                            new SimpleGrantedAuthority(role.getName().name())
                            ).collect(Collectors.toList());

        return new MemberPrinciple(
                member.getId(),
                member.getUserId(),
                member.getEmail(),
                member.getPassword(),
                authorities
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        MemberPrinciple memberPrinciple = (MemberPrinciple) obj;
        return Objects.equals(id,memberPrinciple.id);
    }

}
