import React, { useState, useEffect } from 'react';
import { Search, Filter, SlidersHorizontal, RefreshCw } from 'lucide-react';
import JobCard from '../components/jobs/JobCard';
import JobModal from '../components/jobs/JobModal';
import ApplicationModal from '../components/applications/ApplicationModal';
import { jobsApi, applicationApi } from '../api/services';

export default function ExploreJobsPage({ onNavigate }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [workMode, setWorkMode] = useState('ALL');
  const [employmentType, setEmploymentType] = useState('ALL');
  const [maxExp, setMaxExp] = useState('');
  const [sortBy, setSortBy] = useState('newest');

  const [selectedJob, setSelectedJob] = useState(null);
  const [trackingJob, setTrackingJob] = useState(null);

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const params = {
        keyword: keyword || undefined,
        workMode: workMode !== 'ALL' ? workMode : undefined,
        employmentType: employmentType !== 'ALL' ? employmentType : undefined,
        maxExp: maxExp ? parseFloat(maxExp) : undefined,
        sortBy,
      };
      const res = await jobsApi.getJobs(params);
      setJobs(res.data);
    } catch (err) {
      console.error('Failed to fetch jobs:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      fetchJobs();
    }, 300);
    return () => clearTimeout(delayDebounce);
  }, [keyword, workMode, employmentType, maxExp, sortBy]);

  const handleToggleSave = async (jobId, isSaved) => {
    try {
      if (isSaved) {
        await jobsApi.unsaveJob(jobId);
      } else {
        await jobsApi.saveJob(jobId);
      }
      setJobs((prev) =>
        prev.map((j) => (j.id === jobId ? { ...j, isSaved: !isSaved } : j))
      );
      if (selectedJob && selectedJob.id === jobId) {
        setSelectedJob((prev) => ({ ...prev, isSaved: !isSaved }));
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveApplication = async (payload) => {
    if (trackingJob) {
      await applicationApi.applyOrTrack(trackingJob.id, payload);
      fetchJobs();
    }
  };

  return (
    <div>
      {/* Search & Filter Bar */}
      <div className="filter-bar">
        <div className="filter-input-wrap">
          <Search size={18} />
          <input
            type="text"
            placeholder="Search by role, company, skills (e.g. Java, React)..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>

        <select
          value={workMode}
          onChange={(e) => setWorkMode(e.target.value)}
          aria-label="Work Mode"
        >
          <option value="ALL">All Work Modes</option>
          <option value="REMOTE">Remote</option>
          <option value="HYBRID">Hybrid</option>
          <option value="ONSITE">Onsite</option>
        </select>

        <select
          value={employmentType}
          onChange={(e) => setEmploymentType(e.target.value)}
          aria-label="Employment Type"
        >
          <option value="ALL">All Employment Types</option>
          <option value="FULL_TIME">Full Time</option>
          <option value="PART_TIME">Part Time</option>
          <option value="CONTRACT">Contract</option>
          <option value="INTERNSHIP">Internship</option>
        </select>

        <select
          value={maxExp}
          onChange={(e) => setMaxExp(e.target.value)}
          aria-label="Experience Level"
        >
          <option value="">Any Experience</option>
          <option value="1">Entry Level (&le; 1 yr)</option>
          <option value="2">Junior (&le; 2 yrs)</option>
          <option value="3">Mid Level (&le; 3 yrs)</option>
          <option value="5">Experienced (&le; 5 yrs)</option>
        </select>

        <select
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value)}
          aria-label="Sort By"
        >
          <option value="newest">Sort: Newest</option>
          <option value="salary">Sort: Highest Salary</option>
        </select>
      </div>

      {/* Results Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          Showing <strong>{jobs.length}</strong> active job opportunities
        </p>
        <span className="badge badge-amber">Demo Job Listings</span>
      </div>

      {/* Jobs Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
          <RefreshCw className="spin" size={24} style={{ marginBottom: '12px' }} />
          <p>Filtering job listings...</p>
        </div>
      ) : jobs.length > 0 ? (
        <div className="jobs-grid">
          {jobs.map((job) => (
            <JobCard
              key={job.id}
              job={job}
              onSelect={(j) => setSelectedJob(j)}
              onToggleSave={handleToggleSave}
              onQuickApply={(j) => setTrackingJob(j)}
            />
          ))}
        </div>
      ) : (
        <div className="card" style={{ textAlign: 'center', padding: '60px 20px', color: 'var(--text-muted)' }}>
          <h3>No jobs found matching your criteria</h3>
          <p style={{ fontSize: '0.88rem', marginTop: '8px' }}>
            Try clearing filters or searching for terms like "Java", "React", "Python", or "Remote".
          </p>
          <button
            onClick={() => {
              setKeyword('');
              setWorkMode('ALL');
              setEmploymentType('ALL');
              setMaxExp('');
            }}
            className="btn btn-outline"
            style={{ marginTop: '16px' }}
          >
            Reset All Filters
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
