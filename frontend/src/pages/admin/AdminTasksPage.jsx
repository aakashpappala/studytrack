import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  ListTodo,
  Plus,
  Search,
  CheckCircle2,
  Clock,
  Circle,
  Calendar,
  Upload,
  Video,
  Image as ImageIcon,
  XCircle,
  Eye,
  AlertCircle
} from 'lucide-react';
import { format } from 'date-fns';

export default function AdminTasksPage() {
  const [tasks, setTasks] = useState([]);
  const [pendingTasks, setPendingTasks] = useState([]);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [verificationLoading, setVerificationLoading] = useState(false);

  const [selectedStudentFilter, setSelectedStudentFilter] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');

  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [isRejectModalOpen, setIsRejectModalOpen] = useState(false);
  const [rejectTaskId, setRejectTaskId] = useState(null);
  const [rejectMessage, setRejectMessage] = useState('');

  const [previewTask, setPreviewTask] = useState(null);

  const [formData, setFormData] = useState({
    studentId: '',
    title: '',
    description: '',
    assignedDate: format(new Date(), 'yyyy-MM-dd'),
    dueDate: format(new Date(), 'yyyy-MM-dd'),
    estimatedDurationMinutes: 60,
    priority: 'MEDIUM',
  });

  const fetchTasksAndStudents = async () => {
    try {
      const [tasksRes, studentsRes, pendingRes] = await Promise.all([
        api.get('/admin/tasks'),
        api.get('/admin/students'),
        api.get('/admin/tasks/pending-verification'),
      ]);

      setTasks(tasksRes.data.data || []);
      setStudents(studentsRes.data.data || []);
      setPendingTasks(pendingRes.data.data || []);
    } catch (err) {
      console.error('Error fetching admin tasks data', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasksAndStudents();
  }, []);

  const handleCreateTask = async (e) => {
    e.preventDefault();

    if (!formData.studentId || !formData.title.trim()) return;

    try {
      setSubmitting(true);

      await api.post('/admin/tasks', {
        ...formData,
        studentId: Number(formData.studentId),
      });

      setIsAddModalOpen(false);

      setFormData({
        studentId: '',
        title: '',
        description: '',
        assignedDate: format(new Date(), 'yyyy-MM-dd'),
        dueDate: format(new Date(), 'yyyy-MM-dd'),
        estimatedDurationMinutes: 60,
        priority: 'MEDIUM',
      });

      await fetchTasksAndStudents();
    } catch (err) {
      console.error('Error assigning task', err);
      alert(err?.response?.data?.message || 'Failed to assign task.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleApproveTask = async (taskId) => {
    const confirmed = window.confirm(
      'Are you sure you want to approve this task?'
    );

    if (!confirmed) return;

    try {
      setVerificationLoading(true);
      await api.put(`/admin/tasks/${taskId}/approve`);
      await fetchTasksAndStudents();
      alert('Task approved successfully.');
    } catch (err) {
      console.error('Error approving task', err);
      alert(err?.response?.data?.message || 'Failed to approve task.');
    } finally {
      setVerificationLoading(false);
    }
  };

  const openRejectModal = (taskId) => {
    setRejectTaskId(taskId);
    setRejectMessage('');
    setIsRejectModalOpen(true);
  };

  const handleRejectTask = async (e) => {
    e.preventDefault();

    if (!rejectTaskId) return;

    if (!rejectMessage.trim()) {
      alert('Please enter a rejection message.');
      return;
    }

    try {
      setVerificationLoading(true);

      await api.put(
        `/admin/tasks/${rejectTaskId}/reject`,
        null,
        {
          params: {
            adminMessage: rejectMessage.trim(),
          },
        }
      );

      setIsRejectModalOpen(false);
      setRejectTaskId(null);
      setRejectMessage('');

      await fetchTasksAndStudents();
      alert('Task rejected successfully.');
    } catch (err) {
      console.error('Error rejecting task', err);
      alert(err?.response?.data?.message || 'Failed to reject task.');
    } finally {
      setVerificationLoading(false);
    }
  };

  const filteredTasks = tasks.filter((t) => {
    if (
      selectedStudentFilter !== 'ALL' &&
      String(t.studentId) !== String(selectedStudentFilter)
    ) {
      return false;
    }

    if (searchQuery.trim() !== '') {
      const q = searchQuery.toLowerCase();

      const matchTitle = t.title?.toLowerCase().includes(q);
      const matchStudent = t.studentName?.toLowerCase().includes(q);
      const matchDesc = t.description?.toLowerCase().includes(q);

      if (!matchTitle && !matchStudent && !matchDesc) {
        return false;
      }
    }

    return true;
  });

  // proofUrl is returned by the backend as /uploads/filename.
  // The upload file itself is served by the backend, so when the
  // frontend and backend are separate Render services we must use
  // the backend origin instead of the frontend origin.
  const getProofUrl = (proofUrl) => {
    if (!proofUrl) return '';

    if (
      proofUrl.startsWith('http://') ||
      proofUrl.startsWith('https://')
    ) {
      return proofUrl;
    }

    const apiUrl = import.meta.env.VITE_API_URL || '';

    if (apiUrl) {
      const backendOrigin = apiUrl.replace(/\/api\/?$/, '');
      return `${backendOrigin}${proofUrl.startsWith('/') ? proofUrl : `/${proofUrl}`}`;
    }

    return proofUrl;
  };

  if (loading) {
    return (
      <LoadingSpinner
        size="lg"
        message="Loading tasks assignments..."
      />
    );
  }

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-slate-900 tracking-tight">
            Assigned Tasks Catalog
          </h1>
          <p className="text-xs text-slate-500 mt-1">
            Assign tasks, review student submissions, and verify completed work.
          </p>
        </div>

        <button
          onClick={() => setIsAddModalOpen(true)}
          className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/20 transition-all flex items-center gap-2 self-start"
        >
          <Plus className="w-4 h-4" />
          Assign Task to Student
        </button>
      </div>

      {pendingTasks.length > 0 && (
        <div className="bg-amber-50 border border-amber-200 rounded-3xl shadow-sm overflow-hidden">
          <div className="p-5 border-b border-amber-200 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <div>
              <div className="flex items-center gap-2">
                <Clock className="w-5 h-5 text-amber-600" />
                <h2 className="text-lg font-black text-amber-900">
                  Pending Verification
                </h2>
                <span className="px-2 py-0.5 rounded-full bg-amber-200 text-amber-900 text-[10px] font-black">
                  {pendingTasks.length}
                </span>
              </div>
              <p className="text-xs text-amber-700 mt-1">
                Review the uploaded image/video before approving the task.
              </p>
            </div>
          </div>

          <div className="divide-y divide-amber-200">
            {pendingTasks.map((task) => (
              <div key={task.id} className="p-5 bg-white/70">
                <div className="flex flex-col xl:flex-row gap-5">
                  <div className="flex-1">
                    <div className="flex items-start gap-3">
                      <div className="w-9 h-9 rounded-xl bg-amber-100 text-amber-700 flex items-center justify-center shrink-0">
                        <Upload className="w-5 h-5" />
                      </div>

                      <div className="min-w-0">
                        <h3 className="text-sm font-black text-slate-900">
                          {task.title}
                        </h3>

                        <p className="text-xs text-slate-500 mt-1">
                          Student:{' '}
                          <span className="font-bold text-slate-700">
                            {task.studentName || 'Student'}
                          </span>
                        </p>

                        {task.description && (
                          <p className="text-xs text-slate-500 mt-2">
                            {task.description}
                          </p>
                        )}

                        <div className="flex flex-wrap gap-2 mt-3">
                          <span className="px-2 py-1 rounded-lg bg-slate-100 text-slate-600 text-[10px] font-bold">
                            {task.priority}
                          </span>

                          {task.subjectTitle && (
                            <span className="px-2 py-1 rounded-lg bg-indigo-50 text-indigo-700 text-[10px] font-bold">
                              {task.subjectTitle}
                            </span>
                          )}

                          {task.proofType && (
                            <span className="px-2 py-1 rounded-lg bg-purple-50 text-purple-700 text-[10px] font-bold">
                              {task.proofType}
                            </span>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="xl:w-[360px]">
                    <div className="rounded-2xl border border-slate-200 bg-white overflow-hidden">
                      {task.proofType === 'IMAGE' && task.proofUrl ? (
                        <img
                          src={getProofUrl(task.proofUrl)}
                          alt="Student proof"
                          className="w-full h-48 object-cover cursor-pointer"
                          onClick={() => setPreviewTask(task)}
                        />
                      ) : task.proofType === 'VIDEO' && task.proofUrl ? (
                        <video
                          src={getProofUrl(task.proofUrl)}
                          controls
                          className="w-full h-48 object-cover"
                        />
                      ) : (
                        <div className="h-48 flex flex-col items-center justify-center text-slate-400">
                          <AlertCircle className="w-8 h-8 mb-2" />
                          <p className="text-xs font-semibold">
                            Proof unavailable
                          </p>
                        </div>
                      )}

                      <div className="p-3 border-t border-slate-100">
                        <div className="flex items-center justify-between">
                          <div className="flex items-center gap-2">
                            {task.proofType === 'VIDEO' ? (
                              <Video className="w-4 h-4 text-purple-600" />
                            ) : (
                              <ImageIcon className="w-4 h-4 text-indigo-600" />
                            )}

                            <span className="text-xs font-bold text-slate-700">
                              {task.proofType || 'PROOF'}
                            </span>
                          </div>

                          {task.proofType === 'IMAGE' && task.proofUrl && (
                            <button
                              onClick={() => setPreviewTask(task)}
                              className="flex items-center gap-1 text-[10px] font-bold text-indigo-600 hover:text-indigo-800"
                            >
                              <Eye className="w-3.5 h-3.5" />
                              View
                            </button>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="xl:w-[170px] flex xl:flex-col gap-2 justify-center">
                    <button
                      onClick={() => handleApproveTask(task.id)}
                      disabled={verificationLoading}
                      className="flex-1 xl:flex-none px-4 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 text-white text-xs font-bold flex items-center justify-center gap-2"
                    >
                      <CheckCircle2 className="w-4 h-4" />
                      Approve
                    </button>

                    <button
                      onClick={() => openRejectModal(task.id)}
                      disabled={verificationLoading}
                      className="flex-1 xl:flex-none px-4 py-2.5 rounded-xl bg-rose-600 hover:bg-rose-700 disabled:opacity-50 text-white text-xs font-bold flex items-center justify-center gap-2"
                    >
                      <XCircle className="w-4 h-4" />
                      Reject
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="bg-white p-5 rounded-3xl border border-slate-200/80 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="relative flex-1 max-w-md">
          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />

          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search by task title, student name, or topic..."
            className="w-full pl-10 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
          />
        </div>

        <div className="flex items-center gap-2 text-xs">
          <span className="font-bold text-slate-400 uppercase text-[11px]">
            Filter Student:
          </span>

          <select
            value={selectedStudentFilter}
            onChange={(e) => setSelectedStudentFilter(e.target.value)}
            className="bg-slate-50 border border-slate-200 text-slate-700 rounded-xl px-3 py-1.5 text-xs font-semibold focus:outline-none max-w-[200px]"
          >
            <option value="ALL">
              All Students ({students.length})
            </option>

            {students.map((s) => (
              <option key={s.id} value={s.id}>
                {s.fullName}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-slate-50/80 border-b border-slate-200 text-slate-400 font-bold uppercase tracking-wider text-[10px]">
                <th className="py-4 px-6">Task Title & Details</th>
                <th className="py-4 px-4">Assigned Student</th>
                <th className="py-4 px-4">Subject / Module</th>
                <th className="py-4 px-4">Due Date</th>
                <th className="py-4 px-4">Priority</th>
                <th className="py-4 px-6 text-right">Status</th>
              </tr>
            </thead>

            <tbody className="divide-y divide-slate-100">
              {filteredTasks.length > 0 ? (
                filteredTasks.map((t) => {
                  const isDone = t.status === 'COMPLETED';
                  const isDoing = t.status === 'IN_PROGRESS';
                  const isPending = t.status === 'PENDING_VERIFICATION';
                  const isRejected = t.status === 'REJECTED';

                  return (
                    <tr
                      key={t.id}
                      className="hover:bg-slate-50/50 transition-colors"
                    >
                      <td className="py-4 px-6 max-w-sm">
                        <div className="flex items-start gap-3">
                          <div className="shrink-0 mt-0.5">
                            {isDone ? (
                              <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                            ) : isPending ? (
                              <Clock className="w-4 h-4 text-amber-600" />
                            ) : isRejected ? (
                              <XCircle className="w-4 h-4 text-rose-600" />
                            ) : isDoing ? (
                              <Clock className="w-4 h-4 text-indigo-600" />
                            ) : (
                              <Circle className="w-4 h-4 text-slate-300" />
                            )}
                          </div>

                          <div>
                            <h4 className="font-bold text-slate-900 text-xs">
                              {t.title}
                            </h4>

                            {t.description && (
                              <p className="text-slate-400 text-[11px] line-clamp-1 mt-0.5">
                                {t.description}
                              </p>
                            )}
                          </div>
                        </div>
                      </td>

                      <td className="py-4 px-4 font-semibold text-slate-800">
                        {t.studentName || 'Student'}
                      </td>

                      <td className="py-4 px-4 text-slate-500">
                        {t.subjectTitle ? (
                          <span className="truncate block max-w-[150px]">
                            {t.subjectTitle}
                          </span>
                        ) : (
                          <span className="text-slate-400 italic">
                            Custom Task
                          </span>
                        )}
                      </td>

                      <td className="py-4 px-4 text-slate-600 font-medium">
                        {t.dueDate || '--'}
                      </td>

                      <td className="py-4 px-4">
                        <span
                          className={`px-2 py-0.5 rounded-md text-[10px] font-bold uppercase ${
                            t.priority === 'HIGH'
                              ? 'bg-rose-100 text-rose-700'
                              : t.priority === 'MEDIUM'
                              ? 'bg-amber-100 text-amber-700'
                              : 'bg-sky-100 text-sky-700'
                          }`}
                        >
                          {t.priority}
                        </span>
                      </td>

                      <td className="py-4 px-6 text-right">
                        <span
                          className={`px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                            isDone
                              ? 'bg-emerald-100 text-emerald-800'
                              : isPending
                              ? 'bg-amber-100 text-amber-800'
                              : isRejected
                              ? 'bg-rose-100 text-rose-800'
                              : isDoing
                              ? 'bg-indigo-100 text-indigo-800'
                              : 'bg-slate-100 text-slate-600'
                          }`}
                        >
                          {t.status
                            ? t.status.replace(/_/g, ' ')
                            : 'NOT STARTED'}
                        </span>
                      </td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="6" className="p-12 text-center text-slate-400">
                    No tasks found matching your filter criteria.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <Modal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Assign New Task"
      >
        <form onSubmit={handleCreateTask} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Select Student *
            </label>

            <select
              required
              value={formData.studentId}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  studentId: e.target.value
                })
              }
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            >
              <option value="">-- Choose Student --</option>

              {students.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.fullName} ({s.roadmapTitle || 'No Track'})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Task Title *
            </label>

            <input
              type="text"
              required
              value={formData.title}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  title: e.target.value
                })
              }
              placeholder="e.g. Build JWT Interceptor and Authentication Filter"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Description / Instructions
            </label>

            <textarea
              rows="3"
              value={formData.description}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  description: e.target.value
                })
              }
              placeholder="Specific guidelines, requirements, or links..."
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 resize-none"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Assigned Date
              </label>

              <input
                type="date"
                required
                value={formData.assignedDate}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    assignedDate: e.target.value
                  })
                }
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Due Date
              </label>

              <input
                type="date"
                required
                value={formData.dueDate}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    dueDate: e.target.value
                  })
                }
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Priority
              </label>

              <select
                value={formData.priority}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    priority: e.target.value
                  })
                }
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              >
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Est. Duration (Mins)
              </label>

              <input
                type="number"
                min="15"
                value={formData.estimatedDurationMinutes}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    estimatedDurationMinutes: Number(e.target.value)
                  })
                }
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
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
              {submitting ? 'Assigning...' : 'Assign Task'}
            </button>
          </div>
        </form>
      </Modal>

      <Modal
        isOpen={isRejectModalOpen}
        onClose={() => setIsRejectModalOpen(false)}
        title="Reject Task Proof"
      >
        <form onSubmit={handleRejectTask} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Reason for Rejection *
            </label>

            <textarea
              required
              rows="4"
              value={rejectMessage}
              onChange={(e) => setRejectMessage(e.target.value)}
              placeholder="Explain why the submitted proof was rejected..."
              className="w-full px-3.5 py-3 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-rose-500/20 focus:border-rose-500 resize-none"
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => setIsRejectModalOpen(false)}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={verificationLoading}
              className="px-5 py-2 rounded-xl bg-rose-600 hover:bg-rose-700 text-white text-xs font-bold disabled:opacity-50"
            >
              {verificationLoading ? 'Rejecting...' : 'Reject Task'}
            </button>
          </div>
        </form>
      </Modal>

      <Modal
        isOpen={!!previewTask}
        onClose={() => setPreviewTask(null)}
        title={previewTask?.title || 'Proof Preview'}
      >
        {previewTask?.proofType === 'IMAGE' &&
          previewTask?.proofUrl && (
            <img
              src={getProofUrl(previewTask.proofUrl)}
              alt="Student proof"
              className="w-full max-h-[70vh] object-contain rounded-xl"
            />
          )}
      </Modal>
    </div>
  );
}
