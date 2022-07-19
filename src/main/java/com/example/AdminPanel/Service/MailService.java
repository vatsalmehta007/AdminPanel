package com.example.AdminPanel.Service;

import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Entity.UserEntity;

import java.util.Optional;

public interface MailService
{
    public void sendWelcomeMailToUser(UserDto user);


    void sendResetPasswordLink(UserEntity user);

    void ResetPasswordSuccess(UserEntity user);

    void deleteUserByAdmin(Optional<UserEntity> userEntity);
}
