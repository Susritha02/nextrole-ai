import React from 'react';
import {
  LayoutDashboard,
  Compass,
  Sparkles,
  Bookmark,
  Briefcase,
  FileText,
  Target,
  User,
  LogOut,
  X,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const NAV_ITEMS = [
  { id: 'overview', label: 'Overview', icon: LayoutDashboard },
  { id: 'explore', label: 'Explore Jobs', icon: Compass },
  { id: 'recommended', label: 'Recommended Jobs', icon: Sparkles },
  { id: 'saved', label: 'Saved Jobs', icon: Bookmark },
  { id: 'applications', label: 'My Applications', icon: Briefcase },
  { id: 'resume', label: 'Resume Analysis', icon: FileText },
  { id: 'skill-gap', label: 'Skill Gap Analysis', icon: Target },
  { id: 'profile', label: 'Profile', icon: User },
];

export default function Sidebar({ activeTab, setActiveTab, mobileOpen, setMobileOpen }) {
  const { user, logout } = useAuth();

  const handleNavClick = (id) => {
    setActiveTab(id);
    if (setMobileOpen) setMobileOpen(false);
  };

  return (
    <aside className={`sidebar ${mobileOpen ? 'open' : ''}`}>
      <div className="sidebar-logo">
        <div className="logo-icon-wrapper">
          <Sparkles size={22} />
        </div>
        <div className="logo-text">
          <h2>CareerAI</h2>
          <span>Job & Skill Tracker</span>
        </div>
        {mobileOpen && (
          <button
            onClick={() => setMobileOpen(false)}
            style={{ marginLeft: 'auto', color: 'var(--text-muted)' }}
          >
            <X size={20} />
          </button>
        )}
      </div>

      <nav className="sidebar-nav">
        {NAV_ITEMS.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => handleNavClick(item.id)}
              className={`nav-item ${isActive ? 'active' : ''}`}
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </button>
          );
        })}
      </nav>

      <div className="sidebar-footer">
        <div className="user-profile-badge" style={{ marginBottom: '10px' }}>
          <div className="user-avatar">
            {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
          </div>
          <div className="user-info">
            <h4>{user?.fullName || 'User'}</h4>
            <p>{user?.headline || 'Job Seeker'}</p>
          </div>
        </div>

        <button
          onClick={logout}
          className="nav-item"
          style={{ width: '100%', color: '#f87171' }}
        >
          <LogOut size={18} />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
}
