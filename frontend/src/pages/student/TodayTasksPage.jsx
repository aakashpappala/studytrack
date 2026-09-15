import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  CheckSquare,
  CheckCircle2,
  Clock,
  AlertCircle,
  Search,
  Filter,
  Flame,
  Calendar,
  Layers,
  Sparkles,
  Circle
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { format, isToday, isPast, parseISO } from 'date-fns';

export default function TodayTasksPage() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('ALL');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [viewTab, setViewTab] = useState('TODAY'); // TODAY, ALL
  const [actionLoadingId, setActionLoadingId] = useState(null);

  const fetchTasks = async () => {
    try {
      const res = await api.get('/student/tasks');
      setTasks(res.data.data || []);
    } catch (err) {
      console.error('Error loading student tasks', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleCompleteTask = async (task) => {
    if (task.status === 'COMPLETED') return;
    try {
      setActionLoadingId(task.id);
      await api.put(`/student/tasks/${task.id}/complete`);
      confetti({
        particleCount: 70,
        spread: 60,
        origin: { y: 0.7 }
      });
      await fetchTasks();
    } catch (err) {
      console.error('Error completing task', err);
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleStartTask = async (task) => {
    try {
      setActionLoadingId(task.id);
      await api.put(`/student/tasks/${task.id}`, { status: 'IN_PROGRESS' });
      await fetchTasks();
    } catch (err) {
      console.error('Error updating task to IN_PROGRESS', err);
    } finally {
      setActionLoadingId(null);
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading your assigned tasks..." />;
  }

  // Filter tasks
  const todayStr = format(new Date(), 'yyyy-MM-dd');
  const filteredTasks = tasks.filter((task) => {
    if (viewTab === 'TODAY') {
      const taskDate = task.assignedDate || task.dueDate;
      // If task is assigned today, due today, or pending and due earlier
      if (!taskDate) return false;
      const isTaskToday = taskDate === todayStr;
      const isOverduePending = task.dueDate && task.dueDate < todayStr && task.status !== 'COMPLETED';
      if (!isTaskToday && !isOverduePending) return false;
    }

    if (priorityFilter !== 'ALL' && task.priority !== priorityFilter) return false;
    if (statusFilter !== 'ALL' && task.status !== statusFilter) return false;

    if (searchQuery.trim() !== '') {
      const q = searchQuery.toLowerCase();
      const matchTitle = task.title?.toLowerCase().includes(q);
      const matchDesc = task.description?.toLowerCase().includes(q);
      const matchTopic = task.topicTitle?.toLowerCase().includes(q);
      if (!matchTitle && !matchDesc && !matchTopic) return false;
    }

    return true;
  });

  const todayTasksList = tasks.filter((t) => {
    const taskDate = t.assignedDate || t.dueDate;
    return taskDate === todayStr || (t.dueDate && t.dueDate < todayStr && t.status !== 'COMPLETED');
  });
  const todayCompleted = todayTasksList.filter((t) => t.status === 'COMPLETED').length;
  const todayTotal = todayTasksList.length;
  const todayPct = todayTotal > 0 ? Math.round((todayCompleted / todayTotal) * 100) : 100;

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header Banner */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-700 via-indigo-600 to-purple-700 p-8 text-white shadow-xl">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center md:justify-between gap-6">
          <div className="space-y-2">
            <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-indigo-200">
              <Calendar className="w-4 h-4" />
              <span>{format(new Date(), 'EEEE, MMMM do, yyyy')}</span>
            </div>
            <h1 className="text-3xl font-black tracking-tight">Daily Task Manager</h1>
            <p className="text-sm text-indigo-100 max-w-xl">
              Complete your daily learning activities to maintain your study streak and progress through your assigned curriculum.
            </p>
          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-5 border border-white/20 shrink-0 min-w-[220px]">
            <div className="flex justify-between items-center text-xs font-bold text-indigo-100 mb-2">
              <span>Today's Goal</span>
              <span>{todayCompleted}/{todayTotal} Done</span>
            </div>
            <ProgressBar progress={todayPct} color="amber" size="md" />
            <p className="text-[11px] text-indigo-200 text-center mt-2 font-medium">
              {todayPct === 100 && todayTotal > 0 ? '🎉 All tasks completed today!' : `${todayTotal - todayCompleted} tasks remaining`}
            </p>
          </div>
        </div>
      </div>

      {/* Navigation Tabs & Controls */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-white p-4 rounded-2xl border border-slate-100 shadow-sm">
        <div className="flex items-center gap-2">
          <button
            onClick={() => setViewTab('TODAY')}
            className={`px-4 py-2 text-xs font-bold rounded-xl transition-all ${
              viewTab === 'TODAY'
                ? 'bg-indigo-600 text-white shadow-sm shadow-indigo-600/30'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            }`}
          >
            Today's Focus ({todayTotal})
          </button>
          <button
            onClick={() => setViewTab('ALL')}
            className={`px-4 py-2 text-xs font-bold rounded-xl transition-all ${
              viewTab === 'ALL'
                ? 'bg-indigo-600 text-white shadow-sm shadow-indigo-600/30'
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
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search tasks by topic or title..."
            className="w-full pl-10 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 transition-all"
          />
        </div>
      </div>

      {/* Secondary Filters */}
      <div className="flex flex-wrap items-center justify-between gap-3 text-xs">
        <div className="flex items-center gap-2 flex-wrap">
          <span className="font-bold text-slate-500 uppercase tracking-wider text-[11px]">Priority:</span>
          {['ALL', 'HIGH', 'MEDIUM', 'LOW'].map((p) => (
            <button
              key={p}
              onClick={() => setPriorityFilter(p)}
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

        <div className="flex items-center gap-2 flex-wrap">
          <span className="font-bold text-slate-500 uppercase tracking-wider text-[11px]">Status:</span>
          {[
            { id: 'ALL', label: 'All' },
            { id: 'NOT_STARTED', label: 'Not Started' },
            { id: 'IN_PROGRESS', label: 'In Progress' },
            { id: 'COMPLETED', label: 'Completed' },
          ].map((s) => (
            <button
              key={s.id}
              onClick={() => setStatusFilter(s.id)}
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

      {/* Task Cards List */}
      <div className="space-y-4">
        {filteredTasks.length > 0 ? (
          filteredTasks.map((task) => {
            const isDone = task.status === 'COMPLETED';
            const isInProgress = task.status === 'IN_PROGRESS';
            const isActionLoading = actionLoadingId === task.id;

            return (
              <div
                key={task.id}
                className={`bg-white rounded-2xl border p-5 transition-all duration-200 shadow-sm hover:shadow-md ${
                  isDone
                    ? 'border-emerald-200/80 bg-emerald-50/10'
                    : isInProgress
                    ? 'border-indigo-200/80 bg-indigo-50/10'
                    : 'border-slate-200/80 hover:border-slate-300'
                }`}
              >
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                  <div className="flex items-start gap-4">
                    {/* Action Circle / Checkbox */}
                    <button
                      onClick={() => handleCompleteTask(task)}
                      disabled={isDone || isActionLoading}
                      title={isDone ? 'Completed' : 'Click to mark complete'}
                      className={`mt-1 w-7 h-7 rounded-xl flex items-center justify-center transition-all ${
                        isDone
                          ? 'bg-emerald-500 text-white cursor-default'
                          : 'border-2 border-slate-300 hover:border-emerald-500 text-transparent hover:text-emerald-500 hover:bg-emerald-50'
                      }`}
                    >
                      <CheckCircle2 className="w-5 h-5 fill-current" />
                    </button>

                    <div className="space-y-1.5">
                      <div className="flex items-center gap-2 flex-wrap">
                        {/* Priority Badge */}
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

                        {/* Breadcrumbs / Subject tag */}
                        {task.subjectTitle && (
                          <span className="px-2 py-0.5 rounded-md text-[10px] font-medium bg-slate-100 text-slate-600 border border-slate-200">
                            {task.subjectTitle}
                          </span>
                        )}
                        {task.topicTitle && (
                          <span className="text-xs text-slate-400 font-medium">
                            • {task.topicTitle}
                          </span>
                        )}
                      </div>

                      <h3
                        className={`text-base font-bold ${
                          isDone ? 'line-through text-slate-400' : 'text-slate-800'
                        }`}
                      >
                        {task.title}
                      </h3>

                      {task.description && (
                        <p className="text-xs text-slate-500 max-w-2xl">{task.description}</p>
                      )}

                      <div className="flex items-center gap-4 text-xs text-slate-400 pt-1">
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
                  </div>

                  {/* Right Actions */}
                  <div className="flex items-center gap-3 self-end md:self-center shrink-0">
                    {!isDone && !isInProgress && (
                      <button
                        onClick={() => handleStartTask(task)}
                        disabled={isActionLoading}
                        className="px-3 py-1.5 rounded-xl border border-indigo-200 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 text-xs font-bold transition-all"
                      >
                        Start Task
                      </button>
                    )}

                    {!isDone && (
                      <button
                        onClick={() => handleCompleteTask(task)}
                        disabled={isActionLoading}
                        className="px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold shadow-sm shadow-emerald-600/30 transition-all flex items-center gap-1.5"
                      >
                        <CheckCircle2 className="w-4 h-4" />
                        Mark Done
                      </button>
                    )}

                    {isDone && (
                      <span className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-emerald-100 text-emerald-800 text-xs font-bold">
                        <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                        Completed
                      </span>
                    )}
                  </div>
                </div>
              </div>
            );
          })
        ) : (
          <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 shadow-sm">
            <CheckSquare className="w-12 h-12 text-slate-300 mx-auto mb-3" />
            <h3 className="text-base font-bold text-slate-700">No tasks match your filters</h3>
            <p className="text-xs text-slate-400 mt-1">Try changing the status, priority, or search criteria.</p>
          </div>
        )}
      </div>
    </div>
  );
}
