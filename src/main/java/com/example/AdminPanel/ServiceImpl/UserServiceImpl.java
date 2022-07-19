package com.example.AdminPanel.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Entity.UserEntity;
import com.example.AdminPanel.Repository.UserRepository;
import com.example.AdminPanel.Service.MailService;
import com.example.AdminPanel.Service.UserService;
import com.example.AdminPanel.constant.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> search(String role, String searchText) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (role.equalsIgnoreCase("user")) {
                if (searchText.length() >= 3) {
                    List<String> allUserName = userRepository.findByRoleOrName(role, searchText);
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, allUserName);
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.INVELID_PARAMETER);
                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
            }

        } catch (Exception e) {
            logger.error("Something wrong at UserServiceImpl in method getuserByRole : ", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> DeleteUser(Long id, Long adminId, String adminRole) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Optional<UserEntity> userEntity1 = userRepository.findByAdminRoleOrId(adminId, adminRole);
            if (userEntity1.isPresent()) {
                Optional<UserEntity> userEntity = userRepository.findById(id);
                if (userEntity.isPresent()) {
                    userRepository.deleteUser(id);
                    mailService.deleteUserByAdmin(userEntity);
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_DELETE_SUCCESS);
                    map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
                }
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
            }
        } catch (Exception e) {
            logger.info("Something wrong at UserServiceImpl in method DeleteUser ", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> resetPasswordByOldPassword(Long id, String password, String oldPassword) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<UserEntity> userEntity = userRepository.findById(id);
            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();

                if (user.getVerified().equals(true)) {
                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                    boolean ismatch = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
                    System.out.println(ismatch);
                    if (ismatch) {
                        user.setPassword(passwordEncoder.encode(password));
                        userRepository.save(user);
                        mailService.ResetPasswordSuccess(user);
                        map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                        map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.RESET_PASSWORD_SUCCESS);
                        map.put(ApplicationConstant.RESPONSE_DATA, new ArrayList<>());
                    } else {
                        map.put(ApplicationConstant.RESPONSE_DATA, ApplicationConstant.STATUS_400);
                        map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PASSWORD_DOES_NOT_MATCHED);
                    }
                } else {
                    map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                    map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_VERIFY);
                }

            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Something wrong at UserServiceImpl in method resetPasswordByOldPassword :", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> uploadProfilePicture(Long id, MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();

        try {

            UserEntity userEntity = userRepository.getById(id);
            if (userEntity != null) {
                Cloudinary cloudinary = (Cloudinary) new Cloudinary(
                        ObjectUtils.asMap("cloud_name", ApplicationConstant.CLOUD_NAME, "api_key",
                                ApplicationConstant.API_KEY, "api_secret", ApplicationConstant.API_SECRET));

                Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                        ObjectUtils.asMap("public_id", "user_profile/" + id + "/" + multipartFile.getOriginalFilename()));


                String url = uploadResult.get("url").toString();
                userEntity.setProfile(url);
                userRepository.save(userEntity);

                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PROFILE_UPLOADED_SUCESSFULLY);
                map.put(ApplicationConstant.RESPONSE_DATA, url);
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Something wrong at UserServiceImpl in method uploadProfilePicture :", e.getMessage());
            map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
        }
        return map;
    }

    @Override
    public Map<String, Object> getProfilePicture(Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<UserEntity> userEntity = userRepository.findById(id);

            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();

                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_200);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.PROFILE_IMAGE_SUCESSFULLY);
                map.put(ApplicationConstant.RESPONSE_DATA, user.getProfile());
            } else {
                map.put(ApplicationConstant.RESPONSE_STATUS, ApplicationConstant.STATUS_400);
                map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Something wrong at UserServiceImpl in method getProfilePicture :", e.getMessage());
            map.put(ApplicationConstant.RESPONSE_MESSAGE, ApplicationConstant.SOMETING_WENT_WRONG);
        }
        return map;
    }
}