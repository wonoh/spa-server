package com.spa.wonoh.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userId"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String userId;

    @NotBlank
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Member(String email, String userId, String password, Set<Role> roles){
        this.email=email;
        this.userId=userId;
        this.password=password;
        this.roles=roles;
    }
    public Member(String email,String userId,String password){
        this.email=email;
        this.userId=userId;
        this.password=password;
    }
}
