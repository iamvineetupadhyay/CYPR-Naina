import React, { useState, useEffect } from 'react';
import { User, ShieldCheck, Zap, RefreshCcw, Activity, KeyRound, Search, Globe, Clock, Lock } from 'lucide-react';
import { getUserRateLimit } from '../services/api';

export default function UserProfile({ user }) {
  const [rateLimit, setRateLimit] = useState(null);
  const [countdown, setCountdown] = useState(60);

  const fetchQuota = async () => {
    const q = await getUserRateLimit(user ? user.email : 'vineet@cypr.sec');
    setRateLimit(q);
    if (q && q.resetInSeconds) {
      setCountdown(q.resetInSeconds);
    }
  };

  useEffect(() => {
    fetchQuota();
    const interval = setInterval(() => {
      setCountdown((prev) => (prev > 1 ? prev - 1 : 60));
    }, 1000);
    return () => clearInterval(interval);
  }, [user]);

  const defaultUser = {
    displayName: user ? user.displayName : "Vineet Kumar",
    email: user ? user.email : "vineet@cypr.sec",
    photoURL: user ? user.photoURL : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80",
    provider: user ? "Google OAuth2 (Firebase)" : "Guest Session"
  };

  const usagePercent = rateLimit ? Math.round((rateLimit.usedQuota / rateLimit.maxLimit) * 100) : 20;

  return (
    <div className="space-y-6 animate-fadeIn max-w-xl mx-auto py-4">
      
      {/* Tool Title */}
      <div className="text-center">
        <h2 className="text-2xl font-extrabold text-stone-900 tracking-tight font-heading">
          User Profile & Analytics
        </h2>
      </div>

      {/* User Information Card */}
      <div className="claude-card p-6 shadow-sm space-y-4">
        <div className="flex items-center gap-4">
          <img 
            src={defaultUser.photoURL} 
            alt={defaultUser.displayName} 
            className="w-14 h-14 rounded-full object-cover border-2 border-[#ebd2c5]"
          />
          <div>
            <h3 className="text-lg font-bold text-stone-900 font-heading">{defaultUser.displayName}</h3>
            <p className="text-xs text-stone-500 font-mono">{defaultUser.email}</p>
            <div className="flex items-center gap-2 mt-1">
              <span className="px-2 py-0.5 rounded text-[10px] font-mono font-bold bg-[#f5e9e2] text-[#c96442] border border-[#ebd2c5]">
                {defaultUser.provider}
              </span>
              <span className="text-[11px] text-emerald-600 font-bold flex items-center gap-1 font-mono">
                <ShieldCheck className="w-3.5 h-3.5" /> Active Session
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* SDK Rate Limit Meter Card */}
      <div className="claude-card p-6 shadow-sm space-y-4">
        <div className="flex items-center justify-between">
          <div className="text-[11px] font-mono font-bold text-[#c96442] uppercase tracking-widest flex items-center gap-1.5">
            <Zap className="w-3.5 h-3.5" /> SDK RATE LIMIT & QUOTA
          </div>
          <button 
            onClick={fetchQuota}
            className="text-stone-400 hover:text-stone-600 p-1 rounded hover:bg-stone-100 transition"
            title="Refresh Rate Limit Status"
          >
            <RefreshCcw className="w-3.5 h-3.5" />
          </button>
        </div>

        <div className="space-y-3">
          <div className="flex items-center justify-between text-xs font-mono">
            <span className="text-stone-600">Rate Limit Quota (XposedOrNot SDK):</span>
            <span className="font-bold text-stone-900">
              {rateLimit ? rateLimit.usedQuota : 2} / {rateLimit ? rateLimit.maxLimit : 10} Requests Used
            </span>
          </div>

          {/* Visual Quota Progress Bar */}
          <div className="w-full bg-stone-100 h-3 rounded-full overflow-hidden p-0.5 border border-stone-200">
            <div 
              className={`h-full rounded-full transition-all duration-500 ${
                usagePercent > 80 ? 'bg-rose-500' : usagePercent > 50 ? 'bg-amber-500' : 'bg-[#c96442]'
              }`}
              style={{ width: `${usagePercent}%` }}
            />
          </div>

          <div className="flex items-center justify-between text-[11px] font-mono text-stone-500">
            <span>Quota Reset Window:</span>
            <span className="text-[#c96442] font-bold flex items-center gap-1">
              <Clock className="w-3 h-3" /> Resets in {countdown}s
            </span>
          </div>
        </div>
      </div>

      {/* Personal Scan Activity Analytics */}
      <div className="claude-card p-6 shadow-sm space-y-4">
        <div className="text-[11px] font-mono font-bold text-[#c96442] uppercase tracking-widest flex items-center gap-1.5">
          <Activity className="w-3.5 h-3.5" /> RECENT AUDIT HISTORY
        </div>

        <div className="space-y-2">
          <div className="p-3 bg-[#faf8f5] rounded-xl border border-stone-200 flex items-center justify-between text-xs font-mono">
            <div className="flex items-center gap-2">
              <KeyRound className="w-4 h-4 text-[#c96442]" />
              <span className="text-stone-800 font-semibold">Password Hash Check</span>
            </div>
            <span className="text-emerald-600 font-bold">100% Clean</span>
          </div>

          <div className="p-3 bg-[#faf8f5] rounded-xl border border-stone-200 flex items-center justify-between text-xs font-mono">
            <div className="flex items-center gap-2">
              <Search className="w-4 h-4 text-[#c96442]" />
              <span className="text-stone-800 font-semibold">Identity Exposure Search</span>
            </div>
            <span className="text-rose-600 font-bold">2 Leaks Found</span>
          </div>

          <div className="p-3 bg-[#faf8f5] rounded-xl border border-stone-200 flex items-center justify-between text-xs font-mono">
            <div className="flex items-center gap-2">
              <Globe className="w-4 h-4 text-[#c96442]" />
              <span className="text-stone-800 font-semibold">Domain Security Audit</span>
            </div>
            <span className="text-emerald-600 font-bold">SPF/DMARC Pass</span>
          </div>
        </div>
      </div>

    </div>
  );
}
