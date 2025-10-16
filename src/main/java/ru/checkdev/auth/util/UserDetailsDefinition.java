package ru.checkdev.auth.util;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserDetailsDefinition implements UserDetailsService {

    private final PersonRepository persons;

    @Override
    public UserDetails loadUserByUsername(final String email) throws DisabledException {
        Profile profile = persons.findByEmail(email);
        if (profile != null) {
            if (profile.isActive()) {
                return new User(email,
                        profile.getPassword(),
                        profile.getRoles().stream()
                                .map(
                                        role -> new SimpleGrantedAuthority(role.getValue())
                                ).collect(Collectors.toList())
                ) {
                    public String getKey() {
                        return profile.getKey();
                    }
                };
            } else {
                throw new DisabledException(String.format("Пользователь с почтой %s не активирован.", email));
            }
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
