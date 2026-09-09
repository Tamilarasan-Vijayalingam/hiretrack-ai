import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { Users, Briefcase, GraduationCap, TrendingUp, Building, Award, Download } from 'lucide-react';
import { 
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  BarChart, Bar, PieChart, Pie, Cell, Legend
} from 'recharts';
import api from '../../api/axios';
import ChartTooltip from '../../components/ui/ChartTooltip';

import { useAuth } from '../../context/AuthContext';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8'];

export default function AdminDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const endpoint = user?.role === 'HOD' ? '/analytics/hod' : '/analytics/admin';
        const res = await api.get(endpoint);
        setStats(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  const handleDownloadReport = async (format) => {
    try {
      const response = await api.get(`/reports/placements/${format}`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `placement_report.${format}`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
    } catch (err) {
      console.error(err);
      alert('Failed to download report');
    }
  };

  if (loading || !stats) return <div className="p-8 text-center text-slate-500">Loading Analytics...</div>;

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Admin Analytics</h1>
          <p className="text-slate-500 dark:text-slate-400">System-wide placement overview.</p>
        </div>
        <div className="flex gap-2">
          <button onClick={() => handleDownloadReport('csv')} className="flex items-center gap-2 px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 rounded-lg text-sm font-medium transition-colors">
            <Download size={16} /> CSV Log
          </button>
          <button onClick={() => handleDownloadReport('pdf')} className="flex items-center gap-2 px-4 py-2 bg-primary hover:bg-primary-dark text-white rounded-lg text-sm font-medium transition-colors shadow-sm">
            <Download size={16} /> PDF Report
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Total Students</p>
                <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">{stats.totalStudents}</h3>
                <p className="text-xs text-slate-500 mt-1">{stats.eligibleStudents} Eligible</p>
              </div>
              <div className="p-3 bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400 rounded-lg">
                <Users size={20} />
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Placed Students</p>
                <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">{stats.placedStudents}</h3>
                <p className="text-xs text-green-500 mt-1">{stats.placementPercentage.toFixed(1)}% Placement Rate</p>
              </div>
              <div className="p-3 bg-green-100 text-green-600 dark:bg-green-900/30 dark:text-green-400 rounded-lg">
                <GraduationCap size={20} />
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Average Package</p>
                <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">{stats.averagePackage} LPA</h3>
                <p className="text-xs text-slate-500 mt-1">Highest: {stats.highestPackage} LPA</p>
              </div>
              <div className="p-3 bg-purple-100 text-purple-600 dark:bg-purple-900/30 dark:text-purple-400 rounded-lg">
                <TrendingUp size={20} />
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Active Drives</p>
                <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">{stats.activeDrives}</h3>
                <p className="text-xs text-slate-500 mt-1">{stats.upcomingDrives} Upcoming</p>
              </div>
              <div className="p-3 bg-orange-100 text-orange-600 dark:bg-orange-900/30 dark:text-orange-400 rounded-lg">
                <Building size={20} />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Placement Trends</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={stats.placementTrends} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                  <defs>
                    <linearGradient id="colorPlacements" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#2563eb" stopOpacity={0.8}/>
                      <stop offset="95%" stopColor="#2563eb" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <XAxis dataKey="name" stroke="#888888" fontSize={12} tickLine={false} axisLine={false} />
                  <YAxis stroke="#888888" fontSize={12} tickLine={false} axisLine={false} />
                  <Tooltip content={<ChartTooltip />} />
                  <Area type="monotone" dataKey="placements" stroke="#2563eb" fillOpacity={1} fill="url(#colorPlacements)" />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Department Placements</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-[300px] w-full flex items-center justify-center">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={stats.departmentPlacements}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={80}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {stats.departmentPlacements.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip content={<ChartTooltip />} />
                  <Legend verticalAlign="bottom" height={36}/>
                </PieChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
