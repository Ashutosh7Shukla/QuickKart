package com.Shopping.dream_shop.data;

import com.Shopping.dream_shop.model.Role;
import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.repository.RoleRepository;
import com.Shopping.dream_shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        Collection<Role> userRoles = roleRepository.findByName("ROLE_USER");
        Role userRole = userRoles.stream().findFirst().orElse(null);
        for (int i = 1; i <= 6; i++) {
            String defaultEmail = "user" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("the user");
            user.setLastName("user" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("default user :- " + i + " created success");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Collection<Role> userRoles = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = userRoles.stream().findFirst().orElse(null);
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "Admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("the Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("default admin :- " + i + " created success");
        }
    }


    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);
    }
}
