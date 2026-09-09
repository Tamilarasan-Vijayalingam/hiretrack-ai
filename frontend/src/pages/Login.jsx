import { useTheme } from '../context/ThemeContext';
import { Moon, Sun } from 'lucide-react';

export default function Login() {
  const { theme, toggleTheme } = useTheme();

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-navy p-4 transition-colors duration-300">
      <div className="absolute top-4 right-4">
        <button onClick={toggleTheme} className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-full hover:bg-slate-200 dark:hover:bg-slate-800 transition-colors">
          {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
        </button>
      </div>
      <div className="w-full max-w-md bg-white dark:bg-navy-dark rounded-2xl shadow-xl p-8 border border-slate-100 dark:border-slate-800">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-primary dark:text-primary-light mb-2">HireTrack AI</h1>
          <p className="text-slate-500 dark:text-slate-400">Sign in to your account</p>
        </div>
        <form className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Email Address</label>
            <input type="email" className="w-full px-4 py-2 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy text-slate-900 dark:text-white focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all" placeholder="Enter your email" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Password</label>
            <input type="password" className="w-full px-4 py-2 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy text-slate-900 dark:text-white focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all" placeholder="Enter your password" />
          </div>
          <button type="button" className="w-full bg-primary hover:bg-primary-dark text-white font-medium py-2 px-4 rounded-lg transition-colors">
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
}
