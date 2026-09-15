package com.studytrack.repository;

import com.studytrack.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserEmail(String email);
    Optional<Student> findByUserId(Long userId);

    @Query("SELECT s FROM Student s JOIN FETCH s.user u")
    List<Student> findAllWithUser();

    @Query("SELECT s FROM Student s JOIN FETCH s.user u WHERE u.status = 'ACTIVE'")
    List<Student> findAllActive();
}
