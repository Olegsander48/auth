package ru.checkdev.auth.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.auth.domain.Role;
import ru.checkdev.auth.repository.RoleRepository;

import java.util.List;

/**
 * @author parsentev
 * @since 26.09.2016
 */

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roles;

    public Role save(Role role) {
        return this.roles.save(role);
    }

    public List<Role> findAll() {
        return Lists.newArrayList(this.roles.findAll());
    }
}
