import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  BarChart3,
  Flame,
  Award,
  Clock,
  TrendingUp,
  Calendar,
  CheckCircle2,
  Layers,
  Sparkles,
  Target
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
  Line,
  AreaChart,
  Area
} from 'recharts';

export default function ProgressPage() {
  const [summary, setSummary] = useState(null);
  const [weekly, setWeekly] = useState([]);
  const [monthly, setMonthly] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProgress = async () => {
      try {
        const [sumRes, weekRes, monthRes] = await Promise.all([
          api.get('/student/progress'),
          api.get('/student/progress/weekly'),
          api.get('/student/progress/monthly'),
        ]);
        setSummary(sumRes.data.data);
        setWeekly(weekRes.data.data || []);
        setMonthly(monthRes.data.data || []);
      } catch (err) {
        console.error('Error fetching progress analytics', err);
      } finally {
        setLoading(false);
      }
    };
    fetchProgress();
  }, []);

  if (loading) {
    return <LoadingSpinner size="lg" message="Generating progress analytics..." />;
  }

  // Format weekly chart data
  const chartData = weekly.map((w) => ({
    name: w.weekLabel || 'Week',
    hours: Number((w.studyMinutes / 60).toFixed(1)),
    tasks: w.completedTasks || 0,
    progress: Math.round(w.progressPercentage || 0),
  }));

  const overallPct = Math.round(summary?.overallProgress || 0);

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-black text-slate-900 tracking-tight">Performance & Analytics</h1>
        <p className="text-xs text-slate-500 mt-1">
          Monitor your study hours velocity, topic mastery rates, and consistency trends.
        </p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        <StatCard
          title="Overall Mastery"
          value={`${overallPct}%`}
          subtitle={`Across ${summary?.roadmapTitle || 'Curriculum'}`}
          icon={Award}
          color="indigo"
        />
        <StatCard
          title="Active Streak"
          value={`${summary?.currentStreak || 0} Days`}
          subtitle={`Best: ${summary?.longestStreak || 0} days`}
          icon={Flame}
          color="amber"
        />
        <StatCard
          title="Study Time Logged"
          value={summary?.todayStudyTime || '0m'}
          subtitle={`This week: ${summary?.weeklyStudyTime || '0m'}`}
          icon={Clock}
          color="emerald"
        />
        <StatCard
          title="Tasks Completed"
          value={`${summary?.completedTasks || 0} / ${summary?.totalTasks || 0}`}
          subtitle={`${summary?.todayCompletedTasks || 0} finished today`}
          icon={CheckCircle2}
          color="purple"
        />
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Weekly Study Hours */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Weekly Study Hours</h2>
              <p className="text-xs text-slate-400">Total focused hours logged per week</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <TrendingUp className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="name" stroke="#94a3b8" fontSize={11} tickLine={false} />
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
                <Bar dataKey="hours" fill="#6366f1" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Weekly Tasks Velocity */}
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-sm font-bold text-slate-900">Completed Tasks Trend</h2>
              <p className="text-xs text-slate-400">Weekly rate of finished roadmap items</p>
            </div>
            <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <CheckCircle2 className="w-4 h-4" />
            </div>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorTasks" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#10b981" stopOpacity={0.3} />
                    <stop offset="95%" stopColor="#10b981" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="name" stroke="#94a3b8" fontSize={11} tickLine={false} />
                <YAxis stroke="#94a3b8" fontSize={11} tickLine={false} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1e293b',
                    borderRadius: '12px',
                    border: 'none',
                    color: '#fff',
                    fontSize: '12px',
                  }}
                  formatter={(value) => [`${value} tasks`, 'Completed']}
                />
                <Area type="monotone" dataKey="tasks" stroke="#10b981" strokeWidth={3} fillOpacity={1} fill="url(#colorTasks)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Subject Breakdown Progress */}
      <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-200/80 shadow-sm">
        <div className="flex items-center justify-between mb-6 pb-4 border-b border-slate-100">
          <div>
            <h2 className="text-base font-bold text-slate-900">Subject Mastery Breakdown</h2>
            <p className="text-xs text-slate-500">Track module completion rates across subjects in {summary?.roadmapTitle}</p>
          </div>
          <div className="w-10 h-10 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
            <Layers className="w-5 h-5" />
          </div>
        </div>

        {summary?.subjectProgress && summary.subjectProgress.length > 0 ? (
          <div className="space-y-6">
            {summary.subjectProgress.map((sub, idx) => {
              const pct = Math.round(sub.progressPercentage || 0);
              return (
                <div key={sub.subjectId || idx} className="space-y-2">
                  <div className="flex items-center justify-between text-xs">
                    <div className="flex items-center gap-2">
                      <span className="w-6 h-6 rounded-lg bg-slate-100 text-slate-700 flex items-center justify-center font-bold text-[11px]">
                        0{idx + 1}
                      </span>
                      <span className="font-bold text-slate-800 text-sm">{sub.subjectTitle}</span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="text-slate-400 font-medium">
                        {sub.completedTopics} / {sub.totalTopics} Topics
                      </span>
                      <span className="font-black text-indigo-600 w-10 text-right">{pct}%</span>
                    </div>
                  </div>
                  <ProgressBar
                    progress={pct}
                    color={pct >= 80 ? 'emerald' : pct >= 40 ? 'indigo' : 'amber'}
                    size="md"
                  />
                </div>
              );
            })}
          </div>
        ) : (
          <p className="text-xs text-slate-400 italic text-center py-6">No subject progress data available yet.</p>
        )}
      </div>
    </div>
  );
}
