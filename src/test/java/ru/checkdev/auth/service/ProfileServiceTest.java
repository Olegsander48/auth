package ru.checkdev.auth.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * CheckDev пробное собеседование
 * ProfileServiceTest тестирование слоя бизнес логики обработки модели ProfileDTO
 *
 * @author Dmitry Stepanov
 * @version 01:11
 */

@SpringBootTest
public class ProfileServiceTest {
    private static final int ID_OK = 1;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ProfileService profileService;

    private final ProfileDTO profileDTO1 = new ProfileDTO(
            1, "name1", "experience1", 1, null, null);
    private final ProfileDTO profileDTO2 = new ProfileDTO(
            2, "name2", "experience2", 2, null, null);

    @Test
    public void whenFindByIDThenReturnOptionalProfileDTO() {
        when(personRepository.findProfileById(ID_OK)).thenReturn(profileDTO1);
        var actual = profileService.findProfileByID(ID_OK);
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(profileDTO1);
    }

    @Test
    public void whenFindByIDThenReturnEmpty() {
        when(personRepository.findProfileById(anyInt())).thenReturn(null);
        var actual = profileService.findProfileByID(anyInt());
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindProfilesOrderByCreatedDescThenReturnEmptyList() {
        when(personRepository.findProfileOrderByCreatedDesc()).thenReturn(Collections.emptyList());
        var actual = profileService.findProfilesOrderByCreatedDesc();
        assertThat(actual).usingRecursiveComparison().isEqualTo(Collections.emptyList());

    }

    @Test
    public void whenFindProfilesOrderByCreatedDescThenReturnListProfileDTO() {
        var expected = List.of(profileDTO1, profileDTO2);
        when(personRepository.findProfileOrderByCreatedDesc()).thenReturn(expected);
        var actual = profileService.findProfilesOrderByCreatedDesc();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}