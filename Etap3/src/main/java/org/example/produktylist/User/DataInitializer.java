package org.example.produktylist.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!userRepository.findByUserName("admin").isPresent()) {
            User adminUser = new User();
            adminUser.setUserName("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setActive(true);
            adminUser.setRoles("ROLE_ADMIN");
            userRepository.save(adminUser);
        }

        if (!userRepository.findByUserName("user").isPresent()) {
            User user = new User();
            user.setUserName("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setActive(true);
            user.setRoles("ROLE_USER");
            userRepository.save(user);
        }
    }
}
