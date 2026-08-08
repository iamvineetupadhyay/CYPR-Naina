import React from 'react';

export default function CyprLogo({ className = "h-7" }) {
  return (
    <div className={`flex items-center gap-2.5 select-none ${className}`}>
      {/* Exact CYPR Futuristic Tech Typography */}
      <svg className="h-7 w-auto" viewBox="0 0 160 40" fill="none" xmlns="http://www.w3.org/2000/svg">
        {/* Letter C */}
        <path d="M30 8H10C7.79086 8 6 9.79086 6 12V28C6 30.2091 7.79086 32 10 32H30V25H14V15H30V8Z" fill="#0F172A" />
        {/* Letter Y */}
        <path d="M42 8L50 20V32H57V20L65 8H57L53.5 14.5L50 8H42Z" fill="#0F172A" />
        {/* Letter P */}
        <path d="M74 8H94C96.2091 8 98 9.79086 98 12V20C98 22.2091 96.2091 24 94 24H82V32H74V8ZM82 17H90V14H82V17Z" fill="#0F172A" />
        {/* Letter R */}
        <path d="M106 8H126C128.209 8 130 9.79086 130 12V18C130 20.2091 128.209 22 126 22L131 32H123L118.5 23H114V32H106V8ZM114 16H122V13H114V16Z" fill="#0F172A" />
      </svg>

      <span className="text-[11px] font-mono font-bold text-[#c96442] bg-[#f5e9e2] px-2 py-0.5 rounded-md border border-[#ebd2c5] tracking-wider uppercase">
        NAINA ENGINE
      </span>
    </div>
  );
}
