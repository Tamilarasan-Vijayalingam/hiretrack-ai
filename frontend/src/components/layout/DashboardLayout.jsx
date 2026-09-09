import { useState, useEffect } from 'react';
import { Outlet, useNavigate, Link } from 'react-router-dom';
import { useTheme } from '../../context/ThemeContext';
import { useAuth } from '../../context/AuthContext';
import { formatDistanceToNow } from 'date-fns';
import api from '../../api/axios';
import { 
  Moon, Sun, Menu, X, LayoutDashboard, Users, Building, 
  Calendar, Settings, LogOut, Bell, FileText, BrainCircuit, BarChart3, Video
} from 'lucide-react';

export default function DashboardLayout() {
  const { theme, toggleTheme } = useTheme();
  const { user, logout } = useAuth();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchNotifs = async () => {
      try {
        const res = await api.get('/notifications');
        setNotifications(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    if (user) fetchNotifs();
  }, [user]);

  const unreadCount = notifications.filter(n => !n.isRead).length;

  const handleMarkAllRead = async () => {
    try {
      await api.put('/notifications/read-all');
      setNotifications(notifications.map(n => ({ ...n, isRead: true })));
    } catch (err) {
      console.error(err);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getNavItems = () => {
    const role = user?.role;
    if (role === 'ADMIN') {
      return [
        { name: 'Dashboard', icon: LayoutDashboard, href: '/admin/dashboard' },
        { name: 'Students', icon: Users, href: '/admin/students' },
        { name: 'Companies', icon: Building, href: '/admin/companies' },
        { name: 'Drives', icon: Calendar, href: '/admin/drives' },
        { name: 'Analytics', icon: BarChart3, href: '/admin/analytics' },
      ];
    }
    if (role === 'PLACEMENT_OFFICER') {
      return [
        { name: 'Dashboard', icon: LayoutDashboard, href: '/po/dashboard' },
        { name: 'Companies', icon: Building, href: '/po/companies' },
        { name: 'Students', icon: Users, href: '/po/students' },
        { name: 'Drives', icon: Calendar, href: '/po/drives' },
        { name: 'Placements', icon: FileText, href: '/po/placements' },
      ];
    }
    if (role === 'HOD') {
      return [
        { name: 'Dashboard', icon: LayoutDashboard, href: '/hod/dashboard' },
        { name: 'Companies', icon: Building, href: '/hod/companies' },
        { name: 'Department Students', icon: Users, href: '/hod/students' },
        { name: 'Risk Students', icon: FileText, href: '/hod/risk' },
      ];
    }
    if (role === 'STUDENT') {
      return [
        { name: 'Dashboard', icon: LayoutDashboard, href: '/student/dashboard' },
        { name: 'My Profile', icon: Users, href: '/student/profile' },
        { name: 'Resume Analyzer', icon: FileText, href: '/student/resume' },
        { name: 'Mock Interview', icon: Video, href: '/student/mock-interview' },
        { name: 'Placement Prediction', icon: BrainCircuit, href: '/student/prediction' },
        { name: 'Eligible Companies', icon: Building, href: '/student/companies' },
      ];
    }
    return []; // Fallback
  };

  const navigation = getNavItems();

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-navy flex">
      {/* Mobile sidebar */}
      <div className={`fixed inset-0 z-50 lg:hidden ${sidebarOpen ? 'block' : 'hidden'}`}>
        <div className="fixed inset-0 bg-gray-900/80" onClick={() => setSidebarOpen(false)} />
        <div className="fixed inset-y-0 left-0 w-64 bg-white dark:bg-navy-dark border-r border-slate-200 dark:border-slate-800 p-4">
          <div className="flex items-center justify-between mb-8">
            <span className="text-xl font-bold text-primary-dark dark:text-primary-light">HireTrack AI</span>
            <button onClick={() => setSidebarOpen(false)} className="text-slate-500 hover:text-slate-700 dark:text-slate-400">
              <X size={24} />
            </button>
          </div>
          <nav className="space-y-2">
            {navigation.map((item) => (
              <Link key={item.name} to={item.href} onClick={() => setSidebarOpen(false)} className="flex items-center gap-3 px-3 py-2 rounded-lg text-slate-700 hover:bg-slate-100 dark:text-slate-300 dark:hover:bg-slate-800 transition-colors">
                <item.icon size={20} />
                <span>{item.name}</span>
              </Link>
            ))}
          </nav>
        </div>
      </div>

      {/* Desktop sidebar */}
      <div className="hidden lg:flex flex-col w-64 bg-white dark:bg-navy-dark border-r border-slate-200 dark:border-slate-800 p-4">
        <div className="flex items-center mb-8 px-3">
          <span className="text-2xl font-bold text-primary dark:text-primary-light">HireTrack <span className="text-slate-900 dark:text-white">AI</span></span>
        </div>
        <nav className="flex-1 space-y-2">
          {navigation.map((item) => (
            <Link key={item.name} to={item.href} className="flex items-center gap-3 px-3 py-2 rounded-lg text-slate-700 hover:bg-slate-100 dark:text-slate-300 dark:hover:bg-slate-800 transition-colors">
              <item.icon size={20} />
              <span>{item.name}</span>
            </Link>
          ))}
        </nav>
        <div className="mt-auto border-t border-slate-200 dark:border-slate-800 pt-4">
          <button onClick={handleLogout} className="flex w-full items-center gap-3 px-3 py-2 rounded-lg text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors">
            <LogOut size={20} />
            <span>Logout</span>
          </button>
        </div>
      </div>

      {/* Main content */}
      <div className="flex-1 flex flex-col min-h-screen overflow-hidden">
        {/* Top navbar */}
        <header className="h-16 bg-white dark:bg-navy-dark border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-4 sm:px-6 lg:px-8">
          <button onClick={() => setSidebarOpen(true)} className="lg:hidden text-slate-500 hover:text-slate-700 dark:text-slate-400">
            <Menu size={24} />
          </button>
          <div className="flex-1" />
          <div className="flex items-center gap-4">
            <div className="relative">
              <button onClick={() => setNotificationsOpen(!notificationsOpen)} className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative">
                <Bell size={20} />
                {unreadCount > 0 && (
                  <span className="absolute top-1 right-1 h-2.5 w-2.5 rounded-full bg-red-500 border-2 border-white dark:border-navy-dark"></span>
                )}
              </button>
              
              {notificationsOpen && (
                <div className="absolute right-0 mt-2 w-80 bg-white dark:bg-navy-dark rounded-xl shadow-lg border border-slate-200 dark:border-slate-800 z-50 overflow-hidden">
                  <div className="p-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
                    <h3 className="font-semibold text-slate-900 dark:text-white">Notifications</h3>
                    {unreadCount > 0 && (
                      <button onClick={handleMarkAllRead} className="text-xs text-primary hover:underline">Mark all read</button>
                    )}
                  </div>
                  <div className="max-h-96 overflow-y-auto">
                    {notifications.length === 0 ? (
                      <div className="p-4 text-center text-sm text-slate-500">No notifications</div>
                    ) : (
                      notifications.map(n => (
                        <div key={n.id} className={`p-4 border-b border-slate-100 dark:border-slate-800/50 hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors ${!n.isRead ? 'bg-primary/5 dark:bg-primary/10' : ''}`}>
                          <h4 className="text-sm font-medium text-slate-900 dark:text-white">{n.title}</h4>
                          <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{n.message}</p>
                          <p className="text-[10px] text-slate-400 mt-2">{formatDistanceToNow(new Date(n.createdAt), { addSuffix: true })}</p>
                        </div>
                      ))
                    )}
                  </div>
                </div>
              )}
            </div>
            <button onClick={toggleTheme} className="p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors">
              {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
            </button>
            <div className="h-8 w-8 rounded-full bg-primary flex items-center justify-center text-white font-bold cursor-pointer">
              {user?.email?.[0]?.toUpperCase() || 'U'}
            </div>
          </div>
        </header>

        {/* Page content */}
        <main className="flex-1 overflow-y-auto p-4 sm:p-6 lg:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
