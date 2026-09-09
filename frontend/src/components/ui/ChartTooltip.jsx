import React from 'react';

export default function ChartTooltip({ active, payload, label }) {
  if (active && payload && payload.length) {
    return (
      <div className="bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 p-3 rounded-lg shadow-lg">
        {label && <p className="text-slate-900 dark:text-white font-medium mb-1">{label}</p>}
        {payload.map((entry, index) => (
          <p key={`item-${index}`} className="text-sm flex items-center gap-2">
            <span 
              className="w-3 h-3 rounded-full inline-block" 
              style={{ backgroundColor: entry.color }}
            ></span>
            <span className="text-slate-600 dark:text-slate-300">
              {entry.name}:
            </span>
            <span className="text-slate-900 dark:text-white font-semibold">
              {entry.value}
            </span>
          </p>
        ))}
      </div>
    );
  }
  return null;
}
