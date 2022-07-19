package com.example.AdminPanel.Controller;

import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Service.RegistrationService;

import com.example.AdminPanel.ServiceImpl.RegistrationServiceImpl;
import org.aspectj.apache.bcel.classfile.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    public static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> SaveUser(@RequestBody UserDto user,
                                                        @RequestParam(value = "action") String action,
                                                        @RequestParam(required=false,value = "role") String role ) {
        try {
            logger.info("User Registration : ");
            return new ResponseEntity<>(registrationService.saveUser(user,action,role), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur : ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/verification")
    public ResponseEntity<Map<String, Object>> verification(@RequestParam(value = "email") String email,
                                                            @RequestParam(value = "password") String password) {
        try {
            logger.info("verify user : ");
            return new ResponseEntity<>(registrationService.verify(email, password), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur : ");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/forgetpassword")
    public ResponseEntity<Map<String, Object>> forgetpassword(@RequestParam(value = "email") String email) {
        try {
            logger.info("forget password : ");
            return new ResponseEntity<>(registrationService.forgetpassword(email), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur : ");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/resetpassword")
    public ResponseEntity<Map<String, Object>> resetpassword(@RequestParam(value = "email") String email,
                                                             @RequestParam(value = "password") String password, @RequestParam(value = "oldPassword") String oldPassword) {
        try {
            logger.info("Reset Password : ");
            return new ResponseEntity<>(registrationService.resetPassword(email, password, oldPassword), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur :");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/editByAdmin")
    public ResponseEntity<Map<String, Object>> EditUserByAdmin(@RequestParam(value = "adminId") Long adminId, @RequestParam(value = "adminRole") String adminROle,
                                                               @RequestParam(value="userId")Long id, @RequestBody UserDto userDto) {
        try {
            logger.info("Edit User By admin : ");
            return new ResponseEntity<>(registrationService.EditUserByAdmin(adminId,adminROle,id,userDto),HttpStatus.OK);
        } catch (Exception e) {
            logger.error("problem occur at UserController in Method EditUserByAdmin :", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
