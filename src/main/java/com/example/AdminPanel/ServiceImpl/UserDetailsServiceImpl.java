package com.example.AdminPanel.ServiceImpl;

import com.example.AdminPanel.Entity.UserEntity;
import com.example.AdminPanel.Repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("inside loadUserByUsername method");

        Optional<UserEntity> entity = registrationRepository.findOneByEmailIgnoreCase(username);
        UserEntity userEntity = null;

        if (entity.isPresent())
        {
            userEntity = entity.get();
        }
        if (userEntity == null)
        {
            throw new UsernameNotFoundException("", new Throwable("Invalid Creds"));
        }
        UserDetails user = User.withUsername(userEntity.getEmail()).password(userEntity.getPassword())
                .authorities("USER").build();
        return user;
    }
}
