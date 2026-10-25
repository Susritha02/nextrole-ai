import React, { useState } from 'react';
import { Sparkles, Lock, Mail, User, Briefcase, MapPin, ArrowRight, AlertCircle } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage({ onSwitchToLogin }) {
  const { register } = useAuth();
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    headline: 'Junior Software Engineer',
    location: 'Remote',
    experienceYears: 1.0,
    preferredWorkMode: 'REMOTE',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await register({
        ...formData,
        experienceYears: parseFloat(formData.experienceYears) || 0,
      });
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to create account. Please check your details.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: 'var(--bg-primary)',
        padding: '20px',
      }}
    >
      <div
        className="card"
        style={{
          width: '100%',
          maxWidth: '520px',
          padding: '36px',
          boxShadow: 'var(--shadow-lg)',
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <div
            style={{
              width: '48px',
              height: '48px',
              borderRadius: 'var(--radius-md)',
              background: 'linear-gradient(135deg, var(--primary), #a855f7)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 12px',
              color: '#fff',
            }}
          >
            <Sparkles size={26} />
          </div>
          <h2 style={{ fontSize: '1.45rem', fontWeight: 700 }}>Join CareerAI</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '2px' }}>
            Set up your AI Job Recommendation profile in seconds
          </p>
        </div>

        {error && (
          <div
            style={{
              background: 'rgba(239, 68, 68, 0.1)',
              border: '1px solid rgba(239, 68, 68, 0.3)',
              borderRadius: 'var(--radius-sm)',
              padding: '12px 14px',
              color: '#f87171',
              fontSize: '0.85rem',
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
              marginBottom: '18px',
            }}
          >
            <AlertCircle size={16} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
          <div>
            <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
              Full Name
            </label>
            <input
              type="text"
              placeholder="e.g. John Doe"
              value={formData.fullName}
              onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
              required
              style={{ width: '100%' }}
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
                Email Address
              </label>
              <input
                type="email"
                placeholder="john@example.com"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
                Password (min 6 chars)
              </label>
              <input
                type="password"
                placeholder="••••••••"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required
                minLength={6}
                style={{ width: '100%' }}
              />
            </div>
          </div>

          <div>
            <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
              Professional Headline / Target Role
            </label>
            <input
              type="text"
              placeholder="e.g. Frontend Developer | React & TypeScript"
              value={formData.headline}
              onChange={(e) => setFormData({ ...formData, headline: e.target.value })}
              style={{ width: '100%' }}
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
                Experience (Years)
              </label>
              <input
                type="number"
                step="0.5"
                min="0"
                max="25"
                value={formData.experienceYears}
                onChange={(e) => setFormData({ ...formData, experienceYears: e.target.value })}
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.82rem', color: 'var(--text-muted)', display: 'block', marginBottom: '4px' }}>
                Preferred Work Mode
              </label>
              <select
                value={formData.preferredWorkMode}
                onChange={(e) => setFormData({ ...formData, preferredWorkMode: e.target.value })}
                style={{ width: '100%' }}
              >
                <option value="REMOTE">Remote</option>
                <option value="HYBRID">Hybrid</option>
                <option value="ONSITE">Onsite</option>
                <option value="ANY">Any Work Mode</option>
              </select>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
            style={{ width: '100%', padding: '12px', marginTop: '6px' }}
          >
            <span>{loading ? 'Creating Profile...' : 'Complete Registration'}</span>
            <ArrowRight size={16} />
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '20px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
          Already have an account?{' '}
          <button
            onClick={onSwitchToLogin}
            style={{ color: 'var(--primary)', fontWeight: 600, textDecoration: 'underline' }}
          >
            Sign In here
          </button>
        </div>
      </div>
    </div>
  );
}
