import React, { useState, useEffect } from 'react';
import { 
  ShieldCheck, Search, KeyRound, Globe, Bot, Radio, BarChart3, 
  LogOut, ArrowLeft, User, Lock
} from 'lucide-react';
import LandingPage from './components/LandingPage';
import PasswordChecker from './components/PasswordChecker';
import IdentityScanner from './components/IdentityScanner';
import DomainAuditor from './components/DomainAuditor';
import ScraperControl from './components/ScraperControl';
import ThreatFeed from './components/ThreatFeed';
import AnalyticsDashboard from './components/AnalyticsDashboard';
import UserProfile from './components/UserProfile';
import CyprLogo from './components/CyprLogo';
import { getSystemStats } from './services/api';
import { triggerRealGoogleOAuth, logoutUser } from './firebase';

export default function App() {
  const [showLanding, setShowLanding] = useState(true);
  const [activeTab, setActiveTab] = useState('password');
  const [user, setUser] = useState(null);
  const [stats, setStats] = useState(null);

  useEffect(() => {
    getSystemStats().then(setStats).catch(() => {});
  }, []);

  const handleLogout = async () => {
    await logoutUser();
    setUser(null);
    setShowLanding(true);
  };

  const handleGoogleSignIn = () => {
    triggerRealGoogleOAuth((authenticatedUser) => {
      setUser(authenticatedUser);
      setShowLanding(false);
    });
  };

  if (showLanding) {
    return (
      <LandingPage 
        onLaunchApp={() => {
          if (!user) {
            handleGoogleSignIn();
          } else {
            setShowLanding(false);
          }
        }} 
        user={user} 
        setUser={setUser} 
      />
    );
  }

  return (
    <div className="min-h-screen luxury-bg subtle-micro-grid text-stone-900 flex flex-col font-sans selection:bg-[#c96442] selection:text-white relative overflow-hidden">
      
      {/* Top Header */}
      <header className="bg-white/70 border-b border-[#e6dacd]/60 sticky top-0 z-50 px-8 py-4 backdrop-blur-xl shadow-sm">
        <div className="max-w-6xl mx-auto flex items-center justify-between">
          
          {/* Official CYPR Logo & Back to Home */}
          <div className="flex items-center gap-4">
            <button
              onClick={() => setShowLanding(true)}
              className="p-2 rounded-xl bg-white border border-[#e8e1d7] hover:bg-stone-50 text-stone-600 transition text-xs font-bold flex items-center gap-1 shadow-sm"
              title="Return to Home Landing Page"
            >
              <ArrowLeft className="w-4 h-4" /> Home
            </button>

            <CyprLogo className="h-8" />
          </div>

          {/* User Profile / Auth Action */}
          <div className="flex items-center gap-3">
            {user ? (
              <div className="flex items-center gap-3">
                <button
                  onClick={() => setActiveTab('profile')}
                  className="flex items-center gap-2 bg-white hover:bg-stone-50 px-3 py-1.5 rounded-xl border border-[#e8e1d7] text-xs font-mono text-stone-700 transition shadow-sm"
                >
                  <img src={user.photoURL} alt="Avatar" className="w-5 h-5 rounded-full object-cover" />
                  <span className="font-bold">{user.displayName}</span>
                </button>
                <button
                  onClick={handleLogout}
                  className="p-2 rounded-xl bg-white border border-[#e8e1d7] hover:bg-stone-50 text-stone-600 transition text-xs shadow-sm"
                  title="Sign Out"
                >
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <button
                onClick={handleGoogleSignIn}
                className="bg-white hover:bg-stone-50 border border-[#e8e1d7] text-stone-800 px-3.5 py-1.5 rounded-xl text-xs font-bold transition flex items-center gap-2 shadow-sm"
              >
                <svg className="w-4 h-4" viewBox="0 0 24 24">
                  <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                  <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                  <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                  <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                </svg>
                Continue with Google
              </button>
            )}
          </div>

        </div>
      </header>

      {/* Main Navigation Bar */}
      <div className="bg-white/60 border-b border-[#e6dacd]/60 py-3 px-6 shadow-sm backdrop-blur-xl">
        <div className="max-w-6xl mx-auto flex items-center justify-center overflow-x-auto gap-2">
          
          <button
            onClick={() => setActiveTab('password')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'password'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <KeyRound className="w-4 h-4" /> Password Test
          </button>

          <button
            onClick={() => setActiveTab('identity')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'identity'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <Search className="w-4 h-4" /> Identity Search
          </button>

          <button
            onClick={() => setActiveTab('domain')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'domain'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <Globe className="w-4 h-4" /> Domain Audit
          </button>

          <button
            onClick={() => setActiveTab('scraper')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'scraper'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <Bot className="w-4 h-4" /> Web Scraper Engine
          </button>

          <button
            onClick={() => setActiveTab('threats')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'threats'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <Radio className="w-4 h-4" /> Live Threats
          </button>

          <button
            onClick={() => setActiveTab('analytics')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'analytics'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <BarChart3 className="w-4 h-4" /> Analytics
          </button>

          <button
            onClick={() => setActiveTab('profile')}
            className={`px-4 py-2 rounded-xl text-xs font-bold font-mono transition-all flex items-center gap-2 ${
              activeTab === 'profile'
                ? 'bg-gradient-to-br from-[#d9704c] to-[#c96442] text-white shadow-md shadow-[#c96442]/25'
                : 'bg-white text-stone-700 hover:bg-stone-50 border border-[#e8e1d7]'
            }`}
          >
            <User className="w-4 h-4" /> Profile & Quota
          </button>

        </div>
      </div>

      {/* Main Content Area */}
      <main className="max-w-4xl mx-auto px-4 py-10 flex-1 w-full space-y-8 relative z-10">
        {!user ? (
          /* Authentication Gate Card */
          <div className="claude-card p-8 max-w-md mx-auto text-center space-y-5 animate-fadeIn shadow-lg">
            <div className="w-14 h-14 rounded-2xl bg-[#f5e9e2] text-[#c96442] flex items-center justify-center mx-auto shadow-sm">
              <Lock className="w-7 h-7" />
            </div>
            
            <div className="space-y-2">
              <h3 className="text-xl font-bold text-stone-900 font-heading">
                Google Sign-In Required
              </h3>
              <p className="text-xs text-stone-600 leading-relaxed font-mono">
                To access CYPR Naina Engine tools (Password Auditor, Identity Scanner, Domain Security, Scraper Engine), please sign in with your Google account.
              </p>
            </div>

            <button
              onClick={handleGoogleSignIn}
              className="w-full claude-btn-primary py-3.5 px-6 rounded-xl font-bold text-xs flex items-center justify-center gap-2 shadow-md"
            >
              <svg className="w-4 h-4" viewBox="0 0 24 24">
                <path fill="#ffffff" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              </svg>
              Sign in with Google to Continue
            </button>
          </div>
        ) : (
          <div>
            {activeTab === 'password' && <PasswordChecker />}
            {activeTab === 'identity' && <IdentityScanner />}
            {activeTab === 'domain' && <DomainAuditor />}
            {activeTab === 'scraper' && <ScraperControl />}
            {activeTab === 'threats' && <ThreatFeed />}
            {activeTab === 'analytics' && <AnalyticsDashboard />}
            {activeTab === 'profile' && <UserProfile user={user} />}
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="border-t border-[#e6dacd]/60 bg-white/60 backdrop-blur-xl py-6 mt-12 relative z-10">
        <div className="max-w-4xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs font-medium text-stone-500">
          <div className="flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-[#c96442]" />
            <span className="font-bold text-stone-800">CYPR Naina Engine &copy; 2026</span>
            <span>| Google OAuth2 & Java Spring Boot</span>
          </div>
          <div className="flex items-center gap-4 text-stone-500 font-mono text-[11px]">
            <span>100% Ethical & Client-Isolated</span>
            <span>Free Public APIs</span>
          </div>
        </div>
      </footer>

    </div>
  );
}
