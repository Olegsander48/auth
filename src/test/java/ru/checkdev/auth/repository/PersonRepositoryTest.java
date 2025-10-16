package ru.checkdev.auth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.checkdev.auth.dto.ProfileDTO;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * CheckDev пробное собеседование
 * Тест на класс PersonRepository
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023'T'21:14
 */
@DataJpaTest()
public class PersonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void clearTable() {
        entityManager.getEntityManager().createQuery("delete from profile").executeUpdate();
    }

    @Test
    public void injectedComponentAreNotNull() {
        assertNotNull(entityManager);
        assertNotNull(personRepository);
    }

    @Test
    public void whenFindProfileByIdThenReturnNull() {
        ProfileDTO profileDTO = personRepository.findProfileById(-1);
        assertNull(profileDTO);
    }

    @Test
    public void whenFindProfileOrderByCreatedDescThenReturnEmptyList() {
        var listProfileDTO = personRepository.findProfileOrderByCreatedDesc();
        assertThat(listProfileDTO).isEqualTo(Collections.emptyList());
    }
}