import React from 'react';
import {
  MapPin,
  Building,
  DollarSign,
  Bookmark,
  BookmarkCheck,
  Clock,
  ArrowRight,
  Send,
} from 'lucide-react';
import MatchScoreBadge from './MatchScoreBadge';

export default function JobCard({
  job,
  onSelect,
  onToggleSave,
  onQuickApply,
  matchScore,
}) {
  const formatSalary = (min, max, cur = 'USD') => {
    if (!min && !max) return 'Salary Undisclosed';
    const formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: cur,
      maximumFractionDigits: 0,
    });
    if (min && max) return `${formatter.format(min)} - ${formatter.format(max)}`;
    return `${formatter.format(min || max)}`;
  };

  return (
    <div className="job-card">
      <div>
        <div className="job-card-header">
          <div>
            <h3 className="job-card-title">{job.title}</h3>
            <div className="job-card-company">
              <Building size={14} />
              <span>{job.company}</span>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            {matchScore !== undefined && <MatchScoreBadge score={matchScore} />}
            <button
              onClick={(e) => {
                e.stopPropagation();
                onToggleSave(job.id, job.isSaved);
              }}
              style={{
                color: job.isSaved ? '#818cf8' : 'var(--text-dim)',
                padding: '6px',
                borderRadius: '6px',
                background: job.isSaved ? 'rgba(99, 102, 241, 0.15)' : 'transparent',
              }}
              title={job.isSaved ? 'Remove from Saved' : 'Save Job'}
            >
              {job.isSaved ? <BookmarkCheck size={18} /> : <Bookmark size={18} />}
            </button>
          </div>
        </div>

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px', fontSize: '0.8rem', color: 'var(--text-muted)', margin: '10px 0' }}>
          <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
            <MapPin size={13} /> {job.location}
          </span>
          <span className="badge badge-purple">{job.workMode}</span>
          <span className="badge badge-blue">{job.employmentType?.replace('_', ' ')}</span>
          <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
            <DollarSign size={13} /> {formatSalary(job.minSalary, job.maxSalary, job.salaryCurrency)}
          </span>
        </div>

        <p className="job-desc">{job.description}</p>

        <div className="job-tags">
          {job.requiredSkills?.slice(0, 4).map((s, idx) => (
            <span key={idx} className="badge badge-gray">{s}</span>
          ))}
          {job.requiredSkills?.length > 4 && (
            <span className="badge badge-gray">+{job.requiredSkills.length - 4} more</span>
          )}
        </div>
      </div>

      <div className="job-card-footer">
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.78rem', color: 'var(--text-dim)' }}>
          <Clock size={13} />
          <span>{job.minExperienceYears || 0}-{job.maxExperienceYears || 3} yrs exp</span>
        </div>

        <div style={{ display: 'flex', gap: '8px' }}>
          {job.applicationStatus ? (
            <span className="badge badge-green" style={{ fontSize: '0.78rem', padding: '5px 10px' }}>
              ✓ {job.applicationStatus}
            </span>
          ) : (
            <button
              onClick={(e) => {
                e.stopPropagation();
                onQuickApply(job);
              }}
              className="btn btn-outline btn-sm"
              style={{ padding: '5px 10px', fontSize: '0.8rem' }}
            >
              <Send size={13} />
              <span>Track</span>
            </button>
          )}

          <button
            onClick={() => onSelect(job)}
            className="btn btn-primary btn-sm"
            style={{ padding: '5px 12px' }}
          >
            <span>Details</span>
            <ArrowRight size={14} />
          </button>
        </div>
      </div>
    </div>
  );
}
