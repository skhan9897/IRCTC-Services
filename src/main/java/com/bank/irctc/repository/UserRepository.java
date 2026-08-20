package com.bank.irctc.repository;

import com.bank.irctc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by mobile
    Optional<User> findByMobile(String mobile);

    // Check email already exists
    boolean existsByEmail(String email);

    // Check mobile already exists
    boolean existsByMobile(String mobile);

    // Find users by role
    List<User> findByRole(String role);
}