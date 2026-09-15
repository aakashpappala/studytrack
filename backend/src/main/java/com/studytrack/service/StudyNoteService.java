package com.studytrack.service;

import com.studytrack.dto.note.StudyNoteDto;
import com.studytrack.dto.note.StudyNoteRequest;
import com.studytrack.entity.Student;
import com.studytrack.entity.StudyNote;
import com.studytrack.entity.Task;
import com.studytrack.entity.Topic;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.exception.UnauthorizedException;
import com.studytrack.repository.StudyNoteRepository;
import com.studytrack.repository.TaskRepository;
import com.studytrack.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyNoteService {

    private final StudyNoteRepository studyNoteRepository;
    private final StudentService studentService;
    private final TopicRepository topicRepository;
    private final TaskRepository taskRepository;

    public StudyNoteService(StudyNoteRepository studyNoteRepository, StudentService studentService, TopicRepository topicRepository, TaskRepository taskRepository) {
        this.studyNoteRepository = studyNoteRepository;
        this.studentService = studentService;
        this.topicRepository = topicRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    

    public StudyNoteDto createNote(String studentEmail, StudyNoteRequest request) {
        Student student = studentService.getStudentByEmail(studentEmail);

        Topic topic = request.getTopicId() != null
                ? topicRepository.findById(request.getTopicId()).orElse(null)
                : null;
        Task task = request.getTaskId() != null
                ? taskRepository.findById(request.getTaskId()).orElse(null)
                : null;

        StudyNote note = StudyNote.builder()
                .student(student)
                .topic(topic)
                .task(task)
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .build();

        note = studyNoteRepository.save(note);
        return toDto(note);
    }

    @Transactional(readOnly = true)
    public List<StudyNoteDto> getNotes(String studentEmail) {
        Student student = studentService.getStudentByEmail(studentEmail);
        return studyNoteRepository.findByStudentIdOrderByUpdatedAtDesc(student.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyNoteDto updateNote(Long id, StudyNoteRequest request, String studentEmail) {
        StudyNote note = studyNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        if (!note.getStudent().getUser().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You are not authorized to edit this note");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setTags(request.getTags());

        if (request.getTopicId() != null) {
            note.setTopic(topicRepository.findById(request.getTopicId()).orElse(null));
        }
        if (request.getTaskId() != null) {
            note.setTask(taskRepository.findById(request.getTaskId()).orElse(null));
        }

        note = studyNoteRepository.save(note);
        return toDto(note);
    }

    @Transactional
    public void deleteNote(Long id, String studentEmail) {
        StudyNote note = studyNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        if (!note.getStudent().getUser().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You are not authorized to delete this note");
        }

        studyNoteRepository.delete(note);
    }

    public StudyNoteDto toDto(StudyNote note) {
        return StudyNoteDto.builder()
                .id(note.getId())
                .studentId(note.getStudent().getId())
                .topicId(note.getTopic() != null ? note.getTopic().getId() : null)
                .topicTitle(note.getTopic() != null ? note.getTopic().getTitle() : null)
                .taskId(note.getTask() != null ? note.getTask().getId() : null)
                .taskTitle(note.getTask() != null ? note.getTask().getTitle() : null)
                .title(note.getTitle())
                .content(note.getContent())
                .tags(note.getTags())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
