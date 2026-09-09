import { useState, useEffect, useMemo } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { 
  Building, Plus, Search, Filter, Trash2, Edit3, Eye, 
  CheckCircle2, XCircle, AlertTriangle, ExternalLink, Calendar, 
  DollarSign, Award, Users, MapPin, Mail, Phone, Globe, 
  Briefcase, GraduationCap, Clock, ArrowUpDown, ChevronDown, 
  Check, X, ShieldAlert, Sparkles, Loader2, LayoutGrid, ListFilter
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';

export default function CompanyManagement() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const isOfficer = user?.role === 'PLACEMENT_OFFICER';
  const canManage = isAdmin || isOfficer;

  // Data state
  const [companies, setCompanies] = useState([]);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successToast, setSuccessToast] = useState(null);

  // Filters & Controls
  const [searchQuery, setSearchQuery] = useState('');
  const [industryFilter, setIndustryFilter] = useState('ALL');
  const [locationFilter, setLocationFilter] = useState('ALL');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('name-asc');
  const [viewMode, setViewMode] = useState('grid'); // 'grid' or 'table'

  // Modals state
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [editingCompany, setEditingCompany] = useState(null);
  const [viewingCompany, setViewingCompany] = useState(null);
  const [deletingCompany, setDeletingCompany] = useState(null);

  // Details Modal Active Tab
  const [detailsTab, setDetailsTab] = useState('overview'); // 'overview', 'eligibility', 'statistics'
  const [eligibleStudents, setEligibleStudents] = useState([]);
  const [loadingEligibility, setLoadingEligibility] = useState(false);
  const [studentEligibilityFilter, setStudentEligibilityFilter] = useState('ALL'); // 'ALL', 'ELIGIBLE', 'INELIGIBLE'
  const [studentSearch, setStudentSearch] = useState('');

  // Fetch all companies & summary metrics
  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [compRes, sumRes] = await Promise.all([
        api.get('/companies'),
        api.get('/companies/summary')
      ]);
      setCompanies(compRes.data);
      setSummary(sumRes.data);
    } catch (err) {
      console.error("Failed to load companies:", err);
      setError(err.response?.data?.message || "Failed to load companies. Please ensure the backend service is running.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Fetch eligible students when viewing details tab changes to 'eligibility'
  useEffect(() => {
    if (viewingCompany && detailsTab === 'eligibility') {
      const fetchEligibility = async () => {
        try {
          setLoadingEligibility(true);
          const res = await api.get(`/companies/${viewingCompany.id}/eligible-students`);
          setEligibleStudents(res.data);
        } catch (err) {
          console.error("Failed to load eligible students:", err);
        } finally {
          setLoadingEligibility(false);
        }
      };
      fetchEligibility();
    }
  }, [viewingCompany, detailsTab]);

  // Unique industries & locations for filter dropdowns
  const uniqueIndustries = useMemo(() => {
    const set = new Set(companies.map(c => c.industry).filter(Boolean));
    return Array.from(set).sort();
  }, [companies]);

  const uniqueLocations = useMemo(() => {
    const set = new Set(companies.map(c => c.location).filter(Boolean));
    return Array.from(set).sort();
  }, [companies]);

  // Filtered & Sorted companies
  const filteredCompanies = useMemo(() => {
    return companies.filter(c => {
      const matchesSearch = 
        !searchQuery ||
        c.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        c.jobRole?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        c.industry?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        c.location?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        c.requiredSkills?.toLowerCase().includes(searchQuery.toLowerCase());

      const matchesIndustry = industryFilter === 'ALL' || c.industry === industryFilter;
      const matchesLocation = locationFilter === 'ALL' || c.location === locationFilter;
      const matchesStatus = statusFilter === 'ALL' || (c.hiringStatus || 'ACTIVE').toUpperCase() === statusFilter;

      return matchesSearch && matchesIndustry && matchesLocation && matchesStatus;
    }).sort((a, b) => {
      if (sortBy === 'name-asc') return (a.name || '').localeCompare(b.name || '');
      if (sortBy === 'name-desc') return (b.name || '').localeCompare(a.name || '');
      if (sortBy === 'openings-desc') return (b.openings || 0) - (a.openings || 0);
      if (sortBy === 'cgpa-asc') return (a.minCgpa || 0) - (b.minCgpa || 0);
      if (sortBy === 'date-asc') {
        if (!a.driveDate) return 1;
        if (!b.driveDate) return -1;
        return new Date(a.driveDate) - new Date(b.driveDate);
      }
      return 0;
    });
  }, [companies, searchQuery, industryFilter, locationFilter, statusFilter, sortBy]);

  const showToast = (message) => {
    setSuccessToast(message);
    setTimeout(() => setSuccessToast(null), 4000);
  };

  const handleDelete = async () => {
    if (!deletingCompany) return;
    try {
      await api.delete(`/companies/${deletingCompany.id}`);
      showToast(`Company "${deletingCompany.name}" has been removed.`);
      setDeletingCompany(null);
      fetchData();
    } catch (err) {
      console.error("Failed to delete company:", err);
      alert(err.response?.data?.message || "Failed to delete company.");
    }
  };

  const getStatusBadge = (status) => {
    const s = (status || 'ACTIVE').toUpperCase();
    if (s === 'ACTIVE') {
      return <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-800 dark:bg-emerald-950/40 dark:text-emerald-400 border border-emerald-300 dark:border-emerald-800"><span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>Active Hiring</span>;
    }
    if (s === 'UPCOMING') {
      return <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-800 dark:bg-blue-950/40 dark:text-blue-400 border border-blue-300 dark:border-blue-800"><Clock size={12} />Drive Announced</span>;
    }
    return <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-400 border border-slate-300 dark:border-slate-700">Closed</span>;
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto pb-12">
      {/* Toast Notification */}
      {successToast && (
        <div className="fixed top-5 right-5 z-50 flex items-center gap-2 bg-emerald-600 text-white px-4 py-3 rounded-lg shadow-xl border border-emerald-500 animate-in fade-in slide-in-from-top-4">
          <CheckCircle2 size={20} />
          <span className="font-medium text-sm">{successToast}</span>
        </div>
      )}

      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 dark:text-white flex items-center gap-3 tracking-tight">
            <div className="p-2.5 bg-primary/10 dark:bg-primary/20 rounded-xl text-primary">
              <Building size={28} />
            </div>
            Companies
          </h1>
          <p className="text-slate-500 dark:text-slate-400 mt-1 text-sm md:text-base">
            Manage recruiting companies, hiring criteria and placement drives.
          </p>
        </div>

        {canManage && (
          <Button 
            onClick={() => { setEditingCompany(null); setIsAddModalOpen(true); }} 
            className="flex items-center gap-2 shadow-lg shadow-primary/20 hover:shadow-primary/30"
          >
            <Plus size={18} /> Add Company
          </Button>
        )}
      </div>

      {/* Metric Summary Cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 md:gap-5">
        <Card className="border border-slate-200/80 dark:border-slate-800 shadow-sm hover:shadow transition-shadow bg-gradient-to-br from-white to-slate-50 dark:from-navy-dark dark:to-navy">
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded-xl">
              <Building size={24} />
            </div>
            <div>
              <p className="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Total Companies</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-0.5">
                {summary ? summary.totalCompanies : companies.length}
              </h3>
            </div>
          </CardContent>
        </Card>

        <Card className="border border-slate-200/80 dark:border-slate-800 shadow-sm hover:shadow transition-shadow bg-gradient-to-br from-white to-slate-50 dark:from-navy-dark dark:to-navy">
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 bg-emerald-100 dark:bg-emerald-900/30 text-emerald-600 dark:text-emerald-400 rounded-xl">
              <Sparkles size={24} />
            </div>
            <div>
              <p className="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Active Hiring</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-0.5">
                {summary ? summary.activeHiringCompanies : companies.filter(c => (c.hiringStatus || 'ACTIVE') === 'ACTIVE').length}
              </h3>
            </div>
          </CardContent>
        </Card>

        <Card className="border border-slate-200/80 dark:border-slate-800 shadow-sm hover:shadow transition-shadow bg-gradient-to-br from-white to-slate-50 dark:from-navy-dark dark:to-navy">
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 bg-indigo-100 dark:bg-indigo-900/30 text-indigo-600 dark:text-indigo-400 rounded-xl">
              <Users size={24} />
            </div>
            <div>
              <p className="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Total Openings</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-0.5">
                {summary ? summary.totalOpenPositions : companies.reduce((acc, c) => acc + (c.openings || 0), 0)}
              </h3>
            </div>
          </CardContent>
        </Card>

        <Card className="border border-slate-200/80 dark:border-slate-800 shadow-sm hover:shadow transition-shadow bg-gradient-to-br from-white to-slate-50 dark:from-navy-dark dark:to-navy">
          <CardContent className="p-5 flex items-center gap-4">
            <div className="p-3 bg-purple-100 dark:bg-purple-900/30 text-purple-600 dark:text-purple-400 rounded-xl">
              <Award size={24} />
            </div>
            <div>
              <p className="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Average Package</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-0.5">
                {summary ? summary.averagePackage : "8.5 LPA"}
              </h3>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filter and Search Bar */}
      <Card className="border border-slate-200 dark:border-slate-800 shadow-sm">
        <CardContent className="p-4 space-y-3">
          <div className="flex flex-col md:flex-row gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input
                type="text"
                placeholder="Search by company name, job role, industry, location, required skills..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-900 dark:text-white focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition"
              />
              {searchQuery && (
                <button 
                  onClick={() => setSearchQuery('')}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                >
                  <X size={16} />
                </button>
              )}
            </div>

            <div className="flex items-center gap-2">
              <select
                value={industryFilter}
                onChange={(e) => setIndustryFilter(e.target.value)}
                className="px-3 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-700 dark:text-slate-300 outline-none"
              >
                <option value="ALL">All Industries</option>
                {uniqueIndustries.map(ind => (
                  <option key={ind} value={ind}>{ind}</option>
                ))}
              </select>

              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-3 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-700 dark:text-slate-300 outline-none"
              >
                <option value="ALL">All Status</option>
                <option value="ACTIVE">Active Hiring</option>
                <option value="UPCOMING">Drive Announced</option>
                <option value="CLOSED">Closed</option>
              </select>

              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="px-3 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-700 dark:text-slate-300 outline-none"
              >
                <option value="name-asc">Sort: Name (A-Z)</option>
                <option value="name-desc">Sort: Name (Z-A)</option>
                <option value="openings-desc">Sort: Openings (Highest)</option>
                <option value="cgpa-asc">Sort: Min CGPA (Lowest)</option>
                <option value="date-asc">Sort: Drive Date (Soonest)</option>
              </select>

              <div className="hidden sm:flex border border-slate-300 dark:border-slate-700 rounded-lg overflow-hidden">
                <button
                  onClick={() => setViewMode('grid')}
                  className={`p-2 transition-colors ${viewMode === 'grid' ? 'bg-primary text-white' : 'text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800'}`}
                  title="Grid View"
                >
                  <LayoutGrid size={18} />
                </button>
                <button
                  onClick={() => setViewMode('table')}
                  className={`p-2 transition-colors ${viewMode === 'table' ? 'bg-primary text-white' : 'text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800'}`}
                  title="Table View"
                >
                  <ListFilter size={18} />
                </button>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Loading State */}
      {loading && (
        <div className="flex flex-col items-center justify-center py-20">
          <Loader2 className="w-10 h-10 animate-spin text-primary mb-3" />
          <p className="text-slate-500 dark:text-slate-400 font-medium">Loading recruiting companies...</p>
        </div>
      )}

      {/* Error State */}
      {error && !loading && (
        <div className="p-6 bg-red-50 dark:bg-red-950/20 border border-red-200 dark:border-red-800/40 rounded-xl text-center space-y-3">
          <AlertTriangle className="w-10 h-10 text-red-500 mx-auto" />
          <h3 className="text-lg font-bold text-red-700 dark:text-red-400">Failed to load companies</h3>
          <p className="text-sm text-red-600 dark:text-red-300">{error}</p>
          <Button onClick={fetchData} variant="outline" size="sm">Retry</Button>
        </div>
      )}

      {/* Empty State */}
      {!loading && !error && filteredCompanies.length === 0 && (
        <Card className="border-dashed border-2 border-slate-300 dark:border-slate-700 p-12 text-center">
          <div className="w-16 h-16 bg-slate-100 dark:bg-slate-800 text-slate-400 rounded-2xl flex items-center justify-center mx-auto mb-4">
            <Building size={32} />
          </div>
          <h3 className="text-lg font-bold text-slate-800 dark:text-white">No companies found</h3>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1 max-w-md mx-auto">
            {searchQuery || industryFilter !== 'ALL' || statusFilter !== 'ALL'
              ? "No companies match your current search or filter criteria. Try adjusting your search."
              : "No recruiting companies have been registered yet. Add your first hiring company to begin."}
          </p>
          {canManage && (
            <Button onClick={() => setIsAddModalOpen(true)} className="mt-5" size="sm">
              <Plus size={16} className="mr-1.5" /> Add Company
            </Button>
          )}
        </Card>
      )}

      {/* Companies List - Grid View */}
      {!loading && !error && viewMode === 'grid' && filteredCompanies.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCompanies.map((company) => (
            <Card 
              key={company.id}
              className="border border-slate-200/90 dark:border-slate-800/80 shadow-sm hover:shadow-md hover:border-primary/40 dark:hover:border-primary/40 transition-all flex flex-col justify-between overflow-hidden group"
            >
              <div>
                {/* Card Top Banner */}
                <div className="p-5 pb-4 border-b border-slate-100 dark:border-slate-800/60">
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex items-center gap-3.5">
                      <div className="w-12 h-12 rounded-xl bg-gradient-to-tr from-primary/80 to-primary-dark text-white font-bold text-lg flex items-center justify-center shadow-md shadow-primary/20 shrink-0">
                        {company.name ? company.name.substring(0, 2).toUpperCase() : 'CO'}
                      </div>
                      <div>
                        <h3 className="font-bold text-slate-900 dark:text-white text-lg group-hover:text-primary transition-colors line-clamp-1">
                          {company.name}
                        </h3>
                        <p className="text-xs font-medium text-slate-500 dark:text-slate-400 flex items-center gap-1.5 mt-0.5">
                          <span>{company.industry || 'Technology'}</span>
                          {company.location && (
                            <>
                              <span>•</span>
                              <span className="flex items-center gap-0.5 truncate max-w-[120px]">
                                <MapPin size={11} /> {company.location}
                              </span>
                            </>
                          )}
                        </p>
                      </div>
                    </div>
                    <div>
                      {getStatusBadge(company.hiringStatus)}
                    </div>
                  </div>
                </div>

                {/* Card Body */}
                <div className="p-5 space-y-4">
                  {/* Job Role & Package */}
                  <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-slate-800/50 border border-slate-100 dark:border-slate-800">
                    <div>
                      <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider">Role</span>
                      <p className="text-sm font-bold text-slate-900 dark:text-white mt-0.5">{company.jobRole || 'Software Engineer'}</p>
                    </div>
                    <div className="text-right">
                      <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider">CTC Offered</span>
                      <p className="text-sm font-bold text-emerald-600 dark:text-emerald-400 mt-0.5">{company.packageCtc || 'Competitive'}</p>
                    </div>
                  </div>

                  {/* Criteria Badges */}
                  <div className="grid grid-cols-2 gap-2 text-xs">
                    <div className="p-2 rounded-lg bg-slate-50/70 dark:bg-slate-900/40 border border-slate-100 dark:border-slate-800">
                      <span className="text-slate-400 block text-[10px] uppercase font-semibold">Min CGPA</span>
                      <span className="font-bold text-slate-800 dark:text-slate-200">
                        {company.minCgpa ? `${company.minCgpa} / 10` : 'No CGPA Cutoff'}
                      </span>
                    </div>
                    <div className="p-2 rounded-lg bg-slate-50/70 dark:bg-slate-900/40 border border-slate-100 dark:border-slate-800">
                      <span className="text-slate-400 block text-[10px] uppercase font-semibold">Max Backlogs</span>
                      <span className="font-bold text-slate-800 dark:text-slate-200">
                        {company.maxBacklogs !== null && company.maxBacklogs !== undefined ? company.maxBacklogs : 'No limit'}
                      </span>
                    </div>
                  </div>

                  {/* Required Skills */}
                  {company.requiredSkills && (
                    <div>
                      <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block mb-1.5">Required Skills</span>
                      <div className="flex flex-wrap gap-1.5">
                        {company.requiredSkills.split(/[,;\n]+/).slice(0, 4).map((skill, idx) => (
                          <span 
                            key={idx} 
                            className="px-2 py-0.5 text-xs rounded-md bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300 font-medium border border-slate-200/60 dark:border-slate-700/60"
                          >
                            {skill.trim()}
                          </span>
                        ))}
                        {company.requiredSkills.split(/[,;\n]+/).length > 4 && (
                          <span className="px-1.5 py-0.5 text-[11px] text-slate-400 font-medium">
                            +{company.requiredSkills.split(/[,;\n]+/).length - 4} more
                          </span>
                        )}
                      </div>
                    </div>
                  )}

                  {/* Drive Date / Openings */}
                  <div className="pt-2 border-t border-slate-100 dark:border-slate-800/60 flex items-center justify-between text-xs text-slate-500 dark:text-slate-400">
                    <span className="flex items-center gap-1.5">
                      <Calendar size={13} className="text-primary" />
                      {company.driveDate ? `Drive: ${company.driveDate}` : 'Date TBA'}
                    </span>
                    <span className="font-semibold text-slate-700 dark:text-slate-300">
                      {company.openings ? `${company.openings} Positions` : 'Multiple Openings'}
                    </span>
                  </div>
                </div>
              </div>

              {/* Card Footer Actions */}
              <div className="p-4 bg-slate-50/70 dark:bg-slate-900/40 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between gap-2">
                <Button 
                  onClick={() => { setViewingCompany(company); setDetailsTab('overview'); }}
                  variant="outline" 
                  size="sm"
                  className="flex-1 flex items-center justify-center gap-1.5 text-xs font-semibold"
                >
                  <Eye size={14} /> View Details
                </Button>

                {canManage && (
                  <div className="flex items-center gap-1.5">
                    <button 
                      onClick={() => { setEditingCompany(company); setIsAddModalOpen(true); }}
                      className="p-2 text-slate-600 hover:text-primary dark:text-slate-400 dark:hover:text-primary-light hover:bg-slate-200/50 dark:hover:bg-slate-800 rounded-lg transition-colors"
                      title="Edit Company"
                    >
                      <Edit3 size={16} />
                    </button>
                    {isAdmin && (
                      <button 
                        onClick={() => setDeletingCompany(company)}
                        className="p-2 text-slate-400 hover:text-red-600 dark:hover:text-red-400 hover:bg-red-50 dark:hover:bg-red-950/30 rounded-lg transition-colors"
                        title="Delete Company"
                      >
                        <Trash2 size={16} />
                      </button>
                    )}
                  </div>
                )}
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Companies List - Table View */}
      {!loading && !error && viewMode === 'table' && filteredCompanies.length > 0 && (
        <Card className="border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm text-slate-600 dark:text-slate-300">
              <thead className="bg-slate-50 dark:bg-slate-900/60 text-xs uppercase tracking-wider text-slate-400 font-semibold border-b border-slate-200 dark:border-slate-800">
                <tr>
                  <th className="py-3.5 px-4">Company</th>
                  <th className="py-3.5 px-4">Job Role</th>
                  <th className="py-3.5 px-4">Package</th>
                  <th className="py-3.5 px-4">Criteria</th>
                  <th className="py-3.5 px-4">Openings</th>
                  <th className="py-3.5 px-4">Drive Date</th>
                  <th className="py-3.5 px-4">Status</th>
                  <th className="py-3.5 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 dark:divide-slate-800">
                {filteredCompanies.map(c => (
                  <tr key={c.id} className="hover:bg-slate-50/80 dark:hover:bg-slate-800/40 transition-colors">
                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 rounded-lg bg-primary/10 text-primary font-bold flex items-center justify-center shrink-0">
                          {c.name ? c.name.substring(0, 2).toUpperCase() : 'CO'}
                        </div>
                        <div>
                          <p className="font-bold text-slate-900 dark:text-white leading-tight">{c.name}</p>
                          <p className="text-xs text-slate-400 mt-0.5">{c.industry} • {c.location || 'India'}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-3.5 px-4 font-medium text-slate-800 dark:text-slate-200">{c.jobRole || 'Software Engineer'}</td>
                    <td className="py-3.5 px-4 font-bold text-emerald-600 dark:text-emerald-400">{c.packageCtc || 'Competitive'}</td>
                    <td className="py-3.5 px-4 text-xs">
                      <span className="font-semibold text-slate-700 dark:text-slate-300">CGPA: {c.minCgpa || 'None'}</span>
                      <span className="text-slate-400 block">Max Backlogs: {c.maxBacklogs ?? 'Any'}</span>
                    </td>
                    <td className="py-3.5 px-4 font-semibold text-slate-700 dark:text-slate-300">{c.openings || 'Multiple'}</td>
                    <td className="py-3.5 px-4 text-xs text-slate-500 dark:text-slate-400">{c.driveDate || 'TBA'}</td>
                    <td className="py-3.5 px-4">{getStatusBadge(c.hiringStatus)}</td>
                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        <Button 
                          onClick={() => { setViewingCompany(c); setDetailsTab('overview'); }}
                          variant="ghost" 
                          size="sm"
                          className="h-8 px-2"
                          title="View Details"
                        >
                          <Eye size={16} />
                        </Button>
                        {canManage && (
                          <Button 
                            onClick={() => { setEditingCompany(c); setIsAddModalOpen(true); }}
                            variant="ghost" 
                            size="sm"
                            className="h-8 px-2"
                            title="Edit Company"
                          >
                            <Edit3 size={16} />
                          </Button>
                        )}
                        {isAdmin && (
                          <Button 
                            onClick={() => setDeletingCompany(c)}
                            variant="ghost" 
                            size="sm"
                            className="h-8 px-2 text-red-500 hover:text-red-700 hover:bg-red-50 dark:hover:bg-red-950/20"
                            title="Delete Company"
                          >
                            <Trash2 size={16} />
                          </Button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      )}

      {/* ==================================================== */}
      {/* ADD / EDIT COMPANY MODAL                             */}
      {/* ==================================================== */}
      {isAddModalOpen && (
        <CompanyFormModal
          isOpen={isAddModalOpen}
          onClose={() => { setIsAddModalOpen(false); setEditingCompany(null); }}
          onSuccess={(msg) => {
            setIsAddModalOpen(false);
            setEditingCompany(null);
            fetchData();
            showToast(msg);
          }}
          initialData={editingCompany}
        />
      )}

      {/* ==================================================== */}
      {/* COMPANY DETAILS & SMART ELIGIBILITY MODAL            */}
      {/* ==================================================== */}
      {viewingCompany && (
        <CompanyDetailsModal
          company={viewingCompany}
          onClose={() => setViewingCompany(null)}
          activeTab={detailsTab}
          setActiveTab={setDetailsTab}
          eligibleStudents={eligibleStudents}
          loadingEligibility={loadingEligibility}
          studentEligibilityFilter={studentEligibilityFilter}
          setStudentEligibilityFilter={setStudentEligibilityFilter}
          studentSearch={studentSearch}
          setStudentSearch={setStudentSearch}
          onEdit={() => {
            const comp = viewingCompany;
            setViewingCompany(null);
            setEditingCompany(comp);
            setIsAddModalOpen(true);
          }}
          canManage={canManage}
        />
      )}

      {/* ==================================================== */}
      {/* DELETE CONFIRMATION MODAL                            */}
      {/* ==================================================== */}
      {deletingCompany && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/70 backdrop-blur-sm animate-in fade-in">
          <Card className="max-w-md w-full border-red-200 dark:border-red-900 shadow-2xl overflow-hidden">
            <CardHeader className="bg-red-50 dark:bg-red-950/30 border-b border-red-100 dark:border-red-900/40 p-5">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-red-100 dark:bg-red-900/50 text-red-600 dark:text-red-400 rounded-lg">
                  <AlertTriangle size={24} />
                </div>
                <div>
                  <CardTitle className="text-red-700 dark:text-red-400 text-lg">Delete Company</CardTitle>
                  <p className="text-xs text-red-600/80 dark:text-red-400/70">This action cannot be undone.</p>
                </div>
              </div>
            </CardHeader>
            <CardContent className="p-6 space-y-4">
              <p className="text-sm text-slate-600 dark:text-slate-300">
                Are you sure you want to delete <strong className="text-slate-900 dark:text-white">"{deletingCompany.name}"</strong>?
              </p>
              <p className="text-xs text-slate-500 dark:text-slate-400 bg-slate-50 dark:bg-slate-800/50 p-3 rounded-lg border border-slate-200 dark:border-slate-800">
                ⚠️ Deleting this company will also cancel and remove any scheduled campus drives and applications tied to it.
              </p>
              <div className="flex items-center justify-end gap-3 pt-2">
                <Button variant="outline" onClick={() => setDeletingCompany(null)}>Cancel</Button>
                <Button variant="danger" onClick={handleDelete} className="bg-red-600 hover:bg-red-700 text-white">
                  Confirm Delete
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
}

// =========================================================================
// ADD / EDIT COMPANY FORM MODAL
// =========================================================================
function CompanyFormModal({ isOpen, onClose, onSuccess, initialData }) {
  const isEdit = !!initialData;
  const [loading, setLoading] = useState(false);
  const [formError, setFormError] = useState(null);

  const [formData, setFormData] = useState({
    name: initialData?.name || '',
    industry: initialData?.industry || '',
    description: initialData?.description || '',
    website: initialData?.website || '',
    location: initialData?.location || '',
    hrName: initialData?.hrName || '',
    hrEmail: initialData?.hrEmail || '',
    hrPhone: initialData?.hrPhone || '',
    jobRole: initialData?.jobRole || '',
    packageCtc: initialData?.packageCtc || '',
    minCgpa: initialData?.minCgpa !== undefined && initialData?.minCgpa !== null ? initialData.minCgpa : '7.0',
    maxBacklogs: initialData?.maxBacklogs !== undefined && initialData?.maxBacklogs !== null ? initialData.maxBacklogs : '0',
    requiredSkills: initialData?.requiredSkills || '',
    requiredCertifications: initialData?.requiredCertifications || '',
    openings: initialData?.openings !== undefined && initialData?.openings !== null ? initialData.openings : '20',
    hiringStatus: initialData?.hiringStatus || 'ACTIVE',
    driveDate: initialData?.driveDate || '',
    registrationDeadline: initialData?.registrationDeadline || '',
    selectionProcess: initialData?.selectionProcess || 'Online Test -> Technical Interview -> HR Round'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const validate = () => {
    if (!formData.name.trim()) return "Company Name is required.";
    if (!formData.industry.trim()) return "Industry is required.";
    if (!formData.jobRole.trim()) return "Job Role is required.";
    if (!formData.packageCtc.trim()) return "Package/CTC is required.";

    if (formData.hrEmail && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.hrEmail)) {
      return "Please enter a valid HR Email address.";
    }

    if (formData.website && !formData.website.startsWith('http://') && !formData.website.startsWith('https://')) {
      return "Website URL must start with http:// or https://";
    }

    const cgpa = parseFloat(formData.minCgpa);
    if (isNaN(cgpa) || cgpa < 0 || cgpa > 10) {
      return "Minimum CGPA must be a valid number between 0.0 and 10.0.";
    }

    const openings = parseInt(formData.openings);
    if (isNaN(openings) || openings < 0) {
      return "Openings must be a positive number.";
    }

    if (formData.registrationDeadline && formData.driveDate) {
      if (new Date(formData.registrationDeadline) > new Date(formData.driveDate)) {
        return "Registration deadline cannot be after the drive date.";
      }
    }

    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errorMsg = validate();
    if (errorMsg) {
      setFormError(errorMsg);
      return;
    }

    try {
      setLoading(true);
      setFormError(null);

      const payload = {
        ...formData,
        minCgpa: formData.minCgpa ? parseFloat(formData.minCgpa) : null,
        maxBacklogs: formData.maxBacklogs !== '' ? parseInt(formData.maxBacklogs) : null,
        openings: formData.openings ? parseInt(formData.openings) : null,
        driveDate: formData.driveDate || null,
        registrationDeadline: formData.registrationDeadline || null
      };

      if (isEdit) {
        await api.put(`/companies/${initialData.id}`, payload);
        onSuccess(`Company "${formData.name}" updated successfully.`);
      } else {
        await api.post('/companies', payload);
        onSuccess(`Company "${formData.name}" created and drive announced.`);
      }
    } catch (err) {
      console.error("Save company error:", err);
      setFormError(err.response?.data?.message || "Failed to save company details. Please check the inputs.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/70 backdrop-blur-sm overflow-y-auto animate-in fade-in">
      <div className="max-w-3xl w-full my-8 bg-white dark:bg-navy-dark rounded-2xl shadow-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
        {/* Header */}
        <div className="p-6 border-b border-slate-100 dark:border-slate-800 flex items-center justify-between bg-slate-50/50 dark:bg-slate-900/30">
          <div className="flex items-center gap-3">
            <div className="p-2.5 bg-primary/10 text-primary rounded-xl">
              <Building size={22} />
            </div>
            <div>
              <h2 className="text-xl font-bold text-slate-900 dark:text-white">
                {isEdit ? `Edit Company: ${initialData.name}` : 'Add Recruiting Company'}
              </h2>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                Configure corporate profile, compensation package, and placement criteria.
              </p>
            </div>
          </div>
          <button 
            onClick={onClose}
            className="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6 max-h-[75vh] overflow-y-auto">
          {formError && (
            <div className="p-4 bg-red-50 dark:bg-red-950/30 border border-red-200 dark:border-red-900/40 rounded-xl text-red-700 dark:text-red-400 text-sm flex items-center gap-2.5">
              <AlertTriangle size={18} className="shrink-0" />
              <span>{formError}</span>
            </div>
          )}

          {/* Section 1: Company Profile */}
          <div className="space-y-4">
            <h4 className="text-xs font-bold uppercase tracking-wider text-primary border-b border-primary/20 pb-1.5 flex items-center gap-2">
              <Briefcase size={14} /> 1. Company Overview
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Company Name *</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Zoho Corporation, Google, TCS"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Industry Sector *</label>
                <input
                  type="text"
                  name="industry"
                  value={formData.industry}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Software Product, IT Services, Cloud & AI"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Headquarters / Location</label>
                <input
                  type="text"
                  name="location"
                  value={formData.location}
                  onChange={handleChange}
                  placeholder="e.g. Chennai, Bengaluru, Hybrid"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Corporate Website</label>
                <input
                  type="url"
                  name="website"
                  value={formData.website}
                  onChange={handleChange}
                  placeholder="https://www.company.com"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Company Description</label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows={2}
                placeholder="Brief overview of company mission, domain, and work culture..."
                className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary resize-none"
              />
            </div>
          </div>

          {/* Section 2: Recruiter Contact */}
          <div className="space-y-4 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-primary border-b border-primary/20 pb-1.5 flex items-center gap-2">
              <Mail size={14} /> 2. Recruiter & HR Contact
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">HR / POC Name</label>
                <input
                  type="text"
                  name="hrName"
                  value={formData.hrName}
                  onChange={handleChange}
                  placeholder="e.g. Suresh Kumar"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">HR Email</label>
                <input
                  type="email"
                  name="hrEmail"
                  value={formData.hrEmail}
                  onChange={handleChange}
                  placeholder="recruiter@company.com"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">HR Phone</label>
                <input
                  type="text"
                  name="hrPhone"
                  value={formData.hrPhone}
                  onChange={handleChange}
                  placeholder="+91 98401 23456"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>
          </div>

          {/* Section 3: Job Role & Package */}
          <div className="space-y-4 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-primary border-b border-primary/20 pb-1.5 flex items-center gap-2">
              <DollarSign size={14} /> 3. Role & Compensation
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="md:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Job Role *</label>
                <input
                  type="text"
                  name="jobRole"
                  value={formData.jobRole}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Software Development Engineer (SDE-1)"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Package / CTC *</label>
                <input
                  type="text"
                  name="packageCtc"
                  value={formData.packageCtc}
                  onChange={handleChange}
                  required
                  placeholder="e.g. 9.5 LPA"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Open Positions</label>
                <input
                  type="number"
                  name="openings"
                  value={formData.openings}
                  onChange={handleChange}
                  min="0"
                  placeholder="e.g. 25"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>
          </div>

          {/* Section 4: Eligibility Criteria */}
          <div className="space-y-4 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-primary border-b border-primary/20 pb-1.5 flex items-center gap-2">
              <GraduationCap size={14} /> 4. Smart Eligibility Criteria
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Min CGPA Required (0 - 10)</label>
                <input
                  type="number"
                  step="0.1"
                  min="0"
                  max="10"
                  name="minCgpa"
                  value={formData.minCgpa}
                  onChange={handleChange}
                  placeholder="e.g. 7.5"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Max Allowed Active Backlogs</label>
                <input
                  type="number"
                  min="0"
                  name="maxBacklogs"
                  value={formData.maxBacklogs}
                  onChange={handleChange}
                  placeholder="e.g. 0"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Hiring Status</label>
                <select
                  name="hiringStatus"
                  value={formData.hiringStatus}
                  onChange={handleChange}
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="ACTIVE">Active Hiring</option>
                  <option value="UPCOMING">Upcoming Drive</option>
                  <option value="CLOSED">Closed</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Required Skills (Comma-separated)</label>
                <textarea
                  name="requiredSkills"
                  value={formData.requiredSkills}
                  onChange={handleChange}
                  rows={2}
                  placeholder="e.g. Java, Spring Boot, React, SQL, Git"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary resize-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Required Certifications (Optional)</label>
                <textarea
                  name="requiredCertifications"
                  value={formData.requiredCertifications}
                  onChange={handleChange}
                  rows={2}
                  placeholder="e.g. AWS Certified, Oracle Java Associate"
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary resize-none"
                />
              </div>
            </div>
          </div>

          {/* Section 5: Drive Calendar Integration */}
          <div className="space-y-4 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-primary border-b border-primary/20 pb-1.5 flex items-center gap-2">
              <Calendar size={14} /> 5. Drive Calendar & Selection Process
            </h4>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Placement Drive Date</label>
                <input
                  type="date"
                  name="driveDate"
                  value={formData.driveDate}
                  onChange={handleChange}
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Registration Deadline</label>
                <input
                  type="date"
                  name="registrationDeadline"
                  value={formData.registrationDeadline}
                  onChange={handleChange}
                  className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Selection Process Flow</label>
              <input
                type="text"
                name="selectionProcess"
                value={formData.selectionProcess}
                onChange={handleChange}
                placeholder="e.g. Online Assessment -> Technical Coding Round -> Managerial Round -> HR"
                className="w-full px-3.5 py-2 text-sm rounded-lg border border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
          </div>

          {/* Footer Actions */}
          <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-100 dark:border-slate-800">
            <Button type="button" variant="outline" onClick={onClose} disabled={loading}>
              Cancel
            </Button>
            <Button type="submit" disabled={loading} className="flex items-center gap-2">
              {loading && <Loader2 size={16} className="animate-spin" />}
              {isEdit ? 'Save Changes' : 'Create Company & Announce Drive'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

// =========================================================================
// COMPANY DETAILS & SMART ELIGIBILITY MODAL
// =========================================================================
function CompanyDetailsModal({ 
  company, 
  onClose, 
  activeTab, 
  setActiveTab, 
  eligibleStudents, 
  loadingEligibility,
  studentEligibilityFilter,
  setStudentEligibilityFilter,
  studentSearch,
  setStudentSearch,
  onEdit,
  canManage 
}) {
  const eligibleCount = eligibleStudents.filter(s => s.eligible).length;
  const inEligibleCount = eligibleStudents.filter(s => !s.eligible).length;

  const filteredStudents = eligibleStudents.filter(s => {
    const matchesFilter = 
      studentEligibilityFilter === 'ALL' ||
      (studentEligibilityFilter === 'ELIGIBLE' && s.eligible) ||
      (studentEligibilityFilter === 'INELIGIBLE' && !s.eligible);

    const matchesSearch = 
      !studentSearch ||
      s.name?.toLowerCase().includes(studentSearch.toLowerCase()) ||
      s.registerNumber?.toLowerCase().includes(studentSearch.toLowerCase()) ||
      s.department?.toLowerCase().includes(studentSearch.toLowerCase());

    return matchesFilter && matchesSearch;
  });

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/70 backdrop-blur-sm overflow-y-auto animate-in fade-in">
      <div className="max-w-5xl w-full my-8 bg-white dark:bg-navy-dark rounded-2xl shadow-2xl border border-slate-200 dark:border-slate-800 overflow-hidden flex flex-col max-h-[85vh]">
        {/* Header */}
        <div className="p-6 border-b border-slate-100 dark:border-slate-800 bg-slate-50/70 dark:bg-slate-900/50 flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
          <div className="flex items-center gap-4">
            <div className="w-14 h-14 rounded-2xl bg-gradient-to-tr from-primary to-primary-dark text-white font-extrabold text-xl flex items-center justify-center shadow-lg shadow-primary/20 shrink-0">
              {company.name ? company.name.substring(0, 2).toUpperCase() : 'CO'}
            </div>
            <div>
              <div className="flex items-center gap-3">
                <h2 className="text-2xl font-bold text-slate-900 dark:text-white">{company.name}</h2>
                {company.website && (
                  <a 
                    href={company.website} 
                    target="_blank" 
                    rel="noreferrer" 
                    className="text-primary hover:text-primary-dark transition-colors inline-flex items-center gap-1 text-xs"
                  >
                    <ExternalLink size={14} /> Website
                  </a>
                )}
              </div>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-1 flex items-center gap-2">
                <span>{company.industry}</span>
                <span>•</span>
                <span>{company.location || 'Location Not Specified'}</span>
                <span>•</span>
                <span className="font-semibold text-emerald-600 dark:text-emerald-400">{company.packageCtc}</span>
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            {canManage && (
              <Button onClick={onEdit} variant="outline" size="sm" className="flex items-center gap-1.5">
                <Edit3 size={14} /> Edit
              </Button>
            )}
            <button 
              onClick={onClose}
              className="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-lg hover:bg-slate-200/50 dark:hover:bg-slate-800 transition-colors"
            >
              <X size={20} />
            </button>
          </div>
        </div>

        {/* Tab Navigation */}
        <div className="px-6 border-b border-slate-100 dark:border-slate-800 flex gap-4 bg-white dark:bg-navy-dark shrink-0">
          <button
            onClick={() => setActiveTab('overview')}
            className={`py-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
              activeTab === 'overview'
                ? 'border-primary text-primary'
                : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200'
            }`}
          >
            <Briefcase size={16} /> Overview & Criteria
          </button>
          <button
            onClick={() => setActiveTab('eligibility')}
            className={`py-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
              activeTab === 'eligibility'
                ? 'border-primary text-primary'
                : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200'
            }`}
          >
            <GraduationCap size={16} /> Smart Eligibility Engine
            {eligibleStudents.length > 0 && (
              <span className="px-2 py-0.5 rounded-full text-xs bg-primary/10 text-primary">
                {eligibleCount} Eligible
              </span>
            )}
          </button>
          <button
            onClick={() => setActiveTab('statistics')}
            className={`py-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
              activeTab === 'statistics'
                ? 'border-primary text-primary'
                : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200'
            }`}
          >
            <Award size={16} /> Placement Statistics
          </button>
        </div>

        {/* Modal Scrollable Content */}
        <div className="p-6 overflow-y-auto flex-1 space-y-6">
          {/* TAB 1: OVERVIEW */}
          {activeTab === 'overview' && (
            <div className="space-y-6">
              {/* Description */}
              <div>
                <h4 className="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-2">
                  About the Company
                </h4>
                <p className="text-sm text-slate-600 dark:text-slate-300 leading-relaxed bg-slate-50 dark:bg-slate-900/40 p-4 rounded-xl border border-slate-100 dark:border-slate-800">
                  {company.description || "Leading industry employer actively seeking campus talent with high technical aptitude and collaborative skills."}
                </p>
              </div>

              {/* Hiring Specifications */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Card className="border border-slate-200 dark:border-slate-800">
                  <CardContent className="p-4 space-y-3">
                    <h5 className="font-bold text-sm text-slate-900 dark:text-white flex items-center gap-2">
                      <GraduationCap className="text-primary" size={16} /> Academic Criteria
                    </h5>
                    <div className="space-y-2 text-xs">
                      <div className="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                        <span className="text-slate-500">Minimum CGPA:</span>
                        <span className="font-bold text-slate-800 dark:text-slate-200">{company.minCgpa ? `${company.minCgpa} / 10.0` : 'None'}</span>
                      </div>
                      <div className="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                        <span className="text-slate-500">Max Allowed Backlogs:</span>
                        <span className="font-bold text-slate-800 dark:text-slate-200">{company.maxBacklogs ?? 'No restriction'}</span>
                      </div>
                      <div className="flex justify-between py-1">
                        <span className="text-slate-500">Required Certifications:</span>
                        <span className="font-bold text-slate-800 dark:text-slate-200">{company.requiredCertifications || 'None specified'}</span>
                      </div>
                    </div>
                  </CardContent>
                </Card>

                <Card className="border border-slate-200 dark:border-slate-800">
                  <CardContent className="p-4 space-y-3">
                    <h5 className="font-bold text-sm text-slate-900 dark:text-white flex items-center gap-2">
                      <Calendar className="text-primary" size={16} /> Drive & Calendar Schedule
                    </h5>
                    <div className="space-y-2 text-xs">
                      <div className="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                        <span className="text-slate-500">Drive Date:</span>
                        <span className="font-bold text-slate-800 dark:text-slate-200">{company.driveDate || 'To be announced'}</span>
                      </div>
                      <div className="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                        <span className="text-slate-500">Registration Deadline:</span>
                        <span className="font-bold text-red-600 dark:text-red-400">{company.registrationDeadline || 'To be announced'}</span>
                      </div>
                      <div className="flex justify-between py-1">
                        <span className="text-slate-500">Positions Available:</span>
                        <span className="font-bold text-slate-800 dark:text-slate-200">{company.openings || 'Multiple'}</span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>

              {/* Required Skills */}
              <div>
                <h4 className="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-2">
                  Required Competencies & Skills
                </h4>
                <div className="flex flex-wrap gap-2">
                  {company.requiredSkills ? (
                    company.requiredSkills.split(/[,;\n]+/).map((s, idx) => (
                      <span key={idx} className="px-3 py-1.5 rounded-lg text-xs font-semibold bg-primary/10 text-primary border border-primary/20">
                        {s.trim()}
                      </span>
                    ))
                  ) : (
                    <span className="text-xs text-slate-400">No specific skills listed.</span>
                  )}
                </div>
              </div>

              {/* Selection Process */}
              <div>
                <h4 className="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-2">
                  Selection Process Timeline
                </h4>
                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/40 border border-slate-100 dark:border-slate-800">
                  <div className="flex flex-wrap items-center gap-3">
                    {(company.selectionProcess || 'Aptitude Test -> Technical Interview -> HR Round')
                      .split(/->|>/)
                      .map((step, idx, arr) => (
                        <div key={idx} className="flex items-center gap-2">
                          <span className="px-3 py-1 rounded-lg text-xs font-semibold bg-white dark:bg-navy text-slate-800 dark:text-slate-200 shadow-sm border border-slate-200 dark:border-slate-700">
                            {idx + 1}. {step.trim()}
                          </span>
                          {idx < arr.length - 1 && (
                            <span className="text-slate-400">→</span>
                          )}
                        </div>
                      ))}
                  </div>
                </div>
              </div>

              {/* HR / Contact details */}
              {(company.hrName || company.hrEmail || company.hrPhone) && (
                <div className="p-4 rounded-xl bg-blue-50/60 dark:bg-blue-950/20 border border-blue-200 dark:border-blue-800/40 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3">
                  <div>
                    <span className="text-[11px] font-bold uppercase tracking-wider text-blue-600 dark:text-blue-400">Campus Recruiter Contact</span>
                    <p className="font-bold text-sm text-slate-900 dark:text-white mt-0.5">{company.hrName || 'Corporate Relations Desk'}</p>
                  </div>
                  <div className="flex flex-wrap items-center gap-4 text-xs text-slate-600 dark:text-slate-300">
                    {company.hrEmail && (
                      <span className="flex items-center gap-1">
                        <Mail size={14} className="text-blue-500" /> {company.hrEmail}
                      </span>
                    )}
                    {company.hrPhone && (
                      <span className="flex items-center gap-1">
                        <Phone size={14} className="text-blue-500" /> {company.hrPhone}
                      </span>
                    )}
                  </div>
                </div>
              )}
            </div>
          )}

          {/* TAB 2: SMART ELIGIBILITY ENGINE */}
          {activeTab === 'eligibility' && (
            <div className="space-y-5">
              <div className="p-4 bg-gradient-to-r from-primary/10 via-primary/5 to-transparent rounded-xl border border-primary/20 flex flex-col md:flex-row justify-between items-start md:items-center gap-3">
                <div>
                  <h4 className="font-bold text-base text-slate-900 dark:text-white flex items-center gap-2">
                    <Sparkles size={18} className="text-primary" /> Deterministic Eligibility Engine
                  </h4>
                  <p className="text-xs text-slate-600 dark:text-slate-300 mt-1">
                    Every student is verified against CGPA (≥ {company.minCgpa || '0'}), backlogs (≤ {company.maxBacklogs ?? 'Any'}), required skills, and certifications.
                  </p>
                </div>
                <div className="flex items-center gap-2 shrink-0">
                  <span className="px-3 py-1.5 rounded-lg text-xs font-bold bg-emerald-100 text-emerald-800 dark:bg-emerald-950/50 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800">
                    ✓ {eligibleCount} Eligible
                  </span>
                  <span className="px-3 py-1.5 rounded-lg text-xs font-bold bg-red-100 text-red-800 dark:bg-red-950/50 dark:text-red-300 border border-red-300 dark:border-red-800">
                    ✕ {inEligibleCount} Non-Eligible
                  </span>
                </div>
              </div>

              {/* Filter controls */}
              <div className="flex flex-col sm:flex-row justify-between gap-3">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                  <input
                    type="text"
                    placeholder="Search candidate by name, register number or department..."
                    value={studentSearch}
                    onChange={(e) => setStudentSearch(e.target.value)}
                    className="w-full pl-9 pr-3 py-1.5 text-xs rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-navy-dark text-slate-900 dark:text-white outline-none"
                  />
                </div>

                <div className="flex items-center gap-1.5">
                  <button
                    onClick={() => setStudentEligibilityFilter('ALL')}
                    className={`px-3 py-1.5 text-xs rounded-lg font-semibold transition-colors ${
                      studentEligibilityFilter === 'ALL'
                        ? 'bg-slate-900 text-white dark:bg-white dark:text-slate-900'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300'
                    }`}
                  >
                    All ({eligibleStudents.length})
                  </button>
                  <button
                    onClick={() => setStudentEligibilityFilter('ELIGIBLE')}
                    className={`px-3 py-1.5 text-xs rounded-lg font-semibold transition-colors ${
                      studentEligibilityFilter === 'ELIGIBLE'
                        ? 'bg-emerald-600 text-white'
                        : 'bg-emerald-50 dark:bg-emerald-950/30 text-emerald-700 dark:text-emerald-400'
                    }`}
                  >
                    Eligible Only ({eligibleCount})
                  </button>
                  <button
                    onClick={() => setStudentEligibilityFilter('INELIGIBLE')}
                    className={`px-3 py-1.5 text-xs rounded-lg font-semibold transition-colors ${
                      studentEligibilityFilter === 'INELIGIBLE'
                        ? 'bg-red-600 text-white'
                        : 'bg-red-50 dark:bg-red-950/30 text-red-700 dark:text-red-400'
                    }`}
                  >
                    Non-Eligible ({inEligibleCount})
                  </button>
                </div>
              </div>

              {/* Students List */}
              {loadingEligibility ? (
                <div className="py-12 flex flex-col items-center justify-center">
                  <Loader2 size={28} className="animate-spin text-primary mb-2" />
                  <p className="text-xs text-slate-500">Checking criteria against student pool...</p>
                </div>
              ) : filteredStudents.length === 0 ? (
                <p className="text-xs text-slate-500 text-center py-8">No students match this filter.</p>
              ) : (
                <div className="space-y-3">
                  {filteredStudents.map(student => (
                    <div 
                      key={student.studentId}
                      className={`p-4 rounded-xl border transition-all ${
                        student.eligible 
                          ? 'bg-emerald-50/40 dark:bg-emerald-950/10 border-emerald-200 dark:border-emerald-900/40' 
                          : 'bg-slate-50/80 dark:bg-slate-900/40 border-slate-200 dark:border-slate-800'
                      }`}
                    >
                      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3">
                        <div>
                          <div className="flex items-center gap-2">
                            <span className="font-bold text-sm text-slate-900 dark:text-white">{student.name}</span>
                            <span className="text-xs text-slate-400">({student.registerNumber})</span>
                            <span className="text-xs px-2 py-0.5 rounded bg-slate-200/70 dark:bg-slate-800 text-slate-700 dark:text-slate-300">
                              {student.department}
                            </span>
                          </div>
                          <div className="flex items-center gap-3 text-xs text-slate-500 mt-1">
                            <span>CGPA: <strong className="text-slate-800 dark:text-slate-200">{student.cgpa}</strong></span>
                            <span>•</span>
                            <span>Backlogs: <strong className="text-slate-800 dark:text-slate-200">{student.backlogs}</strong></span>
                            <span>•</span>
                            <span>Match: <strong className="text-primary">{student.matchPercentage}%</strong></span>
                          </div>
                        </div>

                        <div>
                          {student.eligible ? (
                            <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-400 border border-emerald-300 dark:border-emerald-800">
                              <Check size={14} /> Eligible Candidate
                            </span>
                          ) : (
                            <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-red-100 text-red-800 dark:bg-red-950/60 dark:text-red-400 border border-red-300 dark:border-red-800">
                              <X size={14} /> Not Eligible
                            </span>
                          )}
                        </div>
                      </div>

                      {/* Deterministic Reasons for Non-Eligibility */}
                      {!student.eligible && student.reasons?.length > 0 && (
                        <div className="mt-3 pt-3 border-t border-red-100 dark:border-red-950/50 space-y-1">
                          <span className="text-[11px] font-bold text-red-600 dark:text-red-400 uppercase tracking-wider block">
                            Specific Ineligibility Reasons:
                          </span>
                          <ul className="space-y-1">
                            {student.reasons.map((reason, idx) => (
                              <li key={idx} className="text-xs text-red-700 dark:text-red-300 flex items-start gap-1.5">
                                <span className="text-red-500 font-bold">•</span>
                                <span>{reason}</span>
                              </li>
                            ))}
                          </ul>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* TAB 3: STATISTICS */}
          {activeTab === 'statistics' && (
            <div className="space-y-6">
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 text-center">
                  <span className="text-xs font-semibold text-slate-400 uppercase">Eligible Students</span>
                  <h4 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">
                    {company.eligibleStudentCount ?? eligibleCount}
                  </h4>
                </div>
                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 text-center">
                  <span className="text-xs font-semibold text-slate-400 uppercase">Applicants</span>
                  <h4 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">
                    {company.appliedCount ?? 0}
                  </h4>
                </div>
                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 text-center">
                  <span className="text-xs font-semibold text-slate-400 uppercase">Shortlisted</span>
                  <h4 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">
                    {company.shortlistedCount ?? 0}
                  </h4>
                </div>
                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 text-center">
                  <span className="text-xs font-semibold text-slate-400 uppercase">Offers Accepted</span>
                  <h4 className="text-2xl font-bold text-emerald-600 dark:text-emerald-400 mt-1">
                    {company.selectedCount ?? 0}
                  </h4>
                </div>
              </div>

              <div className="p-5 rounded-xl bg-slate-50 dark:bg-slate-800/40 border border-slate-100 dark:border-slate-800 space-y-3">
                <div className="flex justify-between items-center text-sm">
                  <span className="font-semibold text-slate-800 dark:text-slate-200">Selection Conversion Rate</span>
                  <span className="font-bold text-primary">{company.selectionPercentage || 0}%</span>
                </div>
                <div className="w-full h-3 bg-slate-200 dark:bg-slate-700 rounded-full overflow-hidden">
                  <div 
                    className="h-full bg-primary rounded-full transition-all duration-500"
                    style={{ width: `${Math.min(100, company.selectionPercentage || 0)}%` }}
                  />
                </div>
                <p className="text-xs text-slate-500">
                  Percentage of drive applicants that cleared technical interviews and received formal job offers.
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
