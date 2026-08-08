import React, { useState } from 'react';
import { Search, ShieldAlert, ShieldCheck, Database, Zap, Cpu, Lock } from 'lucide-react';
import { searchIdentity } from '../services/api';

export default function IdentityScanner() {
  const [query, setQuery] = useState('');
  const [useXonSdk, setUseXonSdk] = useState(true);
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    const mode = useXonSdk ? 'XPOSEDORNOT' : 'LOCAL';
    try {
      const data = await searchIdentity(query, 'vineet@cypr.sec', mode);
      setResults(data);
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
          Identity Exposure Search
        </h2>
      </div>

      {/* Engine Switch Toggle */}
      <div className="claude-card p-4 shadow-sm flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className={`p-2 rounded-lg ${useXonSdk ? 'bg-[#f5e9e2] text-[#c96442]' : 'bg-stone-100 text-stone-700'}`}>
            {useXonSdk ? <Zap className="w-4 h-4" /> : <Database className="w-4 h-4" />}
          </div>
          <div>
            <h4 className="text-xs font-bold text-stone-900 font-mono">
              {useXonSdk ? 'XposedOrNot Global SDK' : 'Local Engine (Offline H2 DB)'}
            </h4>
            <span className="text-[10px] text-stone-500 font-mono block">
              {useXonSdk ? 'Wider Data Set (10 req/min rate limit)' : 'Unlimited searches on scraped local DB'}
            </span>
          </div>
        </div>

        {/* Toggle Switch */}
        <button
          type="button"
          onClick={() => setUseXonSdk(!useXonSdk)}
          className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none ${
            useXonSdk ? 'bg-[#c96442]' : 'bg-stone-300'
          }`}
        >
          <span
            className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
              useXonSdk ? 'translate-x-5' : 'translate-x-0'
            }`}
          />
        </button>
      </div>

      {/* Input Form Card */}
      <div className="claude-card p-6 shadow-sm">
        <form onSubmit={handleSearch} className="space-y-3">
          <div className="relative">
            <input
              type="text"
              value={query}
              onChange={(e) => {
                setQuery(e.target.value);
                if (!e.target.value) setResults(null);
              }}
              placeholder="Enter email address..."
              className="w-full claude-input rounded-xl px-4 py-3.5 text-stone-900 placeholder:text-stone-400 font-mono text-sm pr-28"
            />
            <button
              type="submit"
              disabled={loading || !query}
              className="absolute right-2 top-1/2 -translate-y-1/2 px-4 py-2 claude-btn-primary rounded-lg font-bold text-xs transition disabled:opacity-50"
            >
              {loading ? 'Searching...' : 'Scan Email'}
            </button>
          </div>
        </form>
      </div>

      {/* Results */}
      {results && (
        <div className="space-y-4 animate-fadeIn">
          <div className={`p-5 rounded-2xl border shadow-sm ${results.isExposed ? 'bg-rose-50 border-rose-200' : 'bg-emerald-50 border-emerald-200'}`}>
            <div className="flex items-start gap-3">
              <div className={`p-2.5 rounded-xl ${results.isExposed ? 'bg-rose-100 text-rose-600' : 'bg-emerald-100 text-emerald-600'}`}>
                {results.isExposed ? <ShieldAlert className="w-5 h-5" /> : <ShieldCheck className="w-5 h-5" />}
              </div>

              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h3 className="text-base font-bold text-stone-900">
                    {results.isExposed ? 'Exposed in Public Breaches' : 'No Breach Matches'}
                  </h3>
                  <span className="text-xs font-mono text-stone-500 font-bold">
                    {results.exposureCount} Leaks Found
                  </span>
                </div>
                <div className="flex items-center justify-between mt-2 pt-2 border-t border-stone-200 text-xs text-stone-600 font-mono">
                  <span>Target: <strong className="text-[#c96442]">{results.query}</strong></span>
                  <span className="px-2 py-0.5 bg-white rounded border border-stone-200 font-bold text-[10px] text-stone-700">
                    Source: {results.dataSource || (useXonSdk ? 'XposedOrNot SDK' : 'Local H2 DB')}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Breach List */}
          <div className="space-y-2">
            {results.breaches.map((breach) => (
              <div key={breach.id || breach.title} className="p-4 rounded-xl bg-white border border-stone-200 flex items-center justify-between shadow-sm">
                <div>
                  <h5 className="font-bold text-stone-900 text-xs">{breach.title}</h5>
                  <p className="text-[11px] text-stone-500 font-mono mt-0.5">
                    Date: {breach.breachDate} | Affected: {(breach.pwnCount / 1000000).toFixed(1)}M accounts
                  </p>
                </div>
                <span className="px-2 py-0.5 bg-[#f5e9e2] text-[#c96442] rounded text-[10px] font-bold font-mono border border-[#ebd2c5]">
                  {breach.severity}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

    </div>
  );
}
