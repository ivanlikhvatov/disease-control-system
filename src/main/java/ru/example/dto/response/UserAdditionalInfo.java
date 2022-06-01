package ru.example.dto.response;

import lombok.Data;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfo;

@Data
public class UserAdditionalInfo {
    private DecanatAdditionalInfo decanatAdditionalInfo;
}
