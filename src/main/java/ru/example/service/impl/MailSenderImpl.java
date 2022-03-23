package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import ru.example.dao.entity.user.User;
import ru.example.service.MailSender;
import ru.example.utils.mail.MailMessages;
import ru.example.utils.mail.MailSubjects;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final static String SPACE = " ";

    @Value("${spring.mail.username}")
    private String username;

    @Value("${vue-client.url}")
    private String url;

    @Value("${vue-client.confirm-register-path}")
    private String activationPath;

    @Value("${vue-client.login}")
    private String loginPath;

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationMessage(User user) {
        String userName = buildUserName(user);
        String activationFullPath = url + activationPath + user.getActivationCode();
        String message = MailMessages.createConfirmRegistrationMessage(userName, activationFullPath);

        send(user.getEmail(), MailSubjects.ACTIVATION_SUBJECT.getText(), message);
    }

    @Override
    public void sendSuccessActivationMessage(User user) {
        String userName = buildUserName(user);
        String loginFullPath = url + loginPath;
        String message = MailMessages.createSuccessActivationMessage(userName, loginFullPath);

        send(user.getEmail(), MailSubjects.SUCCESS_ACTIVATION.getText(), message);
    }

    private void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    private String buildUserName(User user) {
        return String.format(
                user.getFirstname(),
                SPACE,
                user.getPatronymic()
        );
    }
}