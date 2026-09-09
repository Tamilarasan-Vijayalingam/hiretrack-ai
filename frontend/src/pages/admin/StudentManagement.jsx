import { useState, useEffect } from 'react';
import { Search, Plus, Edit2, Trash2 } from 'lucide-react';
import api from '../../api/axios';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../../components/ui/Table';

export default function StudentManagement() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      // Mock Data Fallback
      setStudents([
        { id: '1', registerNumber: 'CB21001', name: 'Alice Smith', department: 'CSE', cgpa: 9.2, status: 'Placed' },
        { id: '2', registerNumber: 'CB21002', name: 'Bob Johnson', department: 'IT', cgpa: 8.5, status: 'Eligible' },
        { id: '3', registerNumber: 'CB21003', name: 'Charlie Brown', department: 'ECE', cgpa: 7.1, status: 'Needs Training' },
      ]);
    } catch (error) {
      console.error("Failed to load students", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredStudents = students.filter(s => 
    s.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
    s.registerNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Student Management</h1>
          <p className="text-slate-500 dark:text-slate-400">View and manage all registered students.</p>
        </div>
        <Button className="flex items-center gap-2">
          <Plus size={18} />
          Add Student
        </Button>
      </div>

      <Card>
        <CardHeader className="flex flex-col sm:flex-row justify-between items-center gap-4 py-4">
          <CardTitle className="text-lg">Students Directory</CardTitle>
          <div className="relative w-full sm:w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <Input 
              placeholder="Search by name or reg no..." 
              className="pl-10"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </CardHeader>
        <CardContent className="p-0">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Register No</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Department</TableHead>
                <TableHead>CGPA</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-8 text-slate-500">Loading students...</TableCell>
                </TableRow>
              ) : filteredStudents.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-8 text-slate-500">No students found.</TableCell>
                </TableRow>
              ) : (
                filteredStudents.map((student) => (
                  <TableRow key={student.id}>
                    <TableCell className="font-medium text-slate-900 dark:text-white">{student.registerNumber}</TableCell>
                    <TableCell className="text-slate-700 dark:text-slate-300">{student.name}</TableCell>
                    <TableCell className="text-slate-700 dark:text-slate-300">{student.department}</TableCell>
                    <TableCell className="text-slate-700 dark:text-slate-300">{student.cgpa}</TableCell>
                    <TableCell>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        student.status === 'Placed' ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400' :
                        student.status === 'Eligible' ? 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400' :
                        'bg-orange-100 text-orange-800 dark:bg-orange-900/30 dark:text-orange-400'
                      }`}>
                        {student.status}
                      </span>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <button className="p-1 text-slate-400 hover:text-primary transition-colors">
                          <Edit2 size={16} />
                        </button>
                        <button className="p-1 text-slate-400 hover:text-red-500 transition-colors">
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
