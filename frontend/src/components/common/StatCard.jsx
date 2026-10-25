import React from 'react';

export default function StatCard({ title, value, icon: Icon, color = 'var(--primary)', bg = 'var(--primary-light)', subtitle }) {
  return (
    <div className="stat-card">
      <div className="stat-icon" style={{ backgroundColor: bg, color }}>
        <Icon size={24} />
      </div>
      <div className="stat-info">
        <p>{title}</p>
        <h3 style={{ color }}>{value}</h3>
        {subtitle && <span style={{ fontSize: '0.72rem', color: 'var(--text-dim)' }}>{subtitle}</span>}
      </div>
    </div>
  );
}
