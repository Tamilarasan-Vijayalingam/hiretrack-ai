import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Video, Mic, CheckCircle2, AlertTriangle, Loader2, ArrowRight, BookOpen } from 'lucide-react';
import api from '../../api/axios';

export default function MockInterview() {
  const [topic, setTopic] = useState('Software Engineering');
  const [type, setType] = useState('TECHNICAL');
  const [question, setQuestion] = useState(null);
  const [answer, setAnswer] = useState('');
  const [feedback, setFeedback] = useState(null);
  
  const [loadingQuestion, setLoadingQuestion] = useState(false);
  const [loadingFeedback, setLoadingFeedback] = useState(false);
  const [error, setError] = useState(null);

  const topics = ['Software Engineering', 'Java Spring Boot', 'React Frontend', 'System Design', 'Database SQL', 'HR & Behavioral'];

  const handleGenerate = async () => {
    try {
      setLoadingQuestion(true);
      setError(null);
      setFeedback(null);
      setAnswer('');
      
      const reqType = topic === 'HR & Behavioral' ? 'HR' : 'TECHNICAL';
      setType(reqType);
      
      const res = await api.post('/ai/mock-interview/generate', { topic, type: reqType });
      setQuestion(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to generate question. Please try again.");
    } finally {
      setLoadingQuestion(false);
    }
  };

  const handleEvaluate = async () => {
    if (!answer.trim()) return;
    try {
      setLoadingFeedback(true);
      setError(null);
      const res = await api.post('/ai/mock-interview/evaluate', { 
        question: question.question, 
        answer 
      });
      setFeedback(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to evaluate answer. Please try again.");
    } finally {
      setLoadingFeedback(false);
    }
  };

  return (
    <div className="space-y-6 max-w-5xl mx-auto">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
          <Video className="text-primary" />
          AI Mock Interviews
        </h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">
          Practice technical and HR questions with an AI interviewer and get instant feedback.
        </p>
      </div>

      {!question && !feedback && (
        <Card>
          <CardHeader>
            <CardTitle>Select Interview Topic</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
              {topics.map(t => (
                <button
                  key={t}
                  onClick={() => setTopic(t)}
                  className={`p-4 rounded-xl border text-left transition-all ${
                    topic === t 
                      ? 'border-primary bg-primary/5 text-primary ring-1 ring-primary' 
                      : 'border-slate-200 dark:border-slate-700 hover:border-slate-300 dark:hover:border-slate-600 text-slate-700 dark:text-slate-300'
                  }`}
                >
                  <BookOpen className="mb-2" size={20} />
                  <span className="font-medium text-sm">{t}</span>
                </button>
              ))}
            </div>
            
            {error && <p className="text-red-500 text-sm">{error}</p>}
            
            <Button 
              onClick={handleGenerate} 
              disabled={loadingQuestion} 
              className="w-full flex items-center justify-center gap-2 mt-4"
              size="lg"
            >
              {loadingQuestion && <Loader2 className="w-5 h-5 animate-spin" />}
              {loadingQuestion ? 'Generating Question...' : 'Start Interview Session'}
            </Button>
          </CardContent>
        </Card>
      )}

      {question && !feedback && (
        <Card className="border-t-4 border-t-primary">
          <CardHeader>
            <div className="flex justify-between items-center">
              <span className="text-xs font-bold uppercase tracking-wider text-slate-500 bg-slate-100 dark:bg-slate-800 px-3 py-1 rounded-full">
                {question.category} QUESTION
              </span>
              <span className="text-sm font-medium text-primary">Topic: {topic}</span>
            </div>
          </CardHeader>
          <CardContent className="space-y-6">
            <h2 className="text-xl font-medium text-slate-900 dark:text-white leading-relaxed">
              "{question.question}"
            </h2>
            
            <div>
              <label className="block text-sm font-medium mb-2 text-slate-700 dark:text-slate-300 flex items-center gap-2">
                <Mic size={16} /> Your Answer
              </label>
              <textarea
                value={answer}
                onChange={(e) => setAnswer(e.target.value)}
                placeholder="Type your complete answer here as you would speak it..."
                className="w-full h-48 p-4 border rounded-md border-slate-300 dark:border-slate-700 bg-transparent text-slate-900 dark:text-white focus:ring-2 focus:ring-primary focus:border-transparent outline-none resize-none"
              />
            </div>
            
            {error && <p className="text-red-500 text-sm">{error}</p>}
          </CardContent>
          <CardFooter className="flex justify-between">
            <Button variant="outline" onClick={() => setQuestion(null)}>Cancel</Button>
            <Button 
              onClick={handleEvaluate} 
              disabled={loadingFeedback || !answer.trim()}
              className="flex items-center gap-2"
            >
              {loadingFeedback && <Loader2 className="w-4 h-4 animate-spin" />}
              {loadingFeedback ? 'Evaluating...' : 'Submit Answer'} <ArrowRight size={16} />
            </Button>
          </CardFooter>
        </Card>
      )}

      {feedback && (
        <div className="space-y-6">
          <Card className="bg-slate-900 text-white border-none">
            <CardContent className="p-6">
              <p className="text-slate-400 text-sm mb-2">Question:</p>
              <p className="text-lg font-medium">"{question.question}"</p>
              
              <div className="mt-6 pt-6 border-t border-slate-800">
                <p className="text-slate-400 text-sm mb-2">Your Answer:</p>
                <p className="text-slate-300 italic">"{answer}"</p>
              </div>
            </CardContent>
          </Card>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card className="bg-gradient-to-br from-indigo-500 to-indigo-600 text-white border-none">
              <CardContent className="p-6 flex flex-col items-center justify-center text-center h-full">
                <p className="text-indigo-100 font-medium mb-2">Evaluation Score</p>
                <div className="flex items-end gap-1">
                  <h2 className="text-6xl font-bold">{feedback.score}</h2>
                  <span className="text-indigo-200 mb-2">/ 100</span>
                </div>
              </CardContent>
            </Card>

            <Card className="md:col-span-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <CheckCircle2 className="text-emerald-500" /> AI Feedback
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-slate-700 dark:text-slate-300 leading-relaxed">
                  {feedback.feedback}
                </p>
                
                <div className="mt-6">
                  <h4 className="font-semibold text-slate-900 dark:text-white mb-3 flex items-center gap-2">
                    <AlertTriangle size={18} className="text-orange-500" /> Areas for Improvement
                  </h4>
                  <ul className="space-y-2">
                    {feedback.improvementSuggestions?.map((suggestion, i) => (
                      <li key={i} className="flex gap-2 text-sm text-slate-600 dark:text-slate-400">
                        <span className="text-primary mt-1">•</span>
                        {suggestion}
                      </li>
                    ))}
                  </ul>
                </div>
              </CardContent>
            </Card>
          </div>
          
          <Button onClick={handleGenerate} className="w-full flex justify-center gap-2" size="lg">
            <Video size={18} /> Try Another Question
          </Button>
        </div>
      )}
    </div>
  );
}
