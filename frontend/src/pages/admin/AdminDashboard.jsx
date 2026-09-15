import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Users,
  Map,
  CheckCircle2,
  TrendingUp,
  Clock,
  AlertTriangle,
  ArrowRight,
  Plus,
  Radio,
  BarChart3,
  Flame,
  PieChart as PieChartIcon,
  ShieldCheck,
  Calendar
} from 'lucide-react';
import { Link } from 'react-router-dom';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  Legend
} from 'recharts';

const PIE_COLORS = ['#10b981', '#6366f1', '#f59e0b', '#ef4444'];

export default function AdminDashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    try {
      const res = await api.get('/admin/dashboard');
      setData(res.data.data);
    } catch (err) {
      console.error('Error fetching admin dashboard', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading Admin Command Center..." />;
  }

  // Format chart data
  const distData = (data?.studentProgressDistribution || []).map((d) => ({
    bracket: d.bracket || d.range || 'Range',
    count: d.count || 0,
  }));

  const trendData = (data?.weeklyStudyHoursTrend || []).map((w) => ({
    week: w.weekLabel || 'Week',
    hours: Number((w.studyMinutes / 60).toFixed(1)),
    tasks: w.completedTasks || 0,
  }));

  const taskBreakdownData = Object.entries(data?.taskStatusBreakdown || {}).map(([key, val]) => ({
    name: key.replace('_', ' '),
    value: val,
  }));

  const roadmapCompData = (data?.roadmapCompletionComparison || []).map((r) => ({
    title: r.title?.length > 18 ? r.title.substring(0, 18) + '...' : r.title,
    fullTitle: r.title,
    progress: Math.round(r.averageProgress || 0),
    students: r.enrolledStudents || 0,
  }));

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header & Quick Action Bar */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2 text-xs font-bold text-indigo-600 uppercase tracking-wider mb-1">
            <ShieldCheck className="w-4 h-4" />
            <span>Administrator Control Center</span>
          </div>
          <h1 className="text-3xl font-black text-slate-900 tracking-tight">System Performance Overview</h1>
          <p className="text-xs text-slate-500 mt-1">
            Real-time telemetry across students, personalized learning roadmaps, task completion, and study hours.
          </p>
        </div>

        <div className="flex items-center gap-2 flex-wrap">
          <Link
            to="/admin/students"
            className="px-3.5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-sm shadow-indigo-600/30 transition-all flex items-center gap-1.5"
          >
            <Plus className="w-4 h-4" />
            Add Student
          </Link>
          <Link
            to="/admin/roadmaps"
            className="px-3.5 py-2 rounded-xl bg-slate-900 hover:bg-slate-800 text-white text-xs font-bold shadow-sm transition-all flex items-center gap-1.5"
          >
            <Map className="w-4 h-4" />
            New Roadmap
          </Link>
          <Link
            to="/admin/announcements"
            className="px-3.5 py-2 rounded-xl border border-slate-200 bg-white hover:bg-slate-50 text-slate-700 text-xs font-bold transition-all flex items-center gap-1.5"
          >
            <Radio className="w-4 h-4 text-indigo-600" />
            Broadcast
          </Link>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        <StatCard
          title="Total Students"
          value={data?.totalStudents || 0}
          subtitle={`${data?.activeStudents || 0} active now`}
          icon={Users}
          color="indigo"
        />
        <StatCard
          title="Active Roadmaps"
          value={data?.totalRoadmaps || 0}
          subtitle="Learning pathways"
          icon={Map}
          color="purple"
        />
        <StatCard
          title="Done Today"
          value={data?.tasksCompletedToday || 0}
          subtitle="Tasks completed"
          icon={CheckCircle2}
          color="emerald"
        />
        <StatCard
          title="Avg Progress"
          value={`${Math.round(data?.averageStudentProgress || 0)}%`}
          subtitle="Across all cohorts"
          icon={TrendingUp}
          color="blue"
        />
        <StatCard
          title="Total Study Time"
          value={`${Math.round(data?.totalStudyHours || 0)}h`}
          subtitle="Hours logged"
          icon={Clock}
          color="amber"
        />
        <StatCard
          title="Falling Behind"
          value={data?.studentsFallingBehindCount || 0}
          subtitle="Require mentoring"
          icon={AlertTriangle}
          color="rose"
        />
      </div>

      {/* Primary Visualizations */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Progress Distribution Chart */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Student Progress Distribution</h2>
              <p className="text-xs text-slate-400">Number of students within completion brackets</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <BarChart3 className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={distData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="bracket" stroke="#94a3b8" fontSize={11} tickLine={false} />
                <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} allowDecimals={false} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1e293b',
                    borderRadius: '12px',
                    border: 'none',
                    color: '#fff',
                    fontSize: '12px',
                  }}
                  formatter={(value) => [`${value} students`, 'Count']}
                />
                <Bar dataKey="count" fill="#6366f1" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Weekly Study Hours Trend */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Weekly Total Study Hours</h2>
              <p className="text-xs text-slate-400">Total student focus hours logged across cohorts</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <TrendingUp className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={trendData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
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
                  formatter={(value) => [`${value} hours`, 'Study Time']}
                />
                <Line
                  type="monotone"
                  dataKey="hours"
                  stroke="#10b981"
                  strokeWidth={3}
                  dot={{ fill: '#10b981', r: 5 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Secondary Row: Roadmap Comparison & Task Breakdown */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Roadmap Completion Comparison */}
        <div className="lg:col-span-2 bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Roadmap Progress Comparison</h2>
              <p className="text-xs text-slate-400">Average student completion percentage per curriculum</p>
            </div>
            <Link
              to="/admin/roadmaps"
              className="text-xs font-bold text-indigo-600 hover:text-indigo-700 flex items-center gap-1"
            >
              View All <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="space-y-4 pt-1">
            {roadmapCompData.length > 0 ? (
              roadmapCompData.map((rm, idx) => (
                <div key={idx} className="space-y-1.5">
                  <div className="flex justify-between items-center text-xs">
                    <span className="font-bold text-slate-800" title={rm.fullTitle}>
                      {rm.title}
                    </span>
                    <div className="flex items-center gap-3 text-slate-500">
                      <span>{rm.students} Enrolled</span>
                      <span className="font-black text-indigo-600 w-10 text-right">{rm.progress}%</span>
                    </div>
                  </div>
                  <ProgressBar progress={rm.progress} color="indigo" size="md" />
                </div>
              ))
            ) : (
              <p className="text-xs text-slate-400 italic py-4">No active roadmaps found.</p>
            )}
          </div>
        </div>

        {/* Task Status Breakdown */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-2">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Task Completion Breakdown</h2>
              <p className="text-xs text-slate-400">Status of assigned student tasks</p>
            </div>
            <PieChartIcon className="w-4 h-4 text-slate-400" />
          </div>

          <div className="h-52 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={taskBreakdownData}
                  cx="50%"
                  cy="50%"
                  innerRadius={50}
                  outerRadius={75}
                  paddingAngle={5}
                  dataKey="value"
                >
                  {taskBreakdownData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={PIE_COLORS[index % PIE_COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1e293b',
                    borderRadius: '12px',
                    border: 'none',
                    color: '#fff',
                    fontSize: '12px',
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>

          {/* Legend */}
          <div className="grid grid-cols-2 gap-2 pt-2 border-t border-slate-100">
            {taskBreakdownData.map((entry, idx) => (
              <div key={idx} className="flex items-center gap-2 text-xs">
                <span
                  className="w-3 h-3 rounded-full shrink-0"
                  style={{ backgroundColor: PIE_COLORS[idx % PIE_COLORS.length] }}
                ></span>
                <span className="text-slate-600 font-medium truncate">{entry.name}:</span>
                <span className="font-bold text-slate-800 ml-auto">{entry.value}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom Row: Students Falling Behind & Recent Activity Feed */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Students Falling Behind Watchlist */}
        <div className="bg-white rounded-3xl p-6 border border-rose-100 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center">
                <AlertTriangle className="w-4 h-4" />
              </div>
              <div>
                <h2 className="text-sm font-bold text-slate-900">Students Falling Behind</h2>
                <p className="text-xs text-slate-400">Progress under 30% or inactive study streak</p>
              </div>
            </div>
            <Link
              to="/admin/analytics"
              className="text-xs font-bold text-rose-600 hover:text-rose-700 flex items-center gap-1"
            >
              Analyze <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="divide-y divide-slate-100">
            {data?.studentsFallingBehind && data.studentsFallingBehind.length > 0 ? (
              data.studentsFallingBehind.slice(0, 4).map((student) => (
                <div key={student.id} className="py-3 flex items-center justify-between gap-4">
                  <div className="flex items-center gap-3 min-w-0">
                    <div className="w-9 h-9 rounded-full bg-rose-50 text-rose-700 font-bold flex items-center justify-center text-xs shrink-0">
                      {student.fullName?.charAt(0)}
                    </div>
                    <div className="min-w-0">
                      <Link
                        to={`/admin/students/${student.id}`}
                        className="text-xs font-bold text-slate-800 hover:text-indigo-600 transition-colors truncate block"
                      >
                        {student.fullName}
                      </Link>
                      <p className="text-[11px] text-slate-400 truncate">{student.roadmapTitle || 'No Roadmap'}</p>
                    </div>
                  </div>

                  <div className="flex items-center gap-3 shrink-0 text-right">
                    <div>
                      <span className="text-xs font-bold text-rose-600">
                        {Math.round(student.overallProgress)}%
                      </span>
                      <p className="text-[10px] text-slate-400">{student.currentStreak}d streak</p>
                    </div>
                    <Link
                      to={`/admin/students/${student.id}`}
                      className="px-2.5 py-1 rounded-lg bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold"
                    >
                      Inspect
                    </Link>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-xs text-slate-400 italic py-4">Great news! No students currently falling behind.</p>
            )}
          </div>
        </div>

        {/* Recent Activity Feed */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Recent System Activity</h2>
              <p className="text-xs text-slate-400">Live feed of student milestone completions & events</p>
            </div>
            <span className="text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-slate-100 text-slate-600">
              Live Feed
            </span>
          </div>

          <div className="space-y-3">
            {data?.recentActivity && data.recentActivity.length > 0 ? (
              data.recentActivity.slice(0, 5).map((act, idx) => (
                <div key={idx} className="flex items-start gap-3 p-3 rounded-2xl bg-slate-50/60 border border-slate-100">
                  <div className="w-8 h-8 rounded-xl bg-indigo-100 text-indigo-700 flex items-center justify-center shrink-0 mt-0.5">
                    <CheckCircle2 className="w-4 h-4 text-indigo-600" />
                  </div>
                  <div className="space-y-0.5 min-w-0 flex-1">
                    <p className="text-xs font-bold text-slate-800 truncate">{act.message || act.description}</p>
                    <div className="flex items-center gap-2 text-[10px] text-slate-400">
                      <span>{act.studentName || 'Student'}</span>
                      <span>•</span>
                      <span>{act.timestamp || 'Today'}</span>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-xs text-slate-400 italic py-4">No recent activity recorded.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
