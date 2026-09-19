import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';

import {
  CheckSquare,
  CheckCircle2,
  Clock,
  Search,
  Calendar,
  Upload,
  Video,
  Image as ImageIcon,
  AlertCircle,
  RefreshCw,
  X
} from 'lucide-react';

import { format } from 'date-fns';

export default function TodayTasksPage() {

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  const [searchQuery, setSearchQuery] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('ALL');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [viewTab, setViewTab] = useState('TODAY');

  const [actionLoadingId, setActionLoadingId] = useState(null);

  // Multiple files for each task
  const [selectedFiles, setSelectedFiles] = useState({});

  // =========================================================
  // FETCH TASKS
  // =========================================================

  const fetchTasks = async () => {
    try {

      const res = await api.get('/student/tasks');

      setTasks(res.data.data || []);

    } catch (err) {

      console.error(
        'Error loading student tasks',
        err
      );

    } finally {

      setLoading(false);

    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  // =========================================================
  // FILE SELECT - MULTIPLE FILES
  // =========================================================

  const handleFileSelect = (taskId, fileList) => {

    if (!fileList || fileList.length === 0) {
      return;
    }

    const files = Array.from(fileList);

    // Maximum number of files
    if (files.length > 10) {

      alert(
        'You can upload maximum 10 files at a time.'
      );

      return;
    }

    // Validate every file
    for (const file of files) {

      const isImage =
        file.type.startsWith('image/');

      const isVideo =
        file.type.startsWith('video/');

      if (!isImage && !isVideo) {

        alert(
          `Only image or video files are allowed.\n\nInvalid file: ${file.name}`
        );

        return;
      }

      // 50 MB per file
      if (file.size > 50 * 1024 * 1024) {

        alert(
          `File size must be less than 50 MB.\n\nFile: ${file.name}`
        );

        return;
      }
    }

    // Save multiple files for this task
    setSelectedFiles((prev) => ({
      ...prev,
      [taskId]: files
    }));
  };

  // =========================================================
  // REMOVE SELECTED FILE
  // =========================================================

  const removeSelectedFile = (taskId, fileIndex) => {

    setSelectedFiles((prev) => {

      const currentFiles =
        prev[taskId] || [];

      const updatedFiles =
        currentFiles.filter(
          (_, index) => index !== fileIndex
        );

      return {
        ...prev,
        [taskId]: updatedFiles
      };
    });
  };

  // =========================================================
  // CLEAR SELECTED FILES
  // =========================================================

  const clearSelectedFiles = (taskId) => {

    setSelectedFiles((prev) => {

      const updated = {
        ...prev
      };

      delete updated[taskId];

      return updated;
    });
  };

  // =========================================================
  // SUBMIT MULTIPLE PROOFS
  // =========================================================

  const handleSubmitProof = async (task) => {

    const files =
      selectedFiles[task.id] || [];

    if (files.length === 0) {

      alert(
        'Please select at least one image or video.'
      );

      return;
    }

    try {

      setActionLoadingId(task.id);

      const formData =
        new FormData();

      // Add every file using "files"
      files.forEach((file) => {

        formData.append(
          'files',
          file
        );

      });

      await api.post(
        `/student/tasks/${task.id}/submit-proof`,
        formData
      );

      // Clear selected files
      clearSelectedFiles(task.id);

      // Refresh tasks
      await fetchTasks();

      alert(
        'Proof files uploaded successfully. Waiting for admin verification.'
      );

    } catch (err) {

      console.error(
        'Error uploading proofs',
        err
      );

      const message =
        err?.response?.data?.message ||
        'Failed to upload proof files. Please try again.';

      alert(message);

    } finally {

      setActionLoadingId(null);
    }
  };

  // =========================================================
  // LOADING
  // =========================================================

  if (loading) {

    return (
      <LoadingSpinner
        size="lg"
        message="Loading your assigned tasks..."
      />
    );
  }

  // =========================================================
  // FILTER TASKS
  // =========================================================

  const todayStr =
    format(
      new Date(),
      'yyyy-MM-dd'
    );

  const filteredTasks =
    tasks.filter((task) => {

      if (viewTab === 'TODAY') {

        const taskDate =
          task.assignedDate ||
          task.dueDate;

        if (!taskDate) {
          return false;
        }

        const isTaskToday =
          taskDate === todayStr;

        const isOverduePending =
          task.dueDate &&
          task.dueDate < todayStr &&
          task.status !== 'COMPLETED';

        if (
          !isTaskToday &&
          !isOverduePending
        ) {
          return false;
        }
      }

      if (
        priorityFilter !== 'ALL' &&
        task.priority !== priorityFilter
      ) {
        return false;
      }

      if (
        statusFilter !== 'ALL' &&
        task.status !== statusFilter
      ) {
        return false;
      }

      if (
        searchQuery.trim() !== ''
      ) {

        const q =
          searchQuery.toLowerCase();

        const matchTitle =
          task.title
            ?.toLowerCase()
            .includes(q);

        const matchDesc =
          task.description
            ?.toLowerCase()
            .includes(q);

        const matchTopic =
          task.topicTitle
            ?.toLowerCase()
            .includes(q);

        if (
          !matchTitle &&
          !matchDesc &&
          !matchTopic
        ) {
          return false;
        }
      }

      return true;
    });

  // =========================================================
  // TODAY PROGRESS
  // =========================================================

  const todayTasksList =
    tasks.filter((t) => {

      const taskDate =
        t.assignedDate ||
        t.dueDate;

      return (
        taskDate === todayStr ||
        (
          t.dueDate &&
          t.dueDate < todayStr &&
          t.status !== 'COMPLETED'
        )
      );
    });

  const todayCompleted =
    todayTasksList.filter(
      (t) =>
        t.status === 'COMPLETED'
    ).length;

  const todayTotal =
    todayTasksList.length;

  const todayPct =
    todayTotal > 0
      ? Math.round(
          (todayCompleted /
            todayTotal) *
            100
        )
      : 100;

  // =========================================================
  // STATUS HELPERS
  // =========================================================

  const getStatusLabel =
    (status) => {

      switch (status) {

        case 'NOT_STARTED':
          return 'Not Started';

        case 'IN_PROGRESS':
          return 'In Progress';

        case 'PENDING_VERIFICATION':
          return 'Pending Verification';

        case 'REJECTED':
          return 'Rejected';

        case 'COMPLETED':
          return 'Completed';

        default:
          return status;
      }
    };

  const getStatusClass =
    (status) => {

      switch (status) {

        case 'COMPLETED':
          return 'bg-emerald-100 text-emerald-800 border-emerald-200';

        case 'PENDING_VERIFICATION':
          return 'bg-amber-100 text-amber-800 border-amber-200';

        case 'REJECTED':
          return 'bg-rose-100 text-rose-800 border-rose-200';

        case 'IN_PROGRESS':
          return 'bg-indigo-100 text-indigo-800 border-indigo-200';

        default:
          return 'bg-slate-100 text-slate-700 border-slate-200';
      }
    };

  // =========================================================
  // UI
  // =========================================================

  return (

    <div className="space-y-8 animate-fade-in pb-12">

      {/* Header Banner */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-700 via-indigo-600 to-purple-700 p-8 text-white shadow-xl">

        <div className="relative z-10 flex flex-col md:flex-row md:items-center md:justify-between gap-6">

          <div className="space-y-2">

            <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-indigo-200">

              <Calendar className="w-4 h-4" />

              <span>
                {format(
                  new Date(),
                  'EEEE, MMMM do, yyyy'
                )}
              </span>

            </div>

            <h1 className="text-3xl font-black tracking-tight">
              Daily Task Manager
            </h1>

            <p className="text-sm text-indigo-100 max-w-xl">
              Complete your daily learning activities by uploading
              image or video proof for admin verification.
            </p>

          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-5 border border-white/20 shrink-0 min-w-[220px]">

            <div className="flex justify-between items-center text-xs font-bold text-indigo-100 mb-2">

              <span>
                Today's Goal
              </span>

              <span>
                {todayCompleted}/{todayTotal} Done
              </span>

            </div>

            <ProgressBar
              progress={todayPct}
              color="amber"
              size="md"
            />

            <p className="text-[11px] text-indigo-200 text-center mt-2 font-medium">

              {todayPct === 100 &&
              todayTotal > 0
                ? '🎉 All tasks completed today!'
                : `${todayTotal - todayCompleted} tasks remaining`
              }

            </p>

          </div>

        </div>

      </div>

      {/* Navigation Tabs */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-white p-4 rounded-2xl border border-slate-100 shadow-sm">

        <div className="flex items-center gap-2">

          <button
            onClick={() =>
              setViewTab('TODAY')
            }
            className={`px-4 py-2 text-xs font-bold rounded-xl transition-all ${
              viewTab === 'TODAY'
                ? 'bg-indigo-600 text-white shadow-sm'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            }`}
          >
            Today's Focus ({todayTotal})
          </button>

          <button
            onClick={() =>
              setViewTab('ALL')
            }
            className={`px-4 py-2 text-xs font-bold rounded-xl transition-all ${
              viewTab === 'ALL'
                ? 'bg-indigo-600 text-white shadow-sm'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            }`}
          >
            All Tasks ({tasks.length})
          </button>

        </div>

        {/* Search */}
        <div className="relative flex-1 max-w-md">

          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />

          <input
            type="text"
            value={searchQuery}
            onChange={(e) =>
              setSearchQuery(
                e.target.value
              )
            }
            placeholder="Search tasks by topic or title..."
            className="w-full pl-10 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 transition-all"
          />

        </div>

      </div>

      {/* Filters */}
      <div className="flex flex-wrap items-center justify-between gap-3 text-xs">

        {/* Priority */}
        <div className="flex items-center gap-2 flex-wrap">

          <span className="font-bold text-slate-500 uppercase tracking-wider text-[11px]">
            Priority:
          </span>

          {[
            'ALL',
            'HIGH',
            'MEDIUM',
            'LOW'
          ].map((p) => (

            <button
              key={p}
              onClick={() =>
                setPriorityFilter(p)
              }
              className={`px-3 py-1 rounded-lg font-semibold transition-all ${
                priorityFilter === p
                  ? 'bg-slate-800 text-white'
                  : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
            >
              {p}
            </button>

          ))}

        </div>

        {/* Status */}
        <div className="flex items-center gap-2 flex-wrap">

          <span className="font-bold text-slate-500 uppercase tracking-wider text-[11px]">
            Status:
          </span>

          {[
            {
              id: 'ALL',
              label: 'All'
            },
            {
              id: 'NOT_STARTED',
              label: 'Not Started'
            },
            {
              id: 'IN_PROGRESS',
              label: 'In Progress'
            },
            {
              id: 'PENDING_VERIFICATION',
              label: 'Pending Verification'
            },
            {
              id: 'REJECTED',
              label: 'Rejected'
            },
            {
              id: 'COMPLETED',
              label: 'Completed'
            }
          ].map((s) => (

            <button
              key={s.id}
              onClick={() =>
                setStatusFilter(s.id)
              }
              className={`px-3 py-1 rounded-lg font-semibold transition-all ${
                statusFilter === s.id
                  ? 'bg-slate-800 text-white'
                  : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
            >
              {s.label}
            </button>

          ))}

        </div>

      </div>

      {/* Task Cards */}
      <div className="space-y-4">

        {filteredTasks.length > 0 ? (

          filteredTasks.map((task) => {

            const isDone =
              task.status === 'COMPLETED';

            const isPending =
              task.status === 'PENDING_VERIFICATION';

            const isRejected =
              task.status === 'REJECTED';

            const isActionLoading =
              actionLoadingId === task.id;

            const taskFiles =
              selectedFiles[task.id] || [];

            return (

              <div
                key={task.id}
                className={`bg-white rounded-2xl border p-5 transition-all duration-200 shadow-sm hover:shadow-md ${
                  isDone
                    ? 'border-emerald-200/80 bg-emerald-50/10'
                    : isPending
                    ? 'border-amber-200/80 bg-amber-50/10'
                    : isRejected
                    ? 'border-rose-200/80 bg-rose-50/10'
                    : 'border-slate-200/80 hover:border-slate-300'
                }`}
              >

                <div className="flex flex-col gap-5">

                  {/* Main task information */}
                  <div className="flex items-start gap-4">

                    <div
                      className={`mt-1 w-8 h-8 rounded-xl flex items-center justify-center shrink-0 ${
                        isDone
                          ? 'bg-emerald-500 text-white'
                          : isPending
                          ? 'bg-amber-100 text-amber-700'
                          : isRejected
                          ? 'bg-rose-100 text-rose-700'
                          : 'bg-slate-100 text-slate-500'
                      }`}
                    >

                      {isDone ? (
                        <CheckCircle2 className="w-5 h-5" />
                      ) : isPending ? (
                        <Clock className="w-5 h-5" />
                      ) : isRejected ? (
                        <AlertCircle className="w-5 h-5" />
                      ) : (
                        <CheckSquare className="w-5 h-5" />
                      )}

                    </div>

                    <div className="space-y-1.5 flex-1">

                      <div className="flex items-center gap-2 flex-wrap">

                        {/* Priority */}
                        <span
                          className={`px-2 py-0.5 rounded-md text-[10px] font-bold uppercase tracking-wider ${
                            task.priority === 'HIGH'
                              ? 'bg-rose-100 text-rose-700 border border-rose-200'
                              : task.priority === 'MEDIUM'
                              ? 'bg-amber-100 text-amber-700 border border-amber-200'
                              : 'bg-sky-100 text-sky-700 border border-sky-200'
                          }`}
                        >
                          {task.priority}
                        </span>

                        {/* Subject */}
                        {task.subjectTitle && (
                          <span className="px-2 py-0.5 rounded-md text-[10px] font-medium bg-slate-100 text-slate-600 border border-slate-200">
                            {task.subjectTitle}
                          </span>
                        )}

                        {/* Topic */}
                        {task.topicTitle && (
                          <span className="text-xs text-slate-400 font-medium">
                            • {task.topicTitle}
                          </span>
                        )}

                      </div>

                      <h3
                        className={`text-base font-bold ${
                          isDone
                            ? 'line-through text-slate-400'
                            : 'text-slate-800'
                        }`}
                      >
                        {task.title}
                      </h3>

                      {task.description && (
                        <p className="text-xs text-slate-500 max-w-2xl">
                          {task.description}
                        </p>
                      )}

                      <div className="flex items-center gap-4 text-xs text-slate-400 pt-1 flex-wrap">

                        {task.estimatedDurationMinutes && (
                          <span className="flex items-center gap-1 font-medium">
                            <Clock className="w-3.5 h-3.5" />
                            {task.estimatedDurationMinutes} mins
                          </span>
                        )}

                        {task.dueDate && (
                          <span className="flex items-center gap-1 font-medium text-slate-500">
                            <Calendar className="w-3.5 h-3.5" />
                            Due {task.dueDate}
                          </span>
                        )}

                      </div>

                    </div>

                    {/* Status */}
                    <span
                      className={`shrink-0 px-3 py-1.5 rounded-xl border text-xs font-bold ${getStatusClass(task.status)}`}
                    >
                      {getStatusLabel(task.status)}
                    </span>

                  </div>

                  {/* Rejection Message */}
                  {isRejected &&
                    task.adminMessage && (

                      <div className="ml-0 md:ml-12 p-4 rounded-xl bg-rose-50 border border-rose-200">

                        <div className="flex items-start gap-2">

                          <AlertCircle className="w-4 h-4 text-rose-600 mt-0.5 shrink-0" />

                          <div>

                            <p className="text-xs font-bold text-rose-800">
                              Admin Message
                            </p>

                            <p className="text-xs text-rose-700 mt-1">
                              {task.adminMessage}
                            </p>

                          </div>

                        </div>

                      </div>
                  )}

                  {/* Pending Message */}
                  {isPending && (

                    <div className="ml-0 md:ml-12 p-4 rounded-xl bg-amber-50 border border-amber-200">

                      <div className="flex items-center gap-2">

                        <Clock className="w-4 h-4 text-amber-600" />

                        <p className="text-xs font-semibold text-amber-800">
                          Your proof has been submitted. Waiting for admin verification.
                        </p>

                      </div>

                    </div>
                  )}

                  {/* Proof Upload Area */}
                  {!isDone &&
                    !isPending && (

                      <div className="ml-0 md:ml-12 p-5 rounded-2xl bg-slate-50 border border-slate-200">

                        <div className="flex flex-col gap-4">

                          <div>

                            <div className="flex items-center gap-2">

                              {isRejected ? (
                                <RefreshCw className="w-5 h-5 text-rose-600" />
                              ) : (
                                <Upload className="w-5 h-5 text-indigo-600" />
                              )}

                              <h4 className="text-sm font-bold text-slate-800">

                                {isRejected
                                  ? 'Resubmit Proof'
                                  : 'Upload Proof'}

                              </h4>

                            </div>

                            <p className="text-xs text-slate-500 mt-1">
                              Upload one or more images/videos showing that you completed this task.
                            </p>

                          </div>

                          {/* File Picker */}
                          <label className="cursor-pointer">

                            <div className="flex items-center gap-3 px-4 py-3 bg-white border border-slate-200 rounded-xl hover:border-indigo-400 transition-all">

                              <div className="w-9 h-9 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center shrink-0">

                                <Upload className="w-5 h-5" />

                              </div>

                              <div className="min-w-0">

                                <p className="text-xs font-bold text-slate-700">

                                  {taskFiles.length > 0
                                    ? `${taskFiles.length} file${taskFiles.length > 1 ? 's' : ''} selected`
                                    : 'Choose Images or Videos'}

                                </p>

                                <p className="text-[10px] text-slate-400">
                                  Maximum 10 files • 50 MB per file
                                </p>

                              </div>

                            </div>

                            <input
                              type="file"
                              accept="image/*,video/*"
                              multiple
                              className="hidden"
                              onChange={(e) =>
                                handleFileSelect(
                                  task.id,
                                  e.target.files
                                )
                              }
                            />

                          </label>

                          {/* Selected Files */}
                          {taskFiles.length > 0 && (

                            <div className="space-y-2">

                              {taskFiles.map(
                                (file, index) => (

                                  <div
                                    key={`${file.name}-${index}`}
                                    className="flex items-center gap-3 bg-white border border-slate-200 rounded-xl px-3 py-2"
                                  >

                                    <div className="w-8 h-8 rounded-lg bg-slate-100 flex items-center justify-center shrink-0">

                                      {file.type.startsWith('video/')
                                        ? (
                                          <Video className="w-4 h-4 text-indigo-600" />
                                        )
                                        : (
                                          <ImageIcon className="w-4 h-4 text-indigo-600" />
                                        )}

                                    </div>

                                    <div className="flex-1 min-w-0">

                                      <p className="text-xs font-semibold text-slate-700 truncate">
                                        {file.name}
                                      </p>

                                      <p className="text-[10px] text-slate-400">
                                        {(file.size / (1024 * 1024)).toFixed(2)} MB
                                      </p>

                                    </div>

                                    <button
                                      type="button"
                                      onClick={() =>
                                        removeSelectedFile(
                                          task.id,
                                          index
                                        )
                                      }
                                      className="w-7 h-7 rounded-lg hover:bg-rose-50 text-slate-400 hover:text-rose-600 flex items-center justify-center"
                                    >
                                      <X className="w-4 h-4" />
                                    </button>

                                  </div>

                                )
                              )}

                            </div>
                          )}

                          {/* Buttons */}
                          <div className="flex flex-col md:flex-row gap-3">

                            <button
                              onClick={() =>
                                handleSubmitProof(task)
                              }
                              disabled={
                                taskFiles.length === 0 ||
                                isActionLoading
                              }
                              className="flex-1 px-5 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 disabled:bg-slate-300 disabled:cursor-not-allowed text-white text-xs font-bold transition-all flex items-center justify-center gap-2"
                            >

                              {isActionLoading ? (
                                <>
                                  <RefreshCw className="w-4 h-4 animate-spin" />
                                  Uploading...
                                </>
                              ) : (
                                <>
                                  <Upload className="w-4 h-4" />
                                  Submit Proof
                                </>
                              )}

                            </button>

                            {taskFiles.length > 0 && (

                              <button
                                type="button"
                                onClick={() =>
                                  clearSelectedFiles(
                                    task.id
                                  )
                                }
                                disabled={isActionLoading}
                                className="px-5 py-3 rounded-xl bg-white border border-slate-200 hover:bg-slate-50 text-slate-600 text-xs font-bold transition-all"
                              >
                                Clear
                              </button>

                            )}

                          </div>

                        </div>

                      </div>
                  )}

{/* Completed + Final Approved Proof */}
{isDone && (

  <div className="ml-0 md:ml-12 space-y-4">

    {/* Completed Message */}
    <div className="flex items-center gap-2 px-4 py-3 rounded-xl bg-emerald-50 border border-emerald-200">

      <CheckCircle2 className="w-5 h-5 text-emerald-600" />

      <p className="text-xs font-bold text-emerald-800">
        Task completed and verified by admin.
      </p>

    </div>

    {/* Final Approved Proof */}
    {Array.isArray(task.proofs) &&
      task.proofs.length > 0 && (

        <div className="p-5 rounded-2xl bg-slate-50 border border-slate-200">

          <div className="flex items-center gap-2 mb-4">

            <CheckCircle2 className="w-5 h-5 text-emerald-600" />

            <div>
              <h4 className="text-sm font-bold text-slate-800">
                Final Approved Proof
              </h4>

              <p className="text-[11px] text-slate-500">
                Your latest proof approved by admin.
              </p>
            </div>

          </div>

          {/* Proof Files */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

            {task.proofs.map((proof, index) => {

              const proofUrl =
                proof.proofUrl?.startsWith('http')
                  ? proof.proofUrl
                  : proof.proofUrl;

              const isVideo =
                proof.proofType?.toUpperCase() === 'VIDEO' ||
                proof.proofUrl?.match(
                  /\.(mp4|webm|mov|avi|mkv)(\?|$)/i
                );

              return (

                <div
                  key={proof.id || index}
                  className="bg-white rounded-xl border border-slate-200 overflow-hidden"
                >

                  {/* Video */}
                  {isVideo ? (

                    <video
                      src={proofUrl}
                      controls
                      className="w-full max-h-[400px] object-contain bg-black"
                    />

                  ) : (

                    /* Image */
                    <img
                      src={proofUrl}
                      alt={`Approved proof ${index + 1}`}
                      className="w-full max-h-[400px] object-contain bg-slate-100"
                    />

                  )}

                  <div className="px-3 py-2 flex items-center justify-between">

                    <div className="flex items-center gap-2">

                      {isVideo ? (
                        <Video className="w-4 h-4 text-indigo-600" />
                      ) : (
                        <ImageIcon className="w-4 h-4 text-indigo-600" />
                      )}

                      <span className="text-[11px] font-semibold text-slate-600">
                        {isVideo ? 'Video Proof' : 'Image Proof'}
                      </span>

                    </div>

                    <span className="text-[10px] font-bold text-emerald-600">
                      Approved
                    </span>

                  </div>

                </div>

              );
            })}

          </div>

        </div>
    )}

  </div>
)}

                </div>

              </div>
            );
          })

        ) : (

          <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 shadow-sm">

            <CheckSquare className="w-12 h-12 text-slate-300 mx-auto mb-3" />

            <h3 className="text-base font-bold text-slate-700">
              No tasks match your filters
            </h3>

            <p className="text-xs text-slate-400 mt-1">
              Try changing the status, priority, or search criteria.
            </p>

          </div>
        )}

      </div>

    </div>
  );
}