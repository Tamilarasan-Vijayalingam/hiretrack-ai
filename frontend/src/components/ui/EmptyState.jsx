import { cn } from '../../utils/cn';

export function EmptyState({ icon: Icon, title, description, action, className }) {
  return (
    <div className={cn("flex flex-col items-center justify-center p-12 text-center h-full min-h-[400px] bg-white dark:bg-navy-dark rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm", className)}>
      {Icon && (
        <div className="w-16 h-16 bg-slate-100 dark:bg-slate-800 text-slate-400 dark:text-slate-500 rounded-full flex items-center justify-center mb-6">
          <Icon size={32} />
        </div>
      )}
      <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">{title}</h3>
      {description && <p className="text-slate-500 dark:text-slate-400 max-w-sm mb-6">{description}</p>}
      {action && <div>{action}</div>}
    </div>
  );
}
