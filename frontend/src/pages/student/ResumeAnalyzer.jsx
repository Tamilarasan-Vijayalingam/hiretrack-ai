import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { FileText, UploadCloud, CheckCircle2, AlertTriangle, Loader2 } from 'lucide-react';
import api from '../../api/axios';

export default function ResumeAnalyzer() {
  const [resumeText, setResumeText] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const handleAnalyze = async () => {
    if (!resumeText.trim()) return;
    try {
      setLoading(true);
      setError(null);
      const res = await api.post('/ai/resume/analyze', resumeText, {
        headers: { 'Content-Type': 'text/plain' }
      });
      setResult(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to analyze resume. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6 max-w-5xl mx-auto">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
          <FileText className="text-primary" />
          AI Resume Analyzer
        </h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">
          Paste your resume text to receive an instant ATS score and improvement suggestions.
        </p>
      </div>

      {!result ? (
        <Card>
          <CardContent className="p-6 space-y-4">
            <div className="relative">
              <textarea
                value={resumeText}
                onChange={(e) => setResumeText(e.target.value)}
                placeholder="Paste your resume text here (experience, skills, education)..."
                className="w-full h-64 p-4 border rounded-md border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white focus:ring-2 focus:ring-primary focus:border-transparent outline-none resize-none"
              />
              {!resumeText && (
                <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none text-slate-400">
                  <UploadCloud className="w-12 h-12 mb-2 opacity-50" />
                  <p>Paste text to begin analysis</p>
                </div>
              )}
            </div>
            {error && <p className="text-red-500 text-sm">{error}</p>}
            <Button 
              onClick={handleAnalyze} 
              disabled={loading || !resumeText.trim()} 
              className="w-full flex items-center justify-center gap-2"
            >
              {loading && <Loader2 className="w-4 h-4 animate-spin" />}
              {loading ? 'Analyzing...' : 'Analyze Resume'}
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card className="bg-gradient-to-br from-indigo-500 to-indigo-600 text-white border-none shadow-lg transform transition-all hover:scale-105">
              <CardContent className="p-6">
                <p className="text-indigo-100 font-medium mb-2">Overall ATS Score</p>
                <div className="flex items-end gap-2">
                  <h2 className="text-5xl font-bold">{result.atsScore}</h2>
                  <span className="text-indigo-200 mb-1">/ 100</span>
                </div>
                <p className="mt-4 text-sm bg-indigo-700/30 inline-block px-3 py-1 rounded-full border border-indigo-400/30">
                  Quality: {result.qualityRating}
                </p>
              </CardContent>
            </Card>

            <Card className="bg-gradient-to-br from-slate-800 to-slate-900 text-white border-none shadow-lg transform transition-all hover:scale-105">
              <CardContent className="p-6">
                <p className="text-slate-400 font-medium mb-2">Keyword Match Ratio</p>
                <div className="flex items-end gap-2">
                  <h2 className="text-5xl font-bold">{(result.keywordMatchRatio * 100).toFixed(0)}%</h2>
                </div>
                <p className="mt-4 text-sm text-slate-400">
                  Compared against standard industry job descriptions.
                </p>
              </CardContent>
            </Card>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card>
              <CardHeader>
                <CardTitle className="text-emerald-600 flex items-center gap-2">
                  <CheckCircle2 size={20} /> Matched Keywords
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex flex-wrap gap-2">
                  {result.matchedKeywords?.map((kw, i) => (
                    <span key={i} className="px-3 py-1 bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 rounded-full text-sm">
                      {kw}
                    </span>
                  ))}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-red-500 flex items-center gap-2">
                  <AlertTriangle size={20} /> Missing Keywords
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex flex-wrap gap-2">
                  {result.missingKeywords?.map((kw, i) => (
                    <span key={i} className="px-3 py-1 bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400 rounded-full text-sm">
                      {kw}
                    </span>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>AI Improvement Suggestions</CardTitle>
            </CardHeader>
            <CardContent>
              <ul className="space-y-3">
                {result.improvementSuggestions?.map((suggestion, i) => (
                  <li key={i} className="flex gap-3 text-slate-700 dark:text-slate-300">
                    <span className="text-indigo-500 mt-1">•</span>
                    {suggestion}
                  </li>
                ))}
              </ul>
              <Button onClick={() => setResult(null)} variant="outline" className="mt-6 w-full">
                Analyze Another Resume
              </Button>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
}
