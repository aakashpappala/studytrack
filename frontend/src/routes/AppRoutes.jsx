import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import ProtectedRoute from './ProtectedRoute';

// Auth Pages
import Login from '../pages/auth/Login';

// Student Pages
import StudentDashboard from '../pages/student/StudentDashboard';
import MyRoadmapPage from '../pages/student/MyRoadmapPage';
import TodayTasksPage from '../pages/student/TodayTasksPage';
import StudyLogPage from '../pages/student/StudyLogPage';
import CalendarPage from '../pages/student/CalendarPage';
import NotesPage from '../pages/student/NotesPage';
import ProgressPage from '../pages/student/ProgressPage';
import ProfilePage from '../pages/student/ProfilePage';

// Admin Pages
import AdminDashboard from '../pages/admin/AdminDashboard';
import StudentsPage from '../pages/admin/StudentsPage';
import StudentDetailPage from '../pages/admin/StudentDetailPage';
import RoadmapsPage from '../pages/admin/RoadmapsPage';
import AdminTasksPage from '../pages/admin/AdminTasksPage';
import AdminAnalyticsPage from '../pages/admin/AdminAnalyticsPage';
import AnnouncementsPage from '../pages/admin/AnnouncementsPage';
import SettingsPage from '../pages/admin/SettingsPage';

export default function AppRoutes() {
  const { user, token } = useAuth();

  // Root redirector
  const renderRootRedirect = () => {
    if (!token || !user) {
      return <Navigate to="/login" replace />;
    }
    if (user.role === 'ROLE_ADMIN') {
      return <Navigate to="/admin/dashboard" replace />;
    }
    return <Navigate to="/dashboard" replace />;
  };

  return (
    <Routes>
      {/* Public Route */}
      <Route
        path="/login"
        element={
          token && user ? (
            user.role === 'ROLE_ADMIN' ? (
              <Navigate to="/admin/dashboard" replace />
            ) : (
              <Navigate to="/dashboard" replace />
            )
          ) : (
            <Login />
          )
        }
      />

      {/* Root Route */}
      <Route path="/" element={renderRootRedirect()} />

      {/* Student Protected Routes */}
      <Route element={<ProtectedRoute allowedRoles={['ROLE_STUDENT']} />}>
        <Route path="/dashboard" element={<StudentDashboard />} />
        <Route path="/my-roadmap" element={<MyRoadmapPage />} />
        <Route path="/today-tasks" element={<TodayTasksPage />} />
        <Route path="/study-log" element={<StudyLogPage />} />
        <Route path="/calendar" element={<CalendarPage />} />
        <Route path="/notes" element={<NotesPage />} />
        <Route path="/progress" element={<ProgressPage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Route>

      {/* Admin Protected Routes */}
      <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/admin/students" element={<StudentsPage />} />
        <Route path="/admin/students/:id" element={<StudentDetailPage />} />
        <Route path="/admin/roadmaps" element={<RoadmapsPage />} />
        <Route path="/admin/tasks" element={<AdminTasksPage />} />
        <Route path="/admin/analytics" element={<AdminAnalyticsPage />} />
        <Route path="/admin/announcements" element={<AnnouncementsPage />} />
        <Route path="/admin/settings" element={<SettingsPage />} />
      </Route>

      {/* Catch-all fallback */}
      <Route path="*" element={renderRootRedirect()} />
    </Routes>
  );
}
