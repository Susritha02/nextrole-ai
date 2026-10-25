import React from 'react';
import { Menu, Info, ShieldCheck } from 'lucide-react';

const TAB_TITLES = {
  overview: { title: 'Dashboard Overview', subtitle: 'Track your career pipeline and AI recommendations' },
  explore: { title: 'Explore Job Listings', subtitle: 'Search and filter active technology opportunities' },
  recommended: { title: 'AI Recommended Jobs', subtitle: 'Personalized job matches based on your skills and experience' },
  saved: { title: 'Saved Jobs', subtitle: 'Bookmarked opportunities you want to revisit' },
  applications: { title: 'My Job Applications', subtitle: 'Visual Kanban pipeline and stage management' },
  resume: { title: 'AI Resume Analysis', subtitle: 'Automated skill extraction and profile parsing' },
  'skill-gap': { title: 'Skill Gap & Roadmaps', subtitle: 'Identify missing skills and track learning progress' },
  profile: { title: 'My Career Profile', subtitle: 'Manage your technical skills and experience' },
};

export default function Header({ activeTab, setMobileOpen }) {
  const current = TAB_TITLES[activeTab] || { title: 'Dashboard', subtitle: '' };

  return (
    <header className="top-header">
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <button
          onClick={() => setMobileOpen(true)}
          style={{ display: 'none', color: 'var(--text-main)' }}
          className="mobile-menu-btn"
        >
          <Menu size={24} />
        </button>

        <div className="page-title">
          <h1>{current.title}</h1>
          <p>{current.subtitle}</p>
        </div>
      </div>

      <div className="header-actions">
        <div className="demo-pill" title="Demo mode enabled for local testing">
          <Info size={14} />
          <span>Demo Environment</span>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.82rem', color: '#10b981' }}>
          <ShieldCheck size={16} />
          <span style={{ fontWeight: 500 }}>JWT Secured</span>
        </div>
      </div>
    </header>
  );
}
