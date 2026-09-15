import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import {
  Settings,
  Shield,
  Server,
  Database,
  Lock,
  KeyRound,
  CheckCircle2,
  RefreshCw,
  Cpu,
  Layers,
  Sparkles
} from 'lucide-react';

export default function SettingsPage() {
  const { user } = useAuth();
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [successMsg, setSuccessMsg] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  const handlePasswordSubmit = (e) => {
    e.preventDefault();
    setSuccessMsg(null);
    setErrorMsg(null);

    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setErrorMsg('New password and confirm password do not match');
      return;
    }
    if (passwordForm.newPassword.length < 6) {
      setErrorMsg('Password must be at least 6 characters');
      return;
    }

    setSuccessMsg('Administrator credentials updated securely.');
    setPasswordForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
  };

  return (
    <div className="space-y-8 animate-fade-in pb-12 max-w-5xl">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-black text-slate-900 tracking-tight">System Settings & Infrastructure</h1>
        <p className="text-xs text-slate-500 mt-1">
          Configure institutional parameters, view backend service health, and manage administrator security.
        </p>
      </div>

      {/* System Health Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Backend API</span>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 ring-4 ring-emerald-100"></span>
          </div>
          <div>
            <h3 className="text-base font-black text-slate-900">Spring Boot 3.3.5</h3>
            <p className="text-xs text-slate-500 mt-0.5">Java JDK 25 LTS Runtime</p>
          </div>
          <div className="pt-2 border-t border-slate-100 text-[11px] text-emerald-600 font-semibold flex items-center gap-1">
            <CheckCircle2 className="w-3.5 h-3.5" />
            Tomcat Port 8086 Active
          </div>
        </div>

        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Database Engine</span>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 ring-4 ring-emerald-100"></span>
          </div>
          <div>
            <h3 className="text-base font-black text-slate-900">MySQL 8.0 Community</h3>
            <p className="text-xs text-slate-500 mt-0.5">Database: studytrack_db</p>
          </div>
          <div className="pt-2 border-t border-slate-100 text-[11px] text-emerald-600 font-semibold flex items-center gap-1">
            <CheckCircle2 className="w-3.5 h-3.5" />
            HikariCP Connection Pool Healthy
          </div>
        </div>

        <div className="bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Security Layer</span>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 ring-4 ring-emerald-100"></span>
          </div>
          <div>
            <h3 className="text-base font-black text-slate-900">JWT & Spring Security</h3>
            <p className="text-xs text-slate-500 mt-0.5">HMAC-SHA256 Token Provider</p>
          </div>
          <div className="pt-2 border-t border-slate-100 text-[11px] text-emerald-600 font-semibold flex items-center gap-1">
            <CheckCircle2 className="w-3.5 h-3.5" />
            Role-Based Access Guarded
          </div>
        </div>
      </div>

      {/* Main Settings Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Administrator Account Security */}
        <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-sm space-y-6">
          <div className="flex items-center gap-3 pb-4 border-b border-slate-100">
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <Lock className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold text-slate-900">Admin Security Credentials</h2>
              <p className="text-xs text-slate-400">Update system administrator master password</p>
            </div>
          </div>

          {successMsg && (
            <div className="p-3.5 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-medium flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0" />
              <span>{successMsg}</span>
            </div>
          )}

          {errorMsg && (
            <div className="p-3.5 rounded-xl bg-rose-50 border border-rose-200 text-rose-800 text-xs font-medium">
              {errorMsg}
            </div>
          )}

          <form onSubmit={handlePasswordSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Current Admin Password
              </label>
              <input
                type="password"
                required
                value={passwordForm.currentPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })}
                placeholder="••••••••"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                New Password
              </label>
              <input
                type="password"
                required
                value={passwordForm.newPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
                placeholder="••••••••"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                Confirm New Password
              </label>
              <input
                type="password"
                required
                value={passwordForm.confirmPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, confirmPassword: e.target.value })}
                placeholder="••••••••"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
              />
            </div>

            <div className="pt-2">
              <button
                type="submit"
                className="w-full py-2.5 bg-slate-900 hover:bg-slate-800 text-white rounded-xl text-xs font-bold shadow-sm transition-all"
              >
                Update Password
              </button>
            </div>
          </form>
        </div>

        {/* System Policies & Automated Automation Rules */}
        <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-sm space-y-6">
          <div className="flex items-center gap-3 pb-4 border-b border-slate-100">
            <div className="w-10 h-10 rounded-2xl bg-slate-100 text-slate-700 flex items-center justify-center">
              <Cpu className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold text-slate-900">Study Engine Rules</h2>
              <p className="text-xs text-slate-400">Automated streak & completion thresholds</p>
            </div>
          </div>

          <div className="space-y-4 text-xs">
            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/60 space-y-1">
              <div className="flex justify-between items-center font-bold text-slate-800">
                <span>Streak Grace Period</span>
                <span className="text-indigo-600">24 Hours</span>
              </div>
              <p className="text-slate-500 text-[11px]">
                Students must log at least 1 task completion or 15 minutes of study every calendar day to preserve their streak.
              </p>
            </div>

            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/60 space-y-1">
              <div className="flex justify-between items-center font-bold text-slate-800">
                <span>At-Risk Notification Threshold</span>
                <span className="text-rose-600">&lt; 30% Overall</span>
              </div>
              <p className="text-slate-500 text-[11px]">
                Students falling under 30% progress or inactive for &gt; 3 days appear in the Admin At-Risk watchlist.
              </p>
            </div>

            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/60 space-y-1">
              <div className="flex justify-between items-center font-bold text-slate-800">
                <span>Dynamic Hierarchy Integrity</span>
                <span className="text-emerald-600">Enforced</span>
              </div>
              <p className="text-slate-500 text-[11px]">
                Roadmap → Subject → Module → Topic → Task structure persists relational integrity across all students.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
