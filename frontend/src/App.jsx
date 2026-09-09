import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from './context/ThemeContext';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';
import Login from './pages/auth/Login';
import AdminDashboard from './pages/admin/AdminDashboard';
import StudentManagement from './pages/admin/StudentManagement';
import StudentPrediction from './pages/student/StudentPrediction';
import ResumeAnalyzer from './pages/student/ResumeAnalyzer';
import MockInterview from './pages/student/MockInterview';
import DriveCalendar from './pages/shared/DriveCalendar';
import TrainingAnalytics from './pages/admin/TrainingAnalytics';
import CompanyManagement from './pages/admin/CompanyManagement';
import { EmptyState } from './components/ui/EmptyState';
import { Construction, ShieldAlert, AlertCircle } from 'lucide-react';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<Login />} />
            
            <Route path="/" element={<Navigate to="/login" replace />} />

            {/* Admin Routes */}
            <Route path="/admin" element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
              <Route element={<DashboardLayout />}>
                <Route path="dashboard" element={<AdminDashboard />} />
                <Route path="students" element={<StudentManagement />} />
                <Route path="companies" element={<CompanyManagement />} />
                <Route path="drives" element={<DriveCalendar />} />
                <Route path="analytics" element={<TrainingAnalytics />} />
              </Route>
            </Route>

            {/* Placement Officer Routes */}
            <Route path="/po" element={<ProtectedRoute allowedRoles={['PLACEMENT_OFFICER']} />}>
              <Route element={<DashboardLayout />}>
                <Route path="dashboard" element={<AdminDashboard />} />
                <Route path="companies" element={<CompanyManagement />} />
                <Route path="students" element={<StudentManagement />} />
                <Route path="drives" element={<DriveCalendar />} />
                <Route path="placements" element={<TrainingAnalytics />} />
              </Route>
            </Route>

            {/* HOD Routes */}
            <Route path="/hod" element={<ProtectedRoute allowedRoles={['HOD']} />}>
              <Route element={<DashboardLayout />}>
                <Route path="dashboard" element={<AdminDashboard />} />
                <Route path="companies" element={<CompanyManagement />} />
                <Route path="students" element={<StudentManagement />} />
                <Route path="risk" element={<EmptyState icon={Construction} title="Risk Analysis" description="Risk student identification is coming in the next update." />} />
              </Route>
            </Route>

            {/* Student Routes */}
            <Route path="/student" element={<ProtectedRoute allowedRoles={['STUDENT']} />}>
              <Route element={<DashboardLayout />}>
                <Route path="dashboard" element={<EmptyState icon={Construction} title="Student Dashboard" description="We are polishing your new dashboard experience." />} />
                <Route path="companies" element={<CompanyManagement />} />
                <Route path="prediction" element={<StudentPrediction />} />
                <Route path="resume" element={<ResumeAnalyzer />} />
                <Route path="mock-interview" element={<MockInterview />} />
              </Route>
            </Route>

            <Route path="/unauthorized" element={<div className="flex items-center justify-center min-h-screen bg-slate-50 dark:bg-navy p-4"><EmptyState icon={ShieldAlert} title="403 Unauthorized" description="You do not have permission to access this page." /></div>} />
            <Route path="*" element={<div className="flex items-center justify-center min-h-screen bg-slate-50 dark:bg-navy p-4"><EmptyState icon={AlertCircle} title="404 Not Found" description="The page you are looking for does not exist." /></div>} />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
