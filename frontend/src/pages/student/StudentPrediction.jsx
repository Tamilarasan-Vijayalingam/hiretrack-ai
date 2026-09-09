import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { BrainCircuit, Target, TrendingUp, AlertTriangle, CheckCircle2, Loader2 } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';

export default function StudentPrediction() {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [readiness, setReadiness] = useState(null);
  const [prediction, setPrediction] = useState(null);
  const [skillsGap, setSkillsGap] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!user?.id) return;
      try {
        setLoading(true);
        // Fetch all AI endpoints in parallel
        const [readinessRes, predictionRes, gapRes] = await Promise.all([
          api.get(`/ai/readiness/${user.id}`),
          api.get(`/ai/prediction/${user.id}`),
          api.get(`/ai/skills-gap/${user.id}?targetRole=Software Engineer`)
        ]);
        
        setReadiness(readinessRes.data);
        setPrediction(predictionRes.data);
        setSkillsGap(gapRes.data);
        setError(null);
      } catch (err) {
        console.error("Failed to load AI data:", err);
        setError("Unable to load AI predictions at this time. Please ensure the backend is running and the AI service is configured.");
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [user]);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-primary" />
        <p className="mt-4 text-slate-500">Generating AI Placement Insights...</p>
      </div>
    );
  }
  
  if (error) {
    return (
      <div className="bg-red-50 dark:bg-red-900/20 text-red-600 p-6 rounded-lg border border-red-200 text-center">
        <AlertTriangle className="w-10 h-10 mx-auto mb-2 text-red-500" />
        <h3 className="font-semibold text-lg">Analysis Failed</h3>
        <p className="mt-2">{error}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
          <BrainCircuit className="text-primary" />
          AI Placement Prediction
        </h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">
          Based on your academic performance, skills, and historical department data.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="bg-gradient-to-br from-blue-500 to-blue-600 border-none text-white shadow-lg transform transition-all hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-blue-100 font-medium">Placement Probability</p>
                <h2 className="text-4xl font-bold mt-2">{prediction?.placementProbability}%</h2>
              </div>
              <div className="p-3 bg-white/20 rounded-full">
                <TrendingUp size={24} />
              </div>
            </div>
            <p className="text-sm text-blue-100 mt-4">Confidence Score: {prediction?.confidenceScore}%</p>
          </CardContent>
        </Card>

        <Card className="bg-gradient-to-br from-indigo-500 to-purple-600 border-none text-white shadow-lg transform transition-all hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-indigo-100 font-medium">Predicted CTC Range</p>
                <h2 className="text-4xl font-bold mt-2">{prediction?.predictedSalaryRange}</h2>
              </div>
              <div className="p-3 bg-white/20 rounded-full">
                <Target size={24} />
              </div>
            </div>
            <p className="text-sm text-indigo-100 mt-4">Aligned with your current skill stack.</p>
          </CardContent>
        </Card>

        <Card className="bg-gradient-to-br from-emerald-500 to-emerald-600 border-none text-white shadow-lg transform transition-all hover:scale-105">
          <CardContent className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-emerald-100 font-medium">Readiness Score</p>
                <h2 className="text-4xl font-bold mt-2">{readiness?.overallScore}/100</h2>
              </div>
              <div className="p-3 bg-white/20 rounded-full">
                <CheckCircle2 size={24} />
              </div>
            </div>
            <p className="text-sm text-emerald-100 mt-4 line-clamp-2">{readiness?.feedback}</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Skills Gap Analysis</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div>
                  <h4 className="font-semibold text-emerald-600 dark:text-emerald-400 mb-2">Matching Skills</h4>
                  <div className="flex flex-wrap gap-2">
                    {skillsGap?.matchingSkills?.map((skill, index) => (
                      <span key={index} className="px-3 py-1 bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 rounded-full text-sm">
                        {skill}
                      </span>
                    ))}
                  </div>
                </div>
                
                <div className="pt-4 border-t border-slate-200 dark:border-slate-800">
                  <h4 className="font-semibold text-red-600 dark:text-red-400 mb-2">Missing Skills</h4>
                  <div className="flex flex-wrap gap-2">
                    {skillsGap?.missingSkills?.map((skill, index) => (
                      <span key={index} className="px-3 py-1 bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400 rounded-full text-sm">
                        {skill}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="pt-4 border-t border-slate-200 dark:border-slate-800">
                  <h4 className="font-semibold text-slate-700 dark:text-slate-300 mb-2">Learning Roadmap</h4>
                  <ul className="list-disc pl-5 space-y-1">
                    {skillsGap?.learningRoadmap?.map((step, index) => (
                      <li key={index} className="text-sm text-slate-600 dark:text-slate-400">{step}</li>
                    ))}
                  </ul>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>AI Recommendations</CardTitle>
            </CardHeader>
            <CardContent>
              <ul className="space-y-4">
                {prediction?.strengths?.map((strength, idx) => (
                  <li key={`s-${idx}`} className="flex gap-3">
                    <CheckCircle2 className="text-emerald-500 shrink-0 mt-0.5" size={18} />
                    <div>
                      <p className="text-sm font-medium text-slate-900 dark:text-white">Strength</p>
                      <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{strength}</p>
                    </div>
                  </li>
                ))}
                {prediction?.weaknesses?.map((weakness, idx) => (
                  <li key={`w-${idx}`} className="flex gap-3">
                    <AlertTriangle className="text-orange-500 shrink-0 mt-0.5" size={18} />
                    <div>
                      <p className="text-sm font-medium text-slate-900 dark:text-white">Area to Improve</p>
                      <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{weakness}</p>
                    </div>
                  </li>
                ))}
                {prediction?.improvementSuggestions?.map((suggestion, idx) => (
                  <li key={`i-${idx}`} className="flex gap-3">
                    <Target className="text-indigo-500 shrink-0 mt-0.5" size={18} />
                    <div>
                      <p className="text-sm font-medium text-slate-900 dark:text-white">Next Step</p>
                      <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{suggestion}</p>
                    </div>
                  </li>
                ))}
              </ul>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
