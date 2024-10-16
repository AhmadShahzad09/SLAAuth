package com.slatoolapi.authservice.dto;

import com.slatoolapi.authservice.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRegisterDto {
    
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    
    public static UserRegisterDto convert(User user){
        return new UserRegisterDto(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
