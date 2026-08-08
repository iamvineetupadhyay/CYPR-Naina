import React, { useEffect, useState } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Line, Doughnut } from 'react-chartjs-2';
import { Download } from 'lucide-react';
import { fetchAllBreaches } from '../services/api';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

export default function AnalyticsDashboard() {
  const [breaches, setBreaches] = useState([]);

  useEffect(() => {
    fetchAllBreaches().then(setBreaches);
  }, []);

  const breachTimelineData = {
    labels: ['2012', '2013', '2016', '2019', '2021', '2023', '2024'],
    datasets: [
      {
        label: 'Public Expositions (Millions)',
        data: [68.6, 152.4, 164.6, 910.1, 450.0, 620.0, 780.0],
        borderColor: '#c96442',
        backgroundColor: 'rgba(201, 100, 66, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  };

  const dataTypeData = {
    labels: ['Passwords', 'Email Addresses', 'Usernames', 'Phone Numbers', 'IP Addresses'],
    datasets: [
      {
        data: [42, 38, 25, 18, 12],
        backgroundColor: ['#ef4444', '#c96442', '#f59e0b', '#10b981', '#6366f1'],
        borderWidth: 1,
        borderColor: '#ffffff',
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        labels: { color: '#44403c', font: { family: 'JetBrains Mono', size: 11 } },
      },
    },
    scales: {
      x: { grid: { color: '#f5f2eb' }, ticks: { color: '#78716c', font: { family: 'JetBrains Mono' } } },
      y: { grid: { color: '#f5f2eb' }, ticks: { color: '#78716c', font: { family: 'JetBrains Mono' } } },
    },
  };

  const handleExportJSON = () => {
    const jsonString = `data:text/json;charset=utf-8,${encodeURIComponent(
      JSON.stringify({ reportDate: new Date().toISOString(), breaches }, null, 2)
    )}`;
    const downloadAnchor = document.createElement('a');
    downloadAnchor.setAttribute('href', jsonString);
    downloadAnchor.setAttribute('download', `cypr_report_${Date.now()}.json`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();
  };

  return (
    <div className="space-y-6 animate-fadeIn max-w-xl mx-auto py-4">
      
      {/* Tool Title */}
      <div className="text-center">
        <h2 className="text-2xl font-extrabold text-stone-900 tracking-tight font-heading">
          Threat Analytics & Reports
        </h2>
      </div>

      <div className="claude-card p-6 space-y-4 shadow-sm">
        <div className="flex items-center justify-between">
          <div className="text-[11px] font-mono font-bold text-[#c96442] uppercase tracking-widest">
            EXPOSURE TIMELINE TREND
          </div>
          <button
            onClick={handleExportJSON}
            className="px-3 py-1.5 claude-btn-primary font-mono text-xs font-bold rounded-lg transition flex items-center gap-1.5"
          >
            <Download className="w-3.5 h-3.5" />
            Export JSON
          </button>
        </div>
        <Line data={breachTimelineData} options={chartOptions} />
      </div>

      <div className="claude-card p-6 space-y-4 shadow-sm">
        <div className="text-[11px] font-mono font-bold text-[#c96442] uppercase tracking-widest">
          COMPROMISED DATA VECTOR BREAKDOWN
        </div>
        <div className="w-56 mx-auto py-2">
          <Doughnut data={dataTypeData} options={{ plugins: { legend: { labels: { color: '#44403c', font: { family: 'JetBrains Mono' } } } } }} />
        </div>
      </div>

    </div>
  );
}
