package ru.example.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailMessages {

    CONFIRM_ACCOUNT("Уважаемый %s, приветствуем Вас в системе контроля заболеваемости. Пожалуйста посетите следующую ссылку, чтобы активировать свой аккаунт: %s"),
    SUCCESS_ACTIVATION("Уважаемый %s, Вы успешно зарегистрировались в системе контроля заболеваемости. Для входа на сайт пройдите по ссылке: %s"),
    DISEASE_PROCESSED("Уважаемый %s, информация о Вашем заболевании обрабатывается, вы можете следить за статусом в истории заболеваний"),
    DISEASE_APPROVED("Уважаемый %s, информация о Вашем заболевании успешно подтверждена"),
    DISEASE_REFUND("Уважаемый %s, Ваше заболевание не было принято, по причине некорректных данных. Проверьте корректность прикладываемых данных.\n Сообщение от деканата: %s"),
    DISEASE_REJECT("Уважаемый %s, информация о Вашем заболевании была отклонена. Причина указана в сообщении от деканата. \n Сообщение от деканата: %s");

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

    public static String createDiseaseProcessedMessage(String userName) {
        return String.format(
                DISEASE_PROCESSED.getText(),
                userName
        );
    }

    public static String createDiseaseApprovedMessage(String userName) {
        return String.format(
                DISEASE_APPROVED.getText(),
                userName
        );
    }

    public static String createDiseaseRefundToUserMessage(String userName, String refundCause) {
        return String.format(
                DISEASE_REFUND.getText(),
                userName,
                refundCause
        );
    }

    public static String createDiseaseRejectMessage(String userName, String rejectCause) {
        return String.format(
                DISEASE_REJECT.getText(),
                userName,
                rejectCause
        );
    }
}
