import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StatCard from '../../components/common/StatCard';
import Modal from '../../components/common/Modal';
import ConfirmationDialog from '../../components/common/ConfirmationDialog';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Clock,
  Plus,
  Calendar,
  BookOpen,
  Trash2,
  FileText,
  Timer,
  Sparkles,
  TrendingUp,
  History
} from 'lucide-react';
import { format, parseISO, isToday, subDays } from 'date-fns';

export default function StudyLogPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [deleteTargetId, setDeleteTargetId] = useState(null);

  // Form State
  const [formData, setFormData] = useState({
    date: format(new Date(), 'yyyy-MM-dd'),
    topicName: '',
    startTime: '10:00',
    endTime: '11:30',
    durationMinutes: 90,
    notes: '',
  });

  const fetchLogs = async () => {
    try {
      const res = await api.get('/student/study-logs');
      setLogs(res.data.data || []);
    } catch (err) {
      console.error('Error fetching study logs', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLogs();
  }, []);

  // Calculate duration automatically when start/end times change
  const handleTimeChange = (type, value) => {
    const nextForm = { ...formData, [type]: value };
    if (nextForm.startTime && nextForm.endTime) {
      const [startH, startM] = nextForm.startTime.split(':').map(Number);
      const [endH, endM] = nextForm.endTime.split(':').map(Number);
      const diffMinutes = endH * 60 + endM - (startH * 60 + startM);
      if (diffMinutes > 0) {
        nextForm.durationMinutes = diffMinutes;
      }
    }
    setFormData(nextForm);
  };

  const handleCreateLog = async (e) => {
    e.preventDefault();
    if (!formData.topicName.trim()) return;
    try {
      setSubmitting(true);
      await api.post('/student/study-logs', formData);
      setIsModalOpen(false);
      setFormData({
        date: format(new Date(), 'yyyy-MM-dd'),
        topicName: '',
        startTime: '10:00',
        endTime: '11:30',
        durationMinutes: 90,
        notes: '',
      });
      await fetchLogs();
    } catch (err) {
      console.error('Error creating study log', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteLog = async () => {
    if (!deleteTargetId) return;
    try {
      await api.delete(`/student/study-logs/${deleteTargetId}`);
      setDeleteTargetId(null);
      await fetchLogs();
    } catch (err) {
      console.error('Error deleting study log', err);
    }
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading your study history..." />;
  }

  // Calculate aggregate stats
  const todayStr = format(new Date(), 'yyyy-MM-dd');
  const sevenDaysAgoStr = format(subDays(new Date(), 7), 'yyyy-MM-dd');

  let todayMins = 0;
  let weekMins = 0;
  let totalMins = 0;

  logs.forEach((log) => {
    const mins = log.durationMinutes || 0;
    totalMins += mins;
    if (log.date === todayStr) {
      todayMins += mins;
    }
    if (log.date >= sevenDaysAgoStr) {
      weekMins += mins;
    }
  });

  const formatHoursMins = (mins) => {
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    if (h === 0) return `${m}m`;
    return `${h}h ${m}m`;
  };

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Header Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-slate-900 tracking-tight">Study Time Tracker</h1>
          <p className="text-xs text-slate-500 mt-1">
            Log your daily focused study sessions, track hours spent, and analyze your learning habits.
          </p>
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-500/20 transition-all flex items-center gap-2 self-start"
        >
          <Plus className="w-4 h-4" />
          Log Study Session
        </button>
      </div>

      {/* Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <StatCard
          title="Today's Study Time"
          value={formatHoursMins(todayMins)}
          subtitle="Hours logged today"
          icon={Timer}
          color="indigo"
        />
        <StatCard
          title="This Week's Time"
          value={formatHoursMins(weekMins)}
          subtitle="Last 7 days total"
          icon={TrendingUp}
          color="emerald"
        />
        <StatCard
          title="All-Time Total"
          value={formatHoursMins(totalMins)}
          subtitle={`${logs.length} sessions logged`}
          icon={History}
          color="purple"
        />
      </div>

      {/* Study Logs List */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="p-6 border-b border-slate-100 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <BookOpen className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold text-slate-900">Study Session History</h2>
              <p className="text-xs text-slate-400">Chronological record of completed study sessions</p>
            </div>
          </div>
          <span className="text-xs font-semibold px-3 py-1 rounded-full bg-slate-100 text-slate-600">
            {logs.length} Records
          </span>
        </div>

        {logs.length > 0 ? (
          <div className="divide-y divide-slate-100">
            {logs.map((log) => (
              <div
                key={log.id}
                className="p-5 hover:bg-slate-50/60 transition-colors flex flex-col md:flex-row md:items-center justify-between gap-4"
              >
                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 rounded-2xl bg-slate-100 text-slate-700 flex flex-col items-center justify-center shrink-0 border border-slate-200">
                    <span className="text-[10px] uppercase font-bold text-slate-400">
                      {log.date ? format(parseISO(log.date), 'MMM') : 'DAY'}
                    </span>
                    <span className="text-base font-black leading-none text-slate-800">
                      {log.date ? format(parseISO(log.date), 'dd') : '--'}
                    </span>
                  </div>

                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <h3 className="text-sm font-bold text-slate-800">{log.topicName}</h3>
                      <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-indigo-50 text-indigo-700 border border-indigo-100">
                        {log.durationMinutes} mins
                      </span>
                    </div>

                    <div className="flex items-center gap-3 text-xs text-slate-400">
                      <span className="flex items-center gap-1">
                        <Clock className="w-3.5 h-3.5" />
                        {log.startTime || '--'} - {log.endTime || '--'}
                      </span>
                      {log.date && (
                        <span className="flex items-center gap-1 text-slate-500">
                          <Calendar className="w-3.5 h-3.5" />
                          {log.date}
                        </span>
                      )}
                    </div>

                    {log.notes && (
                      <p className="text-xs text-slate-600 bg-slate-50 p-2.5 rounded-xl border border-slate-200/50 mt-1 max-w-2xl">
                        {log.notes}
                      </p>
                    )}
                  </div>
                </div>

                <div className="flex items-center gap-3 self-end md:self-center shrink-0">
                  <span className="text-xs font-bold text-indigo-600">
                    {formatHoursMins(log.durationMinutes)}
                  </span>
                  <button
                    onClick={() => setDeleteTargetId(log.id)}
                    className="p-2 rounded-xl text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition-all"
                    title="Delete log"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="p-12 text-center">
            <Clock className="w-12 h-12 text-slate-300 mx-auto mb-3" />
            <h3 className="text-base font-bold text-slate-700">No study logs yet</h3>
            <p className="text-xs text-slate-400 mt-1 mb-4">Start recording your study sessions to see your progress here.</p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold shadow-sm"
            >
              Log First Session
            </button>
          </div>
        )}
      </div>

      {/* Log Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Record Study Session"
      >
        <form onSubmit={handleCreateLog} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Topic / Subject Studied *
            </label>
            <input
              type="text"
              required
              value={formData.topicName}
              onChange={(e) => setFormData({ ...formData, topicName: e.target.value })}
              placeholder="e.g. Spring Boot Security & JWT Authentication"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Session Date *
              </label>
              <input
                type="date"
                required
                value={formData.date}
                onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Duration (Minutes) *
              </label>
              <input
                type="number"
                min="5"
                max="1440"
                required
                value={formData.durationMinutes}
                onChange={(e) => setFormData({ ...formData, durationMinutes: Number(e.target.value) })}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Start Time
              </label>
              <input
                type="time"
                value={formData.startTime}
                onChange={(e) => handleTimeChange('startTime', e.target.value)}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                End Time
              </label>
              <input
                type="time"
                value={formData.endTime}
                onChange={(e) => handleTimeChange('endTime', e.target.value)}
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
              Session Notes / Accomplishments
            </label>
            <textarea
              rows="3"
              value={formData.notes}
              onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
              placeholder="What did you learn? Any key takeaways or hurdles overcome?"
              className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 resize-none"
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold shadow-md shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Saving...' : 'Save Study Log'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation */}
      <ConfirmationDialog
        isOpen={!!deleteTargetId}
        onClose={() => setDeleteTargetId(null)}
        onConfirm={handleDeleteLog}
        title="Delete Study Log"
        message="Are you sure you want to delete this study session record? This action cannot be undone and will recalculate your study time metrics."
        confirmText="Delete"
        confirmVariant="danger"
      />
    </div>
  );
}
