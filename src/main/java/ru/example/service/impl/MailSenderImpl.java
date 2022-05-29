package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import ru.example.dao.entity.user.User;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.service.MailSender;
import ru.example.utils.mail.MailMessages;
import ru.example.utils.mail.MailSubjects;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final static String SPACE = " ";
    private final static String ACTIVATION_CODE_URL_PARAMS = "?userName={0}&activationCode={1}";

    @Value("${spring.mail.username}")
    private String username;

    @Value("${vue-client.url}")
    private String clientUrl;

    @Value("${vue-client.confirm-register-path}")
    private String activationPath;

    @Value("${vue-client.login}")
    private String loginPath;

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationMessage(User user) {
        String userName = buildUserName(user);
        String activationFullPath = buildActivationPath(user);
        String message = MailMessages.createConfirmRegistrationMessage(userName, activationFullPath);

        send(user.getEmail(), MailSubjects.ACTIVATION_SUBJECT.getText(), message);
    }

    private String buildActivationPath(User user) {
        String requestParams = buildActivationCodeUriParams(user);

        return clientUrl
                .concat(activationPath)
                .concat(requestParams);
    }

    private String buildActivationCodeUriParams(User user) {
        String fullUsername = buildUserName(user);
        String activationCode = user.getActivationCode();

        return MessageFormat.format(
                ACTIVATION_CODE_URL_PARAMS,
                fullUsername,
                activationCode
        );
    }

    @Override
    public void sendSuccessActivationMessage(User user) {
        String userName = buildUserName(user);
        String loginFullPath = clientUrl.concat(loginPath);
        String message = MailMessages.createSuccessActivationMessage(userName, loginFullPath);

        send(user.getEmail(), MailSubjects.SUCCESS_ACTIVATION.getText(), message);
    }

    @Override
    public void sendDiseaseProcessedMessage(User user) {
        String userName = buildUserName(user);
        String message = MailMessages.createDiseaseProcessedMessage(userName);

        send(user.getEmail(), MailSubjects.DISEASE_PROCESSED.getText(), message);
    }

    @Override
    public void sendDiseaseApprovedMessage(User user) {
        String userName = buildUserName(user);
        String message = MailMessages.createDiseaseApprovedMessage(userName);

        send(user.getEmail(), MailSubjects.DISEASE_APPROVED.getText(), message);
    }

    private void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            throw new ApiException(ErrorContainer.MAIL_EXCEPTION);
        }
    }

    private String buildUserName(User user) {
        return String.format(
                user.getFirstname(),
                SPACE,
                user.getPatronymic()
        );
    }
}