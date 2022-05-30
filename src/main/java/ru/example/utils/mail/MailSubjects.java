package ru.example.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailSubjects {
    ACTIVATION_SUBJECT("Активация аккаунта"),
    SUCCESS_ACTIVATION("Поздравляем, вы успешно зарегистрировались!"),
    DISEASE_PROCESSED("Информация о заболевании обрабатывается"),
    DISEASE_APPROVED("Информация о заболевании подтверждена"),
    DISEASE_REFUND("Заболевание возвращено из-за некорректных данных"),
    DISEASE_REJECT("Информация о заболевании была отклонена");

    private final String text;
}
