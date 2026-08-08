import React, { useState } from 'react';
import { Globe, CheckCircle, XCircle } from 'lucide-react';
import { auditDomain } from '../services/api';

export default function DomainAuditor() {
  const [domain, setDomain] = useState('');
  const [loading, setLoading] = useState(false);
  const [audit, setAudit] = useState(null);

  const handleAudit = async (e) => {
    e.preventDefault();
    if (!domain.trim()) return;
    setLoading(true);
    try {
      const res = await auditDomain(domain);
      setAudit(res);
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
          Domain Security Audit
        </h2>
      </div>

      {/* Single Clean Input Card */}
      <div className="claude-card p-6 shadow-sm">
        <form onSubmit={handleAudit} className="space-y-3">
          <div className="relative">
            <input
              type="text"
              value={domain}
              onChange={(e) => {
                setDomain(e.target.value);
                if (!e.target.value) setAudit(null);
              }}
              placeholder="Enter domain to audit..."
              className="w-full claude-input rounded-xl px-4 py-3.5 text-stone-900 placeholder:text-stone-400 font-mono text-sm pr-28"
            />
            <button
              type="submit"
              disabled={loading || !domain}
              className="absolute right-2 top-1/2 -translate-y-1/2 px-4 py-2 claude-btn-primary rounded-lg font-bold text-xs transition disabled:opacity-50"
            >
              {loading ? 'Auditing...' : 'Audit Domain'}
            </button>
          </div>
        </form>
      </div>

      {/* Results */}
      {audit && (
        <div className="animate-fadeIn">
          <div className="p-5 rounded-2xl bg-white border border-stone-200 shadow-sm flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-2xl font-black font-mono border ${
                audit.grade === 'A' ? 'bg-emerald-50 border-emerald-300 text-emerald-600' : 'bg-[#f5e9e2] border-[#ebd2c5] text-[#c96442]'
              }`}>
                {audit.grade}
              </div>
              <div>
                <h3 className="text-base font-bold text-stone-900 font-mono">{audit.domain}</h3>
                <p className="text-xs text-stone-500 font-mono">
                  Score: <strong className="text-[#c96442]">{audit.securityScore}/100</strong>
                </p>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <span className={`px-2.5 py-1 rounded text-xs font-bold font-mono border ${audit.hasSpfRecord ? 'bg-emerald-50 border-emerald-200 text-emerald-700' : 'bg-rose-50 border-rose-200 text-rose-700'}`}>
                SPF: {audit.hasSpfRecord ? 'Pass' : 'Missing'}
              </span>
              <span className={`px-2.5 py-1 rounded text-xs font-bold font-mono border ${audit.hasDmarcRecord ? 'bg-emerald-50 border-emerald-200 text-emerald-700' : 'bg-rose-50 border-rose-200 text-rose-700'}`}>
                DMARC: {audit.hasDmarcRecord ? 'Pass' : 'Missing'}
              </span>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
