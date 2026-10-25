import React, { useState, useEffect } from 'react';
import {
  User,
  Plus,
  Trash2,
  Save,
  CheckCircle2,
  AlertCircle,
  Briefcase,
  MapPin,
  Sparkles,
} from 'lucide-react';
import { profileApi } from '../api/services';
import { useAuth } from '../context/AuthContext';

const WORK_MODES = ['REMOTE', 'HYBRID', 'ONSITE', 'ANY'];
const PROFICIENCY_LEVELS = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'];

export default function ProfilePage() {
  const { refreshProfile } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');

  // Skill input state
  const [newSkillName, setNewSkillName] = useState('');
  const [newSkillLevel, setNewSkillLevel] = useState('INTERMEDIATE');

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      setLoading(true);
      const res = await profileApi.getProfile();
      setProfile(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setMessage('');
    try {
      const res = await profileApi.updateProfile(profile);
      setProfile(res.data);
      await refreshProfile();
      setMessage('Profile updated successfully!');
      setTimeout(() => setMessage(''), 3000);
    } catch (e) {
      console.error(e);
      setMessage('Failed to update profile.');
    } finally {
      setSaving(false);
    }
  };

  const handleAddSkill = async (e) => {
    e.preventDefault();
    if (!newSkillName.trim()) return;
    try {
      const res = await profileApi.addSkill(newSkillName.trim(), newSkillLevel);
      setProfile(res.data);
      setNewSkillName('');
      await refreshProfile();
    } catch (e) {
      console.error(e);
    }
  };

  const handleRemoveSkill = async (skillId) => {
    try {
      const res = await profileApi.removeSkill(skillId);
      setProfile(res.data);
      await refreshProfile();
    } catch (e) {
      console.error(e);
    }
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
        <p>Loading your career profile...</p>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
      {/* Profile Form Card */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h3 style={{ fontSize: '1.2rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <User size={20} color="var(--primary)" /> Personal & Professional Details
            </h3>
            <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)', marginTop: '2px' }}>
              These attributes guide the AI recommendation algorithm in finding tailored roles
            </p>
          </div>

          {message && (
            <span style={{ color: '#34d399', fontSize: '0.85rem', display: 'flex', alignItems: 'center', gap: '4px' }}>
              <CheckCircle2 size={15} /> {message}
            </span>
          )}
        </div>

        <form onSubmit={handleProfileSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Full Name
              </label>
              <input
                type="text"
                value={profile?.fullName || ''}
                onChange={(e) => setProfile({ ...profile, fullName: e.target.value })}
                style={{ width: '100%' }}
                required
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Email Address
              </label>
              <input
                type="email"
                value={profile?.email || ''}
                disabled
                style={{ width: '100%', opacity: 0.6, cursor: 'not-allowed' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Professional Headline
              </label>
              <input
                type="text"
                placeholder="e.g. Junior Full Stack Developer | Java & React"
                value={profile?.headline || ''}
                onChange={(e) => setProfile({ ...profile, headline: e.target.value })}
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Current Location
              </label>
              <input
                type="text"
                placeholder="e.g. New York, NY or Remote"
                value={profile?.location || ''}
                onChange={(e) => setProfile({ ...profile, location: e.target.value })}
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Total Experience (Years)
              </label>
              <input
                type="number"
                step="0.5"
                min="0"
                max="30"
                value={profile?.experienceYears ?? 1.0}
                onChange={(e) => setProfile({ ...profile, experienceYears: parseFloat(e.target.value) || 0 })}
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
                Preferred Work Mode
              </label>
              <select
                value={profile?.preferredWorkMode || 'REMOTE'}
                onChange={(e) => setProfile({ ...profile, preferredWorkMode: e.target.value })}
                style={{ width: '100%' }}
              >
                {WORK_MODES.map((mode) => (
                  <option key={mode} value={mode}>
                    {mode}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div style={{ marginBottom: '20px' }}>
            <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '6px' }}>
              Bio & Summary
            </label>
            <textarea
              rows={3}
              placeholder="Highlight your background, core interests, and goals..."
              value={profile?.bio || ''}
              onChange={(e) => setProfile({ ...profile, bio: e.target.value })}
              style={{ width: '100%', resize: 'vertical' }}
            />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button type="submit" disabled={saving} className="btn btn-primary">
              <Save size={16} />
              <span>{saving ? 'Saving...' : 'Save Profile Changes'}</span>
            </button>
          </div>
        </form>
      </div>

      {/* Skills Manager Card */}
      <div className="card">
        <h3 style={{ fontSize: '1.2rem', marginBottom: '8px' }}>Manage Technical Skills</h3>
        <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)', marginBottom: '20px' }}>
          Add or remove skills. The AI engine cross-references these with job requirements to compute your match percentage.
        </p>

        {/* Add Skill Form */}
        <form onSubmit={handleAddSkill} style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', marginBottom: '24px' }}>
          <input
            type="text"
            placeholder="Skill name (e.g. Docker, Python, AWS, React)..."
            value={newSkillName}
            onChange={(e) => setNewSkillName(e.target.value)}
            style={{ flex: 1, minWidth: '220px' }}
          />

          <select
            value={newSkillLevel}
            onChange={(e) => setNewSkillLevel(e.target.value)}
            style={{ width: '160px' }}
          >
            {PROFICIENCY_LEVELS.map((lvl) => (
              <option key={lvl} value={lvl}>
                {lvl}
              </option>
            ))}
          </select>

          <button type="submit" className="btn btn-primary">
            <Plus size={16} />
            <span>Add Skill</span>
          </button>
        </form>

        {/* Skills Chips Grid */}
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
          {profile?.skills?.map((s) => (
            <div
              key={s.id}
              style={{
                background: 'var(--bg-secondary)',
                border: '1px solid var(--border-color)',
                borderRadius: 'var(--radius-sm)',
                padding: '8px 14px',
                display: 'flex',
                alignItems: 'center',
                gap: '10px',
              }}
            >
              <div>
                <strong style={{ fontSize: '0.9rem' }}>{s.name}</strong>
                <span style={{ fontSize: '0.72rem', color: 'var(--text-dim)', display: 'block' }}>
                  {s.proficiencyLevel} • {s.category}
                </span>
              </div>
              <button
                onClick={() => handleRemoveSkill(s.id)}
                style={{ color: 'var(--text-dim)', padding: '2px' }}
                title="Remove Skill"
              >
                <Trash2 size={14} />
              </button>
            </div>
          ))}

          {(!profile?.skills || profile.skills.length === 0) && (
            <p style={{ color: 'var(--text-dim)', fontSize: '0.88rem' }}>
              No skills added yet. Add your technical skills above or upload a resume to automatically extract them!
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
