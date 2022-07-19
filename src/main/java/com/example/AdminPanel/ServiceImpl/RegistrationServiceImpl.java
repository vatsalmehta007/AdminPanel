package com.example.AdminPanel.ServiceImpl;

import com.example.AdminPanel.Controller.RegistrationController;
import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Entity.UserEntity;
import com.example.AdminPanel.Repository.RegistrationRepository;
import com.example.AdminPanel.Service.MailService;
import com.example.AdminPanel.Service.RegistrationService;
import com.example.AdminPanel.constant.ApplicationConstant;
import org.aspectj.apache.bcel.classfile.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    public static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Map<String, Object> saveUser(final UserDto user, String action, String role) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            if (action.equalsIgnoreCase("add")) {
                Optional<UserEntity> userEntity = registrationRepository.findOneByEmailIgnoreCase(user.getEmail());

                if (!userEntity.isPresent()) {
                    if (user.getPassword() != null || !user.getPassword().isEmpty()) {
                        registrationRepository.save(PopulatData(user));
                        mailService.sendWelcomeMailToUser(user);
                        map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                        map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_SUCCESS);
                        map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                    } else {
                        map.put(ApplicationConstant.RESPONSE_STATUS, "500");
                        map.put(ApplicationConstant.RESPONSE_MESSAGE, "password is not provide ");
                    }
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_EMAIL_EXISTS);
                }
            } else if (action.equalsIgnoreCase("adminAddByAdmin") && role.equalsIgnoreCase("admin")) {
                Optional<UserEntity> userEntity = registrationRepository.findOneByEmailIgnoreCase(user.getEmail());

                if (!userEntity.isPresent()) {
                    UserEntity userModel = PopulatData(user);
                    userModel.setVerified(true);
                    userModel.setRole("admin");
                    registrationRepository.save(userModel);
                    mailService.sendWelcomeMailToUser(user);

                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_EMAIL_EXISTS);

                }
            } else if (action.equalsIgnoreCase("userAddByAdmin") && role.equalsIgnoreCase("admin")) {
                Optional<UserEntity> userEntity = registrationRepository.findOneByEmailIgnoreCase(user.getEmail());

                if (!userEntity.isPresent()) {
                    UserEntity userModel = PopulatData(user);
                    userModel.setVerified(true);
                    userModel.setRole("user");
                    registrationRepository.save(userModel);
                    mailService.sendWelcomeMailToUser(user);

                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());

                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.REGISTRATION_EMAIL_EXISTS);

                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
            }

        } catch (Exception e) {
            logger.error("Somthing Wrong : ", e);
        }
        return map;
    }

    private UserEntity PopulatData(final UserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(user.getRole());
        userEntity.setProfile(null);
        userEntity.setVerified(false);

        return userEntity;

    }

    @Override
    public Map<String, Object> verify(String email, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            Optional<UserEntity> userEntity = registrationRepository.findByEmail(email);
            System.out.println("===========" + userEntity);
            System.out.println(email);

            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();
                if (user.getPassword().equalsIgnoreCase(password)) {
                    user.setVerified(true);
                    registrationRepository.save(user);

                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_VERIFICATION_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PASSWORD_DOES_NOT_MATCHED);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_VERIFICATION_FAILURE);
                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("Somthing  Wrong : ", e);
        }
        return map;
    }

    @Override
    public Map<String, Object> forgetpassword(String email) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<UserEntity> userEntity = registrationRepository.findOneByEmailIgnoreCase(email);
            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();

                if (user.getVerified() != false) {
                    mailService.sendResetPasswordLink(user);
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.FORGOT_PASSWORD_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_VERIFY);
                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("somthing Wrong :", e);
        }
        return map;
    }

    @Override
    public Map<String, Object> resetPassword(String email, String password, String oldPassword) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Optional<UserEntity> userEntity = registrationRepository.findByEmail(email);
            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();
                if (user.getPassword().equalsIgnoreCase(oldPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    registrationRepository.save(user);
                    mailService.ResetPasswordSuccess(user);
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.RESET_PASSWORD_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            logger.error("Somthing wrong : ", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> EditUserByAdmin(Long adminId, String adminROle, Long id, UserDto userDto) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapObj = new HashMap<>();
        try {
            Optional<UserEntity> userEntity = registrationRepository.findByAdminRoleOrId(adminId, adminROle);
            if (userEntity.isPresent()) {
                Optional<UserEntity> userEntity1 = registrationRepository.findById(id);
                if (userEntity1.isPresent()) {

                    UserEntity user = userEntity1.get();
                    user.setName(userDto.getName());
                    user.setRole(userDto.getRole());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    registrationRepository.save(user);

                    mapObj.put("name", userDto.getName());
                    mapObj.put("role", userDto.getRole());
                    mapObj.put("password", userDto.getPassword());

                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_DETAIL_EDIT_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, mapObj);
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_EDIT_ONLY_ADMIN);
            }
        } catch (Exception e) {
            logger.error("Something wrong at UserServiceImpl in method EditUserByAdmin :", e.getMessage());
            map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
        }
        return map;
    }
}