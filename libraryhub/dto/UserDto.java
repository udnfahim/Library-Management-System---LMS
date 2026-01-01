package com.libraryhub.dto;

import com.libraryhub.model.User;

import java.time.LocalDateTime;

public record UserDto( String name , String username , String email,
                      String password , String photoUrl, String confirmPassword) {
    public User toEntity(){
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhotoUrl(photoUrl);
        user.setRegisterDate(LocalDateTime.now());
        return user;
    }
}
