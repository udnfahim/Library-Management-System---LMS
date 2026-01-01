package com.libraryhub.service;

import com.libraryhub.dto.UserDto;
import com.libraryhub.dto.UserPassResetDto;
import com.libraryhub.model.User;
import com.libraryhub.repository.UserRepository;
import com.libraryhub.service.Interface.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public String register(UserDto userDto) {

        if (userDto.name().trim().isEmpty()) {
            return "Full Name cannot be empty or whitespace only.";
        }
        if (userDto.name().length() < 3) {
            return "Full Name must be at least 3 characters long.";
        }
        if (!userDto.name().matches("^[a-zA-Z ]+$")) {
            return "Full Name can only contain letters and spaces.";
        }
        if (userDto.name().length() > 100) {
            return "Full Name cannot exceed 100 characters.";
        }

        if (userDto.username().length() < 5) {
            return "Username must be at least 5 characters long.";
        }
        if (!userDto.username().matches("^[a-zA-Z0-9_]+$")) {
            return "Username can contain only letters, numbers, and underscores.";
        }
        if (userRepository.existsByUsername(userDto.username())) {
            return "This username is already taken. Please choose a different one.";
        }

        if (!userDto.email().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Please enter a valid email address.";
        }
        if (userRepository.existsByEmail(userDto.email())) {
            return "An account with this email already exists. Please use a different email or login.";
        }

        if (!userDto.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            return "Password must include letters, numbers, and at least one special character.";
        }
        if (userDto.password().contains(" ")) {
            return "Password cannot contain spaces.";
        }
        if (userDto.password().equalsIgnoreCase(userDto.username()) || userDto.password().equalsIgnoreCase(userDto.email())) {
            return "Password should not be the same as your username or email.";
        }
        if (userDto.password().length() < 8) {
            return "Your password must contain at least 8 characters for security purposes.";
        }
        if (userDto.username().length() > 30) {
            return "Username cannot exceed 30 characters.";
        }
        if (userDto.password().length() > 128) {
            return "Password cannot exceed 128 characters.";
        }
        if (!userDto.password().equals(userDto.confirmPassword())) {
            return "The passwords entered do not match. Please verify and try again.";
        }

        User user = userDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String match(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "The current password you entered is incorrect. Please try again.";
        }
        return null;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password,user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public String passwordReset(UserPassResetDto dto, User user){

        String current = dto.currentPassword().trim();
        String newPass = dto.newPassword().trim();
        String confirm = dto.confirmPassword().trim();

        if (!passwordEncoder.matches(current,user.getPassword())) {
            return "The current password you entered is incorrect. Please try again.";
        }
        if (!newPass.equals(confirm)) {
            return "The new passwords entered do not match. Please verify and try again.";
        }
        if (newPass.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!newPass.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            return "Password must include letters, numbers, and at least one special character.";
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            return "New password cannot be the same as your current password.";
        }
        if (newPass.contains(" ")) {
            return "Password cannot contain spaces.";
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return null;
    }

    @Override
    public void updateProfile(int userId, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(updatedUser.getName());
            userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteUserById(Integer userId){
        userRepository.deleteById(Math.toIntExact(userId));
    }

    @Override
    public String emailValidation(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email address cannot be empty.";
        }
        if (email.length() < 5 || email.length() > 254) {
            return "Invalid email address length.";
        }

        email = email.trim().toLowerCase();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Please enter a valid email address.";
        }
        if (!userRepository.existsByEmail(email)) {
            return "No account found with this email address.";
        }
        return null;
    }

    @Override
    public String resetPasswordByEmail(String email, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "Email not found.";
        }

        newPassword = newPassword.trim();
        confirmPassword = confirmPassword.trim();

        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        if (newPassword.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (newPassword.length() > 128) {
            return "Password cannot exceed 128 characters.";
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            return "Password must include letters, numbers, and at least one special character.";
        }
        if (newPassword.contains(" ")) {
            return "Password cannot contain spaces.";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return null;
    }
}
