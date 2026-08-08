import React, { useState, useEffect } from 'react';
import { 
  ShieldCheck, ArrowRight, ChevronRight, Sparkles, Database, Lock, Globe, Zap, Search
} from 'lucide-react';
import CyprLogo from './CyprLogo';
import { triggerRealGoogleOAuth } from '../firebase';
import { getSystemStats } from '../services/api';

const formatAccountCount = (count) => {
  if (count === null || count === undefined || count === 0) return '0';
  const num = typeof count === 'number' ? count : parseFloat(count);
  if (isNaN(num) || num === 0) return '0';
  
  if (num >= 1000000000) {
    return (num / 1000000000).toFixed(2) + 'B';
  }
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M';
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K';
  }
  return num.toLocaleString();
};

export default function LandingPage({ onLaunchApp, user, setUser }) {
  const [liveStats, setLiveStats] = useState(null);
  const [animatedAccountCount, setAnimatedAccountCount] = useState(0);
  const [quickSearch, setQuickSearch] = useState('');

  // Fetch Live Stats from Backend Database
  useEffect(() => {
    getSystemStats().then((s) => {
      setLiveStats(s);
      const targetCount = s && s.totalExposedAccounts ? s.totalExposedAccounts : 0;
      
      if (targetCount > 0) {
        const duration = 1400;
        const steps = 40;
        const intervalTime = duration / steps;
        let currentStep = 0;
        
        const timer = setInterval(() => {
          currentStep++;
          const progress = currentStep / steps;
          setAnimatedAccountCount(Math.round(progress * targetCount));

          if (currentStep >= steps) {
            clearInterval(timer);
          }
        }, intervalTime);
      }
    }).catch(() => {});
  }, []);

  const handleGoogleSignIn = () => {
    triggerRealGoogleOAuth((authenticatedUser) => {
      setUser(authenticatedUser);
      onLaunchApp();
    });
  };

  const handleHeroSubmit = (e) => {
    e.preventDefault();
    onLaunchApp();
  };

  const displayExposedAccounts = formatAccountCount(animatedAccountCount);
  const displayIndexedBreaches = liveStats ? liveStats.indexedBreaches : 0;

  return (
    <div className="min-h-screen luxury-bg subtle-micro-grid text-stone-900 flex flex-col font-sans selection:bg-[#c96442] selection:text-white relative">
      
      {/* Sleek Minimalist Top Header */}
      <header className="border-b border-[#e6dacd]/60 bg-white/70 backdrop-blur-xl sticky top-0 z-50 px-8 py-4 shadow-sm">
        <div className="max-w-6xl mx-auto flex items-center justify-between">
          
          {/* Official CYPR Logo */}
          <CyprLogo className="h-8" />

          {/* Action Buttons */}
          <div className="flex items-center gap-3">
            {user ? (
              <div className="flex items-center gap-3">
                <div className="flex items-center gap-2 text-xs font-mono text-stone-700 bg-white px-3 py-1.5 rounded-xl border border-[#e8e1d7] shadow-sm">
                  <img src={user.photoURL} alt="User" className="w-5 h-5 rounded-full object-cover" />
                  <span className="font-bold">{user.displayName}</span>
                </div>
                <button
                  onClick={onLaunchApp}
                  className="claude-btn-primary px-4 py-2 rounded-xl text-xs font-bold transition flex items-center gap-1.5 shadow-sm"
                >
                  Launch App <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <button
                  onClick={handleGoogleSignIn}
                  className="bg-white hover:bg-stone-50 border border-[#e8e1d7] text-stone-800 px-4 py-2 rounded-xl text-xs font-bold transition flex items-center gap-2 shadow-sm"
                >
                  <svg className="w-4 h-4" viewBox="0 0 24 24">
                    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                  </svg>
                  Continue with Google
                </button>

                <button
                  onClick={onLaunchApp}
                  className="claude-btn-primary px-4 py-2 rounded-xl text-xs font-bold transition flex items-center gap-1.5 shadow-sm"
                >
                  Enter Engine <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>
            )}
          </div>

        </div>
      </header>

      {/* Main Hero Container */}
      <main className="max-w-4xl mx-auto px-4 pt-12 pb-16 space-y-10 flex-1 w-full">
        
        {/* Hero Headlines */}
        <div className="text-center space-y-5">
          
          {/* Live Status Pill */}
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/90 border border-[#ebd2c5] text-xs font-mono text-[#c96442] font-semibold shadow-sm">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
            <span>LIVE DB: {displayExposedAccounts} EXPOSED ACCOUNTS INDEXED</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-extrabold text-stone-900 tracking-tight leading-[1.15] font-heading max-w-3xl mx-auto">
            Ethical Threat Intelligence & Public Breach Monitoring
          </h1>

          <p className="text-stone-600 text-base sm:text-lg max-w-xl mx-auto leading-relaxed">
            Detect leaked credentials safely using zero-knowledge <strong>k-anonymity SHA-1 cryptography</strong> and audit domain security protections.
          </p>

          {/* Interactive Hero Input Bar */}
          <div className="max-w-lg mx-auto pt-2">
            <form onSubmit={handleHeroSubmit} className="relative flex items-center">
              <div className="absolute left-4 text-stone-400">
                <Search className="w-5 h-5" />
              </div>
              <input
                type="text"
                value={quickSearch}
                onChange={(e) => setQuickSearch(e.target.value)}
                placeholder="Check your email or password..."
                className="w-full bg-white border border-[#e8e1d7] rounded-2xl pl-12 pr-36 py-4 text-sm text-stone-900 placeholder:text-stone-400 shadow-md focus:border-[#c96442] focus:ring-2 focus:ring-[#c96442]/20 font-mono transition"
              />
              <button
                type="submit"
                className="absolute right-2 claude-btn-primary px-5 py-2.5 rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-sm"
              >
                Scan Exposure <ArrowRight className="w-3.5 h-3.5" />
              </button>
            </form>
          </div>

        </div>

        {/* 4 Tight, Elegant Stat Cards - 100% Pure Dynamic Formatter */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          
          <div className="claude-stat-card p-5 text-center space-y-2">
            <div className="w-9 h-9 rounded-xl bg-[#f5e9e2] text-[#c96442] flex items-center justify-center mx-auto">
              <Database className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-mono text-stone-500 uppercase tracking-wider block font-semibold">Exposed Accounts</span>
            <p className="text-2xl font-black text-stone-900 font-mono">
              {displayExposedAccounts}
            </p>
          </div>

          <div className="claude-stat-card p-5 text-center space-y-2">
            <div className="w-9 h-9 rounded-xl bg-[#f5e9e2] text-[#c96442] flex items-center justify-center mx-auto">
              <Lock className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-mono text-stone-500 uppercase tracking-wider block font-semibold">k-Anonymity Hashes</span>
            <p className="text-2xl font-black text-[#c96442] font-mono">
              800M+
            </p>
          </div>

          <div className="claude-stat-card p-5 text-center space-y-2">
            <div className="w-9 h-9 rounded-xl bg-[#f5e9e2] text-[#c96442] flex items-center justify-center mx-auto">
              <Globe className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-mono text-stone-500 uppercase tracking-wider block font-semibold">Indexed Breaches</span>
            <p className="text-2xl font-black text-stone-900 font-mono">
              {displayIndexedBreaches} Leaks
            </p>
          </div>

          <div className="claude-stat-card p-5 text-center space-y-2">
            <div className="w-9 h-9 rounded-xl bg-[#f5e9e2] text-[#c96442] flex items-center justify-center mx-auto">
              <Zap className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-mono text-stone-500 uppercase tracking-wider block font-semibold">Official SDK</span>
            <p className="text-base font-bold text-stone-900 font-mono pt-1">
              XposedOrNot
            </p>
          </div>

        </div>

      </main>

      {/* Footer */}
      <footer className="border-t border-[#e6dacd]/60 bg-white/60 backdrop-blur-xl py-6 mt-auto">
        <div className="max-w-5xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs font-medium text-stone-500">
          <div className="flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-[#c96442]" />
            <span className="font-bold text-stone-800">CYPR Naina Engine &copy; 2026</span>
            <span>| Google OAuth2 & Java Spring Boot</span>
          </div>
          <div className="flex items-center gap-4 text-stone-500 font-mono text-[11px]">
            <span>100% Ethical Public Security</span>
            <span>Free Public APIs</span>
          </div>
        </div>
      </footer>

    </div>
  );
}
