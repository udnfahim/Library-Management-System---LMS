package com.libraryhub.dto;

public record UserPassResetDto(String currentPassword, String newPassword , String confirmPassword) {
}
