package com.studytrack.service;

import com.studytrack.dto.roadmap.*;
import com.studytrack.entity.*;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final SubjectRepository subjectRepository;
    private final ModuleRepository moduleRepository;
    private final TopicRepository topicRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

    public RoadmapService(RoadmapRepository roadmapRepository, SubjectRepository subjectRepository, ModuleRepository moduleRepository, TopicRepository topicRepository, StudentRoadmapRepository studentRoadmapRepository, StudentRepository studentRepository, TaskRepository taskRepository, NotificationRepository notificationRepository) {
        this.roadmapRepository = roadmapRepository;
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.topicRepository = topicRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.studentRepository = studentRepository;
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    

    public List<RoadmapDto> getAllRoadmaps() {
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        return roadmaps.stream().map(this::toSummaryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoadmapDto getRoadmapById(Long id) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found with id: " + id));
        return toFullDto(roadmap, null);
    }

    @Transactional
    public RoadmapDto createRoadmap(RoadmapDto dto) {
        Roadmap roadmap = Roadmap.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .level(dto.getLevel())
                .estimatedHours(dto.getEstimatedHours() != null ? dto.getEstimatedHours() : 40)
                .build();

        roadmap = roadmapRepository.save(roadmap);

        if (dto.getSubjects() != null) {
            int sIdx = 0;
            for (SubjectDto sDto : dto.getSubjects()) {
                Subject subject = Subject.builder()
                        .roadmap(roadmap)
                        .title(sDto.getTitle())
                        .description(sDto.getDescription())
                        .orderIndex(sIdx++)
                        .build();
                subject = subjectRepository.save(subject);

                if (sDto.getModules() != null) {
                    int mIdx = 0;
                    for (ModuleDto mDto : sDto.getModules()) {
                        RoadmapModule module = RoadmapModule.builder()
                                .subject(subject)
                                .title(mDto.getTitle())
                                .description(mDto.getDescription())
                                .orderIndex(mIdx++)
                                .build();
                        module = moduleRepository.save(module);

                        if (mDto.getTopics() != null) {
                            int tIdx = 0;
                            for (TopicDto tDto : mDto.getTopics()) {
                                Topic topic = Topic.builder()
                                        .module(module)
                                        .title(tDto.getTitle())
                                        .description(tDto.getDescription())
                                        .orderIndex(tIdx++)
                                        .build();
                                topicRepository.save(topic);
                            }
                        }
                    }
                }
            }
        }

        return getRoadmapById(roadmap.getId());
    }

    @Transactional
    public RoadmapDto updateRoadmap(Long id, RoadmapDto dto) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found with id: " + id));

        roadmap.setTitle(dto.getTitle());
        roadmap.setDescription(dto.getDescription());
        roadmap.setCategory(dto.getCategory());
        roadmap.setLevel(dto.getLevel());
        if (dto.getEstimatedHours() != null) {
            roadmap.setEstimatedHours(dto.getEstimatedHours());
        }

        roadmap = roadmapRepository.save(roadmap);
        return getRoadmapById(roadmap.getId());
    }

    @Transactional
    public void deleteRoadmap(Long id) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found with id: " + id));
        roadmapRepository.delete(roadmap);
    }

    @Transactional
    public void assignRoadmap(Long studentId, Long roadmapId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found with id: " + roadmapId));

        // Deactivate any previous active roadmap
        List<StudentRoadmap> existing = studentRoadmapRepository.findByStudentId(studentId);
        for (StudentRoadmap sr : existing) {
            sr.setStatus("INACTIVE");
            studentRoadmapRepository.save(sr);
        }

        StudentRoadmap studentRoadmap = StudentRoadmap.builder()
                .student(student)
                .roadmap(roadmap)
                .assignedAt(LocalDateTime.now())
                .status("ACTIVE")
                .completionPercentage(0.0)
                .build();

        studentRoadmapRepository.save(studentRoadmap);

        // Notify student
        Notification notification = Notification.builder()
                .student(student)
                .title("New Roadmap Assigned")
                .message("You have been assigned the roadmap: " + roadmap.getTitle())
                .type(NotificationType.ROADMAP_UPDATE)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public RoadmapDto getStudentRoadmap(String studentEmail) {
        Student student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + studentEmail));

        StudentRoadmap activeSr = studentRoadmapRepository.findActiveByStudentEmail(studentEmail)
                .orElse(null);

        if (activeSr == null) {
            return null;
        }

        return toFullDto(activeSr.getRoadmap(), student);
    }

    public RoadmapDto toSummaryDto(Roadmap roadmap) {
        List<Subject> subjects = subjectRepository.findByRoadmapIdOrderByOrderIndexAsc(roadmap.getId());
        int totalModules = 0;
        int totalTopics = 0;

        for (Subject s : subjects) {
            List<RoadmapModule> mods = moduleRepository.findBySubjectIdOrderByOrderIndexAsc(s.getId());
            totalModules += mods.size();
            for (RoadmapModule m : mods) {
                totalTopics += topicRepository.findByModuleIdOrderByOrderIndexAsc(m.getId()).size();
            }
        }

        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .category(roadmap.getCategory())
                .level(roadmap.getLevel())
                .estimatedHours(roadmap.getEstimatedHours())
                .totalSubjects(subjects.size())
                .totalModules(totalModules)
                .totalTopics(totalTopics)
                .createdAt(roadmap.getCreatedAt())
                .updatedAt(roadmap.getUpdatedAt())
                .build();
    }

    public RoadmapDto toFullDto(Roadmap roadmap, Student student) {
        List<Subject> subjects = subjectRepository.findByRoadmapIdOrderByOrderIndexAsc(roadmap.getId());
        List<SubjectDto> subjectDtos = new ArrayList<>();

        // Fetch completed task topics for this student if present
        Set<Long> completedTopicIds = new HashSet<>();
        Set<Long> inProgressTopicIds = new HashSet<>();

        if (student != null) {
            List<Task> studentTasks = taskRepository.findByStudentIdAndRoadmapId(student.getId(), roadmap.getId());
            for (Task t : studentTasks) {
                if (t.getTopic() != null) {
                    if (t.getStatus() == TaskStatus.COMPLETED) {
                        completedTopicIds.add(t.getTopic().getId());
                    } else if (t.getStatus() == TaskStatus.IN_PROGRESS) {
                        inProgressTopicIds.add(t.getTopic().getId());
                    }
                }
            }
        }

        int totalRoadmapTopics = 0;
        int totalCompletedRoadmapTopics = 0;

        for (Subject s : subjects) {
            List<RoadmapModule> modules = moduleRepository.findBySubjectIdOrderByOrderIndexAsc(s.getId());
            List<ModuleDto> moduleDtos = new ArrayList<>();

            int subjectTotalTopics = 0;
            int subjectCompletedTopics = 0;

            for (RoadmapModule m : modules) {
                List<Topic> topics = topicRepository.findByModuleIdOrderByOrderIndexAsc(m.getId());
                List<TopicDto> topicDtos = new ArrayList<>();

                int moduleCompletedTopics = 0;

                for (Topic t : topics) {
                    String status = "NOT_STARTED";
                    if (completedTopicIds.contains(t.getId())) {
                        status = "COMPLETED";
                        moduleCompletedTopics++;
                    } else if (inProgressTopicIds.contains(t.getId())) {
                        status = "IN_PROGRESS";
                    }

                    topicDtos.add(TopicDto.builder()
                            .id(t.getId())
                            .moduleId(m.getId())
                            .title(t.getTitle())
                            .description(t.getDescription())
                            .orderIndex(t.getOrderIndex())
                            .status(status)
                            .build());
                }

                double modulePct = topics.isEmpty() ? 0.0 : Math.round(((double) moduleCompletedTopics / topics.size()) * 100.0 * 10.0) / 10.0;
                moduleDtos.add(ModuleDto.builder()
                        .id(m.getId())
                        .subjectId(s.getId())
                        .title(m.getTitle())
                        .description(m.getDescription())
                        .orderIndex(m.getOrderIndex())
                        .topics(topicDtos)
                        .completionPercentage(modulePct)
                        .build());

                subjectTotalTopics += topics.size();
                subjectCompletedTopics += moduleCompletedTopics;
            }

            double subjectPct = subjectTotalTopics == 0 ? 0.0 : Math.round(((double) subjectCompletedTopics / subjectTotalTopics) * 100.0 * 10.0) / 10.0;
            subjectDtos.add(SubjectDto.builder()
                    .id(s.getId())
                    .roadmapId(roadmap.getId())
                    .title(s.getTitle())
                    .description(s.getDescription())
                    .orderIndex(s.getOrderIndex())
                    .modules(moduleDtos)
                    .totalTopics(subjectTotalTopics)
                    .completedTopics(subjectCompletedTopics)
                    .completionPercentage(subjectPct)
                    .build());

            totalRoadmapTopics += subjectTotalTopics;
            totalCompletedRoadmapTopics += subjectCompletedTopics;
        }

        double overallPct = totalRoadmapTopics == 0 ? 0.0 : Math.round(((double) totalCompletedRoadmapTopics / totalRoadmapTopics) * 100.0 * 10.0) / 10.0;

        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .category(roadmap.getCategory())
                .level(roadmap.getLevel())
                .estimatedHours(roadmap.getEstimatedHours())
                .totalSubjects(subjects.size())
                .totalModules(subjectDtos.stream().mapToInt(s -> s.getModules().size()).sum())
                .totalTopics(totalRoadmapTopics)
                .completionPercentage(overallPct)
                .subjects(subjectDtos)
                .createdAt(roadmap.getCreatedAt())
                .updatedAt(roadmap.getUpdatedAt())
                .build();
    }
}
