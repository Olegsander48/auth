package ru.checkdev.auth.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.checkdev.auth.domain.Role;
import ru.checkdev.auth.repository.RoleRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * @author parsentev
 * @since 21.09.2016
 */
@SpringBootTest
public class RoleServiceTest {
    @InjectMocks
    private RoleService service;
    @Mock
    private RoleRepository roles;

    @Test
    public void whenAddRolesThenPersonHasRoles() {
        Role role = new Role("ROLE_ADMIN");
        when(roles.save(any(Role.class))).thenReturn(role);
        Role result = this.service.save(new Role("ROLE_ADMIN"));
        assertThat(role).usingRecursiveComparison().isEqualTo(result);
    }
}