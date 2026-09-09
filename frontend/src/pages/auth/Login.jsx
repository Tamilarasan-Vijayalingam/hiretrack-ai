import { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card, CardContent } from '../../components/ui/Card';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login, isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  if (isAuthenticated) {
    if (user?.role === 'ADMIN') return <Navigate to="/admin/dashboard" replace />;
    if (user?.role === 'PLACEMENT_OFFICER') return <Navigate to="/po/dashboard" replace />;
    if (user?.role === 'HOD') return <Navigate to="/hod/dashboard" replace />;
    if (user?.role === 'STUDENT') return <Navigate to="/student/dashboard" replace />;
    return <Navigate to="/" replace />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      const userData = await login(email, password);
      if (userData?.role === 'ADMIN') navigate('/admin/dashboard');
      else if (userData?.role === 'PLACEMENT_OFFICER') navigate('/po/dashboard');
      else if (userData?.role === 'HOD') navigate('/hod/dashboard');
      else navigate('/student/dashboard');
    } catch (err) {
      // Mock login fallback if backend isn't running for the UI showcase
      if (email === 'admin@hiretrack.com') {
        localStorage.setItem('token', 'mock_token');
        const mockUser = { id: '1', role: 'ADMIN', email: 'admin@hiretrack.com' };
        localStorage.setItem('user', JSON.stringify(mockUser));
        window.location.href = '/admin/dashboard';
      } else {
        setError(err.message || 'Invalid credentials');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-navy p-4">
      <Card className="w-full max-w-md">
        <CardContent className="pt-6">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-primary dark:text-primary-light">HireTrack AI</h1>
            <p className="text-slate-500 dark:text-slate-400 mt-2">Welcome back! Please login to your account.</p>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-50 dark:bg-red-900/20 text-red-600 dark:text-red-400 rounded-lg text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Email Address"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="admin@hiretrack.com"
            />
            
            <Input
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="••••••••"
            />

            <div className="flex items-center justify-between text-sm">
              <label className="flex items-center text-slate-600 dark:text-slate-300">
                <input type="checkbox" className="mr-2 rounded border-slate-300 text-primary focus:ring-primary" />
                Remember me
              </label>
              <a href="#" className="text-primary hover:underline font-medium">Forgot password?</a>
            </div>

            <Button type="submit" className="w-full" isLoading={isLoading}>
              Sign In
            </Button>
            
            <p className="text-xs text-center text-slate-500 mt-4">
              Demo bypass: Use admin@hiretrack.com to login without backend.
            </p>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
