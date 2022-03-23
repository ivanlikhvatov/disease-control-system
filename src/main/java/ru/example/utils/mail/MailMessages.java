package ru.example.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailMessages {

    CONFIRM_ACCOUNT("Уважаемый %s, приветствуем Вас в системе контроля заболеваемости. Пожалуйста посетите следующую ссылку, чтобы активировать свой аккаунт: %s"),
    SUCCESS_ACTIVATION("Уважаемый %s, Вы успешно зарегистрировались в системе контроля заболеваемости. Для входа на сайт пройдите по ссылке: %s");

    private final String text;

    public static String createConfirmRegistrationMessage(String fullName, String url) {
        return String.format(
                CONFIRM_ACCOUNT.getText(),
                fullName,
                url
        );
    }

    public static String createSuccessActivationMessage(String fullName, String url) {
        return String.format(
                SUCCESS_ACTIVATION.getText(),
                fullName,
                url
        );
    }
}
