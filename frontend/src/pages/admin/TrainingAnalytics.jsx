import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { TrendingUp } from 'lucide-react';
import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend
} from 'recharts';
import api from '../../api/axios';
import ChartTooltip from '../../components/ui/ChartTooltip';

export default function TrainingAnalytics() {
  const [data, setData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get('/analytics/training');
        const d = res.data;
        setData({
          programName: d.programName,
          metrics: [
            { subject: 'Aptitude', before: Number(d.beforeAptitude || 0).toFixed(1), after: Number(d.afterAptitude || 0).toFixed(1), improvement: Number(d.aptitudeImprovementPercentage || 0).toFixed(1) },
            { subject: 'Coding', before: Number(d.beforeCoding || 0).toFixed(1), after: Number(d.afterCoding || 0).toFixed(1), improvement: Number(d.codingImprovementPercentage || 0).toFixed(1) },
            { subject: 'Interview', before: Number(d.beforeInterview || 0).toFixed(1), after: Number(d.afterInterview || 0).toFixed(1), improvement: Number(d.interviewImprovementPercentage || 0).toFixed(1) }
          ]
        });
      } catch (error) {
        console.error("Failed to fetch training analytics", error);
      }
    };
    fetchData();
  }, []);

  if (!data) return <div className="p-8 text-center text-slate-500">Loading Training Analytics...</div>;

  return (
    <div className="space-y-6">
      <div className="flex flex-col justify-center">
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Training Analytics</h1>
        <p className="text-slate-500 dark:text-slate-400">Comparing Student Performance: Before vs After {data.programName}</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {data.metrics.map((m, idx) => (
          <Card key={idx} className="bg-gradient-to-br from-slate-50 to-white dark:from-navy-dark dark:to-slate-900 border border-slate-200 dark:border-slate-800">
            <CardContent className="p-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="font-semibold text-lg text-slate-700 dark:text-slate-200">{m.subject}</h3>
                <span className="flex items-center gap-1 text-green-600 bg-green-100 dark:bg-green-900/30 dark:text-green-400 px-2 py-1 rounded text-xs font-bold">
                  <TrendingUp size={14} /> +{m.improvement}%
                </span>
              </div>
              <div className="flex justify-between text-sm">
                <div>
                  <p className="text-slate-500">Before</p>
                  <p className="text-xl font-bold text-slate-900 dark:text-white">{m.before}</p>
                </div>
                <div className="text-right">
                  <p className="text-slate-500">After</p>
                  <p className="text-xl font-bold text-primary">{m.after}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Cohort Progression</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="h-[400px] w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data.metrics} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
                <XAxis dataKey="subject" stroke="#888888" tickLine={false} axisLine={false} />
                <YAxis stroke="#888888" tickLine={false} axisLine={false} />
                <Tooltip content={<ChartTooltip />} cursor={{ fill: 'rgba(0, 0, 0, 0.05)' }} />
                <Legend iconType="circle" />
                <Bar dataKey="before" name="Before Training" fill="#94a3b8" radius={[4, 4, 0, 0]} maxBarSize={60} />
                <Bar dataKey="after" name="After Training" fill="#2563eb" radius={[4, 4, 0, 0]} maxBarSize={60} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
