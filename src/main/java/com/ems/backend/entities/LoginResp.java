package com.ems.backend.entities;

import lombok.Data;

@Data
public class LoginResp {
    private String token;
    private long expiresIn;
}
