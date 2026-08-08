import React, { useState } from 'react';
import { X, Lock, CheckCircle2, ArrowRight } from 'lucide-react';
import { loginWithGoogle, GOOGLE_CLIENT_ID } from '../firebase';

export default function GoogleAuthModal({ isOpen, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);
  const [selectedAccount, setSelectedAccount] = useState(null);

  if (!isOpen) return null;

  const mockAccounts = [
    {
      name: "Vineet Kumar",
      email: "vineet@cypr.sec",
      photo: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80"
    },
    {
      name: "CYPR Security Admin",
      email: "admin@cypr-naina.sec",
      photo: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80"
    }
  ];

  const handleSelectAccount = async (acc) => {
    setLoading(true);
    setSelectedAccount(acc.email);
    setTimeout(async () => {
      const user = await loginWithGoogle();
      const authenticatedUser = {
        displayName: acc.name,
        email: acc.email,
        photoURL: acc.photo,
        uid: `usr_google_454945894114`,
        clientId: GOOGLE_CLIENT_ID
      };
      setLoading(false);
      onSuccess(authenticatedUser);
      onClose();
    }, 600);
  };

  return (
    <div className="fixed inset-0 z-50 bg-stone-900/60 backdrop-blur-sm flex items-center justify-center p-4 animate-fadeIn">
      <div className="bg-white rounded-2xl border border-[#e8e4df] shadow-2xl max-w-md w-full p-6 space-y-6 relative overflow-hidden">
        
        {/* Close Button */}
        <button 
          onClick={onClose}
          className="absolute right-4 top-4 text-stone-400 hover:text-stone-600 p-1.5 rounded-xl hover:bg-stone-100 transition"
        >
          <X className="w-4 h-4" />
        </button>

        {/* Modal Header */}
        <div className="text-center space-y-2 pt-2">
          <div className="w-12 h-12 rounded-2xl bg-[#faf8f5] border border-[#e8e4df] flex items-center justify-center mx-auto shadow-sm">
            <svg className="w-6 h-6" viewBox="0 0 24 24">
              <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
              <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
            </svg>
          </div>

          <h3 className="text-xl font-bold text-stone-900 font-heading">
            Sign in with Google
          </h3>
          <p className="text-xs text-stone-500 font-mono">
            OAuth Client ID: <span className="text-[#c96442] font-semibold">{GOOGLE_CLIENT_ID.substring(0, 24)}...</span>
          </p>
        </div>

        {/* Account Selection */}
        <div className="space-y-2 pt-2">
          {mockAccounts.map((acc) => (
            <button
              key={acc.email}
              onClick={() => handleSelectAccount(acc)}
              disabled={loading}
              className={`w-full p-3.5 rounded-xl border text-left transition flex items-center justify-between group ${
                selectedAccount === acc.email
                  ? 'bg-[#f5e9e2] border-[#c96442]'
                  : 'bg-[#faf8f5] hover:bg-white border-[#e8e4df] hover:border-stone-400'
              }`}
            >
              <div className="flex items-center gap-3">
                <img src={acc.photo} alt={acc.name} className="w-9 h-9 rounded-full object-cover border border-stone-200" />
                <div>
                  <h4 className="text-xs font-bold text-stone-900 group-hover:text-[#c96442] transition">
                    {acc.name}
                  </h4>
                  <span className="text-[11px] font-mono text-stone-500">{acc.email}</span>
                </div>
              </div>

              {selectedAccount === acc.email && loading ? (
                <div className="w-4 h-4 border-2 border-[#c96442] border-t-transparent rounded-full animate-spin" />
              ) : (
                <ArrowRight className="w-4 h-4 text-stone-400 group-hover:text-[#c96442] transition" />
              )}
            </button>
          ))}
        </div>

        {/* Security Footer */}
        <div className="pt-2 border-t border-[#e8e4df] flex items-center justify-between text-[11px] text-stone-400 font-mono">
          <span className="flex items-center gap-1">
            <Lock className="w-3 h-3 text-stone-500" /> Google OAuth2 SSL
          </span>
          <span className="text-emerald-700 font-semibold flex items-center gap-1">
            <CheckCircle2 className="w-3 h-3" /> Verified Client
          </span>
        </div>

      </div>
    </div>
  );
}
