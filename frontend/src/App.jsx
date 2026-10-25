import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import Sidebar from './components/layout/Sidebar';
import Header from './components/layout/Header';

import OverviewPage from './pages/OverviewPage';
import ExploreJobsPage from './pages/ExploreJobsPage';
import RecommendedJobsPage from './pages/RecommendedJobsPage';
import SavedJobsPage from './pages/SavedJobsPage';
import ApplicationsPage from './pages/ApplicationsPage';
import ResumeAnalysisPage from './pages/ResumeAnalysisPage';
import SkillGapPage from './pages/SkillGapPage';
import ProfilePage from './pages/ProfilePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

export default function App() {
  const { user, loading } = useAuth();
  const [authView, setAuthView] = useState('login'); // 'login' or 'register'
  const [activeTab, setActiveTab] = useState('overview');
  const [mobileOpen, setMobileOpen] = useState(false);
  const [targetJobIdForGap, setTargetJobIdForGap] = useState(null);

  if (loading) {
    return (
      <div
        style={{
          minHeight: '100vh',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          backgroundColor: 'var(--bg-primary)',
          color: 'var(--text-muted)',
        }}
      >
        <p>Initializing CareerAI Workspace...</p>
      </div>
    );
  }

  // Unauthenticated view
  if (!user) {
    if (authView === 'register') {
      return <RegisterPage onSwitchToLogin={() => setAuthView('login')} />;
    }
    return <LoginPage onSwitchToRegister={() => setAuthView('register')} />;
  }

  const navigateTo = (tab, extra = {}) => {
    setActiveTab(tab);
    if (extra.jobId) {
      setTargetJobIdForGap(extra.jobId);
    }
  };

  const renderCurrentView = () => {
    switch (activeTab) {
      case 'overview':
        return <OverviewPage onNavigate={navigateTo} />;
      case 'explore':
        return <ExploreJobsPage onNavigate={navigateTo} />;
      case 'recommended':
        return <RecommendedJobsPage onNavigate={navigateTo} />;
      case 'saved':
        return <SavedJobsPage onNavigate={navigateTo} />;
      case 'applications':
        return <ApplicationsPage onNavigate={navigateTo} />;
      case 'resume':
        return <ResumeAnalysisPage onNavigate={navigateTo} />;
      case 'skill-gap':
        return <SkillGapPage targetJobId={targetJobIdForGap} onNavigate={navigateTo} />;
      case 'profile':
        return <ProfilePage />;
      default:
        return <OverviewPage onNavigate={navigateTo} />;
    }
  };

  return (
    <div className="app-container">
      <Sidebar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        mobileOpen={mobileOpen}
        setMobileOpen={setMobileOpen}
      />

      <div className="main-content">
        <Header activeTab={activeTab} setMobileOpen={setMobileOpen} />

        <main className="content-body">
          {renderCurrentView()}
        </main>
      </div>
    </div>
  );
}
