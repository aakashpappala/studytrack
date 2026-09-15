package com.studytrack.service;

import com.studytrack.entity.Student;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    

    public Student getStudentByEmail(String email) {
        return studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for email: " + email));
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Transactional
    public void recordActivityAndCalculateStreak(Student student, int addedStudyMinutes, LocalDate activityDate) {
        if (activityDate == null) {
            activityDate = LocalDate.now();
        }

        if (addedStudyMinutes > 0) {
            long currentTotal = student.getTotalStudyMinutes() != null ? student.getTotalStudyMinutes() : 0L;
            student.setTotalStudyMinutes(currentTotal + addedStudyMinutes);
        }

        LocalDate lastDate = student.getLastStudyDate();
        int currentStreak = student.getCurrentStreak() != null ? student.getCurrentStreak() : 0;
        int longestStreak = student.getLongestStreak() != null ? student.getLongestStreak() : 0;

        if (lastDate == null) {
            currentStreak = 1;
        } else if (lastDate.equals(activityDate)) {
            // Same day activity, maintain streak
            if (currentStreak == 0) {
                currentStreak = 1;
            }
        } else if (lastDate.equals(activityDate.minusDays(1))) {
            // Consecutive day!
            currentStreak += 1;
        } else if (activityDate.isAfter(lastDate)) {
            // Missed one or more days!
            currentStreak = 1;
        }

        student.setLastStudyDate(activityDate);
        student.setCurrentStreak(currentStreak);
        student.setLongestStreak(Math.max(longestStreak, currentStreak));

        studentRepository.save(student);
    }
}
