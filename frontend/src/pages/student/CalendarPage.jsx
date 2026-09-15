import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Calendar as CalendarIcon,
  ChevronLeft,
  ChevronRight,
  Clock,
  CheckCircle2,
  BookOpen,
  Sparkles,
  Flame,
  X
} from 'lucide-react';
import {
  format,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  eachDayOfInterval,
  isSameMonth,
  isSameDay,
  addMonths,
  subMonths,
  parseISO,
  isToday
} from 'date-fns';

export default function CalendarPage() {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [logs, setLogs] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [logsRes, tasksRes] = await Promise.all([
          api.get('/student/study-logs'),
          api.get('/student/tasks'),
        ]);
        setLogs(logsRes.data.data || []);
        setTasks(tasksRes.data.data || []);
      } catch (err) {
        console.error('Error fetching calendar data', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const nextMonth = () => setCurrentMonth(addMonths(currentMonth, 1));
  const prevMonth = () => setCurrentMonth(subMonths(currentMonth, 1));

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading your study calendar..." />;
  }

  // Create date grid
  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart);
  const endDate = endOfWeek(monthEnd);
  const days = eachDayOfInterval({ start: startDate, end: endDate });

  // Map activities by date string "yyyy-MM-dd"
  const activityMap = {};

  logs.forEach((log) => {
    if (!log.date) return;
    if (!activityMap[log.date]) {
      activityMap[log.date] = { minutes: 0, logs: [], completedTasks: [] };
    }
    activityMap[log.date].minutes += log.durationMinutes || 0;
    activityMap[log.date].logs.push(log);
  });

  tasks.forEach((task) => {
    if (task.status === 'COMPLETED' && task.completedAt) {
      const dateStr = task.completedAt.split('T')[0];
      if (!activityMap[dateStr]) {
        activityMap[dateStr] = { minutes: 0, logs: [], completedTasks: [] };
      }
      activityMap[dateStr].completedTasks.push(task);
    }
  });

  const selectedDateStr = format(selectedDate, 'yyyy-MM-dd');
  const selectedActivity = activityMap[selectedDateStr] || { minutes: 0, logs: [], completedTasks: [] };

  const handleDayClick = (day) => {
    setSelectedDate(day);
    setIsDrawerOpen(true);
  };

  const getActivityHeat = (minutes, taskCount) => {
    if (minutes >= 120 || taskCount >= 3) return 'bg-emerald-600 text-white shadow-sm shadow-emerald-600/30';
    if (minutes >= 60 || taskCount >= 2) return 'bg-emerald-400 text-emerald-950 font-bold';
    if (minutes > 0 || taskCount > 0) return 'bg-emerald-100 text-emerald-800 font-semibold';
    return '';
  };

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Calendar Header Card */}
      <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-200/80 shadow-sm">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-slate-100">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <CalendarIcon className="w-6 h-6" />
            </div>
            <div>
              <h1 className="text-2xl font-black text-slate-900 tracking-tight">
                {format(currentMonth, 'MMMM yyyy')}
              </h1>
              <p className="text-xs text-slate-500">Visual activity heat map of your daily progress</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={prevMonth}
              className="p-2 rounded-xl border border-slate-200 hover:bg-slate-100 text-slate-600 transition-colors"
            >
              <ChevronLeft className="w-5 h-5" />
            </button>
            <button
              onClick={() => setCurrentMonth(new Date())}
              className="px-3 py-1.5 rounded-xl border border-slate-200 hover:bg-slate-100 text-xs font-bold text-slate-700 transition-colors"
            >
              Today
            </button>
            <button
              onClick={nextMonth}
              className="p-2 rounded-xl border border-slate-200 hover:bg-slate-100 text-slate-600 transition-colors"
            >
              <ChevronRight className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Heat Legend */}
        <div className="flex items-center gap-4 text-xs text-slate-500 pt-4 pb-2">
          <span className="font-semibold text-slate-600">Activity Level:</span>
          <div className="flex items-center gap-1.5">
            <span className="w-4 h-4 rounded bg-slate-100 border border-slate-200"></span>
            <span className="text-[11px]">None</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-4 h-4 rounded bg-emerald-100"></span>
            <span className="text-[11px]">1 - 59m</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-4 h-4 rounded bg-emerald-400"></span>
            <span className="text-[11px]">1 - 2 hrs</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-4 h-4 rounded bg-emerald-600"></span>
            <span className="text-[11px]">2+ hrs</span>
          </div>
        </div>

        {/* Weekday headers */}
        <div className="grid grid-cols-7 gap-2 text-center text-xs font-bold text-slate-400 uppercase tracking-wider py-3">
          <div>Sun</div>
          <div>Mon</div>
          <div>Tue</div>
          <div>Wed</div>
          <div>Thu</div>
          <div>Fri</div>
          <div>Sat</div>
        </div>

        {/* Days Grid */}
        <div className="grid grid-cols-7 gap-2">
          {days.map((day, idx) => {
            const dateStr = format(day, 'yyyy-MM-dd');
            const inCurrentMonth = isSameMonth(day, currentMonth);
            const isTodayDate = isToday(day);
            const isSelected = isSameDay(day, selectedDate);
            const activity = activityMap[dateStr] || { minutes: 0, completedTasks: [] };
            const heatClass = getActivityHeat(activity.minutes, activity.completedTasks.length);

            return (
              <div
                key={idx}
                onClick={() => handleDayClick(day)}
                className={`min-h-[90px] sm:min-h-[105px] p-2.5 rounded-2xl border transition-all cursor-pointer flex flex-col justify-between ${
                  !inCurrentMonth
                    ? 'opacity-30 border-slate-100 bg-slate-50/50'
                    : isSelected
                    ? 'border-indigo-600 ring-2 ring-indigo-500/20 bg-indigo-50/10'
                    : 'border-slate-200/70 hover:border-slate-300 hover:bg-slate-50/80'
                }`}
              >
                <div className="flex items-center justify-between">
                  <span
                    className={`text-xs font-bold w-6 h-6 rounded-full flex items-center justify-center ${
                      isTodayDate
                        ? 'bg-indigo-600 text-white shadow-sm shadow-indigo-600/30'
                        : 'text-slate-700'
                    }`}
                  >
                    {format(day, 'd')}
                  </span>
                  {activity.completedTasks.length > 0 && (
                    <span className="w-2 h-2 rounded-full bg-emerald-500 ring-2 ring-white"></span>
                  )}
                </div>

                {/* Day Summary Pill */}
                {activity.minutes > 0 ? (
                  <div
                    className={`text-[10px] px-2 py-0.5 rounded-md mt-auto text-center truncate ${heatClass}`}
                  >
                    {Math.floor(activity.minutes / 60) > 0
                      ? `${Math.floor(activity.minutes / 60)}h ${activity.minutes % 60}m`
                      : `${activity.minutes}m`}
                  </div>
                ) : activity.completedTasks.length > 0 ? (
                  <div className="text-[10px] px-2 py-0.5 rounded-md mt-auto text-center truncate bg-emerald-100 text-emerald-800 font-semibold">
                    {activity.completedTasks.length} Done
                  </div>
                ) : (
                  <div className="h-4"></div>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/* Selected Day Details Modal / Drawer */}
      <Modal
        isOpen={isDrawerOpen}
        onClose={() => setIsDrawerOpen(false)}
        title={`Activity: ${format(selectedDate, 'EEEE, MMMM do, yyyy')}`}
      >
        <div className="space-y-6">
          {/* Day Metrics */}
          <div className="grid grid-cols-2 gap-4">
            <div className="p-4 rounded-2xl bg-indigo-50/50 border border-indigo-100">
              <span className="text-xs font-semibold text-indigo-700">Total Study Time</span>
              <p className="text-xl font-black text-indigo-950 mt-1">
                {selectedActivity.minutes > 0
                  ? `${Math.floor(selectedActivity.minutes / 60)}h ${selectedActivity.minutes % 60}m`
                  : '0 mins'}
              </p>
            </div>
            <div className="p-4 rounded-2xl bg-emerald-50/50 border border-emerald-100">
              <span className="text-xs font-semibold text-emerald-700">Tasks Finished</span>
              <p className="text-xl font-black text-emerald-950 mt-1">
                {selectedActivity.completedTasks.length} Completed
              </p>
            </div>
          </div>

          {/* Study Sessions on this date */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-3">
              Study Sessions ({selectedActivity.logs.length})
            </h4>
            {selectedActivity.logs.length > 0 ? (
              <div className="space-y-2">
                {selectedActivity.logs.map((log) => (
                  <div
                    key={log.id}
                    className="p-3.5 rounded-xl border border-slate-200 bg-slate-50/50 space-y-1"
                  >
                    <div className="flex items-center justify-between">
                      <h5 className="text-xs font-bold text-slate-800">{log.topicName}</h5>
                      <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-indigo-100 text-indigo-800">
                        {log.durationMinutes} mins
                      </span>
                    </div>
                    <div className="flex items-center gap-2 text-[11px] text-slate-400">
                      <Clock className="w-3 h-3" />
                      <span>{log.startTime || '--'} - {log.endTime || '--'}</span>
                    </div>
                    {log.notes && (
                      <p className="text-xs text-slate-600 bg-white p-2 rounded-lg border border-slate-100 mt-1">
                        {log.notes}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-400 italic">No study sessions logged for this day.</p>
            )}
          </div>

          {/* Completed Tasks on this date */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-3">
              Completed Tasks ({selectedActivity.completedTasks.length})
            </h4>
            {selectedActivity.completedTasks.length > 0 ? (
              <div className="space-y-2">
                {selectedActivity.completedTasks.map((task) => (
                  <div
                    key={task.id}
                    className="p-3 rounded-xl border border-emerald-200 bg-emerald-50/30 flex items-center justify-between gap-3"
                  >
                    <div className="flex items-center gap-2.5">
                      <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0" />
                      <div>
                        <h5 className="text-xs font-bold text-slate-800">{task.title}</h5>
                        {task.subjectTitle && (
                          <span className="text-[10px] text-slate-400">{task.subjectTitle}</span>
                        )}
                      </div>
                    </div>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-800">
                      Finished
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-400 italic">No tasks completed on this date.</p>
            )}
          </div>
        </div>
      </Modal>
    </div>
  );
}
