import React, { useState, useEffect } from 'react';
import {
  UploadCloud,
  FileText,
  CheckCircle2,
  AlertTriangle,
  Award,
  Clock,
  Sparkles,
  ArrowRight,
  Info,
} from 'lucide-react';
import { resumeApi, profileApi } from '../api/services';
import { useAuth } from '../context/AuthContext';

export default function ResumeAnalysisPage({ onNavigate }) {
  const { refreshProfile } = useAuth();
  const [file, setFile] = useState(null);
  const [analysis, setAnalysis] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [dragActive, setDragActive] = useState(false);

  useEffect(() => {
    const fetchLatest = async () => {
      try {
        setLoading(true);
        const res = await resumeApi.getLatest();
        if (res.data) {
          setAnalysis(res.data);
        }
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchLatest();
  }, []);

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFileSelected(e.dataTransfer.files[0]);
    }
  };

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      handleFileSelected(e.target.files[0]);
    }
  };

  const handleFileSelected = (selectedFile) => {
    setError('');
    const ext = selectedFile.name.split('.').pop().toLowerCase();
    if (!['pdf', 'docx', 'txt'].includes(ext)) {
      setError('Please select a valid PDF or DOCX file.');
      return;
    }
    if (selectedFile.size > 10 * 1024 * 1024) {
      setError('File size exceeds the 10MB limit.');
      return;
    }
    setFile(selectedFile);
  };

  const handleUpload = async () => {
    if (!file) return;
    try {
      setUploading(true);
      setError('');
      const res = await resumeApi.uploadResume(file);
      setAnalysis(res.data);
      await refreshProfile();
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to process resume. Please ensure the file is not corrupted.');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      {/* Disclaimer Alert */}
      <div
        style={{
          background: 'rgba(245, 158, 11, 0.08)',
          border: '1px solid rgba(245, 158, 11, 0.25)',
          borderRadius: 'var(--radius-md)',
          padding: '16px 20px',
          marginBottom: '24px',
          display: 'flex',
          alignItems: 'center',
          gap: '12px',
        }}
      >
        <Info size={20} color="#fbbf24" style={{ flexShrink: 0 }} />
        <p style={{ fontSize: '0.84rem', color: 'var(--text-main)' }}>
          <strong>Automated Analysis Notice:</strong> Resume parsing uses NLP pattern extraction and keyword heuristics. Because resume formatting varies widely, automated parsing may be imperfect. You can review and adjust extracted skills at any time in your Career Profile.
        </p>
      </div>

      {/* Upload Zone Card */}
      <div className="card" style={{ marginBottom: '28px' }}>
        <h3 style={{ fontSize: '1.15rem', marginBottom: '8px' }}>Upload Your Resume</h3>
        <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '20px' }}>
          Upload your resume in PDF or DOCX format (Max 10MB). Our engine will detect technical skills, compute relevant experience, and update your recommendation profile.
        </p>

        <div
          className={`dropzone ${dragActive ? 'active' : ''}`}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
          onClick={() => document.getElementById('resume-file-input').click()}
        >
          <input
            id="resume-file-input"
            type="file"
            accept=".pdf,.docx,.txt"
            onChange={handleFileChange}
            style={{ display: 'none' }}
          />

          <UploadCloud size={44} style={{ color: 'var(--primary)', margin: '0 auto 12px' }} />
          <p style={{ fontWeight: 600, fontSize: '0.95rem' }}>
            {file ? file.name : 'Click to browse or drag & drop your resume here'}
          </p>
          <p style={{ fontSize: '0.78rem', color: 'var(--text-dim)', marginTop: '4px' }}>
            Supports PDF, DOCX, or plain text up to 10MB
          </p>
        </div>

        {error && (
          <p style={{ color: '#f87171', fontSize: '0.85rem', marginTop: '12px', display: 'flex', alignItems: 'center', gap: '6px' }}>
            <AlertTriangle size={15} /> {error}
          </p>
        )}

        {file && (
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '16px' }}>
            <button
              onClick={handleUpload}
              disabled={uploading}
              className="btn btn-primary"
            >
              <Sparkles size={16} />
              <span>{uploading ? 'Extracting Skills & Experience...' : 'Analyze Resume'}</span>
            </button>
          </div>
        )}
      </div>

      {/* Analysis Results */}
      {analysis && (
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '12px', marginBottom: '20px' }}>
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                <span className="badge badge-green">
                  <CheckCircle2 size={13} /> Parsed Successfully
                </span>
                <span style={{ fontSize: '0.78rem', color: 'var(--text-dim)' }}>
                  {analysis.fileName} • {analysis.analyzedAt ? new Date(analysis.analyzedAt).toLocaleDateString() : 'Just now'}
                </span>
              </div>
              <h3 style={{ fontSize: '1.25rem' }}>Resume Extraction Summary</h3>
            </div>

            <button onClick={() => onNavigate('recommended')} className="btn btn-primary btn-sm">
              <Sparkles size={14} />
              <span>View Updated Job Recommendations</span>
            </button>
          </div>

          {/* Quick Metrics */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px', marginBottom: '24px' }}>
            <div style={{ background: 'var(--bg-secondary)', padding: '16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)' }}>
              <span style={{ fontSize: '0.78rem', color: 'var(--text-dim)', display: 'block' }}>Detected Skills</span>
              <h4 style={{ fontSize: '1.4rem', color: '#818cf8', marginTop: '4px' }}>
                {analysis.detectedSkills?.length || 0} Skills
              </h4>
            </div>

            <div style={{ background: 'var(--bg-secondary)', padding: '16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)' }}>
              <span style={{ fontSize: '0.78rem', color: 'var(--text-dim)', display: 'block' }}>Detected Experience</span>
              <h4 style={{ fontSize: '1.4rem', color: '#34d399', marginTop: '4px' }}>
                {analysis.detectedExperienceYears || 1.0} Years
              </h4>
            </div>
          </div>

          {/* Profile Summary */}
          <div style={{ marginBottom: '24px' }}>
            <h4 style={{ fontSize: '0.95rem', marginBottom: '8px', color: 'var(--text-muted)' }}>
              Candidate Overview
            </h4>
            <div style={{ background: 'var(--bg-secondary)', padding: '16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)', fontSize: '0.9rem', lineHeight: '1.6' }}>
              {analysis.profileSummary}
            </div>
          </div>

          {/* Detected Skills Chips */}
          <div>
            <h4 style={{ fontSize: '0.95rem', marginBottom: '10px', color: 'var(--text-muted)' }}>
              Identified Technical Skills
            </h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
              {analysis.detectedSkills?.map((skill, idx) => (
                <span
                  key={idx}
                  className="badge badge-purple"
                  style={{ padding: '6px 14px', fontSize: '0.85rem' }}
                >
                  {skill}
                </span>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
