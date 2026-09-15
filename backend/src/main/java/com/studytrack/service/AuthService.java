package com.studytrack.service;

import com.studytrack.dto.auth.AuthResponse;
import com.studytrack.dto.auth.LoginRequest;
import com.studytrack.dto.auth.RegisterRequest;
import com.studytrack.dto.auth.UserDto;
import com.studytrack.entity.*;
import com.studytrack.exception.BadRequestException;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.repository.RoadmapRepository;
import com.studytrack.repository.StudentRepository;
import com.studytrack.repository.StudentRoadmapRepository;
import com.studytrack.repository.UserRepository;
import com.studytrack.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoadmapRepository roadmapRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, StudentRepository studentRepository, RoadmapRepository roadmapRepository, StudentRoadmapRepository studentRoadmapRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.roadmapRepository = roadmapRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }


    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long studentId = null;
        if (user.getRole() == Role.ROLE_STUDENT) {
            studentId = studentRepository.findByUserEmail(user.getEmail())
                    .map(Student::getId)
                    .orElse(null);
        }

        return AuthResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .studentId(studentId)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);

        Student student = Student.builder()
                .user(user)
                .phone(request.getPhone())
                .college(request.getCollege())
                .enrollmentNo(request.getEnrollmentNo())
                .currentStreak(0)
                .longestStreak(0)
                .totalStudyMinutes(0L)
                .build();

        student = studentRepository.save(student);

        if (request.getRoadmapId() != null) {
            Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
                    .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found"));

            StudentRoadmap sr = StudentRoadmap.builder()
                    .student(student)
                    .roadmap(roadmap)
                    .assignedAt(LocalDateTime.now())
                    .status("ACTIVE")
                    .completionPercentage(0.0)
                    .build();
            studentRoadmapRepository.save(sr);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = tokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .studentId(student.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long studentId = null;
        if (user.getRole() == Role.ROLE_STUDENT) {
            studentId = studentRepository.findByUserEmail(user.getEmail())
                    .map(Student::getId)
                    .orElse(null);
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .studentId(studentId)
                .build();
    }
}
