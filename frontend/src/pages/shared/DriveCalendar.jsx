import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { ChevronLeft, ChevronRight, Calendar as CalendarIcon, List, Clock, Loader2 } from 'lucide-react';
import { format, addMonths, subMonths, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isToday } from 'date-fns';
import api from '../../api/axios';

export default function DriveCalendar() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [view, setView] = useState('calendar'); // 'calendar', 'list', 'timeline'
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDrives = async () => {
      try {
        setLoading(true);
        const res = await api.get('/drives');
        const driveData = res.data;

        const calendarEvents = [];
        driveData.forEach((drive, index) => {
          const companyName = drive.title ? drive.title.replace(' Campus Recruitment', '').replace(' Campus Placement Drive', '') : 'Company';

          if (drive.driveDate) {
            calendarEvents.push({
              id: `${drive.id}-drive`,
              date: new Date(drive.driveDate),
              title: `${companyName} - Campus Placement Drive (${drive.packageOffered || ''})`,
              type: 'INTERVIEW',
              company: companyName,
              role: drive.jobRole
            });
          }

          if (drive.registrationDeadline) {
            calendarEvents.push({
              id: `${drive.id}-deadline`,
              date: new Date(drive.registrationDeadline),
              title: `${companyName} - Registration Deadline`,
              type: 'DEADLINE',
              company: companyName,
              role: drive.jobRole
            });
          }

          if (drive.testDate) {
            calendarEvents.push({
              id: `${drive.id}-test`,
              date: new Date(drive.testDate),
              title: `${companyName} - Online Assessment`,
              type: 'TEST',
              company: companyName,
              role: drive.jobRole
            });
          }
        });

        // Fallback default events if backend has no drives yet
        if (calendarEvents.length === 0) {
          calendarEvents.push(
            { id: 1, date: new Date(new Date().setDate(15)), title: 'Zoho - Coding Round', type: 'TEST', company: 'Zoho' },
            { id: 2, date: new Date(new Date().setDate(18)), title: 'Infosys - Final Interview', type: 'INTERVIEW', company: 'Infosys' },
            { id: 3, date: new Date(new Date().setDate(22)), title: 'TCS - Registration Deadline', type: 'DEADLINE', company: 'TCS' }
          );
        }

        setEvents(calendarEvents);
      } catch (err) {
        console.error("Failed to load drives for calendar:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchDrives();
  }, []);

  const days = eachDayOfInterval({
    start: startOfMonth(currentDate),
    end: endOfMonth(currentDate)
  });

  const nextMonth = () => setCurrentDate(addMonths(currentDate, 1));
  const prevMonth = () => setCurrentDate(subMonths(currentDate, 1));

  const getEventsForDay = (day) => {
    return events.filter(e => 
      e.date.getDate() === day.getDate() && 
      e.date.getMonth() === day.getMonth() &&
      e.date.getFullYear() === day.getFullYear()
    );
  };

  const getEventColor = (type) => {
    switch(type) {
      case 'TEST': return 'bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-300 border-blue-200 dark:border-blue-800';
      case 'INTERVIEW': return 'bg-purple-100 text-purple-700 dark:bg-purple-900/40 dark:text-purple-300 border-purple-200 dark:border-purple-800';
      case 'DEADLINE': return 'bg-red-100 text-red-700 dark:bg-red-900/40 dark:text-red-300 border-red-200 dark:border-red-800';
      default: return 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300 border-slate-200 dark:border-slate-700';
    }
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2.5">
            <CalendarIcon className="text-primary" /> Drive Calendar
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm mt-0.5">
            Real-time schedule of campus recruitment drives, tests, and registration deadlines.
          </p>
        </div>
        
        <div className="flex items-center gap-3">
          {loading && <Loader2 size={16} className="animate-spin text-primary" />}
          <div className="flex items-center bg-white dark:bg-navy-dark rounded-lg p-1 border border-slate-200 dark:border-slate-800 shadow-sm">
            <button onClick={() => setView('calendar')} className={`p-2 rounded-md transition-colors ${view === 'calendar' ? 'bg-slate-100 dark:bg-slate-800 text-primary' : 'text-slate-500 hover:text-slate-700'}`}>
              <CalendarIcon size={18} />
            </button>
            <button onClick={() => setView('list')} className={`p-2 rounded-md transition-colors ${view === 'list' ? 'bg-slate-100 dark:bg-slate-800 text-primary' : 'text-slate-500 hover:text-slate-700'}`}>
              <List size={18} />
            </button>
            <button onClick={() => setView('timeline')} className={`p-2 rounded-md transition-colors ${view === 'timeline' ? 'bg-slate-100 dark:bg-slate-800 text-primary' : 'text-slate-500 hover:text-slate-700'}`}>
              <Clock size={18} />
            </button>
          </div>
        </div>
      </div>

      <Card className="border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
        <CardHeader className="flex flex-row items-center justify-between border-b border-slate-100 dark:border-slate-800 p-4">
          <div className="flex items-center gap-4">
            <h2 className="text-lg font-bold text-slate-900 dark:text-white">
              {format(currentDate, 'MMMM yyyy')}
            </h2>
            <div className="hidden sm:flex items-center gap-2 text-xs">
              <span className="inline-flex items-center gap-1 text-purple-600 dark:text-purple-400 font-medium">
                <span className="w-2 h-2 rounded-full bg-purple-500"></span> Drive
              </span>
              <span className="inline-flex items-center gap-1 text-blue-600 dark:text-blue-400 font-medium">
                <span className="w-2 h-2 rounded-full bg-blue-500"></span> Test
              </span>
              <span className="inline-flex items-center gap-1 text-red-600 dark:text-red-400 font-medium">
                <span className="w-2 h-2 rounded-full bg-red-500"></span> Deadline
              </span>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Button variant="outline" size="sm" onClick={prevMonth}><ChevronLeft size={16} /></Button>
            <Button variant="outline" size="sm" onClick={() => setCurrentDate(new Date())}>Today</Button>
            <Button variant="outline" size="sm" onClick={nextMonth}><ChevronRight size={16} /></Button>
          </div>
        </CardHeader>
        <CardContent className="p-0">
          {view === 'calendar' && (
            <div className="grid grid-cols-7 border-l border-slate-100 dark:border-slate-800">
              {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(day => (
                <div key={day} className="py-2 text-center text-xs font-bold text-slate-500 border-r border-b border-slate-100 dark:border-slate-800 bg-slate-50 dark:bg-slate-900/30 uppercase tracking-wider">
                  {day}
                </div>
              ))}
              
              {/* Padding days for first week */}
              {Array.from({ length: startOfMonth(currentDate).getDay() }).map((_, i) => (
                <div key={`pad-${i}`} className="min-h-[110px] bg-slate-50/40 dark:bg-slate-900/40 border-r border-b border-slate-100 dark:border-slate-800 p-2" />
              ))}
              
              {days.map((day) => {
                const dayEvents = getEventsForDay(day);
                return (
                  <div 
                    key={day.toISOString()} 
                    className={`min-h-[110px] border-r border-b border-slate-100 dark:border-slate-800 p-2 transition-colors hover:bg-slate-50 dark:hover:bg-slate-800/40 ${!isSameMonth(day, currentDate) ? 'opacity-40 bg-slate-50/50 dark:bg-slate-900/50' : ''}`}
                  >
                    <div className="flex justify-between items-start">
                      <span className={`text-xs font-bold w-6 h-6 flex items-center justify-center rounded-full ${isToday(day) ? 'bg-primary text-white shadow-sm' : 'text-slate-700 dark:text-slate-300'}`}>
                        {format(day, 'd')}
                      </span>
                      {dayEvents.length > 0 && (
                        <span className="text-[10px] text-slate-400 font-medium">
                          {dayEvents.length} {dayEvents.length === 1 ? 'event' : 'events'}
                        </span>
                      )}
                    </div>
                    <div className="mt-1.5 space-y-1">
                      {dayEvents.map(event => (
                        <div key={event.id} title={event.title} className={`text-[11px] p-1 rounded font-medium border ${getEventColor(event.type)} truncate cursor-pointer hover:opacity-90 transition-opacity`}>
                          {event.title}
                        </div>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
          {view === 'list' && (
            <div className="p-6 divide-y divide-slate-100 dark:divide-slate-800">
              {events.map(event => (
                <div key={event.id} className="py-3 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <span className={`px-2.5 py-1 text-xs font-bold rounded-lg border ${getEventColor(event.type)}`}>
                      {event.type}
                    </span>
                    <div>
                      <h4 className="font-bold text-sm text-slate-900 dark:text-white">{event.title}</h4>
                      <p className="text-xs text-slate-500">{event.company} {event.role ? `• ${event.role}` : ''}</p>
                    </div>
                  </div>
                  <span className="text-xs font-semibold text-slate-600 dark:text-slate-400">
                    {format(event.date, 'dd MMM yyyy')}
                  </span>
                </div>
              ))}
            </div>
          )}
          {view === 'timeline' && (
            <div className="p-6 space-y-4">
              {events.map((event, idx) => (
                <div key={event.id} className="flex gap-4 items-start">
                  <div className="w-24 text-right text-xs font-bold text-slate-400 shrink-0 pt-0.5">
                    {format(event.date, 'MMM dd')}
                  </div>
                  <div className="w-3 h-3 rounded-full bg-primary mt-1 shrink-0 ring-4 ring-primary/10"></div>
                  <div className="flex-1 pb-4 border-b border-slate-100 dark:border-slate-800">
                    <h5 className="font-bold text-sm text-slate-900 dark:text-white">{event.title}</h5>
                    <p className="text-xs text-slate-500 mt-0.5">{event.company} • {event.type}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
