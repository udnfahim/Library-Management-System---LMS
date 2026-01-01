package com.libraryhub.service.Interface;

import com.libraryhub.dto.UserDto;
import com.libraryhub.dto.UserPassResetDto;
import com.libraryhub.model.User;

import java.util.List;

public interface UserService {

    public String register(UserDto userDto);
    List<User> getAllUsers();
    User findByUsername(String username);
    public String match(String username , String password);
    public User login(String username, String password);
    public String passwordReset(UserPassResetDto dto, User user);
    public void updateProfile(int userId, User updatedUser);
    public void deleteUserById(Integer userId);
    public String emailValidation(String email);
    public String resetPasswordByEmail(String email, String newPassword, String confirmPassword);
}
