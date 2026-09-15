import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Flame,
  CheckCircle2,
  Clock,
  Map,
  Calendar as CalendarIcon,
  Sparkles,
  ArrowRight,
  Circle,
  Clock3,
  Award
} from 'lucide-react';
import confetti from 'canvas-confetti';
import { format } from 'date-fns';
import { Link } from 'react-router-dom';

export default function StudentDashboard() {
  const { user } = useAuth();
  const [summary, setSummary] = useState(null);
  const [todayTasks, setTodayTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [completingId, setCompletingId] = useState(null);

  const fetchDashboardData = async () => {
    try {
      const [summaryRes, tasksRes] = await Promise.all([
        api.get('/student/progress'),
        api.get('/student/tasks/today'),
      ]);
      setSummary(summaryRes.data.data);
      setTodayTasks(tasksRes.data.data);
    } catch (err) {
      console.error('Error fetching student dashboard', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const handleToggleTask = async (task) => {
    if (task.status === 'COMPLETED') return;
    try {
      setCompletingId(task.id);
      await api.put(`/student/tasks/${task.id}/complete`);

      // Trigger celebration confetti
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.7 },
      });

      // Refresh data immediately
      await fetchDashboardData();
    } catch (err) {
      console.error('Error completing task', err);
    } finally {
      setCompletingId(null);
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading your personalized dashboard..." />;
  }

  const completedToday = todayTasks.filter((t) => t.status === 'COMPLETED');
  const pendingToday = todayTasks.filter((t) => t.status !== 'COMPLETED');
  const todayDateFormatted = format(new Date(), 'EEEE, MMMM do, yyyy');

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Welcome Banner */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-600 via-indigo-700 to-purple-800 p-8 text-white shadow-xl shadow-indigo-500/10">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center md:justify-between gap-6">
          <div className="space-y-2 max-w-xl">
            <div className="flex items-center gap-2 text-xs font-semibold text-indigo-200 uppercase tracking-wider">
              <CalendarIcon className="w-4 h-4" />
              <span>{todayDateFormatted}</span>
            </div>
            <h1 className="text-3xl font-black tracking-tight">
              Welcome back, {user?.fullName}! 👋
            </h1>
            <p className="text-sm text-indigo-100/90 leading-relaxed">
              Your personalized roadmap: <span className="font-bold text-white underline decoration-indigo-300 underline-offset-4">{summary?.roadmapTitle}</span>. Stay consistent and keep up the learning momentum!
            </p>
          </div>

          {/* Streak Callout */}
          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 border border-white/20 flex items-center gap-4 shrink-0">
            <div className="w-12 h-12 rounded-xl bg-amber-500/20 text-amber-300 flex items-center justify-center">
              <Flame className="w-8 h-8 fill-amber-400 text-amber-500 animate-bounce" />
            </div>
            <div>
              <span className="text-[11px] font-bold text-indigo-200 uppercase tracking-wider block">
                Current Streak
              </span>
              <span className="text-2xl font-black text-white">
                {summary?.currentStreak || 0} {summary?.currentStreak === 1 ? 'Day' : 'Days'}
              </span>
              <span className="text-[10px] text-indigo-200/80 block">
                Best: {summary?.longestStreak || 0} Days
              </span>
            </div>
          </div>
        </div>

        {/* Subtle Decorative Elements */}
        <div className="absolute -right-10 -bottom-10 w-64 h-64 bg-purple-500/20 rounded-full blur-3xl pointer-events-none" />
      </div>

      {/* Top Stat Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Today's Progress"
          value={`${summary?.todayProgress || 0}%`}
          subtitle={`${summary?.todayCompletedTasks || 0} of ${summary?.todayTotalTasks || 0} tasks done`}
          icon={CheckCircle2}
          color="emerald"
        />
        <StatCard
          title="Roadmap Completion"
          value={`${summary?.overallProgress || 0}%`}
          subtitle={`${summary?.completedTasks || 0} / ${summary?.totalTasks || 0} total tasks`}
          icon={Map}
          color="indigo"
        />
        <StatCard
          title="Today's Study Time"
          value={summary?.todayStudyTime || '0h 0m'}
          subtitle={`Week: ${summary?.weeklyStudyTime || '0h 0m'}`}
          icon={Clock}
          color="amber"
        />
        <StatCard
          title="Total Study Hours"
          value={`${Math.round(((summary?.totalStudyMinutes || 0) / 60) * 10) / 10}h`}
          subtitle={`Month: ${summary?.monthlyStudyTime || '0h 0m'}`}
          icon={Award}
          color="purple"
        />
      </div>

      {/* Today's Tasks Progress Bar Visual Breakdown */}
      <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-base font-extrabold text-slate-900 tracking-tight flex items-center gap-2">
              <span>Today's Goal Tracker</span>
              <span className="text-xs px-2.5 py-0.5 rounded-full bg-indigo-50 text-indigo-600 font-bold border border-indigo-100">
                {summary?.todayCompletedTasks || 0}/{summary?.todayTotalTasks || 0} Completed
              </span>
            </h3>
            <p className="text-xs text-slate-500 mt-0.5">
              Complete your daily tasks to maintain your study streak and advance your roadmap progress.
            </p>
          </div>
          <span className="text-2xl font-black text-indigo-600">{summary?.todayProgress || 0}%</span>
        </div>

        <ProgressBar
          value={summary?.todayProgress || 0}
          height="h-3.5"
          color={summary?.todayProgress >= 80 ? 'emerald' : summary?.todayProgress >= 40 ? 'amber' : 'brand'}
        />
      </div>

      {/* Main Grid: Today's Tasks & Subject Breakdown */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Today's Tasks (2 cols) */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-black text-slate-900 tracking-tight flex items-center gap-2">
              <span>Today's Tasks</span>
              <span className="text-xs font-semibold text-slate-400">({todayTasks.length})</span>
            </h2>
            <Link
              to="/today-tasks"
              className="text-xs font-bold text-indigo-600 hover:text-indigo-700 flex items-center gap-1"
            >
              <span>View All</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          {todayTasks.length === 0 ? (
            <div className="bg-white rounded-3xl p-10 border border-slate-200/80 text-center space-y-2">
              <div className="w-12 h-12 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center mx-auto">
                <Sparkles className="w-6 h-6" />
              </div>
              <p className="text-sm font-bold text-slate-800">No tasks assigned for today!</p>
              <p className="text-xs text-slate-500">Check your personalized roadmap to pick up upcoming modules.</p>
              <Link
                to="/my-roadmap"
                className="inline-block mt-3 px-4 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold shadow-sm hover:bg-indigo-700 transition-colors"
              >
                Go to My Roadmap
              </Link>
            </div>
          ) : (
            <div className="space-y-3">
              {todayTasks.map((task) => {
                const isDone = task.status === 'COMPLETED';
                const isWorking = completingId === task.id;

                const priorityColors = {
                  HIGH: 'text-rose-600 bg-rose-50 border-rose-100',
                  MEDIUM: 'text-amber-600 bg-amber-50 border-amber-100',
                  LOW: 'text-slate-600 bg-slate-50 border-slate-100',
                };

                return (
                  <div
                    key={task.id}
                    className={`group bg-white rounded-2xl p-4 border transition-all flex items-start justify-between gap-4 ${
                      isDone
                        ? 'border-emerald-200/60 bg-emerald-50/20'
                        : 'border-slate-200/80 hover:border-indigo-200 hover:shadow-sm'
                    }`}
                  >
                    <div className="flex items-start gap-3.5">
                      <button
                        type="button"
                        disabled={isDone || isWorking}
                        onClick={() => handleToggleTask(task)}
                        className={`mt-0.5 w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all ${
                          isDone
                            ? 'bg-emerald-500 border-emerald-500 text-white shadow-sm'
                            : 'border-slate-300 hover:border-indigo-600 text-transparent hover:text-indigo-600'
                        } disabled:cursor-not-allowed`}
                      >
                        <CheckCircle2 className="w-4 h-4" />
                      </button>

                      <div className="space-y-1">
                        <div className="flex items-center gap-2 flex-wrap">
                          <h4
                            className={`text-sm font-bold tracking-tight ${
                              isDone ? 'text-slate-400 line-through' : 'text-slate-900'
                            }`}
                          >
                            {task.title}
                          </h4>
                          <span
                            className={`text-[10px] font-bold px-2 py-0.5 rounded-full border ${
                              priorityColors[task.priority] || priorityColors.MEDIUM
                            }`}
                          >
                            {task.priority}
                          </span>
                        </div>

                        {task.description && (
                          <p className="text-xs text-slate-500 line-clamp-1">{task.description}</p>
                        )}

                        <div className="flex items-center gap-3 text-[11px] text-slate-400 font-medium pt-1">
                          {task.subjectTitle && (
                            <span className="font-semibold text-slate-600">{task.subjectTitle}</span>
                          )}
                          {task.topicTitle && <span>• {task.topicTitle}</span>}
                          {task.estimatedDurationMinutes && (
                            <span className="flex items-center gap-1">
                              <Clock3 className="w-3 h-3" /> {task.estimatedDurationMinutes}m
                            </span>
                          )}
                        </div>
                      </div>
                    </div>

                    <div className="shrink-0">
                      {isDone ? (
                        <span className="text-[11px] font-bold text-emerald-600 bg-emerald-50 px-2.5 py-1 rounded-full border border-emerald-100 flex items-center gap-1">
                          <CheckCircle2 className="w-3.5 h-3.5" /> Done
                        </span>
                      ) : (
                        <button
                          onClick={() => handleToggleTask(task)}
                          disabled={isWorking}
                          className="text-xs font-bold text-indigo-600 hover:text-indigo-700 bg-indigo-50 hover:bg-indigo-100 px-3 py-1.5 rounded-xl transition-colors disabled:opacity-50"
                        >
                          {isWorking ? 'Saving...' : 'Mark Done'}
                        </button>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Right Sidebar: Subject Breakdown & Quick Actions */}
        <div className="space-y-6">
          {/* Subject Mastery Breakdown */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-extrabold text-slate-900 tracking-tight">
                Subject Mastery
              </h3>
              <Link to="/my-roadmap" className="text-xs font-bold text-indigo-600 hover:text-indigo-700">
                View All
              </Link>
            </div>

            <div className="space-y-3.5">
              {summary?.subjectProgress?.length === 0 ? (
                <p className="text-xs text-slate-400 text-center py-4">No subjects found</p>
              ) : (
                summary?.subjectProgress?.map((subj) => (
                  <div key={subj.subjectId} className="space-y-1.5">
                    <div className="flex justify-between items-center text-xs font-bold text-slate-700">
                      <span>{subj.subjectTitle}</span>
                      <span className="text-slate-500">{subj.progressPercentage}%</span>
                    </div>
                    <ProgressBar
                      value={subj.progressPercentage}
                      height="h-2"
                      color={subj.progressPercentage >= 75 ? 'emerald' : subj.progressPercentage >= 40 ? 'amber' : 'brand'}
                    />
                  </div>
                ))
              )}
            </div>
          </div>

          {/* Quick Shortcuts Card */}
          <div className="bg-gradient-to-br from-indigo-50 to-purple-50 rounded-3xl p-6 border border-indigo-100/80 space-y-3">
            <h4 className="text-xs font-bold uppercase tracking-wider text-indigo-700">
              Quick Shortcuts
            </h4>
            <div className="space-y-2">
              <Link
                to="/study-log"
                className="flex items-center justify-between p-3 rounded-2xl bg-white text-xs font-bold text-slate-800 shadow-sm hover:shadow transition-all"
              >
                <div className="flex items-center gap-2">
                  <Clock className="w-4 h-4 text-amber-500" />
                  <span>Log Study Session</span>
                </div>
                <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
              </Link>

              <Link
                to="/calendar"
                className="flex items-center justify-between p-3 rounded-2xl bg-white text-xs font-bold text-slate-800 shadow-sm hover:shadow transition-all"
              >
                <div className="flex items-center gap-2">
                  <CalendarIcon className="w-4 h-4 text-indigo-500" />
                  <span>View Study Calendar</span>
                </div>
                <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
              </Link>

              <Link
                to="/notes"
                className="flex items-center justify-between p-3 rounded-2xl bg-white text-xs font-bold text-slate-800 shadow-sm hover:shadow transition-all"
              >
                <div className="flex items-center gap-2">
                  <Award className="w-4 h-4 text-purple-500" />
                  <span>Add Study Notes</span>
                </div>
                <ArrowRight className="w-3.5 h-3.5 text-slate-400" />
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
