package com.studytrack.config;

import com.studytrack.entity.Role;
import com.studytrack.entity.User;
import com.studytrack.entity.UserStatus;
import com.studytrack.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoadmapRepository roadmapRepository;
    private final SubjectRepository subjectRepository;
    private final ModuleRepository moduleRepository;
    private final TopicRepository topicRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final TaskRepository taskRepository;
    private final StudyLogRepository studyLogRepository;
    private final StudyNoteRepository studyNoteRepository;
    private final NotificationRepository notificationRepository;
    private final AnnouncementRepository announcementRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      StudentRepository studentRepository,
                      RoadmapRepository roadmapRepository,
                      SubjectRepository subjectRepository,
                      ModuleRepository moduleRepository,
                      TopicRepository topicRepository,
                      StudentRoadmapRepository studentRoadmapRepository,
                      TaskRepository taskRepository,
                      StudyLogRepository studyLogRepository,
                      StudyNoteRepository studyNoteRepository,
                      NotificationRepository notificationRepository,
                      AnnouncementRepository announcementRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.roadmapRepository = roadmapRepository;
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.topicRepository = topicRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.taskRepository = taskRepository;
        this.studyLogRepository = studyLogRepository;
        this.studyNoteRepository = studyNoteRepository;
        this.notificationRepository = notificationRepository;
        this.announcementRepository = announcementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Detect if any legacy demo data is present
        boolean hasLegacyDemo = userRepository.existsByEmail("rahul@studytrack.com")
                || userRepository.existsByEmail("priya@studytrack.com")
                || userRepository.existsByEmail("amit@studytrack.com")
                || roadmapRepository.findAll().stream().anyMatch(r ->
                        r.getTitle().toLowerCase().contains("full stack java") ||
                        r.getTitle().toLowerCase().contains("data structures"));

        if (hasLegacyDemo) {
            log.info("Detected legacy demo data. Performing one-time cleanup to ensure 100% clean database...");

            notificationRepository.deleteAll();
            notificationRepository.flush();

            studyNoteRepository.deleteAll();
            studyNoteRepository.flush();

            taskRepository.deleteAll();
            taskRepository.flush();

            studyLogRepository.deleteAll();
            studyLogRepository.flush();

            studentRoadmapRepository.deleteAll();
            studentRoadmapRepository.flush();

            topicRepository.deleteAll();
            topicRepository.flush();
            moduleRepository.deleteAll();
            moduleRepository.flush();
            subjectRepository.deleteAll();
            subjectRepository.flush();
            roadmapRepository.deleteAll();
            roadmapRepository.flush();

            studentRepository.deleteAll();
            studentRepository.flush();

            announcementRepository.deleteAll();
            announcementRepository.flush();

            List<User> nonAdmins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() != Role.ROLE_ADMIN)
                    .toList();
            if (!nonAdmins.isEmpty()) {
                userRepository.deleteAll(nonAdmins);
                userRepository.flush();
            }
            log.info("One-time demo data purge completed successfully.");
        }

        // Ensure master administrator account exists
        if (!userRepository.existsByEmail("admin@studytrack.com")) {
            User admin = User.builder()
                    .email("admin@studytrack.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .fullName("System Administrator")
                    .role(Role.ROLE_ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
            log.info("Master Administrator initialized: admin@studytrack.com / Admin@123");
        } else {
            log.info("Master Administrator account ready: admin@studytrack.com");
        }

        log.info("System ready. Zero demo data seeded. User-created data is fully preserved across restarts.");
    }
}
