package com.jpt.demo.model;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthRequest {

    @NotNull(message = "{not.blank}")
    @NotBlank(message = "{not.blank}")
    private String username;

    @NotNull(message = "{not.blank}")
    @NotBlank(message = "{not.blank}")
    private String password;

    // Por ahora ignoramos el refreshToken
    // private String refreshToken;
}
