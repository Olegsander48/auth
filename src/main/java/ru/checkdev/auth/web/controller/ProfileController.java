package ru.checkdev.auth.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.dto.ProfileTgDTO;
import ru.checkdev.auth.service.ProfileService;

import java.util.List;

/**
 * CheckDev пробное собеседование
 * ProfileController контроллер отправки и приема DTO модели ProfileDTO
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023T23:49
 */

@Tag(name = "ProfileController", description = "Profile REST API")
@RestController
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    /**
     * Обрабатывает get запрос на получение профиля пользователя по запрошенному ID.
     *
     * @param id ID ProfileDTO
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable int id) {
        var profileDTO = profileService.findProfileByID(id);
        return new ResponseEntity<>(
                profileDTO.orElse(new ProfileDTO()),
                profileDTO.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    /**
     * Отправляет все профили пользователей
     *
     * @return ResponseEntity
     */
    @GetMapping("/")
    public ResponseEntity<List<ProfileDTO>> getAllProfilesOrderByCreateDesc() {
        var profiles = profileService.findProfilesOrderByCreatedDesc();
        return new ResponseEntity<>(
                profiles,
                profiles.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    /**
     * Обрабатывает get запрос на получение профиля пользователя по запрошенному ID.
     * Для телеграм бота
     *
     * @param id ProfileTg ID
     * @return ResponseEntity<ProfileTgDTO>
     */
    @GetMapping("/tg/{id}")
    public ResponseEntity<ProfileTgDTO> getProfileTgById(@PathVariable int id) {
        var profileDTO = profileService.findProfileTgByID(id);
        return new ResponseEntity<>(
                profileDTO.orElse(new ProfileTgDTO()),
                profileDTO.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping("/tg/byEmailAndPassword")
    public ResponseEntity<ProfileTgDTO> getProfileTgByEmailAndPassword(@RequestBody Profile profile) {
        var profileDTO = profileService.findProfileTgByEmailAndPassword(profile.getEmail(), profile.getPassword());
        return new ResponseEntity<>(
                profileDTO.orElse(new ProfileTgDTO()),
                profileDTO.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

}
