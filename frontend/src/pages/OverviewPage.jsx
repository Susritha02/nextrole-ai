import React, { useState, useEffect } from 'react';
import {
  Briefcase,
  Bookmark,
  Send,
  Calendar,
  Award,
  Sparkles,
  ArrowRight,
  TrendingUp,
} from 'lucide-react';
import StatCard from '../components/common/StatCard';
import MatchScoreBadge from '../components/jobs/MatchScoreBadge';
import JobModal from '../components/jobs/JobModal';
import ApplicationModal from '../components/applications/ApplicationModal';
import { dashboardApi, jobsApi, applicationApi } from '../api/services';

export default function OverviewPage({ onNavigate }) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState(null);
  const [trackingJob, setTrackingJob] = useState(null);

  const fetchOverview = async () => {
    try {
      setLoading(true);
      const res = await dashboardApi.getOverview();
      setData(res.data);
    } catch (err) {
      console.error('Failed to load dashboard overview:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOverview();
  }, []);

  const handleToggleSave = async (jobId, isSaved) => {
    try {
      if (isSaved) {
        await jobsApi.unsaveJob(jobId);
      } else {
        await jobsApi.saveJob(jobId);
      }
      fetchOverview();
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveApplication = async (payload) => {
    if (trackingJob) {
      await applicationApi.applyOrTrack(trackingJob.id, payload);
      fetchOverview();
    }
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
        <p>Loading your career analytics and AI recommendations...</p>
      </div>
    );
  }

  return (
    <div>
      {/* Welcome Banner */}
      <div
        style={{
          background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(168, 85, 247, 0.15))',
          border: '1px solid rgba(99, 102, 241, 0.3)',
          borderRadius: 'var(--radius-lg)',
          padding: '28px 32px',
          marginBottom: '28px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '16px',
        }}
      >
        <div>
          <h2 style={{ fontSize: '1.5rem', marginBottom: '6px' }}>
            Welcome back to your Career Command Center
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', maxWidth: '600px' }}>
            Our AI engine has matched your {data?.userSkills?.length || 0} profile skills and {data?.profileExperienceYears || 0} years experience with top job openings.
          </p>
        </div>

        <div style={{ display: 'flex', gap: '12px' }}>
          <button onClick={() => onNavigate('recommended')} className="btn btn-primary">
            <Sparkles size={16} />
            <span>AI Recommendations</span>
          </button>
          <button onClick={() => onNavigate('resume')} className="btn btn-outline">
            <span>Upload Resume</span>
          </button>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="stats-grid">
        <StatCard
          title="Total Active Jobs"
          value={data?.totalJobsCount || 0}
          icon={Briefcase}
          color="#818cf8"
          bg="rgba(99, 102, 241, 0.15)"
        />
        <StatCard
          title="Saved Jobs"
          value={data?.savedJobsCount || 0}
          icon={Bookmark}
          color="#38bdf8"
          bg="rgba(56, 189, 248, 0.15)"
        />
        <StatCard
          title="Total Applications"
          value={data?.totalApplicationsCount || 0}
          icon={Send}
          color="#f472b6"
          bg="rgba(244, 114, 182, 0.15)"
        />
        <StatCard
          title="Interviews Scheduled"
          value={data?.interviewCount || 0}
          icon={Calendar}
          color="#c084fc"
          bg="rgba(192, 132, 252, 0.15)"
        />
        <StatCard
          title="Job Offers"
          value={data?.offerCount || 0}
          icon={Award}
          color="#34d399"
          bg="rgba(52, 211, 153, 0.15)"
        />
      </div>

      {/* Two Column Layout: Top Matches & Application Pipeline Snapshot */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(450px, 1fr))', gap: '24px', marginBottom: '32px' }}>
        {/* Top AI Recommendations */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '1.15rem' }}>
              <Sparkles size={18} color="#818cf8" /> Top Recommended Matches
            </h3>
            <button
              onClick={() => onNavigate('recommended')}
              style={{ color: '#818cf8', fontSize: '0.85rem', display: 'flex', alignItems: 'center', gap: '4px' }}
            >
              <span>View All</span>
              <ArrowRight size={14} />
            </button>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            {data?.topRecommendations?.slice(0, 3).map((rec) => (
              <div
                key={rec.job.id}
                onClick={() => setSelectedJob(rec.job)}
                style={{
                  background: 'var(--bg-secondary)',
                  border: '1px solid var(--border-color)',
                  borderRadius: 'var(--radius-sm)',
                  padding: '16px',
                  cursor: 'pointer',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  transition: 'border-color 0.2s',
                }}
              >
                <div>
                  <h4 style={{ fontSize: '0.98rem', marginBottom: '2px' }}>{rec.job.title}</h4>
                  <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
                    {rec.job.company} • {rec.job.location} ({rec.job.workMode})
                  </p>
                  <p style={{ fontSize: '0.78rem', color: '#10b981', marginTop: '4px' }}>
                    Matching Skills: {rec.matchingSkills.slice(0, 3).join(', ') || 'Core fit'}
                  </p>
                </div>
                <MatchScoreBadge score={rec.matchPercentage} />
              </div>
            ))}
          </div>
        </div>

        {/* Recent Applications Pipeline */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '1.15rem' }}>
              <TrendingUp size={18} color="#34d399" /> Active Application Pipeline
            </h3>
            <button
              onClick={() => onNavigate('applications')}
              style={{ color: '#34d399', fontSize: '0.85rem', display: 'flex', alignItems: 'center', gap: '4px' }}
            >
              <span>Manage Kanban</span>
              <ArrowRight size={14} />
            </button>
          </div>

          {data?.recentApplications?.length > 0 ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {data.recentApplications.map((app) => (
                <div
                  key={app.id}
                  style={{
                    background: 'var(--bg-secondary)',
                    border: '1px solid var(--border-color)',
                    borderRadius: 'var(--radius-sm)',
                    padding: '14px 16px',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}
                >
                  <div>
                    <h4 style={{ fontSize: '0.95rem' }}>{app.jobTitle}</h4>
                    <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                      {app.company} • Applied: {app.appliedDate || 'Recently'}
                    </p>
                  </div>
                  <span className={`badge ${app.status === 'OFFER' ? 'badge-green' : app.status === 'INTERVIEW' ? 'badge-purple' : 'badge-blue'}`}>
                    {app.status}
                  </span>
                </div>
              ))}
            </div>
          ) : (
            <div style={{ textAlign: 'center', padding: '30px', color: 'var(--text-muted)' }}>
              <p>No active applications tracked yet.</p>
              <button
                onClick={() => onNavigate('explore')}
                className="btn btn-outline btn-sm"
                style={{ marginTop: '10px' }}
              >
                Explore & Track Jobs
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Modals */}
      {selectedJob && (
        <JobModal
          job={selectedJob}
          onClose={() => setSelectedJob(null)}
          onToggleSave={handleToggleSave}
          onTrackApplication={(job) => setTrackingJob(job)}
          onAnalyzeGap={(jobId) => onNavigate('skill-gap')}
        />
      )}

      {trackingJob && (
        <ApplicationModal
          job={trackingJob}
          onClose={() => setTrackingJob(null)}
          onSave={handleSaveApplication}
        />
      )}
    </div>
  );
}
