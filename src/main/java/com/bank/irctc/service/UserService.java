package com.bank.irctc.service;

import com.bank.irctc.entity.User;
import com.bank.irctc.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET ALL USERS
    // =========================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =========================
    // GET USER BY ID
    // =========================
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + id
                        )
                );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // =========================
    // UPDATE USER
    // =========================
    public User updateUser(Long id, User user) {

        User existingUser = getUserById(id);

        existingUser.setName(user.getName());
        existingUser.setMobile(user.getMobile());

        // Email update only if new email is not used
        if (user.getEmail() != null &&
                !user.getEmail().equals(existingUser.getEmail())) {

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException(
                        "Email already registered"
                );
            }

            existingUser.setEmail(user.getEmail());
        }

        // Password update
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );
        }

        return userRepository.save(existingUser);
    }

    // =========================
    // DELETE USER
    // =========================
    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }
}