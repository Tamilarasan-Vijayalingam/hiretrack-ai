import { cn } from '../../utils/cn';

export function Card({ className, ...props }) {
  return (
    <div className={cn("bg-white dark:bg-navy-dark rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden", className)} {...props} />
  );
}

export function CardHeader({ className, ...props }) {
  return <div className={cn("px-6 py-4 border-b border-slate-200 dark:border-slate-800", className)} {...props} />;
}

export function CardTitle({ className, ...props }) {
  return <h3 className={cn("text-lg font-semibold text-slate-900 dark:text-white", className)} {...props} />;
}

export function CardContent({ className, ...props }) {
  return <div className={cn("p-6", className)} {...props} />;
}

export function CardFooter({ className, ...props }) {
  return <div className={cn("px-6 py-4 border-t border-slate-200 dark:border-slate-800", className)} {...props} />;
}
