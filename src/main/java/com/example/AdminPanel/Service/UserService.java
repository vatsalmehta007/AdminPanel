package com.example.AdminPanel.Service;

import com.example.AdminPanel.Entity.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService
{
    public Map<String, Object> search(String role, String searchText);

    Map<String, Object> resetPasswordByOldPassword(Long id, String password, String oldPassword);

    Map<String, Object> uploadProfilePicture(Long id, MultipartFile multipartFile);

    Map<String, Object> getProfilePicture(Long id);

    Map<String, Object> DeleteUser(Long id, Long adminId, String adminRole);
}
