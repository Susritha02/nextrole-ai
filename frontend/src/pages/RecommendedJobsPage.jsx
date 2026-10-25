import React, { useState, useEffect } from 'react';
import {
  Sparkles,
  Info,
  CheckCircle2,
  AlertCircle,
  Clock,
  MapPin,
  Building,
  Target,
  Send,
  Bookmark,
  BookmarkCheck,
  ChevronRight,
} from 'lucide-react';
import MatchScoreBadge from '../components/jobs/MatchScoreBadge';
import JobModal from '../components/jobs/JobModal';
import ApplicationModal from '../components/applications/ApplicationModal';
import { recommendationApi, jobsApi, applicationApi } from '../api/services';

export default function RecommendedJobsPage({ onNavigate }) {
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState(null);
  const [selectedMatch, setSelectedMatch] = useState(null);
  const [trackingJob, setTrackingJob] = useState(null);

  const fetchRecommendations = async () => {
    try {
      setLoading(true);
      const res = await recommendationApi.getRecommendations(15);
      setRecommendations(res.data);
    } catch (err) {
      console.error('Failed to load recommendations:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecommendations();
  }, []);

  const handleToggleSave = async (jobId, isSaved) => {
    try {
      if (isSaved) {
        await jobsApi.unsaveJob(jobId);
      } else {
        await jobsApi.saveJob(jobId);
      }
      setRecommendations((prev) =>
        prev.map((r) =>
          r.job.id === jobId ? { ...r, job: { ...r.job, isSaved: !isSaved } } : r
        )
      );
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveApplication = async (payload) => {
    if (trackingJob) {
      await applicationApi.applyOrTrack(trackingJob.id, payload);
      fetchRecommendations();
    }
  };

  return (
    <div>
      {/* Algorithm Transparency Banner */}
      <div
        style={{
          background: 'rgba(99, 102, 241, 0.08)',
          border: '1px solid rgba(99, 102, 241, 0.25)',
          borderRadius: 'var(--radius-md)',
          padding: '18px 24px',
          marginBottom: '24px',
          display: 'flex',
          alignItems: 'flex-start',
          gap: '14px',
        }}
      >
        <Info size={22} color="#818cf8" style={{ flexShrink: 0, marginTop: '2px' }} />
        <div>
          <h4 style={{ color: '#818cf8', fontSize: '0.95rem', marginBottom: '4px' }}>
            Transparent AI Scoring Algorithm
          </h4>
          <p style={{ fontSize: '0.84rem', color: 'var(--text-main)', lineHeight: '1.5' }}>
            Match percentages are computed deterministically using:
            <strong> 60% Skill Overlap & TF-IDF Cosine Similarity</strong> (weighted by requirement criticality) +
            <strong> 25% Experience Compatibility Curve</strong> +
            <strong> 15% Work Mode & Location Preference</strong>.
            Scores reflect authentic capability alignment without fabricated random metrics.
          </p>
        </div>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
          <p>Evaluating AI recommendation vectors against all active jobs...</p>
        </div>
      ) : recommendations.length > 0 ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {recommendations.map((rec) => {
            const job = rec.job;
            return (
              <div
                key={job.id}
                className="card"
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '16px',
                  borderLeft: rec.matchPercentage >= 80 ? '4px solid #10b981' : rec.matchPercentage >= 65 ? '4px solid #6366f1' : '4px solid #f59e0b',
                }}
              >
                {/* Header row */}
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '12px' }}>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '4px' }}>
                      <h3 style={{ fontSize: '1.2rem' }}>{job.title}</h3>
                      <span className="badge badge-amber">Demo Job</span>
                    </div>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '14px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                      <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                        <Building size={14} /> {job.company}
                      </span>
                      <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                        <MapPin size={14} /> {job.location}
                      </span>
                      <span className="badge badge-purple">{job.workMode}</span>
                      <span className="badge badge-blue">${job.minSalary?.toLocaleString()} - ${job.maxSalary?.toLocaleString()}</span>
                    </div>
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <MatchScoreBadge score={rec.matchPercentage} />
                    <button
                      onClick={() => handleToggleSave(job.id, job.isSaved)}
                      style={{
                        color: job.isSaved ? '#818cf8' : 'var(--text-dim)',
                        padding: '8px',
                        borderRadius: '6px',
                        background: job.isSaved ? 'rgba(99, 102, 241, 0.15)' : 'var(--bg-input)',
                      }}
                      title={job.isSaved ? 'Saved' : 'Save'}
                    >
                      {job.isSaved ? <BookmarkCheck size={18} /> : <Bookmark size={18} />}
                    </button>
                  </div>
                </div>

                {/* AI Explanation Box */}
                <div style={{ background: 'var(--bg-secondary)', padding: '14px 18px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)' }}>
                  <p style={{ fontSize: '0.88rem', color: 'var(--text-main)', lineHeight: '1.5' }}>
                    <span style={{ color: '#818cf8', fontWeight: 600 }}>Recommendation Insight: </span>
                    {rec.explanation}
                  </p>
                </div>

                {/* Detailed Compatibility Chips */}
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '12px', fontSize: '0.82rem' }}>
                  <div style={{ background: 'var(--bg-input)', padding: '12px', borderRadius: 'var(--radius-sm)' }}>
                    <span style={{ color: '#34d399', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
                      <CheckCircle2 size={14} /> Matching Skills ({rec.matchingSkills?.length || 0})
                    </span>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                      {rec.matchingSkills?.length > 0 ? (
                        rec.matchingSkills.map((s, idx) => (
                          <span key={idx} className="badge badge-green">{s}</span>
                        ))
                      ) : (
                        <span style={{ color: 'var(--text-dim)' }}>No exact skill overlap</span>
                      )}
                    </div>
                  </div>

                  <div style={{ background: 'var(--bg-input)', padding: '12px', borderRadius: 'var(--radius-sm)' }}>
                    <span style={{ color: '#fbbf24', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
                      <AlertCircle size={14} /> Missing Skills ({rec.missingSkills?.length || 0})
                    </span>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                      {rec.missingSkills?.length > 0 ? (
                        rec.missingSkills.map((s, idx) => (
                          <span key={idx} className="badge badge-amber">{s}</span>
                        ))
                      ) : (
                        <span style={{ color: '#34d399' }}>All required skills present!</span>
                      )}
                    </div>
                  </div>

                  <div style={{ background: 'var(--bg-input)', padding: '12px', borderRadius: 'var(--radius-sm)' }}>
                    <span style={{ color: 'var(--text-muted)', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
                      <Clock size={14} /> Experience & Location Fit
                    </span>
                    <p style={{ color: 'var(--text-main)', fontSize: '0.8rem' }}>{rec.experienceCompatibility}</p>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.78rem', marginTop: '2px' }}>{rec.locationCompatibility}</p>
                  </div>
                </div>

                {/* Footer Actions */}
                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', paddingTop: '8px', borderTop: '1px solid rgba(255, 255, 255, 0.05)' }}>
                  <button
                    onClick={() => onNavigate('skill-gap')}
                    className="btn btn-outline btn-sm"
                  >
                    <Target size={14} />
                    <span>Skill Gap Roadmap</span>
                  </button>

                  <button
                    onClick={() => setTrackingJob(job)}
                    className="btn btn-outline btn-sm"
                  >
                    <Send size={14} />
                    <span>{job.applicationStatus ? `Applied (${job.applicationStatus})` : 'Track Application'}</span>
                  </button>

                  <button
                    onClick={() => {
                      setSelectedJob(job);
                      setSelectedMatch(rec);
                    }}
                    className="btn btn-primary btn-sm"
                  >
                    <span>View Full Details</span>
                    <ChevronRight size={14} />
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="card" style={{ textAlign: 'center', padding: '60px 20px', color: 'var(--text-muted)' }}>
          <h3>No recommendations available yet</h3>
          <p style={{ fontSize: '0.88rem', marginTop: '8px' }}>
            Update your profile skills or upload a resume to receive AI match scoring!
          </p>
          <button
            onClick={() => onNavigate('profile')}
            className="btn btn-primary"
            style={{ marginTop: '16px' }}
          >
            Add Skills to Profile
          </button>
        </div>
      )}

      {/* Modals */}
      {selectedJob && (
        <JobModal
          job={selectedJob}
          matchData={selectedMatch}
          onClose={() => {
            setSelectedJob(null);
            setSelectedMatch(null);
          }}
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
