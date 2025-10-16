package ru.checkdev.auth.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.ProfileTgDTO;
import ru.checkdev.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * @author parsentev
 * @since 26.09.2016
 */

@Tag(name = "AuthController", description = "Authentication REST API")
@RestController
public class AuthController {
    private final PersonService persons;
    private final String ping = "{}";

    @Autowired
    public AuthController(final PersonService persons) {
        this.persons = persons;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/ping")
    public String ping() {
        return this.ping;
    }

    @GetMapping("/auth/activated/{key}")
    public Object activated(@PathVariable String key) {
        if (this.persons.activated(key)) {
            return new Object() {
                public boolean getSuccess() {
                    return true;
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "Notify has already activated";
                }
            };
        }
    }

    @PostMapping("/registration")
    public Object registration(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.reg(profile);
        if (result.isPresent()) {
            return new ProfileTgDTO(result.get().getId(),
                    result.get().getUsername(),
                    result.get().getEmail());
        } else {
            return new Object() {
                public String getError() {
                    return String.format("Пользователь с почтой %s уже существует.", profile.getEmail());
                }
            };
        }
    }

    @PostMapping("/forgot")
    public Object forgot(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.forgot(profile);
        if (result.isPresent()) {
            return new Object() {
                public String getOk() {
                    return "ok";
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "E-mail не найден.";
                }
            };
        }
    }

    @PostMapping("/forgotTg")
    public Object forgotTg(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.forgotTg(profile);
        if (result.isPresent()) {
            return new Object() {
                public String getOk() {
                    return "ok";
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "E-mail не найден.";
                }
            };
        }
    }


    @GetMapping("/revoke")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {

    }
}