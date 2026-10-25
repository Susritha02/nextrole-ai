import React, { useState, useEffect } from 'react';
import {
  Target,
  CheckCircle2,
  AlertCircle,
  BookOpen,
  ExternalLink,
  Save,
  Check,
  TrendingUp,
  Sliders,
} from 'lucide-react';
import { skillGapApi, jobsApi } from '../api/services';

export default function SkillGapPage({ targetJobId }) {
  const [jobs, setJobs] = useState([]);
  const [selectedJobId, setSelectedJobId] = useState(targetJobId || '');
  const [skillGap, setSkillGap] = useState(null);
  const [loading, setLoading] = useState(false);
  const [savingGoal, setSavingGoal] = useState({});

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const res = await jobsApi.getJobs({});
        setJobs(res.data);
        if (!selectedJobId && res.data.length > 0) {
          setSelectedJobId(res.data[0].id);
        }
      } catch (e) {
        console.error(e);
      }
    };
    fetchJobs();
  }, []);

  useEffect(() => {
    if (selectedJobId) {
      loadSkillGap(selectedJobId);
    }
  }, [selectedJobId]);

  const loadSkillGap = async (jobId) => {
    try {
      setLoading(true);
      const res = await skillGapApi.getSkillGap(jobId);
      setSkillGap(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateGoal = async (skillName, newStatus, newPercentage) => {
    setSavingGoal((prev) => ({ ...prev, [skillName]: true }));
    try {
      await skillGapApi.saveGoal({
        skillName,
        jobTitle: skillGap.jobTitle,
        status: newStatus,
        progressPercentage: newPercentage,
      });

      // Update local state
      setSkillGap((prev) => {
        if (!prev) return prev;
        const updatedMissing = prev.missingSkills.map((m) => {
          if (m.skillName === skillName) {
            return {
              ...m,
              status: newStatus !== undefined ? newStatus : m.status,
              progressPercentage: newPercentage !== undefined ? newPercentage : m.progressPercentage,
            };
          }
          return m;
        });
        return { ...prev, missingSkills: updatedMissing };
      });
    } catch (e) {
      console.error(e);
    } finally {
      setSavingGoal((prev) => ({ ...prev, [skillName]: false }));
    }
  };

  return (
    <div>
      {/* Target Job Selector Header */}
      <div className="card" style={{ marginBottom: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <h3 style={{ fontSize: '1.15rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Target size={20} color="var(--primary)" /> Select Target Career Role
            </h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '2px' }}>
              Select a position to calculate your skill readiness score and generate learning milestones
            </p>
          </div>

          <select
            value={selectedJobId}
            onChange={(e) => setSelectedJobId(e.target.value)}
            style={{ minWidth: '300px', fontSize: '0.9rem' }}
          >
            {jobs.map((j) => (
              <option key={j.id} value={j.id}>
                {j.title} ({j.company})
              </option>
            ))}
          </select>
        </div>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
          <p>Analyzing skill gap against job requirements...</p>
        </div>
      ) : skillGap ? (
        <div>
          {/* Readiness Summary Bar */}
          <div
            style={{
              background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15), rgba(16, 185, 129, 0.1))',
              border: '1px solid var(--border-color)',
              borderRadius: 'var(--radius-md)',
              padding: '24px',
              marginBottom: '24px',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              flexWrap: 'wrap',
              gap: '16px',
            }}
          >
            <div>
              <span className="badge badge-purple" style={{ marginBottom: '6px' }}>
                Role: {skillGap.jobTitle} • {skillGap.company}
              </span>
              <h2 style={{ fontSize: '1.4rem', marginTop: '4px' }}>
                Skill Readiness Score: {skillGap.matchPercentage}%
              </h2>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '4px' }}>
                You possess {skillGap.matchingSkillsCount} of {skillGap.totalRequiredSkills} total skills required for this position.
              </p>
            </div>

            <div style={{ display: 'flex', gap: '16px' }}>
              <div style={{ background: 'var(--bg-card)', padding: '12px 18px', borderRadius: 'var(--radius-sm)', textAlign: 'center' }}>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', display: 'block' }}>Matching Skills</span>
                <strong style={{ color: '#34d399', fontSize: '1.2rem' }}>{skillGap.matchingSkillsCount}</strong>
              </div>
              <div style={{ background: 'var(--bg-card)', padding: '12px 18px', borderRadius: 'var(--radius-sm)', textAlign: 'center' }}>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)', display: 'block' }}>Missing Gaps</span>
                <strong style={{ color: '#fbbf24', fontSize: '1.2rem' }}>{skillGap.missingSkillsCount}</strong>
              </div>
            </div>
          </div>

          {/* Existing Matching Skills */}
          {skillGap.matchingSkills?.length > 0 && (
            <div className="card" style={{ marginBottom: '24px' }}>
              <h4 style={{ fontSize: '1rem', color: '#34d399', display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '12px' }}>
                <CheckCircle2 size={18} /> Verified Matching Skills ({skillGap.matchingSkills.length})
              </h4>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                {skillGap.matchingSkills.map((s, idx) => (
                  <span key={idx} className="badge badge-green" style={{ padding: '6px 12px', fontSize: '0.85rem' }}>
                    ✓ {s}
                  </span>
                ))}
              </div>
            </div>
          )}

          {/* Missing Skills & Learning Roadmaps */}
          <div style={{ marginBottom: '16px' }}>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '6px', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <BookOpen size={20} color="#fbbf24" /> Targeted Learning Roadmaps ({skillGap.missingSkills?.length || 0})
            </h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
              Follow these recommended study milestones to bridge your skill gap and become an ideal candidate.
            </p>
          </div>

          {skillGap.missingSkills?.length > 0 ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
              {skillGap.missingSkills.map((skill) => (
                <div key={skill.skillName} className="card" style={{ borderLeft: skill.isMandatory ? '4px solid #ef4444' : '4px solid #fbbf24' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '12px', marginBottom: '14px' }}>
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <h4 style={{ fontSize: '1.15rem' }}>{skill.skillName}</h4>
                        <span className={`badge ${skill.isMandatory ? 'badge-red' : 'badge-amber'}`}>
                          {skill.isMandatory ? 'Critical Requirement' : 'Recommended'}
                        </span>
                        <span className="badge badge-gray">{skill.category}</span>
                      </div>
                    </div>

                    {/* Progress Control */}
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <select
                        value={skill.status || 'NOT_STARTED'}
                        onChange={(e) => handleUpdateGoal(skill.skillName, e.target.value, skill.progressPercentage)}
                        style={{ fontSize: '0.82rem', padding: '6px 10px' }}
                      >
                        <option value="NOT_STARTED">Not Started</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="COMPLETED">Completed</option>
                      </select>

                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', minWidth: '150px' }}>
                        <input
                          type="range"
                          min="0"
                          max="100"
                          value={skill.progressPercentage || 0}
                          onChange={(e) => handleUpdateGoal(skill.skillName, skill.status, parseInt(e.target.value))}
                          style={{ flex: 1 }}
                        />
                        <span style={{ fontSize: '0.82rem', fontWeight: 600, color: 'var(--text-main)', width: '38px' }}>
                          {skill.progressPercentage || 0}%
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Two column: Topics and Resources */}
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
                    {/* Topics */}
                    <div style={{ background: 'var(--bg-secondary)', padding: '14px 18px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)' }}>
                      <h5 style={{ fontSize: '0.88rem', color: '#818cf8', marginBottom: '8px' }}>
                        Core Study Topics
                      </h5>
                      <ul style={{ paddingLeft: '18px', fontSize: '0.82rem', color: 'var(--text-muted)', display: 'flex', flexDirection: 'column', gap: '6px' }}>
                        {skill.learningTopics?.map((t, i) => (
                          <li key={i}>{t}</li>
                        ))}
                      </ul>
                    </div>

                    {/* Curated Resources */}
                    <div style={{ background: 'var(--bg-secondary)', padding: '14px 18px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-color)' }}>
                      <h5 style={{ fontSize: '0.88rem', color: '#34d399', marginBottom: '8px' }}>
                        Curated Learning Resources
                      </h5>
                      <ul style={{ paddingLeft: '18px', fontSize: '0.82rem', color: 'var(--text-muted)', display: 'flex', flexDirection: 'column', gap: '6px' }}>
                        {skill.recommendedResources?.map((r, i) => (
                          <li key={i} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                            <span>{r}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="card" style={{ textAlign: 'center', padding: '40px', color: '#10b981' }}>
              <CheckCircle2 size={36} style={{ margin: '0 auto 10px' }} />
              <h4>100% Skill Fit!</h4>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', marginTop: '4px' }}>
                You possess all skills required for this role. You are ready to apply!
              </p>
            </div>
          )}
        </div>
      ) : null}
    </div>
  );
}
