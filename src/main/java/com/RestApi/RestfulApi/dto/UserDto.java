package com.RestApi.RestfulApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String Password;
    private String Username;
    private String Lastname;
    private UserRole UserRole;
    private UserRole role;
}
