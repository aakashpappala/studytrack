import React from 'react';

export default function ProgressBar({ value = 0, max = 100, height = 'h-2.5', showLabel = false, labelText = '', color = 'brand' }) {
  const percentage = Math.min(100, Math.max(0, Math.round((value / max) * 100)));

  const getColorClass = () => {
    if (color === 'emerald' || percentage >= 80) return 'bg-emerald-500';
    if (color === 'amber' || percentage >= 40) return 'bg-indigo-600';
    return 'bg-amber-500';
  };

  return (
    <div className="w-full">
      {(showLabel || labelText) && (
        <div className="flex justify-between items-center text-xs font-semibold text-slate-600 mb-1.5">
          <span>{labelText}</span>
          <span>{percentage}%</span>
        </div>
      )}
      <div className={`w-full bg-slate-100 rounded-full overflow-hidden ${height} border border-slate-200/50`}>
        <div
          className={`h-full transition-all duration-500 rounded-full ${getColorClass()}`}
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  );
}
