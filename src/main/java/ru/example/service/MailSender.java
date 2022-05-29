package ru.example.service;

import ru.example.dao.entity.user.User;

public interface MailSender {

    void sendConfirmationMessage(User user);

    void sendSuccessActivationMessage(User user);

    void sendDiseaseProcessedMessage(User user);

    void sendDiseaseApprovedMessage(User user);
}
