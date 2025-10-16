package ru.checkdev.auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author parsentev
 * @since 21.09.2016
 */

@SpringBootTest
public class PersonServiceTest {
    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository persons;

    @AfterEach
    @Test
    public void whenRegDuplicatePersonThenResultEmpty() {
        Profile profile = new Profile("Петр Арсентьев", "parsentev@yandex.ru", "password");
        this.service.reg(profile);
        Optional<Profile> result = this.service.reg(profile);
        assertTrue(result.isEmpty());
    }

    @Test
    public void whenRegPersonRolesThenDropRoles() {
        Profile profile = new Profile("Петр Арсентьев", "parsentev@yandex.ru", "password");
        profile.setKey("test");
        this.persons.save(profile);
    }

    @Test
    public void whenSelectAllPersonsThenListContainTestRecord() {
        when(persons.findAll()).thenReturn(List.of(new Profile()));
        List<Profile> profileList = this.service.getAll();
        assertFalse(profileList.isEmpty());
    }
}