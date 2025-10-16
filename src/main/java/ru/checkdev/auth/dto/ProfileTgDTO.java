package ru.checkdev.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * CheckDev пробное собеседование
 * DTO модель ProfileTgDto для отправки данных в телеграм
 *
 * @author Dmitry Stepanov
 * @version 14.11.2023 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfileTgDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private String username;
    private String email;
}
