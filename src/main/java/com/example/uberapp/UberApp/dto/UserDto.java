package com.example.uberapp.UberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.example.uberapp.UberApp.entities.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;
    private String email;
    private Set<Role> roles;
}
