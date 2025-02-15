package com.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private SocialProvider socialProvider;
}