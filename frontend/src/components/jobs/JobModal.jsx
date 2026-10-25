import React from 'react';
import {
  X,
  Building,
  MapPin,
  DollarSign,
  Briefcase,
  CheckCircle2,
  Bookmark,
  BookmarkCheck,
  Send,
  Target,
} from 'lucide-react';
import MatchScoreBadge from './MatchScoreBadge';

export default function JobModal({
  job,
  onClose,
  onToggleSave,
  onTrackApplication,
  onAnalyzeGap,
  matchData,
}) {
  if (!job) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
              <span className="badge badge-amber">Demo Job Listing</span>
              {matchData && <MatchScoreBadge score={matchData.matchPercentage} />}
            </div>
            <h2 style={{ fontSize: '1.35rem' }}>{job.title}</h2>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', color: 'var(--text-muted)', fontSize: '0.9rem', marginTop: '4px' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <Building size={14} /> {job.company}
              </span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <MapPin size={14} /> {job.location}
              </span>
              <span className="badge badge-purple">{job.workMode}</span>
            </div>
          </div>
          <button onClick={onClose} style={{ color: 'var(--text-muted)', padding: '6px' }}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* AI Recommendation Insights if available */}
          {matchData && (
            <div style={{ background: 'rgba(99, 102, 241, 0.08)', border: '1px solid rgba(99, 102, 241, 0.25)', borderRadius: 'var(--radius-md)', padding: '16px' }}>
              <h4 style={{ color: '#818cf8', fontSize: '0.95rem', marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle2 size={16} /> Why AI Recommended This Job
              </h4>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-main)', marginBottom: '12px' }}>
                {matchData.explanation}
              </p>

              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '10px', fontSize: '0.8rem' }}>
                <div style={{ background: 'var(--bg-card)', padding: '10px', borderRadius: '6px' }}>
                  <span style={{ color: 'var(--text-dim)', display: 'block' }}>Matching Skills</span>
                  <strong style={{ color: '#34d399' }}>{matchData.matchingSkills?.join(', ') || 'None yet'}</strong>
                </div>
                <div style={{ background: 'var(--bg-card)', padding: '10px', borderRadius: '6px' }}>
                  <span style={{ color: 'var(--text-dim)', display: 'block' }}>Missing Skills</span>
                  <strong style={{ color: '#fbbf24' }}>{matchData.missingSkills?.join(', ') || 'None! Fully matched'}</strong>
                </div>
                <div style={{ background: 'var(--bg-card)', padding: '10px', borderRadius: '6px' }}>
                  <span style={{ color: 'var(--text-dim)', display: 'block' }}>Experience Match</span>
                  <span>{matchData.experienceCompatibility}</span>
                </div>
              </div>
            </div>
          )}

          {/* Quick Metrics Bar */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: '12px', background: 'var(--bg-card)', padding: '14px', borderRadius: 'var(--radius-sm)' }}>
            <div>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', display: 'block' }}>Experience Req</span>
              <strong style={{ fontSize: '0.9rem' }}>{job.minExperienceYears || 0} - {job.maxExperienceYears || 5} years</strong>
            </div>
            <div>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', display: 'block' }}>Employment Type</span>
              <strong style={{ fontSize: '0.9rem' }}>{job.employmentType?.replace('_', ' ')}</strong>
            </div>
            <div>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', display: 'block' }}>Salary Range</span>
              <strong style={{ fontSize: '0.9rem', color: '#10b981' }}>
                ${job.minSalary?.toLocaleString()} - ${job.maxSalary?.toLocaleString()}
              </strong>
            </div>
          </div>

          {/* Required Skills */}
          <div>
            <h4 style={{ fontSize: '0.92rem', marginBottom: '8px' }}>Required Skills</h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
              {job.requiredSkills?.map((s, idx) => (
                <span key={idx} className="badge badge-blue" style={{ padding: '6px 12px', fontSize: '0.82rem' }}>
                  {s}
                </span>
              ))}
              {job.optionalSkills?.map((s, idx) => (
                <span key={idx} className="badge badge-gray" style={{ padding: '6px 12px', fontSize: '0.82rem' }}>
                  {s} (Nice to have)
                </span>
              ))}
            </div>
          </div>

          {/* Description */}
          <div>
            <h4 style={{ fontSize: '0.92rem', marginBottom: '8px' }}>Job Overview</h4>
            <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', lineHeight: '1.6' }}>
              {job.description}
            </p>
          </div>

          {/* Responsibilities */}
          {job.responsibilities && (
            <div>
              <h4 style={{ fontSize: '0.92rem', marginBottom: '8px' }}>Key Responsibilities</h4>
              <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', lineHeight: '1.6' }}>
                {job.responsibilities}
              </p>
            </div>
          )}

          {/* Eligibility */}
          {job.eligibility && (
            <div>
              <h4 style={{ fontSize: '0.92rem', marginBottom: '8px' }}>Candidate Eligibility</h4>
              <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)', lineHeight: '1.6' }}>
                {job.eligibility}
              </p>
            </div>
          )}
        </div>

        <div className="modal-footer">
          <button
            onClick={() => onToggleSave(job.id, job.isSaved)}
            className="btn btn-outline"
          >
            {job.isSaved ? <BookmarkCheck size={16} /> : <Bookmark size={16} />}
            <span>{job.isSaved ? 'Saved' : 'Save Job'}</span>
          </button>

          {onAnalyzeGap && (
            <button
              onClick={() => {
                onClose();
                onAnalyzeGap(job.id);
              }}
              className="btn btn-outline"
            >
              <Target size={16} />
              <span>Skill Gap Roadmap</span>
            </button>
          )}

          <button
            onClick={() => {
              onClose();
              onTrackApplication(job);
            }}
            className="btn btn-primary"
          >
            <Send size={16} />
            <span>{job.applicationStatus ? `Manage Application (${job.applicationStatus})` : 'Track Application'}</span>
          </button>
        </div>
      </div>
    </div>
  );
}
