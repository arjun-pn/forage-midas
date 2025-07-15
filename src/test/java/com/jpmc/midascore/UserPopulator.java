package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserPopulator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void populate() {
        userRepository.save(new UserRecord("waldorf", 1000));
        userRepository.save(new UserRecord("john", 1000));
        userRepository.save(new UserRecord("alice", 1000));
        userRepository.save(new UserRecord("bob", 1000));
        userRepository.save(new UserRecord("charlie", 1000));
        userRepository.save(new UserRecord("david", 1000));
        userRepository.save(new UserRecord("emma", 1000));
        userRepository.save(new UserRecord("frank", 1000));
        userRepository.save(new UserRecord("grace", 1000));
        userRepository.save(new UserRecord("henry", 1000));
    }

    @Override
    public void run(String... args) {
        populate(); // Calls the same method for boot-time init
    }
}

