import React, { useState } from 'react';
import { Lock, Eye, EyeOff, ShieldCheck, ShieldAlert } from 'lucide-react';
import { checkPasswordPwned } from '../services/api';

export default function PasswordChecker() {
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleCheck = async (e) => {
    e.preventDefault();
    if (!password) return;
    setLoading(true);
    try {
      const res = await checkPasswordPwned(password);
      setResult(res);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6 animate-fadeIn max-w-xl mx-auto py-4">
      
      {/* Tool Title */}
      <div className="text-center">
        <h2 className="text-2xl font-extrabold text-stone-900 tracking-tight font-heading">
          Password Strength Test
        </h2>
      </div>

      {/* Single Clean Input Card */}
      <div className="claude-card p-6 shadow-sm">
        <form onSubmit={handleCheck} className="space-y-4">
          <div className="relative">
            <input
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                if (!e.target.value) setResult(null);
              }}
              placeholder="Enter password to test..."
              className="w-full claude-input rounded-xl px-4 py-3.5 text-stone-900 placeholder:text-stone-400 font-mono text-sm pr-28"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-24 top-1/2 -translate-y-1/2 text-stone-400 hover:text-stone-600 p-1"
            >
              {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
            </button>
            <button
              type="submit"
              disabled={loading || !password}
              className="absolute right-2 top-1/2 -translate-y-1/2 px-4 py-2 claude-btn-primary rounded-lg font-bold text-xs transition disabled:opacity-50"
            >
              {loading ? 'Testing...' : 'Test Hash'}
            </button>
          </div>
        </form>
      </div>

      {/* Clean Results Card */}
      {result && (
        <div className="animate-fadeIn">
          <div className={`p-5 rounded-2xl border shadow-sm ${result.isExposed ? 'bg-rose-50 border-rose-200' : 'bg-emerald-50 border-emerald-200'}`}>
            <div className="flex items-start gap-4">
              <div className={`p-2.5 rounded-xl ${result.isExposed ? 'bg-rose-100 text-rose-600' : 'bg-emerald-100 text-emerald-600'}`}>
                {result.isExposed ? <ShieldAlert className="w-5 h-5" /> : <ShieldCheck className="w-5 h-5" />}
              </div>

              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h3 className="text-base font-bold text-stone-900">
                    {result.isExposed ? 'Exposed in Public Leaks' : 'Clean Hash - No Matches'}
                  </h3>
                  <span className={`px-2.5 py-0.5 text-xs font-mono rounded-full font-bold ${
                    result.isExposed ? 'bg-rose-100 text-rose-700' : 'bg-emerald-100 text-emerald-700'
                  }`}>
                    {result.strengthRating}
                  </span>
                </div>

                <div className="grid grid-cols-3 gap-2 mt-3 text-center">
                  <div className="bg-white p-2.5 rounded-xl border border-stone-200">
                    <span className="text-[10px] text-stone-400 font-mono uppercase block">Leak Count</span>
                    <p className="text-base font-bold text-stone-900">{result.pwnCount.toLocaleString()}</p>
                  </div>
                  <div className="bg-white p-2.5 rounded-xl border border-stone-200">
                    <span className="text-[10px] text-stone-400 font-mono uppercase block">Entropy</span>
                    <p className="text-base font-bold text-[#c96442]">{result.entropyScore} bits</p>
                  </div>
                  <div className="bg-white p-2.5 rounded-xl border border-stone-200">
                    <span className="text-[10px] text-stone-400 font-mono uppercase block">Hash Prefix</span>
                    <p className="text-sm font-mono font-bold text-stone-800">{result.sha1Prefix}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
