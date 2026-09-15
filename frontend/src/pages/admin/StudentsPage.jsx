import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import ProgressBar from '../../components/common/ProgressBar';
import Modal from '../../components/common/Modal';
import ConfirmationDialog from '../../components/common/ConfirmationDialog';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Users,
  Plus,
  Search,
  Filter,
  Eye,
  Edit2,
  Trash2,
  Flame,
  Clock,
  BookOpen,
  Map,
  CheckCircle2,
  AlertCircle
} from 'lucide-react';
import { Link } from 'react-router-dom';

export default function StudentsPage() {
  const [students, setStudents] = useState([]);
  const [roadmaps, setRoadmaps] = useState([]);
  const [loading, setLoading] = useState(true);

  // Filters
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [roadmapFilter, setRoadmapFilter] = useState('ALL');

  // Modals
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isAssignModalOpen, setIsAssignModalOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [deleteTargetId, setDeleteTargetId] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Forms
  const [createForm, setCreateForm] = useState({
    fullName: '',
    email: '',
    password: 'Student@123',
    phone: '',
    college: '',
    enrollmentNo: '',
    roadmapId: '',
  });

  const [editForm, setEditForm] = useState({
    fullName: '',
    email: '',
    phone: '',
    college: '',
    enrollmentNo: '',
    status: 'ACTIVE',
    roadmapId: '',
    newPassword: '',
  });

  const [assignForm, setAssignForm] = useState({
    roadmapId: '',
  });

  const fetchStudentsAndRoadmaps = async () => {
    try {
      const [studentsRes, roadmapsRes] = await Promise.all([
        api.get('/admin/students'),
        api.get('/roadmaps'),
      ]);
      setStudents(studentsRes.data.data || []);
      setRoadmaps(roadmapsRes.data.data || []);
    } catch (err) {
      console.error('Error fetching admin students data', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudentsAndRoadmaps();
  }, []);

  const handleCreateStudent = async (e) => {
    e.preventDefault();
    try {
      setSubmitting(true);
      const payload = {
        ...createForm,
        roadmapId: createForm.roadmapId ? Number(createForm.roadmapId) : null,
      };
      await api.post('/admin/students', payload);
      setIsAddModalOpen(false);
      setCreateForm({
        fullName: '',
        email: '',
        password: 'Student@123',
        phone: '',
        college: '',
        enrollmentNo: '',
        roadmapId: '',
      });
      await fetchStudentsAndRoadmaps();
    } catch (err) {
      console.error('Error creating student', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleOpenEdit = (student) => {
    setSelectedStudent(student);
    setEditForm({
      fullName: student.fullName || '',
      email: student.email || '',
      phone: student.phone || '',
      college: student.college || '',
      enrollmentNo: student.enrollmentNo || '',
      status: student.status || 'ACTIVE',
      roadmapId: student.roadmapId ? String(student.roadmapId) : '',
      newPassword: '',
    });
    setIsEditModalOpen(true);
  };

  const handleUpdateStudent = async (e) => {
    e.preventDefault();
    if (!selectedStudent) return;
    try {
      setSubmitting(true);
      const payload = {
        ...editForm,
        roadmapId: editForm.roadmapId ? Number(editForm.roadmapId) : null,
      };
      await api.put(`/admin/students/${selectedStudent.id}`, payload);
      setIsEditModalOpen(false);
      await fetchStudentsAndRoadmaps();
    } catch (err) {
      console.error('Error updating student', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleOpenAssign = (student) => {
    setSelectedStudent(student);
    setAssignForm({
      roadmapId: student.roadmapId ? String(student.roadmapId) : '',
    });
    setIsAssignModalOpen(true);
  };

  const handleAssignRoadmap = async (e) => {
    e.preventDefault();
    if (!selectedStudent || !assignForm.roadmapId) return;
    try {
      setSubmitting(true);
      await api.post('/roadmaps/assign', {
        studentId: selectedStudent.id,
        roadmapId: Number(assignForm.roadmapId),
      });
      setIsAssignModalOpen(false);
      await fetchStudentsAndRoadmaps();
    } catch (err) {
      console.error('Error assigning roadmap', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteStudent = async () => {
    const idToDelete = deleteTargetId;
    if (!idToDelete) return;
    try {
      setDeleteTargetId(null);
      await api.delete(`/admin/students/${idToDelete}`);
      await fetchStudentsAndRoadmaps();
    } catch (err) {
      console.error('Error deleting student', err);
      alert('Failed to delete student: ' + (err.response?.data?.message || err.message));
      await fetchStudentsAndRoadmaps();
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading student roster..." />;
  }

  // Filter logic
  const filteredStudents = students.filter((s) => {
    if (statusFilter !== 'ALL' && s.status !== statusFilter) return false;
    if (roadmapFilter !== 'ALL' && String(s.roadmapId) !== String(roadmapFilter)) return false;
    if (search.trim() !== '') {
      const q = search.toLowerCase();
      const matchName = s.fullName?.toLowerCase().includes(q);
      const matchEmail = s.email?.toLowerCase().includes(q);
      const matchEnroll = s.enrollmentNo?.toLowerCase().includes(q);
      const matchRoadmap = s.roadmapTitle?.toLowerCase().includes(q);
      if (!matchName && !matchEmail && !matchEnroll && !matchRoadmap) return false;
    }
    return true;
  });

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Top Banner */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-slate-900 tracking-tight">Student Cohort Management</h1>
          <p className="text-xs text-slate-500 mt-1">
            Oversee enrolled students, assign personalized curriculums, inspect progress telemetry, and manage accounts.
          </p>
        </div>
        <button
          onClick={() => setIsAddModalOpen(true)}
          className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/20 transition-all flex items-center gap-2 self-start"
        >
          <Plus className="w-4 h-4" />
          Enroll New Student
        </button>
      </div>

      {/* Filter Toolbar */}
      <div className="bg-white p-5 rounded-3xl border border-slate-200/80 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
        {/* Search */}
        <div className="relative flex-1 max-w-md">
          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search students by name, email, or enrollment..."
            className="w-full pl-10 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
          />
        </div>

        {/* Dropdowns */}
        <div className="flex items-center gap-3 flex-wrap">
          <div className="flex items-center gap-1.5 text-xs text-slate-600">
            <span className="font-bold uppercase text-[11px] text-slate-400">Status:</span>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="bg-slate-50 border border-slate-200 text-slate-700 rounded-xl px-3 py-1.5 text-xs font-semibold focus:outline-none"
            >
              <option value="ALL">All Status</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>

          <div className="flex items-center gap-1.5 text-xs text-slate-600">
            <span className="font-bold uppercase text-[11px] text-slate-400">Roadmap:</span>
            <select
              value={roadmapFilter}
              onChange={(e) => setRoadmapFilter(e.target.value)}
              className="bg-slate-50 border border-slate-200 text-slate-700 rounded-xl px-3 py-1.5 text-xs font-semibold focus:outline-none max-w-[180px]"
            >
              <option value="ALL">All Roadmaps</option>
              {roadmaps.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.title}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Students Table */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-slate-50/80 border-b border-slate-200 text-slate-400 font-bold uppercase tracking-wider text-[10px]">
                <th className="py-4 px-6">Student Info</th>
                <th className="py-4 px-4">Assigned Roadmap</th>
                <th className="py-4 px-4 w-44">Overall Progress</th>
                <th className="py-4 px-4">Streak</th>
                <th className="py-4 px-4">Hours</th>
                <th className="py-4 px-4">Status</th>
                <th className="py-4 px-6 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredStudents.length > 0 ? (
                filteredStudents.map((s) => {
                  const progressPct = Math.round(s.overallProgress || 0);
                  const studyHours = (s.totalStudyMinutes / 60).toFixed(1);

                  return (
                    <tr key={s.id} className="hover:bg-slate-50/50 transition-colors">
                      {/* Student Info */}
                      <td className="py-4 px-6">
                        <div className="flex items-center gap-3">
                          <div className="w-9 h-9 rounded-xl bg-indigo-100 text-indigo-700 font-black flex items-center justify-center text-xs shrink-0">
                            {s.fullName?.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <Link
                              to={`/admin/students/${s.id}`}
                              className="font-bold text-slate-900 hover:text-indigo-600 transition-colors text-sm block"
                            >
                              {s.fullName}
                            </Link>
                            <span className="text-slate-400 text-[11px] block">{s.email}</span>
                            {s.enrollmentNo && (
                              <span className="text-[10px] text-slate-400 font-mono">#{s.enrollmentNo}</span>
                            )}
                          </div>
                        </div>
                      </td>

                      {/* Roadmap */}
                      <td className="py-4 px-4">
                        {s.roadmapTitle ? (
                          <span className="font-semibold text-slate-700 block line-clamp-1 max-w-[200px]" title={s.roadmapTitle}>
                            {s.roadmapTitle}
                          </span>
                        ) : (
                          <span className="text-rose-500 font-medium italic">Unassigned</span>
                        )}
                        <button
                          onClick={() => handleOpenAssign(s)}
                          className="text-[10px] text-indigo-600 hover:underline font-bold mt-0.5 inline-block"
                        >
                          {s.roadmapTitle ? 'Switch Track' : 'Assign Track'}
                        </button>
                      </td>

                      {/* Progress */}
                      <td className="py-4 px-4">
                        <div className="space-y-1">
                          <div className="flex justify-between font-bold text-[11px] text-slate-600">
                            <span>{progressPct}%</span>
                            <span className="text-slate-400 font-normal">Today: {Math.round(s.todayProgress || 0)}%</span>
                          </div>
                          <ProgressBar progress={progressPct} size="sm" color={progressPct >= 70 ? 'emerald' : 'indigo'} />
                        </div>
                      </td>

                      {/* Streak */}
                      <td className="py-4 px-4 font-bold">
                        <span className="inline-flex items-center gap-1 text-amber-600 bg-amber-50 px-2 py-0.5 rounded-lg border border-amber-200/60">
                          <Flame className="w-3.5 h-3.5 fill-current" />
                          {s.currentStreak || 0}d
                        </span>
                      </td>

                      {/* Study Hours */}
                      <td className="py-4 px-4 font-bold text-slate-700">
                        {studyHours}h
                      </td>

                      {/* Status */}
                      <td className="py-4 px-4">
                        <span
                          className={`px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                            s.status === 'ACTIVE'
                              ? 'bg-emerald-100 text-emerald-800'
                              : 'bg-slate-100 text-slate-600'
                          }`}
                        >
                          {s.status}
                        </span>
                      </td>

                      {/* Actions */}
                      <td className="py-4 px-6 text-right">
                        <div className="flex items-center justify-end gap-1.5">
                          <Link
                            to={`/admin/students/${s.id}`}
                            className="p-1.5 rounded-lg text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 transition-colors"
                            title="Inspect Student Drill-Down"
                          >
                            <Eye className="w-4 h-4" />
                          </Link>
                          <button
                            onClick={() => handleOpenEdit(s)}
                            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 hover:bg-slate-100 transition-colors"
                            title="Edit Student"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => setDeleteTargetId(s.id)}
                            className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                            title="Delete Student"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="7" className="p-12 text-center text-slate-400">
                    No students match your filter criteria.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Student Modal */}
      <Modal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Enroll New Student"
      >
        <form onSubmit={handleCreateStudent} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Full Name *
              </label>
              <input
                type="text"
                required
                value={createForm.fullName}
                onChange={(e) => setCreateForm({ ...createForm, fullName: e.target.value })}
                placeholder="e.g. Arjun Mehta"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Email Address *
              </label>
              <input
                type="email"
                required
                value={createForm.email}
                onChange={(e) => setCreateForm({ ...createForm, email: e.target.value })}
                placeholder="e.g. arjun@studytrack.com"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Initial Password *
              </label>
              <input
                type="password"
                required
                value={createForm.password}
                onChange={(e) => setCreateForm({ ...createForm, password: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Enrollment Number
              </label>
              <input
                type="text"
                value={createForm.enrollmentNo}
                onChange={(e) => setCreateForm({ ...createForm, enrollmentNo: e.target.value })}
                placeholder="e.g. ENR2026-006"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                College / University
              </label>
              <input
                type="text"
                value={createForm.college}
                onChange={(e) => setCreateForm({ ...createForm, college: e.target.value })}
                placeholder="e.g. IIT Delhi"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Phone Number
              </label>
              <input
                type="text"
                value={createForm.phone}
                onChange={(e) => setCreateForm({ ...createForm, phone: e.target.value })}
                placeholder="+91 98765 43210"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Assign Learning Roadmap
            </label>
            <select
              value={createForm.roadmapId}
              onChange={(e) => setCreateForm({ ...createForm, roadmapId: e.target.value })}
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            >
              <option value="">-- No Roadmap Assigned Initially --</option>
              {roadmaps.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.title} ({r.category})
                </option>
              ))}
            </select>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => setIsAddModalOpen(false)}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Enrolling...' : 'Enroll Student'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Edit Student Modal */}
      <Modal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        title={`Edit Student: ${selectedStudent?.fullName}`}
      >
        <form onSubmit={handleUpdateStudent} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Full Name *
              </label>
              <input
                type="text"
                required
                value={editForm.fullName}
                onChange={(e) => setEditForm({ ...editForm, fullName: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Email *
              </label>
              <input
                type="email"
                required
                value={editForm.email}
                onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Status
              </label>
              <select
                value={editForm.status}
                onChange={(e) => setEditForm({ ...editForm, status: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              >
                <option value="ACTIVE">ACTIVE</option>
                <option value="INACTIVE">INACTIVE</option>
              </select>
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Enrollment Number
              </label>
              <input
                type="text"
                value={editForm.enrollmentNo}
                onChange={(e) => setEditForm({ ...editForm, enrollmentNo: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                College
              </label>
              <input
                type="text"
                value={editForm.college}
                onChange={(e) => setEditForm({ ...editForm, college: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Phone
              </label>
              <input
                type="text"
                value={editForm.phone}
                onChange={(e) => setEditForm({ ...editForm, phone: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Reset Password (Optional)
            </label>
            <input
              type="password"
              value={editForm.newPassword}
              onChange={(e) => setEditForm({ ...editForm, newPassword: e.target.value })}
              placeholder="Leave blank to keep existing password"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => setIsEditModalOpen(false)}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Saving...' : 'Update Student'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Assign Roadmap Modal */}
      <Modal
        isOpen={isAssignModalOpen}
        onClose={() => setIsAssignModalOpen(false)}
        title={`Assign Roadmap: ${selectedStudent?.fullName}`}
      >
        <form onSubmit={handleAssignRoadmap} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Select Learning Roadmap *
            </label>
            <select
              required
              value={assignForm.roadmapId}
              onChange={(e) => setAssignForm({ roadmapId: e.target.value })}
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            >
              <option value="">-- Choose Curriculum --</option>
              {roadmaps.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.title} ({r.category} • {r.level})
                </option>
              ))}
            </select>
          </div>

          <p className="text-xs text-slate-500">
            Assigning a new roadmap will initialize the curriculum hierarchy and create progressive tasks for this student.
          </p>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => setIsAssignModalOpen(false)}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Assigning...' : 'Confirm Assignment'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Dialog */}
      <ConfirmationDialog
        isOpen={!!deleteTargetId}
        onClose={() => setDeleteTargetId(null)}
        onConfirm={handleDeleteStudent}
        title="Delete Student Record"
        message="Are you sure you want to permanently delete this student record, their task history, and study logs?"
        confirmText="Delete Student"
        confirmVariant="danger"
      />
    </div>
  );
}
