import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import ProgressBar from '../../components/common/ProgressBar';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import {
  Map,
  BookOpen,
  FolderGit2,
  CheckCircle2,
  Clock,
  Circle,
  ChevronDown,
  ChevronRight,
  Sparkles,
  Layers,
  Award,
  Filter,
  BarChart3
} from 'lucide-react';

export default function MyRoadmapPage() {
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);
  const [openSubjects, setOpenSubjects] = useState({});
  const [openModules, setOpenModules] = useState({});
  const [filterStatus, setFilterStatus] = useState('ALL'); // ALL, COMPLETED, IN_PROGRESS, NOT_STARTED

  useEffect(() => {
    fetchMyRoadmap();
  }, []);

  const fetchMyRoadmap = async () => {
    try {
      const res = await api.get('/roadmaps/my');
      const data = res.data.data;
      setRoadmap(data);
      if (data && data.subjects) {
        // Open first subject by default
        const initialOpenSub = {};
        const initialOpenMod = {};
        data.subjects.forEach((sub, idx) => {
          if (idx === 0) {
            initialOpenSub[sub.id] = true;
            if (sub.modules && sub.modules.length > 0) {
              initialOpenMod[sub.modules[0].id] = true;
            }
          }
        });
        setOpenSubjects(initialOpenSub);
        setOpenModules(initialOpenMod);
      }
    } catch (err) {
      console.error('Error fetching student roadmap', err);
    } finally {
      setLoading(false);
    }
  };

  const toggleSubject = (subjectId) => {
    setOpenSubjects((prev) => ({
      ...prev,
      [subjectId]: !prev[subjectId],
    }));
  };

  const toggleModule = (moduleId) => {
    setOpenModules((prev) => ({
      ...prev,
      [moduleId]: !prev[moduleId],
    }));
  };

  const expandAll = () => {
    if (!roadmap) return;
    const subObj = {};
    const modObj = {};
    roadmap.subjects.forEach((s) => {
      subObj[s.id] = true;
      (s.modules || []).forEach((m) => {
        modObj[m.id] = true;
      });
    });
    setOpenSubjects(subObj);
    setOpenModules(modObj);
  };

  const collapseAll = () => {
    setOpenSubjects({});
    setOpenModules({});
  };

  if (loading) {
    return <LoadingSpinner size="lg" message="Loading your customized learning roadmap..." />;
  }

  if (!roadmap) {
    return (
      <div className="rounded-3xl bg-white p-12 text-center shadow-sm border border-slate-100 max-w-xl mx-auto">
        <div className="w-16 h-16 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center mx-auto mb-4">
          <Map className="w-8 h-8" />
        </div>
        <h2 className="text-xl font-bold text-slate-800 mb-2">No Roadmap Assigned Yet</h2>
        <p className="text-slate-500 text-sm mb-6">
          Your instructor or administrator has not assigned a personalized learning track to your profile yet. Please contact support or check back soon!
        </p>
      </div>
    );
  }

  const completionPct = Math.round(roadmap.completionPercentage || 0);

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Roadmap Overview Hero */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-slate-900 via-indigo-950 to-slate-900 p-8 text-white shadow-xl border border-indigo-900/30">
        <div className="relative z-10 space-y-6">
          <div className="flex flex-wrap items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <span className="px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                {roadmap.category || 'Curriculum'}
              </span>
              <span className="px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider bg-emerald-500/20 text-emerald-300 border border-emerald-500/30">
                {roadmap.level || 'Intermediate'}
              </span>
              <span className="px-3 py-1 rounded-full text-xs font-semibold bg-white/10 text-slate-200">
                Est. {roadmap.estimatedHours} Hours
              </span>
            </div>
            <div className="flex items-center gap-2 text-xs">
              <button
                onClick={expandAll}
                className="px-3 py-1.5 rounded-lg bg-white/10 hover:bg-white/20 transition-all font-medium text-slate-200"
              >
                Expand All
              </button>
              <button
                onClick={collapseAll}
                className="px-3 py-1.5 rounded-lg bg-white/10 hover:bg-white/20 transition-all font-medium text-slate-200"
              >
                Collapse All
              </button>
            </div>
          </div>

          <div>
            <h1 className="text-3xl font-black tracking-tight text-white mb-2">
              {roadmap.title}
            </h1>
            <p className="text-slate-300 text-sm max-w-3xl leading-relaxed">
              {roadmap.description}
            </p>
          </div>

          {/* Quick Metrics Bar */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 pt-4 border-t border-slate-800">
            <div>
              <p className="text-xs text-slate-400 font-medium">Total Subjects</p>
              <p className="text-xl font-bold text-white">{roadmap.totalSubjects || roadmap.subjects?.length || 0}</p>
            </div>
            <div>
              <p className="text-xs text-slate-400 font-medium">Total Modules</p>
              <p className="text-xl font-bold text-white">{roadmap.totalModules || 0}</p>
            </div>
            <div>
              <p className="text-xs text-slate-400 font-medium">Total Topics</p>
              <p className="text-xl font-bold text-white">{roadmap.totalTopics || 0}</p>
            </div>
            <div>
              <p className="text-xs text-slate-400 font-medium">Mastery Completed</p>
              <p className="text-xl font-bold text-emerald-400">{completionPct}%</p>
            </div>
          </div>

          {/* Progress Bar in Hero */}
          <div className="space-y-1.5 pt-2">
            <div className="flex justify-between text-xs text-slate-300 font-medium">
              <span>Overall Roadmap Completion</span>
              <span>{completionPct}% Done</span>
            </div>
            <ProgressBar progress={completionPct} color="emerald" size="lg" />
          </div>
        </div>
      </div>

      {/* Filter / Search Toolbar */}
      <div className="flex flex-wrap items-center justify-between gap-4 bg-white p-4 rounded-2xl border border-slate-100 shadow-sm">
        <div className="flex items-center gap-2">
          <Filter className="w-4 h-4 text-slate-400" />
          <span className="text-xs font-bold text-slate-600 uppercase tracking-wider">Filter Topics:</span>
        </div>
        <div className="flex items-center gap-1.5 flex-wrap">
          {[
            { key: 'ALL', label: 'All Topics' },
            { key: 'COMPLETED', label: 'Completed' },
            { key: 'IN_PROGRESS', label: 'In Progress' },
            { key: 'NOT_STARTED', label: 'Not Started' },
          ].map((f) => (
            <button
              key={f.key}
              onClick={() => setFilterStatus(f.key)}
              className={`px-3 py-1.5 text-xs font-semibold rounded-xl transition-all ${
                filterStatus === f.key
                  ? 'bg-indigo-600 text-white shadow-sm shadow-indigo-600/30'
                  : 'bg-slate-50 text-slate-600 hover:bg-slate-100'
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>
      </div>

      {/* Hierarchical Subjects Tree */}
      <div className="space-y-6">
        {roadmap.subjects && roadmap.subjects.map((subject, sIdx) => {
          const isSubOpen = !!openSubjects[subject.id];
          const subProgress = Math.round(subject.completionPercentage || 0);

          return (
            <div
              key={subject.id}
              className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden transition-all duration-200 hover:border-indigo-200"
            >
              {/* Subject Accordion Header */}
              <div
                onClick={() => toggleSubject(subject.id)}
                className="p-6 cursor-pointer flex flex-col md:flex-row md:items-center justify-between gap-4 bg-slate-50/50 hover:bg-slate-50 transition-colors"
              >
                <div className="flex items-start gap-4">
                  <div className="w-10 h-10 rounded-2xl bg-indigo-100 text-indigo-700 flex items-center justify-center font-bold shrink-0 text-sm mt-0.5">
                    0{sIdx + 1}
                  </div>
                  <div>
                    <div className="flex items-center gap-3">
                      <h2 className="text-lg font-bold text-slate-900">{subject.title}</h2>
                      <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold bg-indigo-50 text-indigo-700 border border-indigo-100">
                        {subject.modules?.length || 0} Modules
                      </span>
                    </div>
                    <p className="text-xs text-slate-500 mt-1 max-w-2xl">{subject.description}</p>
                  </div>
                </div>

                <div className="flex items-center gap-6 self-end md:self-center">
                  <div className="w-36 text-right">
                    <div className="flex justify-between text-xs font-semibold text-slate-700 mb-1">
                      <span>Progress</span>
                      <span>{subProgress}%</span>
                    </div>
                    <ProgressBar progress={subProgress} size="sm" color="indigo" />
                  </div>
                  <div className="p-2 rounded-xl text-slate-400 hover:text-slate-600 bg-white border border-slate-200/60 shadow-xs">
                    {isSubOpen ? <ChevronDown className="w-5 h-5" /> : <ChevronRight className="w-5 h-5" />}
                  </div>
                </div>
              </div>

              {/* Subject Content: Modules */}
              {isSubOpen && (
                <div className="p-6 pt-2 space-y-4 bg-white border-t border-slate-100">
                  {subject.modules && subject.modules.length > 0 ? (
                    subject.modules.map((module, mIdx) => {
                      const isModOpen = !!openModules[module.id];
                      const modProgress = Math.round(module.completionPercentage || 0);

                      // Filter topics
                      const filteredTopics = (module.topics || []).filter((t) => {
                        if (filterStatus === 'ALL') return true;
                        return t.status === filterStatus;
                      });

                      return (
                        <div
                          key={module.id}
                          className="rounded-2xl border border-slate-200/80 bg-slate-50/30 overflow-hidden"
                        >
                          {/* Module Header */}
                          <div
                            onClick={() => toggleModule(module.id)}
                            className="p-4 cursor-pointer flex items-center justify-between gap-4 hover:bg-slate-100/50 transition-colors"
                          >
                            <div className="flex items-center gap-3">
                              <FolderGit2 className="w-5 h-5 text-indigo-500 shrink-0" />
                              <div>
                                <h3 className="text-sm font-bold text-slate-800">
                                  Module {mIdx + 1}: {module.title}
                                </h3>
                                {module.description && (
                                  <p className="text-xs text-slate-500 line-clamp-1">{module.description}</p>
                                )}
                              </div>
                            </div>

                            <div className="flex items-center gap-4">
                              <span className="text-xs font-medium text-slate-500">
                                {module.topics?.length || 0} Topics ({modProgress}% done)
                              </span>
                              <div className="text-slate-400">
                                {isModOpen ? <ChevronDown className="w-4 h-4" /> : <ChevronRight className="w-4 h-4" />}
                              </div>
                            </div>
                          </div>

                          {/* Module Topics */}
                          {isModOpen && (
                            <div className="px-4 pb-4 pt-1 space-y-2">
                              {filteredTopics.length > 0 ? (
                                filteredTopics.map((topic, tIdx) => {
                                  const isDone = topic.status === 'COMPLETED';
                                  const isDoing = topic.status === 'IN_PROGRESS';

                                  return (
                                    <div
                                      key={topic.id}
                                      className={`p-3 rounded-xl border flex items-center justify-between gap-4 transition-all ${
                                        isDone
                                          ? 'bg-emerald-50/50 border-emerald-200/60 text-emerald-950'
                                          : isDoing
                                          ? 'bg-indigo-50/50 border-indigo-200/60 text-indigo-950'
                                          : 'bg-white border-slate-200/60 text-slate-800'
                                      }`}
                                    >
                                      <div className="flex items-center gap-3">
                                        <div className="shrink-0">
                                          {isDone ? (
                                            <CheckCircle2 className="w-5 h-5 text-emerald-600 fill-emerald-100" />
                                          ) : isDoing ? (
                                            <Clock className="w-5 h-5 text-indigo-600 animate-pulse" />
                                          ) : (
                                            <Circle className="w-5 h-5 text-slate-300" />
                                          )}
                                        </div>
                                        <div>
                                          <div className="flex items-center gap-2">
                                            <span className="text-xs font-bold text-slate-400">
                                              #{tIdx + 1}
                                            </span>
                                            <h4 className="text-xs font-bold">{topic.title}</h4>
                                          </div>
                                          {topic.description && (
                                            <p className="text-xs text-slate-500 mt-0.5">{topic.description}</p>
                                          )}
                                        </div>
                                      </div>

                                      <div className="shrink-0">
                                        <span
                                          className={`px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                                            isDone
                                              ? 'bg-emerald-100 text-emerald-800'
                                              : isDoing
                                              ? 'bg-indigo-100 text-indigo-800'
                                              : 'bg-slate-100 text-slate-600'
                                          }`}
                                        >
                                          {topic.status ? topic.status.replace('_', ' ') : 'NOT STARTED'}
                                        </span>
                                      </div>
                                    </div>
                                  );
                                })
                              ) : (
                                <p className="text-xs text-slate-400 italic py-2 px-1">
                                  No topics match the selected status filter.
                                </p>
                              )}
                            </div>
                          )}
                        </div>
                      );
                    })
                  ) : (
                    <p className="text-xs text-slate-400 italic py-4">No modules found for this subject.</p>
                  )}
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
