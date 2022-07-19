package com.example.AdminPanel.ServiceImpl;

import com.example.AdminPanel.Entity.UserDto;
import com.example.AdminPanel.Entity.UserEntity;
import com.example.AdminPanel.Repository.UserRepository;
import com.example.AdminPanel.Service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
public class MailServiceImpl implements MailService {
    public static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    Session session;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender sender;

    @Override
    public void sendWelcomeMailToUser(UserDto user) {
        // TODO Auto-generated method stub
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(user.getEmail());

            helper.setText("hello !" + user.getName() + " Welcome to AdminPanel project");
            helper.setSubject("AdminPanel small project");
            sender.send(message);

            logger.info("Welcome Mail sent successfully to newly created user {} - verify ", user.getEmail());

        } catch (Exception e) {
            logger.error("Problem occurred while sending Welcome mail , Please check logs : " + e.getMessage());
        }

    }

    @Override
    public void sendResetPasswordLink(UserEntity user)
    {
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(user.getEmail());

            helper.setText( "Hello! "+user.getName()+" Reset your password (-_-)" );
            helper.setSubject("Reset Password Link :");
            sender.send(message);

            logger.info("Send Reset Password Link Mail sent successfully  {} - verify ", user.getEmail());

        } catch (Exception e) {
            logger.error("Problem occurred while sending Reset Password Link mail , Please check logs : " + e.getMessage());
        }
    }

    @Override
    public void ResetPasswordSuccess(UserEntity user)
    {
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(user.getEmail());

            helper.setText("Hello! "+user.getName()+" your password Reset Successfully(^_^)");
            helper.setSubject("Reset Password successfully :");
            sender.send(message);

            logger.info("Reset Password Mail sent successfully  {} - verify ", user.getEmail());

        } catch (Exception e) {
            logger.error("Problem occurred while sending Reset Password mail , Please check logs : " + e.getMessage());
        }
    }

    @Override
    public void deleteUserByAdmin(Optional<UserEntity> userEntity) {
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(userEntity.get().getEmail());

            helper.setText("Hello! "+userEntity.get().getName()+" your Account Delete Successfully(^_^)");
            helper.setSubject("Account Delete successfully :");
            sender.send(message);

            logger.info("Account Delete Mail sent successfully  {} - verify ", userEntity.get().getEmail());

        } catch (Exception e) {
            logger.error("Problem occurred while sending Delete Account mail , Please check logs : " + e.getMessage());
        }
    }
}
