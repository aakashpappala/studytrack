package com.studytrack.repository;

import com.studytrack.entity.Role;
import com.studytrack.entity.User;
import com.studytrack.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    long countByRole(Role role);
    long countByRoleAndStatus(Role role, UserStatus status);
}
