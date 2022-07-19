package com.example.AdminPanel.Service;

import com.example.AdminPanel.Entity.UserDto;

import java.util.Map;

public interface RegistrationService {
    public Map<String,Object> saveUser(UserDto user,String action,String role);

    public Map<String,Object> verify(String email, String password);

    public Map<String,Object> forgetpassword(String email);

    public Map<String,Object> resetPassword(String email, String password, String oldPassword);

    Map<String,Object> EditUserByAdmin(Long adminId, String adminROle, Long id, UserDto userDto);
}
