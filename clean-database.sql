-- ==========================================================
-- StudyTrack Database Reset Script
-- Cleans all tables with 0 demo data.
-- Only the master administrator account will be initialized by Spring Boot on startup.
-- ==========================================================

USE studytrack_db;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM notifications;
DELETE FROM study_notes;
DELETE FROM tasks;
DELETE FROM study_logs;
DELETE FROM student_roadmaps;
DELETE FROM topics;
DELETE FROM modules;
DELETE FROM subjects;
DELETE FROM roadmaps;
DELETE FROM students;
DELETE FROM announcements;
DELETE FROM users WHERE role != 'ROLE_ADMIN';

SET FOREIGN_KEY_CHECKS = 1;
