package com.example.AdminPanel.Controller;


import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(value = "role") String role,
                                                      @RequestParam(value = "searchText") String searchText) {
        try {
            logger.info("Get User By Role : ");
            return new ResponseEntity<>(userService.search(role, searchText), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur at UserController in method getUserByRole: ", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/deleteUserByAdmin")
    public ResponseEntity<Map<String, Object>> DeleteUser(@RequestParam(value = "userId") Long id,
                                                          @RequestParam(value = "adminId") Long adminId,
                                                          @RequestParam(value = "adminRole") String adminRole) {
        try {
            logger.info("Delete User By Admin: ");
            return new ResponseEntity<>(userService.DeleteUser(id, adminId, adminRole), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur at UserController in method DeleteUser", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/resetPasswordByOldPassword")
    public ResponseEntity<Map<String, Object>> resetPasswordByOldPassword(@RequestParam(value = "id") Long id,
                                                                          @RequestParam(value = "password") String password,
                                                                          @RequestParam(value = "oldPassword") String oldPassword) {
        try {
            logger.info("Reset Password By Old Password : ");
            return new ResponseEntity<>(userService.resetPasswordByOldPassword(id, password, oldPassword), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur at UserController in method resetPasswordByOldPassword", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<Map<String, Object>> uploadProfilePicture(@RequestParam(value = "id") Long id,
                                                                    @RequestParam(value = "file") MultipartFile multipartFile) {
        try {
            logger.info("Upload profile picture : ");
            return new ResponseEntity<>(userService.uploadProfilePicture(id, multipartFile), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur at UserController in method uploadProfilePicture", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/getProfilePicture")
    public ResponseEntity<Map<String, Object>> getProfilePicture(@RequestParam(value = "id") Long id) {
        try {
            logger.info("Get profile picture :");
            return new ResponseEntity<>(userService.getProfilePicture(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occure at UserController in mehtod getProfilePicture :", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}