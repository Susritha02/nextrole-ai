import React, { useState, useEffect } from 'react';
import { Bookmark, Sparkles, Send, Trash2, ArrowRight } from 'lucide-react';
import JobCard from '../components/jobs/JobCard';
import JobModal from '../components/jobs/JobModal';
import ApplicationModal from '../components/applications/ApplicationModal';
import { jobsApi, applicationApi } from '../api/services';

export default function SavedJobsPage({ onNavigate }) {
  const [savedJobs, setSavedJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState(null);
  const [trackingJob, setTrackingJob] = useState(null);

  const fetchSaved = async () => {
    try {
      setLoading(true);
      const res = await jobsApi.getSavedJobs();
      setSavedJobs(res.data);
    } catch (err) {
      console.error('Failed to load saved jobs:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSaved();
  }, []);

  const handleToggleSave = async (jobId) => {
    try {
      await jobsApi.unsaveJob(jobId);
      setSavedJobs((prev) => prev.filter((j) => j.id !== jobId));
      if (selectedJob && selectedJob.id === jobId) {
        setSelectedJob(null);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveApplication = async (payload) => {
    if (trackingJob) {
      await applicationApi.applyOrTrack(trackingJob.id, payload);
      fetchSaved();
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem' }}>Saved Opportunities</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
            You have {savedJobs.length} bookmarked {savedJobs.length === 1 ? 'job' : 'jobs'}
          </p>
        </div>

        <button onClick={() => onNavigate('explore')} className="btn btn-outline btn-sm">
          <span>Find More Jobs</span>
          <ArrowRight size={14} />
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
          <p>Loading saved jobs...</p>
        </div>
      ) : savedJobs.length > 0 ? (
        <div className="jobs-grid">
          {savedJobs.map((job) => (
            <JobCard
              key={job.id}
              job={{ ...job, isSaved: true }}
              onSelect={(j) => setSelectedJob(j)}
              onToggleSave={handleToggleSave}
              onQuickApply={(j) => setTrackingJob(j)}
            />
          ))}
        </div>
      ) : (
        <div className="card" style={{ textAlign: 'center', padding: '60px 20px', color: 'var(--text-muted)' }}>
          <Bookmark size={40} style={{ margin: '0 auto 12px', color: 'var(--text-dim)' }} />
          <h3>No saved jobs yet</h3>
          <p style={{ fontSize: '0.88rem', marginTop: '6px' }}>
            Click the bookmark icon on any job in Explore or Recommended Jobs to save it for later.
          </p>
          <button
            onClick={() => onNavigate('explore')}
            className="btn btn-primary"
            style={{ marginTop: '16px' }}
          >
            Explore Jobs
          </button>
        </div>
      )}

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
