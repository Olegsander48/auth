package ru.checkdev.auth.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.service.PersonService;
import ru.checkdev.auth.service.RoleService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author parsentev
 * @since 30.09.2016
 */

@Tag(name = "PersonController", description = "Person REST API")
@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PasswordEncoder passwordEncoder;
    private final PersonService persons;
    private final RoleService roles;

    @GetMapping("/current")
    public Profile getCurrent(Principal user) {
        return persons.findByEmail(user.getName()).get();
    }

    @PostMapping("/hh")
    public Profile getPersonFromHh(@RequestBody String linc) {
        return persons.loadFromHh(linc);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list/{limit}/{page}/{search}")
    public ResponseEntity<HashMap<String, Object>> getAll(@PathVariable int limit, @PathVariable int page, @PathVariable String search, Principal user) throws ServletException {
        HashMap<String, Object> data = new HashMap<>();
        if ("noSearch".equals(search)) {
            data.put("users", persons.findAll(PageRequest.of(page, limit, Sort.Direction.ASC, "email")));
        } else {
            data.put("users", persons.findBySearch(search, PageRequest.of(page, limit, Sort.Direction.ASC, "email")));
        }
        data.put("total", persons.total());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> get(@PathVariable int id, Principal user) throws ServletException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("person", persons.findById(id));
        data.put("roles", roles.findAll());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public Profile loadProfile(@RequestParam String key) throws ServletException {
        return persons.findByKey(key);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/")
    public Profile save(@RequestBody Profile profile, Principal user) throws ServletException {
        persons.saveRole(profile);
        return profile;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/updateMultipart")
    public ResponseEntity<String> update(@RequestPart Profile profile, @RequestPart(required = false) MultipartFile file, Principal user) throws ServletException, IOException {
        String email = ((Map<String, String>) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("username");
        profile.setEmail(email);
        return new ResponseEntity<>(persons.update(email, file, profile), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Object createPerson(@RequestBody Profile profile, Principal user) {
        Optional<Profile> result = persons.create(profile);
        return result.<Object>map(per -> new Object() {
            public Profile getPerson() {
                return per;
            }
        }).orElseGet(() -> new Object() {
            public String getError() {
                return String.format("Пользователь с почтой %s уже существует.", profile.getEmail());
            }
        });
    }

    @PutMapping("/changePassword")
    public void changePassword(@RequestBody Profile profile, Principal user) {
        String email = ((Map<String, String>) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("username");
        persons.changePassword(profile, email);
    }

    @PutMapping("/pass")
    public ResponseEntity<String> changePass(@RequestBody Profile.Password password) {
        Profile profileDb = persons.findById(password.getId());
        String response;
        if (passwordEncoder.matches(password.getPassword(), profileDb.getPassword())) {
            profileDb.setPassword(passwordEncoder.encode(password.getNewPass()));
            persons.save(profileDb);
            response = "ok";
        } else {
            response = "Введите правильный пароль";
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<Map<String, Object>> showRandomPerson() {
        Map<String, Object> map = new HashMap<>();
        map.put("resume", persons.findByShow(true, 5));
        map.put("countResume", persons.showed());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/resume/{limit}/{page}")
    public ResponseEntity<Map<String, Object>> getResume(@PathVariable int limit, @PathVariable int page) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> map = new HashMap<>();
        map.put("personsShowed", persons.findByShow(true, PageRequest.of(pageToShow, limit)));
        map.put("getTotal", persons.showed());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}