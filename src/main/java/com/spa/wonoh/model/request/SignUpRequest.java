package com.spa.wonoh.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class SignUpRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String email;

    @NotBlank
    private Set<String> role;

    @NotBlank
    private String password;
}
