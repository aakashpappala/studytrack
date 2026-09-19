import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Plus,
  Search,
  CheckCircle2,
  Clock,
  Circle,
  Upload,
  Video,
  Image as ImageIcon,
  XCircle,
  Eye,
  AlertCircle,
  History,
  ChevronDown,
  ChevronUp
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

  // Submission history
  const [historyTask, setHistoryTask] = useState(null);
  const [expandedSubmissionId, setExpandedSubmissionId] = useState(null);

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

  // ============================================================
  // CREATE TASK
  // ============================================================

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
      alert(
        err?.response?.data?.message ||
        'Failed to assign task.'
      );
    } finally {
      setSubmitting(false);
    }
  };

  // ============================================================
  // APPROVE TASK
  // ============================================================

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

      alert(
        err?.response?.data?.message ||
        'Failed to approve task.'
      );
    } finally {
      setVerificationLoading(false);
    }
  };

  // ============================================================
  // REJECT TASK
  // ============================================================

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

      alert(
        err?.response?.data?.message ||
        'Failed to reject task.'
      );
    } finally {
      setVerificationLoading(false);
    }
  };

  // ============================================================
  // FILTER TASKS
  // ============================================================

  const filteredTasks = tasks.filter((t) => {
    if (
      selectedStudentFilter !== 'ALL' &&
      String(t.studentId) !== String(selectedStudentFilter)
    ) {
      return false;
    }

    if (searchQuery.trim() !== '') {
      const q = searchQuery.toLowerCase();

      const matchTitle =
        t.title?.toLowerCase().includes(q);

      const matchStudent =
        t.studentName?.toLowerCase().includes(q);

      const matchDesc =
        t.description?.toLowerCase().includes(q);

      if (!matchTitle && !matchStudent && !matchDesc) {
        return false;
      }
    }

    return true;
  });

  // ============================================================
  // PROOF URL
  // ============================================================

  const getProofUrl = (proofUrl) => {
    if (!proofUrl) return '';

    if (
      proofUrl.startsWith('http://') ||
      proofUrl.startsWith('https://')
    ) {
      return proofUrl;
    }

    const apiUrl =
      import.meta.env.VITE_API_URL || '';

    if (apiUrl) {
      const backendOrigin =
        apiUrl.replace(/\/api\/?$/, '');

      return `${backendOrigin}${
        proofUrl.startsWith('/')
          ? proofUrl
          : `/${proofUrl}`
      }`;
    }

    return proofUrl;
  };

  // ============================================================
  // GET CURRENT TASK PROOFS
  // ============================================================

  const getTaskProofs = (task) => {
    if (
      Array.isArray(task?.proofs) &&
      task.proofs.length > 0
    ) {
      return task.proofs;
    }

    // Legacy backend fallback
    if (task?.proofUrl) {
      return [
        {
          id: `legacy-${task.id}`,
          proofType:
            task.proofType || 'IMAGE',
          proofUrl: task.proofUrl,
          uploadedAt:
            task.submittedAt || null,
        },
      ];
    }

    return [];
  };

  // ============================================================
  // GET SUBMISSION HISTORY
  // ============================================================

  const getSubmissionHistory = (task) => {
    if (
      Array.isArray(task?.submissionHistory)
    ) {
      return task.submissionHistory;
    }

    return [];
  };

  // ============================================================
  // OPEN PROOF PREVIEW
  // ============================================================

  const openProofPreview = (task, proof) => {
    setPreviewTask({
      ...task,
      previewProof: proof,
    });
  };

  // ============================================================
  // OPEN HISTORY
  // ============================================================

  const openHistoryModal = (task) => {
    setHistoryTask(task);

    const history =
      getSubmissionHistory(task);

    if (history.length > 0) {
      setExpandedSubmissionId(
        history[0].id
      );
    } else {
      setExpandedSubmissionId(null);
    }
  };

  const closeHistoryModal = () => {
    setHistoryTask(null);
    setExpandedSubmissionId(null);
  };

  const toggleSubmission = (submissionId) => {
    setExpandedSubmissionId(
      expandedSubmissionId === submissionId
        ? null
        : submissionId
    );
  };

  // ============================================================
  // SUBMISSION STATUS
  // ============================================================

  const getSubmissionStatusClasses = (status) => {
    switch (status) {
      case 'COMPLETED':
        return 'bg-emerald-100 text-emerald-800';

      case 'REJECTED':
        return 'bg-rose-100 text-rose-800';

      case 'PENDING_VERIFICATION':
        return 'bg-amber-100 text-amber-800';

      default:
        return 'bg-slate-100 text-slate-600';
    }
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

      {/* ====================================================== */}
      {/* HEADER */}
      {/* ====================================================== */}

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

      {/* ====================================================== */}
      {/* PENDING VERIFICATION */}
      {/* ====================================================== */}

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
                Review all uploaded image/video proofs before approving the task.
              </p>

            </div>

          </div>

          <div className="divide-y divide-amber-200">

            {pendingTasks.map((task) => {

              const proofs =
                getTaskProofs(task);

              return (
                <div
                  key={task.id}
                  className="p-5 bg-white/70"
                >

                  <div className="flex flex-col gap-5">

                    {/* TASK INFORMATION */}

                    <div className="flex items-start gap-3">

                      <div className="w-9 h-9 rounded-xl bg-amber-100 text-amber-700 flex items-center justify-center shrink-0">
                        <Upload className="w-5 h-5" />
                      </div>

                      <div className="min-w-0 flex-1">

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

                          <span className="px-2 py-1 rounded-lg bg-purple-50 text-purple-700 text-[10px] font-bold">
                            {proofs.length}{' '}
                            {proofs.length === 1
                              ? 'Proof'
                              : 'Proofs'}
                          </span>

                        </div>

                      </div>

                    </div>

                    {/* PROOFS */}

                    <div>

                      <div className="flex items-center justify-between mb-3">

                        <h4 className="text-xs font-black text-slate-700 uppercase tracking-wider">
                          Submitted Proofs
                        </h4>

                        <span className="text-[10px] font-bold text-slate-400">
                          {proofs.length} file
                          {proofs.length === 1
                            ? ''
                            : 's'}
                        </span>

                      </div>

                      {proofs.length > 0 ? (

                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">

                          {proofs.map((proof, index) => {

                            const proofUrl =
                              getProofUrl(
                                proof.proofUrl
                              );

                            const isVideo =
                              proof.proofType?.toUpperCase() ===
                              'VIDEO';

                            return (
                              <div
                                key={
                                  proof.id ||
                                  `${task.id}-${index}`
                                }
                                className="rounded-2xl border border-slate-200 bg-white overflow-hidden shadow-sm"
                              >

                                <div className="relative bg-slate-100">

                                  {isVideo &&
                                  proofUrl ? (
                                    <video
                                      src={proofUrl}
                                      controls
                                      className="w-full h-52 object-cover"
                                    />
                                  ) : !isVideo &&
                                    proofUrl ? (
                                    <img
                                      src={proofUrl}
                                      alt={`Student proof ${index + 1}`}
                                      className="w-full h-52 object-cover cursor-pointer"
                                      onClick={() =>
                                        openProofPreview(
                                          task,
                                          proof
                                        )
                                      }
                                    />
                                  ) : (
                                    <div className="h-52 flex flex-col items-center justify-center text-slate-400">

                                      <AlertCircle className="w-8 h-8 mb-2" />

                                      <p className="text-xs font-semibold">
                                        Proof unavailable
                                      </p>

                                    </div>
                                  )}

                                </div>

                                <div className="p-3">

                                  <div className="flex items-center justify-between gap-2">

                                    <div className="flex items-center gap-2 min-w-0">

                                      {isVideo ? (
                                        <Video className="w-4 h-4 text-purple-600 shrink-0" />
                                      ) : (
                                        <ImageIcon className="w-4 h-4 text-indigo-600 shrink-0" />
                                      )}

                                      <span className="text-xs font-bold text-slate-700">
                                        {proof.proofType ||
                                          'PROOF'}
                                      </span>

                                    </div>

                                    {!isVideo &&
                                      proofUrl && (
                                        <button
                                          onClick={() =>
                                            openProofPreview(
                                              task,
                                              proof
                                            )
                                          }
                                          className="flex items-center gap-1 text-[10px] font-bold text-indigo-600 hover:text-indigo-800"
                                        >
                                          <Eye className="w-3.5 h-3.5" />
                                          View
                                        </button>
                                      )}

                                  </div>

                                  {proof.uploadedAt && (
                                    <p className="text-[9px] text-slate-400 mt-2">
                                      Uploaded:{' '}
                                      {new Date(
                                        proof.uploadedAt
                                      ).toLocaleString()}
                                    </p>
                                  )}

                                </div>

                              </div>
                            );
                          })}

                        </div>

                      ) : (

                        <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-8 text-center">

                          <AlertCircle className="w-8 h-8 mx-auto text-slate-400 mb-2" />

                          <p className="text-xs font-semibold text-slate-500">
                            No proof files found
                          </p>

                        </div>

                      )}

                    </div>

                    {/* ACTIONS */}

                    <div className="flex flex-col sm:flex-row gap-2 justify-end pt-2 border-t border-slate-100">

                      <button
                        onClick={() =>
                          openHistoryModal(task)
                        }
                        className="px-5 py-2.5 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-bold flex items-center justify-center gap-2"
                      >
                        <History className="w-4 h-4" />
                        View History
                      </button>

                      <button
                        onClick={() =>
                          handleApproveTask(task.id)
                        }
                        disabled={verificationLoading}
                        className="px-5 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 text-white text-xs font-bold flex items-center justify-center gap-2"
                      >
                        <CheckCircle2 className="w-4 h-4" />
                        Approve Task
                      </button>

                      <button
                        onClick={() =>
                          openRejectModal(task.id)
                        }
                        disabled={verificationLoading}
                        className="px-5 py-2.5 rounded-xl bg-rose-600 hover:bg-rose-700 disabled:opacity-50 text-white text-xs font-bold flex items-center justify-center gap-2"
                      >
                        <XCircle className="w-4 h-4" />
                        Reject Task
                      </button>

                    </div>

                  </div>

                </div>
              );
            })}

          </div>

        </div>
      )}

      {/* ====================================================== */}
      {/* SEARCH + FILTER */}
      {/* ====================================================== */}

      <div className="bg-white p-5 rounded-3xl border border-slate-200/80 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">

        <div className="relative flex-1 max-w-md">

          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />

          <input
            type="text"
            value={searchQuery}
            onChange={(e) =>
              setSearchQuery(e.target.value)
            }
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
            onChange={(e) =>
              setSelectedStudentFilter(
                e.target.value
              )
            }
            className="bg-slate-50 border border-slate-200 text-slate-700 rounded-xl px-3 py-1.5 text-xs font-semibold focus:outline-none max-w-[200px]"
          >

            <option value="ALL">
              All Students ({students.length})
            </option>

            {students.map((s) => (
              <option
                key={s.id}
                value={s.id}
              >
                {s.fullName}
              </option>
            ))}

          </select>

        </div>

      </div>

      {/* ====================================================== */}
      {/* TASK TABLE */}
      {/* ====================================================== */}

      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">

        <div className="overflow-x-auto">

          <table className="w-full text-left border-collapse text-xs">

            <thead>

              <tr className="bg-slate-50/80 border-b border-slate-200 text-slate-400 font-bold uppercase tracking-wider text-[10px]">

                <th className="py-4 px-6">
                  Task Title & Details
                </th>

                <th className="py-4 px-4">
                  Assigned Student
                </th>

                <th className="py-4 px-4">
                  Subject / Module
                </th>

                <th className="py-4 px-4">
                  Due Date
                </th>

                <th className="py-4 px-4">
                  Priority
                </th>

                <th className="py-4 px-4">
                  Status
                </th>

                <th className="py-4 px-6 text-right">
                  Action
                </th>

              </tr>

            </thead>

            <tbody className="divide-y divide-slate-100">

              {filteredTasks.length > 0 ? (

                filteredTasks.map((t) => {

                  const isDone =
                    t.status === 'COMPLETED';

                  const isDoing =
                    t.status === 'IN_PROGRESS';

                  const isPending =
                    t.status ===
                    'PENDING_VERIFICATION';

                  const isRejected =
                    t.status === 'REJECTED';

                  const history =
                    getSubmissionHistory(t);

                  const hasHistory =
                    history.length > 0;

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

                      <td className="py-4 px-4">

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
                            ? t.status.replace(
                                /_/g,
                                ' '
                              )
                            : 'NOT STARTED'}
                        </span>

                      </td>

                      <td className="py-4 px-6 text-right">

                        {hasHistory ? (
                          <button
                            onClick={() =>
                              openHistoryModal(t)
                            }
                            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-indigo-50 hover:bg-indigo-100 text-indigo-700 text-[10px] font-bold transition-colors"
                          >
                            <History className="w-3.5 h-3.5" />
                            View History
                          </button>
                        ) : (
                          <span className="text-[10px] text-slate-400">
                            No submissions
                          </span>
                        )}

                      </td>

                    </tr>
                  );
                })

              ) : (

                <tr>

                  <td
                    colSpan="7"
                    className="p-12 text-center text-slate-400"
                  >
                    No tasks found matching your filter criteria.
                  </td>

                </tr>

              )}

            </tbody>

          </table>

        </div>

      </div>

      {/* ====================================================== */}
      {/* ASSIGN TASK MODAL */}
      {/* ====================================================== */}

      <Modal
        isOpen={isAddModalOpen}
        onClose={() =>
          setIsAddModalOpen(false)
        }
        title="Assign New Task"
      >

        <form
          onSubmit={handleCreateTask}
          className="space-y-4"
        >

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

              <option value="">
                -- Choose Student --
              </option>

              {students.map((s) => (
                <option
                  key={s.id}
                  value={s.id}
                >
                  {s.fullName} (
                  {s.roadmapTitle ||
                    'No Track'}
                  )
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

                <option value="LOW">
                  LOW
                </option>

                <option value="MEDIUM">
                  MEDIUM
                </option>

                <option value="HIGH">
                  HIGH
                </option>

              </select>

            </div>

            <div>

              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Est. Duration (Mins)
              </label>

              <input
                type="number"
                min="15"
                value={
                  formData.estimatedDurationMinutes
                }
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    estimatedDurationMinutes:
                      Number(e.target.value)
                  })
                }
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />

            </div>

          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">

            <button
              type="button"
              onClick={() =>
                setIsAddModalOpen(false)
              }
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting
                ? 'Assigning...'
                : 'Assign Task'}
            </button>

          </div>

        </form>

      </Modal>

      {/* ====================================================== */}
      {/* REJECT MODAL */}
      {/* ====================================================== */}

      <Modal
        isOpen={isRejectModalOpen}
        onClose={() =>
          setIsRejectModalOpen(false)
        }
        title="Reject Task Proof"
      >

        <form
          onSubmit={handleRejectTask}
          className="space-y-4"
        >

          <div>

            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Reason for Rejection *
            </label>

            <textarea
              required
              rows="4"
              value={rejectMessage}
              onChange={(e) =>
                setRejectMessage(e.target.value)
              }
              placeholder="Explain why the submitted proof was rejected..."
              className="w-full px-3.5 py-3 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-rose-500/20 focus:border-rose-500 resize-none"
            />

          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">

            <button
              type="button"
              onClick={() =>
                setIsRejectModalOpen(false)
              }
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={verificationLoading}
              className="px-5 py-2 rounded-xl bg-rose-600 hover:bg-rose-700 text-white text-xs font-bold disabled:opacity-50"
            >
              {verificationLoading
                ? 'Rejecting...'
                : 'Reject Task'}
            </button>

          </div>

        </form>

      </Modal>

      {/* ====================================================== */}
      {/* PROOF PREVIEW MODAL */}
      {/* ====================================================== */}

      <Modal
        isOpen={!!previewTask}
        onClose={() =>
          setPreviewTask(null)
        }
        title={
          previewTask?.title ||
          'Proof Preview'
        }
      >

        {previewTask?.previewProof && (
          <div className="space-y-3">

            <div className="flex items-center gap-2">

              {previewTask.previewProof.proofType?.toUpperCase() ===
              'VIDEO' ? (
                <Video className="w-4 h-4 text-purple-600" />
              ) : (
                <ImageIcon className="w-4 h-4 text-indigo-600" />
              )}

              <span className="text-xs font-bold text-slate-700">
                {previewTask.previewProof.proofType ||
                  'PROOF'}
              </span>

            </div>

            {previewTask.previewProof.proofType?.toUpperCase() ===
            'VIDEO' ? (
              <video
                src={getProofUrl(
                  previewTask.previewProof.proofUrl
                )}
                controls
                className="w-full max-h-[70vh] rounded-xl"
              />
            ) : (
              <img
                src={getProofUrl(
                  previewTask.previewProof.proofUrl
                )}
                alt="Student proof"
                className="w-full max-h-[70vh] object-contain rounded-xl"
              />
            )}

          </div>
        )}

      </Modal>

      {/* ====================================================== */}
      {/* SUBMISSION HISTORY MODAL */}
      {/* ====================================================== */}

      <Modal
        isOpen={!!historyTask}
        onClose={closeHistoryModal}
        title="Submission History"
      >

        {historyTask && (
          <div className="space-y-5">

            {/* TASK HEADER */}

            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200">

              <div className="flex items-start gap-3">

                <div className="w-10 h-10 rounded-xl bg-indigo-100 text-indigo-700 flex items-center justify-center shrink-0">
                  <History className="w-5 h-5" />
                </div>

                <div className="min-w-0">

                  <h3 className="text-sm font-black text-slate-900">
                    {historyTask.title}
                  </h3>

                  <p className="text-xs text-slate-500 mt-1">
                    Student:{' '}
                    <span className="font-bold text-slate-700">
                      {historyTask.studentName ||
                        'Student'}
                    </span>
                  </p>

                  {historyTask.description && (
                    <p className="text-xs text-slate-500 mt-2">
                      {historyTask.description}
                    </p>
                  )}

                </div>

              </div>

            </div>

            {/* HISTORY */}

            {getSubmissionHistory(
              historyTask
            ).length > 0 ? (

              <div className="space-y-3">

                {getSubmissionHistory(
                  historyTask
                ).map(
                  (
                    submission,
                    index
                  ) => {

                    const isExpanded =
                      expandedSubmissionId ===
                      submission.id;

                    const proofs =
                      Array.isArray(
                        submission.proofs
                      )
                        ? submission.proofs
                        : [];

                    return (
                      <div
                        key={
                          submission.id ||
                          `submission-${index}`
                        }
                        className="border border-slate-200 rounded-2xl overflow-hidden bg-white"
                      >

                        {/* SUBMISSION HEADER */}

                        <button
                          type="button"
                          onClick={() =>
                            toggleSubmission(
                              submission.id
                            )
                          }
                          className="w-full p-4 flex items-center justify-between gap-3 hover:bg-slate-50 transition-colors text-left"
                        >

                          <div className="flex items-center gap-3 min-w-0">

                            <div className="w-9 h-9 rounded-xl bg-slate-100 flex items-center justify-center text-xs font-black text-slate-600 shrink-0">
                              #{index + 1}
                            </div>

                            <div className="min-w-0">

                              <div className="flex items-center gap-2 flex-wrap">

                                <span
                                  className={`px-2 py-1 rounded-full text-[9px] font-black uppercase ${getSubmissionStatusClasses(
                                    submission.status
                                  )}`}
                                >
                                  {submission.status
                                    ? submission.status.replace(
                                        /_/g,
                                        ' '
                                      )
                                    : 'UNKNOWN'}
                                </span>

                                <span className="text-[10px] font-bold text-slate-400">
                                  {proofs.length}{' '}
                                  {proofs.length === 1
                                    ? 'proof'
                                    : 'proofs'}
                                </span>

                              </div>

                              <p className="text-[10px] text-slate-400 mt-1">

                                Submitted:{' '}

                                {submission.submittedAt
                                  ? new Date(
                                      submission.submittedAt
                                    ).toLocaleString()
                                  : '--'}

                              </p>

                            </div>

                          </div>

                          {isExpanded ? (
                            <ChevronUp className="w-4 h-4 text-slate-400 shrink-0" />
                          ) : (
                            <ChevronDown className="w-4 h-4 text-slate-400 shrink-0" />
                          )}

                        </button>

                        {/* SUBMISSION DETAILS */}

                        {isExpanded && (
                          <div className="border-t border-slate-100 p-4 space-y-4">

                            {/* VERIFIED DATE */}

                            {submission.verifiedAt && (
                              <div className="text-[10px] text-slate-400">
                                Verified:{' '}
                                <span className="font-semibold text-slate-600">
                                  {new Date(
                                    submission.verifiedAt
                                  ).toLocaleString()}
                                </span>
                              </div>
                            )}

                            {/* ADMIN MESSAGE */}

                            {submission.adminMessage && (
                              <div
                                className={`p-3 rounded-xl text-xs ${
                                  submission.status ===
                                  'REJECTED'
                                    ? 'bg-rose-50 border border-rose-100 text-rose-800'
                                    : 'bg-emerald-50 border border-emerald-100 text-emerald-800'
                                }`}
                              >

                                <p className="text-[10px] font-black uppercase tracking-wider mb-1">
                                  Admin Message
                                </p>

                                <p>
                                  {
                                    submission.adminMessage
                                  }
                                </p>

                              </div>
                            )}

                            {/* PROOFS */}

                            {proofs.length > 0 ? (

                              <div>

                                <p className="text-[10px] font-black text-slate-500 uppercase tracking-wider mb-3">
                                  Submitted Proofs
                                </p>

                                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">

                                  {proofs.map(
                                    (
                                      proof,
                                      proofIndex
                                    ) => {

                                      const proofUrl =
                                        getProofUrl(
                                          proof.proofUrl
                                        );

                                      const isVideo =
                                        proof.proofType?.toUpperCase() ===
                                        'VIDEO';

                                      return (
                                        <div
                                          key={
                                            proof.id ||
                                            `${submission.id}-${proofIndex}`
                                          }
                                          className="rounded-xl border border-slate-200 overflow-hidden bg-slate-50"
                                        >

                                          <div className="bg-slate-100">

                                            {isVideo &&
                                            proofUrl ? (
                                              <video
                                                src={
                                                  proofUrl
                                                }
                                                controls
                                                className="w-full h-48 object-cover"
                                              />
                                            ) : !isVideo &&
                                              proofUrl ? (
                                              <img
                                                src={
                                                  proofUrl
                                                }
                                                alt={`Submission proof ${
                                                  proofIndex +
                                                  1
                                                }`}
                                                className="w-full h-48 object-cover cursor-pointer"
                                                onClick={() =>
                                                  openProofPreview(
                                                    historyTask,
                                                    proof
                                                  )
                                                }
                                              />
                                            ) : (
                                              <div className="h-48 flex flex-col items-center justify-center text-slate-400">

                                                <AlertCircle className="w-7 h-7 mb-2" />

                                                <span className="text-[10px] font-semibold">
                                                  Proof unavailable
                                                </span>

                                              </div>
                                            )}

                                          </div>

                                          <div className="p-3">

                                            <div className="flex items-center justify-between">

                                              <div className="flex items-center gap-2">

                                                {isVideo ? (
                                                  <Video className="w-4 h-4 text-purple-600" />
                                                ) : (
                                                  <ImageIcon className="w-4 h-4 text-indigo-600" />
                                                )}

                                                <span className="text-[10px] font-bold text-slate-700">
                                                  {proof.proofType ||
                                                    'PROOF'}
                                                </span>

                                              </div>

                                              {!isVideo &&
                                                proofUrl && (
                                                  <button
                                                    type="button"
                                                    onClick={() =>
                                                      openProofPreview(
                                                        historyTask,
                                                        proof
                                                      )
                                                    }
                                                    className="flex items-center gap-1 text-[10px] font-bold text-indigo-600 hover:text-indigo-800"
                                                  >
                                                    <Eye className="w-3 h-3" />
                                                    View
                                                  </button>
                                                )}

                                            </div>

                                            {proof.uploadedAt && (
                                              <p className="text-[9px] text-slate-400 mt-1.5">
                                                Uploaded:{' '}
                                                {new Date(
                                                  proof.uploadedAt
                                                ).toLocaleString()}
                                              </p>
                                            )}

                                          </div>

                                        </div>
                                      );
                                    }
                                  )}

                                </div>

                              </div>

                            ) : (

                              <div className="p-5 rounded-xl border border-dashed border-slate-300 text-center">

                                <AlertCircle className="w-6 h-6 mx-auto text-slate-400 mb-2" />

                                <p className="text-[10px] text-slate-500 font-semibold">
                                  No proof files found for this submission.
                                </p>

                              </div>

                            )}

                          </div>
                        )}

                      </div>
                    );
                  }
                )}

              </div>

            ) : (

              <div className="p-8 rounded-2xl border border-dashed border-slate-300 text-center">

                <History className="w-8 h-8 mx-auto text-slate-400 mb-2" />

                <p className="text-xs font-semibold text-slate-500">
                  No submission history found.
                </p>

              </div>

            )}

          </div>
        )}

      </Modal>

    </div>
  );
}