import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import ProgressBar from '../../components/common/ProgressBar';
import ConfirmationDialog from '../../components/common/ConfirmationDialog';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  ArrowLeft,
  User,
  Mail,
  GraduationCap,
  Calendar,
  Flame,
  Award,
  Clock,
  CheckCircle2,
  ListTodo,
  History,
  TrendingUp,
  Layers,
  Sparkles,
  Circle,
  Trash2
} from 'lucide-react';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  LineChart,
  Line
} from 'recharts';

export default function StudentDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [student, setStudent] = useState(null);
  const [studyLogs, setStudyLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('TASKS'); // TASKS, LOGS, CHARTS
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  useEffect(() => {
    const fetchStudentData = async () => {
      try {
        const [studentRes, logsRes] = await Promise.all([
          api.get(`/admin/students/${id}`),
          api.get(`/admin/students/${id}/study-hours`),
        ]);
        setStudent(studentRes.data.data);
        setStudyLogs(logsRes.data.data || []);
      } catch (err) {
        console.error('Error fetching student detail drill-down', err);
      } finally {
        setLoading(false);
      }
    };
    fetchStudentData();
  }, [id]);

  const handleDeleteStudent = async () => {
    try {
      setShowDeleteConfirm(false);
      await api.delete(`/admin/students/${id}`);
      navigate('/admin/students');
    } catch (err) {
      console.error('Error deleting student', err);
      alert('Failed to delete student: ' + (err.response?.data?.message || err.message));
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading student drill-down profile..." />;
  }

  if (!student) {
    return (
      <div className="p-12 text-center bg-white rounded-3xl border border-slate-100">
        <h2 className="text-xl font-bold text-slate-800">Student Not Found</h2>
        <p className="text-xs text-slate-400 mt-2 mb-4">The requested student ID does not exist in the database.</p>
        <Link to="/admin/students" className="px-4 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold">
          Back to Students
        </Link>
      </div>
    );
  }

  const overallPct = Math.round(student.overallProgress || 0);
  const studyHours = (student.totalStudyMinutes / 60).toFixed(1);

  const weeklyChartData = (student.weeklyProgress || []).map((w) => ({
    week: w.weekLabel || 'W',
    hours: Number((w.studyMinutes / 60).toFixed(1)),
    tasks: w.completedTasks || 0,
  }));

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Back button & Breadcrumbs */}
      <div>
        <Link
          to="/admin/students"
          className="inline-flex items-center gap-1.5 text-xs font-bold text-slate-500 hover:text-indigo-600 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          Back to Students Roster
        </Link>
      </div>

      {/* Student Hero Profile Header */}
      <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-sm flex flex-col md:flex-row items-center md:items-start gap-6">
        <div className="w-20 h-20 rounded-3xl bg-indigo-600 text-white flex items-center justify-center font-black text-2xl shadow-lg shadow-indigo-600/20 shrink-0">
          {student.fullName?.charAt(0).toUpperCase()}
        </div>

        <div className="space-y-2 text-center md:text-left flex-1">
          <div className="flex flex-wrap items-center justify-center md:justify-start gap-3">
            <h1 className="text-2xl font-black text-slate-900">{student.fullName}</h1>
            <span
              className={`px-3 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                student.status === 'ACTIVE'
                  ? 'bg-emerald-100 text-emerald-800'
                  : 'bg-slate-100 text-slate-600'
              }`}
            >
              {student.status}
            </span>
          </div>

          <p className="text-xs text-slate-500">
            Enrolled Track:{' '}
            <span className="font-bold text-indigo-600">
              {student.roadmapTitle || 'No Roadmap Assigned'}
            </span>
          </p>

          <div className="flex flex-wrap items-center justify-center md:justify-start gap-4 pt-2 text-xs text-slate-500">
            <span className="flex items-center gap-1.5">
              <Mail className="w-4 h-4 text-slate-400" />
              {student.email}
            </span>
            {student.enrollmentNo && (
              <span className="flex items-center gap-1.5 font-mono text-[11px]">
                #{student.enrollmentNo}
              </span>
            )}
            {student.college && (
              <span className="flex items-center gap-1.5">
                <GraduationCap className="w-4 h-4 text-slate-400" />
                {student.college}
              </span>
            )}
          </div>
        </div>

        <div className="self-center md:self-start">
          <button
            onClick={() => setShowDeleteConfirm(true)}
            className="px-4 py-2 rounded-xl bg-rose-50 hover:bg-rose-100 text-rose-600 text-xs font-bold transition-colors flex items-center gap-1.5 border border-rose-200"
          >
            <Trash2 className="w-4 h-4" />
            Delete Student
          </button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        <StatCard
          title="Overall Mastery"
          value={`${overallPct}%`}
          subtitle="Curriculum completion"
          icon={Award}
          color="indigo"
        />
        <StatCard
          title="Current Streak"
          value={`${student.currentStreak || 0} Days`}
          subtitle={`Best: ${student.longestStreak || 0} days`}
          icon={Flame}
          color="amber"
        />
        <StatCard
          title="Total Focus Time"
          value={`${studyHours} Hours`}
          subtitle={`This week: ${student.weeklyStudyTime || '0m'}`}
          icon={Clock}
          color="emerald"
        />
        <StatCard
          title="Tasks Completed"
          value={student.completedTasks?.length || 0}
          subtitle={`${student.todayTasks?.length || 0} assigned today`}
          icon={CheckCircle2}
          color="purple"
        />
      </div>

      {/* Subject Breakdown */}
      <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-200/80 shadow-sm space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-slate-100">
          <div>
            <h2 className="text-sm font-bold text-slate-900">Curriculum Subject Progress</h2>
            <p className="text-xs text-slate-400">Detailed breakdown of topic completion by subject</p>
          </div>
          <Layers className="w-5 h-5 text-slate-400" />
        </div>

        {student.subjectProgress && student.subjectProgress.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 pt-2">
            {student.subjectProgress.map((sub, idx) => {
              const pct = Math.round(sub.progressPercentage || 0);
              return (
                <div key={sub.subjectId || idx} className="p-4 rounded-2xl bg-slate-50/60 border border-slate-200/60 space-y-2">
                  <div className="flex justify-between items-center text-xs">
                    <span className="font-bold text-slate-800">{sub.subjectTitle}</span>
                    <span className="font-black text-indigo-600">{pct}%</span>
                  </div>
                  <ProgressBar progress={pct} size="sm" color={pct >= 70 ? 'emerald' : 'indigo'} />
                  <p className="text-[11px] text-slate-400">
                    {sub.completedTopics} of {sub.totalTopics} topics mastered
                  </p>
                </div>
              );
            })}
          </div>
        ) : (
          <p className="text-xs text-slate-400 italic py-4">No subject progress data available.</p>
        )}
      </div>

      {/* Interactive Tabs: Tasks, Study Logs, Charts */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="flex border-b border-slate-200 bg-slate-50/50 p-2 gap-2">
          {[
            { id: 'TASKS', label: 'Assigned Tasks', icon: ListTodo },
            { id: 'LOGS', label: 'Study Sessions History', icon: History },
            { id: 'CHARTS', label: 'Velocity Charts', icon: TrendingUp },
          ].map((tab) => {
            const Icon = tab.icon;
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`px-4 py-2.5 rounded-2xl text-xs font-bold transition-all flex items-center gap-2 ${
                  isActive
                    ? 'bg-white text-indigo-600 shadow-sm'
                    : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
              </button>
            );
          })}
        </div>

        <div className="p-6">
          {/* TAB 1: Tasks */}
          {activeTab === 'TASKS' && (
            <div className="space-y-6">
              {/* Today's Tasks */}
              <div>
                <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
                  Today's Tasks ({student.todayTasks?.length || 0})
                </h3>
                {student.todayTasks && student.todayTasks.length > 0 ? (
                  <div className="space-y-2">
                    {student.todayTasks.map((t) => (
                      <div
                        key={t.id}
                        className="p-3.5 rounded-xl border border-slate-200 flex items-center justify-between gap-4"
                      >
                        <div className="flex items-center gap-3">
                          {t.status === 'COMPLETED' ? (
                            <CheckCircle2 className="w-5 h-5 text-emerald-600" />
                          ) : (
                            <Circle className="w-5 h-5 text-slate-300" />
                          )}
                          <div>
                            <h4 className="text-xs font-bold text-slate-800">{t.title}</h4>
                            <span className="text-[10px] text-slate-400">
                              {t.subjectTitle} • Due {t.dueDate || 'Today'}
                            </span>
                          </div>
                        </div>
                        <span
                          className={`px-2.5 py-1 rounded-full text-[10px] font-bold uppercase ${
                            t.status === 'COMPLETED'
                              ? 'bg-emerald-100 text-emerald-800'
                              : 'bg-amber-100 text-amber-800'
                          }`}
                        >
                          {t.status}
                        </span>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-xs text-slate-400 italic">No tasks scheduled for today.</p>
                )}
              </div>

              {/* Completed Tasks */}
              <div>
                <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
                  Recently Completed Tasks ({student.completedTasks?.length || 0})
                </h3>
                {student.completedTasks && student.completedTasks.length > 0 ? (
                  <div className="space-y-2">
                    {student.completedTasks.slice(0, 5).map((t) => (
                      <div
                        key={t.id}
                        className="p-3 rounded-xl border border-emerald-100 bg-emerald-50/20 flex items-center justify-between gap-4"
                      >
                        <div className="flex items-center gap-3">
                          <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0" />
                          <span className="text-xs font-semibold text-slate-700">{t.title}</span>
                        </div>
                        <span className="text-[11px] text-slate-400 font-medium">
                          {t.completedAt ? t.completedAt.split('T')[0] : 'Done'}
                        </span>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-xs text-slate-400 italic">No tasks completed yet.</p>
                )}
              </div>
            </div>
          )}

          {/* TAB 2: Study Logs */}
          {activeTab === 'LOGS' && (
            <div className="space-y-4">
              {studyLogs.length > 0 ? (
                <div className="divide-y divide-slate-100">
                  {studyLogs.map((log) => (
                    <div key={log.id} className="py-3 flex items-start justify-between gap-4">
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <h4 className="text-xs font-bold text-slate-800">{log.topicName}</h4>
                          <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-indigo-50 text-indigo-700">
                            {log.durationMinutes} mins
                          </span>
                        </div>
                        <div className="flex items-center gap-3 text-[11px] text-slate-400">
                          <span>{log.date}</span>
                          <span>•</span>
                          <span>{log.startTime || '--'} - {log.endTime || '--'}</span>
                        </div>
                        {log.notes && (
                          <p className="text-xs text-slate-600 bg-slate-50 p-2 rounded-lg mt-1">
                            {log.notes}
                          </p>
                        )}
                      </div>
                      <span className="text-xs font-bold text-indigo-600 shrink-0">
                        {(log.durationMinutes / 60).toFixed(1)}h
                      </span>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-xs text-slate-400 italic py-6 text-center">No study logs recorded for this student.</p>
              )}
            </div>
          )}

          {/* TAB 3: Charts */}
          {activeTab === 'CHARTS' && (
            <div className="space-y-8">
              <div className="h-64 w-full">
                <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">
                  Weekly Study Hours Velocity
                </h4>
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={weeklyChartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                    <XAxis dataKey="week" stroke="#94a3b8" fontSize={11} tickLine={false} />
                    <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} unit="h" />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: '#1e293b',
                        borderRadius: '12px',
                        border: 'none',
                        color: '#fff',
                        fontSize: '12px',
                      }}
                    />
                    <Bar dataKey="hours" fill="#6366f1" radius={[8, 8, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Delete Confirmation Dialog */}
      <ConfirmationDialog
        isOpen={showDeleteConfirm}
        onClose={() => setShowDeleteConfirm(false)}
        onConfirm={handleDeleteStudent}
        title="Delete Student Record"
        message={`Are you sure you want to permanently delete ${student.fullName}? This will erase all their assigned tasks, study logs, notes, roadmaps, and user account.`}
        confirmText="Delete Student"
        confirmVariant="danger"
      />
    </div>
  );
}
