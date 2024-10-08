package com.online_store.web.generator.service;

import com.online_store.web.user.dao.UserRepository;
import com.online_store.web.user.model.Role;
import com.online_store.web.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserGeneratorService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<User> users = new ArrayList<>();

    public void generateUsers(int amount) {
        // start time
        long time = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            users.add(createUser());
        }
        userRepository.saveAll(users);
        long time2 = System.currentTimeMillis();
        long duration = time2 - time;
        log.info("Users generated in {} ms", duration);
    }

    private User createUser() {
        Faker faker = new Faker();
        User user = new User();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(passwordEncoder.encode(faker.internet().password(6, 10)));
        user.setActive(true);
        user.setRoles(Set.of(Role.USER));
        return user;
    }
}
