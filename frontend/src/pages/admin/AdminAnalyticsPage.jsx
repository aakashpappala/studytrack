import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  TrendingUp,
  Award,
  Clock,
  AlertTriangle,
  Flame,
  CheckCircle2,
  Calendar,
  Users,
  Layers,
  ArrowRight,
  Sparkles
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
  AreaChart,
  Area
} from 'recharts';

export default function AdminAnalyticsPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const res = await api.get('/admin/analytics');
        setData(res.data.data);
      } catch (err) {
        console.error('Error fetching admin analytics', err);
      } finally {
        setLoading(false);
      }
    };
    fetchAnalytics();
  }, []);

  if (loading) {
    return <LoadingSpinner size="lg" message="Generating institutional telemetry and analytics..." />;
  }

  // Format trend data
  const trendData = (data?.dailyCompletionTrend || []).map((d) => ({
    date: d.date || 'Day',
    rate: Math.round(d.completionRate || d.rate || 0),
    tasks: d.completedTasks || 0,
  }));

  const roadmapData = (data?.roadmapStats || []).map((r) => ({
    name: r.title?.length > 15 ? r.title.substring(0, 15) + '...' : r.title,
    fullTitle: r.title,
    progress: Math.round(r.averageProgress || 0),
    students: r.studentsCount || 0,
  }));

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-black text-slate-900 tracking-tight">Institutional Learning Analytics</h1>
        <p className="text-xs text-slate-500 mt-1">
          Deep diagnostic metrics on cohort progress, study velocity, curriculum efficacy, and student interventions.
        </p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        <StatCard
          title="Average Progress"
          value={`${Math.round(data?.averageProgress || 0)}%`}
          subtitle="Across all cohorts"
          icon={TrendingUp}
          color="indigo"
        />
        <StatCard
          title="Avg Study Focus"
          value={`${Math.round(data?.averageStudyHours || 0)}h`}
          subtitle="Hours per student"
          icon={Clock}
          color="emerald"
        />
        <StatCard
          title="Daily Task Rate"
          value={`${Math.round(data?.dailyCompletionRate || 0)}%`}
          subtitle="Today's task delivery"
          icon={CheckCircle2}
          color="purple"
        />
        <StatCard
          title="Top Curriculum"
          value={data?.mostCompletedRoadmapTitle || 'Java Full Stack'}
          subtitle="Highest completion rate"
          icon={Award}
          color="amber"
        />
      </div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Daily Completion Trend */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Daily Task Completion Velocity</h2>
              <p className="text-xs text-slate-400">Total tasks completed across cohorts over recent days</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <TrendingUp className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={trendData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorAdminTasks" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3} />
                    <stop offset="95%" stopColor="#6366f1" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="date" stroke="#94a3b8" fontSize={11} tickLine={false} />
                <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1e293b',
                    borderRadius: '12px',
                    border: 'none',
                    color: '#fff',
                    fontSize: '12px',
                  }}
                />
                <Area type="monotone" dataKey="tasks" stroke="#6366f1" strokeWidth={3} fillOpacity={1} fill="url(#colorAdminTasks)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Roadmap Efficacy Bar Chart */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Curriculum Efficacy Comparison</h2>
              <p className="text-xs text-slate-400">Average student progress % per learning track</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <Layers className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={roadmapData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="name" stroke="#94a3b8" fontSize={11} tickLine={false} />
                <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} unit="%" />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1e293b',
                    borderRadius: '12px',
                    border: 'none',
                    color: '#fff',
                    fontSize: '12px',
                  }}
                  formatter={(val) => [`${val}%`, 'Avg Progress']}
                />
                <Bar dataKey="progress" fill="#10b981" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Cohort Leaderboards & Watchlists */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Top Performing Students */}
        <div className="bg-white rounded-3xl p-6 border border-emerald-100 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
                <Award className="w-4 h-4" />
              </div>
              <div>
                <h2 className="text-sm font-bold text-slate-900">Highest Performing Students</h2>
                <p className="text-xs text-slate-400">Leading student completion & study consistency</p>
              </div>
            </div>
            <span className="text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-emerald-50 text-emerald-700">
              Top Cohort
            </span>
          </div>

          <div className="divide-y divide-slate-100">
            {data?.highestPerformingStudents && data.highestPerformingStudents.length > 0 ? (
              data.highestPerformingStudents.map((s, idx) => (
                <div key={s.id} className="py-3 flex items-center justify-between gap-4">
                  <div className="flex items-center gap-3 min-w-0">
                    <span className="w-6 h-6 rounded-lg bg-emerald-100 text-emerald-800 font-black text-xs flex items-center justify-center shrink-0">
                      {idx + 1}
                    </span>
                    <div className="min-w-0">
                      <Link
                        to={`/admin/students/${s.id}`}
                        className="text-xs font-bold text-slate-800 hover:text-indigo-600 truncate block"
                      >
                        {s.fullName}
                      </Link>
                      <p className="text-[11px] text-slate-400 truncate">{s.roadmapTitle}</p>
                    </div>
                  </div>

                  <div className="flex items-center gap-4 shrink-0 text-right">
                    <div className="text-right">
                      <span className="text-xs font-bold text-emerald-600">
                        {Math.round(s.overallProgress)}%
                      </span>
                      <p className="text-[10px] text-slate-400">
                        {(s.totalStudyMinutes / 60).toFixed(1)}h logged
                      </p>
                    </div>
                    <Link
                      to={`/admin/students/${s.id}`}
                      className="p-1.5 rounded-lg text-slate-400 hover:text-indigo-600 hover:bg-slate-100"
                    >
                      <ArrowRight className="w-4 h-4" />
                    </Link>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-xs text-slate-400 italic py-4">No top performer data available.</p>
            )}
          </div>
        </div>

        {/* Students Falling Behind Watchlist */}
        <div className="bg-white rounded-3xl p-6 border border-rose-100 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center">
                <AlertTriangle className="w-4 h-4" />
              </div>
              <div>
                <h2 className="text-sm font-bold text-slate-900">At-Risk Students Watchlist</h2>
                <p className="text-xs text-slate-400">Progress under 30% or stalled activity</p>
              </div>
            </div>
            <span className="text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-rose-50 text-rose-700">
              Needs Intervention
            </span>
          </div>

          <div className="divide-y divide-slate-100">
            {data?.studentsFallingBehind && data.studentsFallingBehind.length > 0 ? (
              data.studentsFallingBehind.map((s) => (
                <div key={s.id} className="py-3 flex items-center justify-between gap-4">
                  <div className="flex items-center gap-3 min-w-0">
                    <div className="w-8 h-8 rounded-full bg-rose-100 text-rose-700 font-bold flex items-center justify-center text-xs shrink-0">
                      !
                    </div>
                    <div className="min-w-0">
                      <Link
                        to={`/admin/students/${s.id}`}
                        className="text-xs font-bold text-slate-800 hover:text-indigo-600 truncate block"
                      >
                        {s.fullName}
                      </Link>
                      <p className="text-[11px] text-slate-400 truncate">{s.email}</p>
                    </div>
                  </div>

                  <div className="flex items-center gap-4 shrink-0 text-right">
                    <div className="text-right">
                      <span className="text-xs font-bold text-rose-600">
                        {Math.round(s.overallProgress)}%
                      </span>
                      <p className="text-[10px] text-slate-400">{s.currentStreak}d streak</p>
                    </div>
                    <Link
                      to={`/admin/students/${s.id}`}
                      className="px-2.5 py-1 rounded-lg bg-rose-50 hover:bg-rose-100 text-rose-700 text-xs font-semibold"
                    >
                      Intervene
                    </Link>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-xs text-slate-400 italic py-4">Great! No students are at risk.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
