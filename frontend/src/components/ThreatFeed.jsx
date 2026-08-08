import React from 'react';
import { ExternalLink } from 'lucide-react';

const SAMPLE_THREATS = [
  {
    id: "CVE-2024-30078",
    title: "Windows Wi-Fi Driver Remote Code Execution Vulnerability",
    cve: "CVE-2024-30078",
    severity: "CRITICAL",
    score: 8.8,
    date: "2024-06-11",
    vendor: "Microsoft Windows",
    description: "An unauthenticated attacker could execute arbitrary code remotely on a target system within Wi-Fi range by sending a specially crafted packet."
  },
  {
    id: "CVE-2024-21626",
    title: "runc Container Escape & Arbitrary File Read Vulnerability (Leaky Vessels)",
    cve: "CVE-2024-21626",
    severity: "CRITICAL",
    score: 9.0,
    date: "2024-01-31",
    vendor: "Docker / Kubernetes / OCI runc",
    description: "Flaw in runc allows attackers to escape container isolation and gain host file system write access via malicious image builds."
  },
  {
    id: "CVE-2024-1709",
    title: "ConnectWise ScreenConnect Authentication Bypass Vulnerability",
    cve: "CVE-2024-1709",
    severity: "CRITICAL",
    score: 10.0,
    date: "2024-02-19",
    vendor: "ConnectWise",
    description: "Authentication bypass vulnerability allowing remote unauthenticated attackers to create admin accounts on vulnerable ScreenConnect servers."
  }
];

export default function ThreatFeed() {
  return (
    <div className="space-y-6 animate-fadeIn max-w-xl mx-auto py-4">
      
      {/* Tool Title */}
      <div className="text-center">
        <h2 className="text-2xl font-extrabold text-stone-900 tracking-tight font-heading">
          Live Public Threat Feed
        </h2>
      </div>

      <div className="space-y-3">
        {SAMPLE_THREATS.map((t) => (
          <div key={t.id} className="p-5 rounded-xl bg-white border border-stone-200 shadow-sm space-y-2">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="px-2 py-0.5 bg-rose-100 text-rose-700 font-mono text-xs font-bold rounded">
                  CVSS {t.score}
                </span>
                <span className="text-[#c96442] font-mono text-xs font-bold">{t.cve}</span>
              </div>
              <span className="text-xs text-stone-400 font-mono">{t.date}</span>
            </div>

            <h4 className="text-sm font-bold text-stone-900">{t.title}</h4>
            <p className="text-xs text-stone-600 leading-relaxed">{t.description}</p>

            <div className="pt-2 flex items-center justify-between border-t border-stone-100 text-[11px] font-mono text-stone-400">
              <span>Vendor: <strong className="text-stone-700">{t.vendor}</strong></span>
              <a href={`https://nvd.nist.gov/vuln/detail/${t.cve}`} target="_blank" rel="noreferrer" className="text-[#c96442] hover:underline flex items-center gap-1">
                NVD Record <ExternalLink className="w-3 h-3" />
              </a>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
