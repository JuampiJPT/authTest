package com.jpt.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponse {
    private Long uid;
    private String username;
    private String authToken;

    // Si deseas, agrega más datos:
    // private String nombre;
    // private Date fechaConexion;
}
