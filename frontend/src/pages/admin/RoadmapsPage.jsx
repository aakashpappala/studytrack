import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import Modal from '../../components/common/Modal';
import ConfirmationDialog from '../../components/common/ConfirmationDialog';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Map,
  Plus,
  Edit2,
  Trash2,
  UserPlus,
  BookOpen,
  Layers,
  Clock,
  Sparkles,
  ChevronRight,
  FolderGit2,
  CheckCircle2,
  Filter
} from 'lucide-react';

export default function RoadmapsPage() {
  const [roadmaps, setRoadmaps] = useState([]);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  // Modals
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isAssignModalOpen, setIsAssignModalOpen] = useState(false);
  const [previewRoadmap, setPreviewRoadmap] = useState(null);
  const [selectedRoadmap, setSelectedRoadmap] = useState(null);
  const [deleteTargetId, setDeleteTargetId] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Forms
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: 'Full Stack Development',
    level: 'Beginner to Intermediate',
    estimatedHours: 120,
  });

  const [assignStudentId, setAssignStudentId] = useState('');

  const fetchRoadmapsAndStudents = async () => {
    try {
      const [roadmapsRes, studentsRes] = await Promise.all([
        api.get('/roadmaps'),
        api.get('/admin/students'),
      ]);
      setRoadmaps(roadmapsRes.data.data || []);
      setStudents(studentsRes.data.data || []);
    } catch (err) {
      console.error('Error fetching roadmaps and students', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRoadmapsAndStudents();
  }, []);

  const handleOpenAdd = () => {
    setSelectedRoadmap(null);
    setFormData({
      title: '',
      description: '',
      category: 'Software Engineering',
      level: 'Intermediate',
      estimatedHours: 100,
    });
    setIsAddModalOpen(true);
  };

  const handleOpenEdit = (rm) => {
    setSelectedRoadmap(rm);
    setFormData({
      title: rm.title || '',
      description: rm.description || '',
      category: rm.category || '',
      level: rm.level || '',
      estimatedHours: rm.estimatedHours || 100,
    });
    setIsEditModalOpen(true);
  };

  const handleSaveRoadmap = async (e) => {
    e.preventDefault();
    if (!formData.title.trim()) return;

    try {
      setSubmitting(true);
      if (selectedRoadmap) {
        await api.put(`/roadmaps/${selectedRoadmap.id}`, formData);
      } else {
        await api.post('/roadmaps', formData);
      }
      setIsAddModalOpen(false);
      setIsEditModalOpen(false);
      await fetchRoadmapsAndStudents();
    } catch (err) {
      console.error('Error saving roadmap', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleOpenAssign = (rm) => {
    setSelectedRoadmap(rm);
    setAssignStudentId('');
    setIsAssignModalOpen(true);
  };

  const handleAssignSubmit = async (e) => {
    e.preventDefault();
    if (!selectedRoadmap || !assignStudentId) return;

    try {
      setSubmitting(true);
      await api.post('/roadmaps/assign', {
        studentId: Number(assignStudentId),
        roadmapId: selectedRoadmap.id,
      });
      setIsAssignModalOpen(false);
      await fetchRoadmapsAndStudents();
    } catch (err) {
      console.error('Error assigning roadmap', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteRoadmap = async () => {
    if (!deleteTargetId) return;
    try {
      await api.delete(`/roadmaps/${deleteTargetId}`);
      setDeleteTargetId(null);
      await fetchRoadmapsAndStudents();
    } catch (err) {
      console.error('Error deleting roadmap', err);
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading learning tracks..." />;
  }

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-slate-900 tracking-tight">Roadmap Curriculum Catalog</h1>
          <p className="text-xs text-slate-500 mt-1">
            Build, configure, and assign structured learning roadmaps with Subject → Module → Topic hierarchies.
          </p>
        </div>
        <button
          onClick={handleOpenAdd}
          className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/20 transition-all flex items-center gap-2 self-start"
        >
          <Plus className="w-4 h-4" />
          Create New Roadmap
        </button>
      </div>

      {/* Roadmaps Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {roadmaps.map((rm) => (
          <div
            key={rm.id}
            className="bg-white rounded-3xl border border-slate-200/80 p-6 shadow-sm hover:shadow-md transition-all flex flex-col justify-between group"
          >
            <div className="space-y-4">
              <div className="flex items-center justify-between gap-2">
                <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-indigo-50 text-indigo-700 border border-indigo-100">
                  {rm.category || 'Tech'}
                </span>
                <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-emerald-50 text-emerald-700 border border-emerald-100">
                  {rm.level || 'All Levels'}
                </span>
              </div>

              <div>
                <h3 className="text-lg font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
                  {rm.title}
                </h3>
                <p className="text-xs text-slate-500 mt-1 line-clamp-3 leading-relaxed">
                  {rm.description}
                </p>
              </div>

              {/* Hierarchy Counts Bar */}
              <div className="grid grid-cols-3 gap-2 py-3 border-y border-slate-100 text-center text-xs">
                <div>
                  <p className="text-slate-400 text-[10px] uppercase font-bold">Subjects</p>
                  <p className="font-black text-slate-800 text-sm mt-0.5">{rm.totalSubjects || rm.subjects?.length || 0}</p>
                </div>
                <div>
                  <p className="text-slate-400 text-[10px] uppercase font-bold">Modules</p>
                  <p className="font-black text-slate-800 text-sm mt-0.5">{rm.totalModules || 0}</p>
                </div>
                <div>
                  <p className="text-slate-400 text-[10px] uppercase font-bold">Topics</p>
                  <p className="font-black text-slate-800 text-sm mt-0.5">{rm.totalTopics || 0}</p>
                </div>
              </div>

              <div className="flex items-center gap-2 text-xs text-slate-400">
                <Clock className="w-3.5 h-3.5" />
                <span>Estimated duration: <strong className="text-slate-700">{rm.estimatedHours} Hours</strong></span>
              </div>
            </div>

            {/* Bottom Actions */}
            <div className="pt-5 mt-5 border-t border-slate-100 flex items-center justify-between gap-2">
              <button
                onClick={() => setPreviewRoadmap(rm)}
                className="text-xs font-bold text-indigo-600 hover:text-indigo-800 flex items-center gap-1 transition-colors"
              >
                Inspect Structure <ChevronRight className="w-3.5 h-3.5" />
              </button>

              <div className="flex items-center gap-1">
                <button
                  onClick={() => handleOpenAssign(rm)}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 transition-colors"
                  title="Assign to Student"
                >
                  <UserPlus className="w-4 h-4" />
                </button>
                <button
                  onClick={() => handleOpenEdit(rm)}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 hover:bg-slate-100 transition-colors"
                  title="Edit Roadmap"
                >
                  <Edit2 className="w-4 h-4" />
                </button>
                <button
                  onClick={() => setDeleteTargetId(rm.id)}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-colors"
                  title="Delete Roadmap"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Add / Edit Roadmap Modal */}
      <Modal
        isOpen={isAddModalOpen || isEditModalOpen}
        onClose={() => {
          setIsAddModalOpen(false);
          setIsEditModalOpen(false);
        }}
        title={isEditModalOpen ? 'Edit Roadmap' : 'Create Learning Track'}
      >
        <form onSubmit={handleSaveRoadmap} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Roadmap Title *
            </label>
            <input
              type="text"
              required
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              placeholder="e.g. Cloud & DevOps Engineering"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Category
              </label>
              <input
                type="text"
                value={formData.category}
                onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                placeholder="e.g. Cloud / Backend"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Target Level
              </label>
              <input
                type="text"
                value={formData.level}
                onChange={(e) => setFormData({ ...formData, level: e.target.value })}
                placeholder="e.g. Intermediate to Advanced"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Estimated Duration (Hours)
            </label>
            <input
              type="number"
              min="1"
              value={formData.estimatedHours}
              onChange={(e) => setFormData({ ...formData, estimatedHours: Number(e.target.value) })}
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Description / Overview
            </label>
            <textarea
              rows="4"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Comprehensive syllabus summary, skills covered, and outcomes..."
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 resize-none"
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => {
                setIsAddModalOpen(false);
                setIsEditModalOpen(false);
              }}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Saving...' : isEditModalOpen ? 'Update Roadmap' : 'Create Roadmap'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Assign to Student Modal */}
      <Modal
        isOpen={isAssignModalOpen}
        onClose={() => setIsAssignModalOpen(false)}
        title={`Assign: ${selectedRoadmap?.title}`}
      >
        <form onSubmit={handleAssignSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Select Student *
            </label>
            <select
              required
              value={assignStudentId}
              onChange={(e) => setAssignStudentId(e.target.value)}
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            >
              <option value="">-- Choose Student --</option>
              {students.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.fullName} ({s.email}) — Current: {s.roadmapTitle || 'None'}
                </option>
              ))}
            </select>
          </div>

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
              {submitting ? 'Assigning...' : 'Assign Roadmap'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Hierarchy Preview Modal */}
      <Modal
        isOpen={!!previewRoadmap}
        onClose={() => setPreviewRoadmap(null)}
        title={`Hierarchy Preview: ${previewRoadmap?.title}`}
      >
        <div className="space-y-4 max-h-[70vh] overflow-y-auto pr-2">
          {previewRoadmap?.subjects && previewRoadmap.subjects.length > 0 ? (
            previewRoadmap.subjects.map((sub, sIdx) => (
              <div key={sub.id || sIdx} className="p-4 rounded-2xl bg-slate-50 border border-slate-200 space-y-3">
                <div className="flex items-center gap-2">
                  <span className="w-6 h-6 rounded-lg bg-indigo-100 text-indigo-700 font-bold text-xs flex items-center justify-center">
                    0{sIdx + 1}
                  </span>
                  <h4 className="text-xs font-bold text-slate-900">{sub.title}</h4>
                </div>

                {sub.modules && sub.modules.length > 0 ? (
                  <div className="space-y-2 pl-4 border-l-2 border-slate-200 ml-3">
                    {sub.modules.map((mod, mIdx) => (
                      <div key={mod.id || mIdx} className="space-y-1">
                        <div className="flex items-center gap-2 text-xs font-semibold text-slate-700">
                          <FolderGit2 className="w-3.5 h-3.5 text-indigo-500" />
                          <span>Module {mIdx + 1}: {mod.title}</span>
                        </div>
                        {mod.topics && mod.topics.length > 0 && (
                          <div className="pl-5 space-y-0.5">
                            {mod.topics.map((top, tIdx) => (
                              <p key={top.id || tIdx} className="text-[11px] text-slate-500">
                                • {top.title}
                              </p>
                            ))}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-[11px] text-slate-400 italic pl-4">No modules added yet.</p>
                )}
              </div>
            ))
          ) : (
            <p className="text-xs text-slate-400 italic py-6 text-center">No subject hierarchy defined.</p>
          )}
        </div>
      </Modal>

      {/* Delete Confirmation */}
      <ConfirmationDialog
        isOpen={!!deleteTargetId}
        onClose={() => setDeleteTargetId(null)}
        onConfirm={handleDeleteRoadmap}
        title="Delete Roadmap"
        message="Are you sure you want to delete this roadmap? Students assigned to this roadmap will need to be reassigned."
        confirmText="Delete"
        confirmVariant="danger"
      />
    </div>
  );
}
