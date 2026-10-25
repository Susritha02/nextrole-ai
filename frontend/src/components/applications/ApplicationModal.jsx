import React, { useState } from 'react';
import { X, Calendar, DollarSign, FileText, CheckCircle2 } from 'lucide-react';

const STATUSES = [
  'INTERESTED',
  'APPLIED',
  'ASSESSMENT',
  'INTERVIEW',
  'OFFER',
  'REJECTED',
  'WITHDRAWN',
];

export default function ApplicationModal({
  job,
  application,
  onClose,
  onSave,
}) {
  const [status, setStatus] = useState(application?.status || 'APPLIED');
  const [appliedDate, setAppliedDate] = useState(
    application?.appliedDate || new Date().toISOString().split('T')[0]
  );
  const [deadline, setDeadline] = useState(application?.applicationDeadline || '');
  const [interviewDate, setInterviewDate] = useState(
    application?.interviewDate ? application.interviewDate.substring(0, 16) : ''
  );
  const [salaryOffered, setSalaryOffered] = useState(application?.salaryOffered || '');
  const [notes, setNotes] = useState(application?.notes || '');
  const [nextSteps, setNextSteps] = useState(application?.nextSteps || '');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await onSave({
        status,
        appliedDate: appliedDate || null,
        applicationDeadline: deadline || null,
        interviewDate: interviewDate ? `${interviewDate}:00` : null,
        salaryOffered: salaryOffered ? parseFloat(salaryOffered) : null,
        notes,
        nextSteps,
      });
      onClose();
    } catch (err) {
      console.error(err);
      alert('Failed to save application');
    } finally {
      setLoading(false);
    }
  };

  const title = job?.title || application?.jobTitle;
  const company = job?.company || application?.company;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div>
            <h2 style={{ fontSize: '1.25rem' }}>Track Application</h2>
            <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>
              {title} • {company}
            </p>
          </div>
          <button onClick={onClose} style={{ color: 'var(--text-muted)' }}>
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modal-body" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: 'var(--text-muted)' }}>
                Application Pipeline Status
              </label>
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                style={{ width: '100%' }}
              >
                {STATUSES.map((s) => (
                  <option key={s} value={s}>
                    {s}
                  </option>
                ))}
              </select>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px' }}>
              <div>
                <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: 'var(--text-muted)' }}>
                  Applied Date
                </label>
                <input
                  type="date"
                  value={appliedDate}
                  onChange={(e) => setAppliedDate(e.target.value)}
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: 'var(--text-muted)' }}>
                  Application Deadline
                </label>
                <input
                  type="date"
                  value={deadline}
                  onChange={(e) => setDeadline(e.target.value)}
                  style={{ width: '100%' }}
                />
              </div>
            </div>

            {(status === 'INTERVIEW' || status === 'ASSESSMENT') && (
              <div>
                <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: '#c084fc' }}>
                  Interview Date & Time
                </label>
                <input
                  type="datetime-local"
                  value={interviewDate}
                  onChange={(e) => setInterviewDate(e.target.value)}
                  style={{ width: '100%', borderColor: '#c084fc' }}
                />
              </div>
            )}

            {status === 'OFFER' && (
              <div>
                <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: '#34d399' }}>
                  Salary Offered ($)
                </label>
                <input
                  type="number"
                  placeholder="e.g. 95000"
                  value={salaryOffered}
                  onChange={(e) => setSalaryOffered(e.target.value)}
                  style={{ width: '100%', borderColor: '#34d399' }}
                />
              </div>
            )}

            <div>
              <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: 'var(--text-muted)' }}>
                Next Steps / Preparation Goals
              </label>
              <input
                type="text"
                placeholder="e.g. Revise Spring Security & React hooks"
                value={nextSteps}
                onChange={(e) => setNextSteps(e.target.value)}
                style={{ width: '100%' }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.85rem', display: 'block', marginBottom: '6px', color: 'var(--text-muted)' }}>
                Private Notes & Feedback
              </label>
              <textarea
                rows={3}
                placeholder="Interview notes, recruiter contacts, technical questions asked..."
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                style={{ width: '100%', resize: 'vertical' }}
              />
            </div>
          </div>

          <div className="modal-footer">
            <button type="button" onClick={onClose} className="btn btn-outline">
              Cancel
            </button>
            <button type="submit" disabled={loading} className="btn btn-primary">
              <CheckCircle2 size={16} />
              <span>{loading ? 'Saving...' : 'Save Application'}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
