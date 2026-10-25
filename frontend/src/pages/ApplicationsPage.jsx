import React, { useState, useEffect } from 'react';
import {
  Briefcase,
  Calendar,
  Clock,
  DollarSign,
  Edit2,
  Trash2,
  Plus,
  LayoutGrid,
  List,
  AlertCircle,
} from 'lucide-react';
import ApplicationModal from '../components/applications/ApplicationModal';
import { applicationApi } from '../api/services';

const PIPELINE_COLUMNS = [
  { id: 'INTERESTED', label: 'Interested', color: '#9ca3af' },
  { id: 'APPLIED', label: 'Applied', color: '#60a5fa' },
  { id: 'ASSESSMENT', label: 'Assessment', color: '#fbbf24' },
  { id: 'INTERVIEW', label: 'Interview', color: '#c084fc' },
  { id: 'OFFER', label: 'Offer', color: '#34d399' },
  { id: 'REJECTED', label: 'Rejected', color: '#f87171' },
  { id: 'WITHDRAWN', label: 'Withdrawn', color: '#6b7280' },
];

export default function ApplicationsPage({ onNavigate }) {
  const [applications, setApplications] = useState([]);
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [viewMode, setViewMode] = useState('kanban'); // kanban or table
  const [editingApp, setEditingApp] = useState(null);

  const fetchApplications = async () => {
    try {
      setLoading(true);
      const [appsRes, statsRes] = await Promise.all([
        applicationApi.getApplications(),
        applicationApi.getStats(),
      ]);
      setApplications(appsRes.data);
      setStats(statsRes.data);
    } catch (err) {
      console.error('Failed to load applications:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchApplications();
  }, []);

  const handleUpdateApplication = async (payload) => {
    if (editingApp) {
      await applicationApi.updateApplication(editingApp.id, payload);
      fetchApplications();
    }
  };

  const handleDeleteApplication = async (id, e) => {
    e.stopPropagation();
    if (window.confirm('Are you sure you want to delete this tracked application?')) {
      try {
        await applicationApi.deleteApplication(id);
        setApplications((prev) => prev.filter((a) => a.id !== id));
        fetchApplications();
      } catch (err) {
        console.error(err);
      }
    }
  };

  return (
    <div>
      {/* Top Header & View Controls */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', flexWrap: 'wrap', gap: '14px' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem' }}>Application Tracking Pipeline</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
            Manage {applications.length} active job applications across interview stages
          </p>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div style={{ display: 'flex', background: 'var(--bg-secondary)', border: '1px solid var(--border-color)', borderRadius: 'var(--radius-sm)', padding: '3px' }}>
            <button
              onClick={() => setViewMode('kanban')}
              style={{
                padding: '6px 12px',
                borderRadius: '4px',
                background: viewMode === 'kanban' ? 'var(--bg-card)' : 'transparent',
                color: viewMode === 'kanban' ? 'var(--text-main)' : 'var(--text-dim)',
                display: 'flex',
                alignItems: 'center',
                gap: '6px',
                fontSize: '0.85rem',
              }}
            >
              <LayoutGrid size={15} />
              <span>Kanban</span>
            </button>
            <button
              onClick={() => setViewMode('table')}
              style={{
                padding: '6px 12px',
                borderRadius: '4px',
                background: viewMode === 'table' ? 'var(--bg-card)' : 'transparent',
                color: viewMode === 'table' ? 'var(--text-main)' : 'var(--text-dim)',
                display: 'flex',
                alignItems: 'center',
                gap: '6px',
                fontSize: '0.85rem',
              }}
            >
              <List size={15} />
              <span>Table</span>
            </button>
          </div>

          <button onClick={() => onNavigate('explore')} className="btn btn-primary btn-sm">
            <Plus size={16} />
            <span>Track New Job</span>
          </button>
        </div>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-muted)' }}>
          <p>Loading application pipeline...</p>
        </div>
      ) : applications.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '60px 20px', color: 'var(--text-muted)' }}>
          <Briefcase size={40} style={{ margin: '0 auto 12px', color: 'var(--text-dim)' }} />
          <h3>No applications tracked yet</h3>
          <p style={{ fontSize: '0.88rem', marginTop: '6px' }}>
            Track any job by clicking "Track Application" on Explore or Recommended jobs.
          </p>
          <button
            onClick={() => onNavigate('explore')}
            className="btn btn-primary"
            style={{ marginTop: '16px' }}
          >
            Explore Jobs to Apply
          </button>
        </div>
      ) : viewMode === 'kanban' ? (
        /* Visual Kanban Pipeline Board */
        <div className="kanban-board">
          {PIPELINE_COLUMNS.map((col) => {
            const columnApps = applications.filter((a) => a.status === col.id);
            return (
              <div key={col.id} className="kanban-col">
                <div className="kanban-col-header">
                  <h4>
                    <span
                      style={{
                        width: '8px',
                        height: '8px',
                        borderRadius: '50%',
                        backgroundColor: col.color,
                        display: 'inline-block',
                      }}
                    />
                    {col.label}
                  </h4>
                  <span className="kanban-col-count">{columnApps.length}</span>
                </div>

                <div className="kanban-col-body">
                  {columnApps.map((app) => (
                    <div
                      key={app.id}
                      className="kanban-card"
                      onClick={() => setEditingApp(app)}
                    >
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                        <h5 style={{ fontSize: '0.92rem', marginBottom: '2px', color: 'var(--text-main)' }}>
                          {app.jobTitle}
                        </h5>
                        <button
                          onClick={(e) => handleDeleteApplication(app.id, e)}
                          style={{ color: 'var(--text-dim)', padding: '2px' }}
                          title="Delete"
                        >
                          <Trash2 size={13} />
                        </button>
                      </div>

                      <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '8px' }}>
                        {app.company}
                      </p>

                      {app.interviewDate && (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: '#c084fc', marginBottom: '6px' }}>
                          <Calendar size={13} />
                          <span>Interview: {new Date(app.interviewDate).toLocaleString([], { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })}</span>
                        </div>
                      )}

                      {app.salaryOffered && (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem', color: '#34d399', marginBottom: '6px' }}>
                          <DollarSign size={13} />
                          <span>Offer: ${app.salaryOffered.toLocaleString()}</span>
                        </div>
                      )}

                      {app.nextSteps && (
                        <p style={{ fontSize: '0.75rem', color: '#fbbf24', background: 'rgba(245, 158, 11, 0.1)', padding: '4px 6px', borderRadius: '4px', marginTop: '6px' }}>
                          Next: {app.nextSteps}
                        </p>
                      )}

                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '10px', fontSize: '0.72rem', color: 'var(--text-dim)' }}>
                        <span>Applied: {app.appliedDate || 'N/A'}</span>
                        <span style={{ color: '#818cf8', display: 'flex', alignItems: 'center', gap: '2px' }}>
                          <Edit2 size={11} /> Edit
                        </span>
                      </div>
                    </div>
                  ))}

                  {columnApps.length === 0 && (
                    <div style={{ textAlign: 'center', padding: '24px 8px', color: 'var(--text-dim)', fontSize: '0.8rem' }}>
                      No applications
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        /* Table View */
        <div className="card" style={{ padding: '0', overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.88rem' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)', background: 'var(--bg-secondary)', color: 'var(--text-muted)' }}>
                <th style={{ padding: '14px 18px' }}>Job Role & Company</th>
                <th style={{ padding: '14px 18px' }}>Status</th>
                <th style={{ padding: '14px 18px' }}>Applied Date</th>
                <th style={{ padding: '14px 18px' }}>Interview / Deadline</th>
                <th style={{ padding: '14px 18px' }}>Next Steps</th>
                <th style={{ padding: '14px 18px', textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.05)' }}>
                  <td style={{ padding: '14px 18px' }}>
                    <div style={{ fontWeight: 600 }}>{app.jobTitle}</div>
                    <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{app.company} • {app.location}</div>
                  </td>
                  <td style={{ padding: '14px 18px' }}>
                    <span className={`badge ${app.status === 'OFFER' ? 'badge-green' : app.status === 'INTERVIEW' ? 'badge-purple' : 'badge-blue'}`}>
                      {app.status}
                    </span>
                  </td>
                  <td style={{ padding: '14px 18px', color: 'var(--text-muted)' }}>
                    {app.appliedDate || 'N/A'}
                  </td>
                  <td style={{ padding: '14px 18px' }}>
                    {app.interviewDate ? (
                      <span style={{ color: '#c084fc', fontSize: '0.82rem' }}>
                        {new Date(app.interviewDate).toLocaleString([], { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })}
                      </span>
                    ) : app.applicationDeadline ? (
                      <span style={{ color: 'var(--text-dim)', fontSize: '0.82rem' }}>Due: {app.applicationDeadline}</span>
                    ) : (
                      <span style={{ color: 'var(--text-dim)' }}>-</span>
                    )}
                  </td>
                  <td style={{ padding: '14px 18px', color: 'var(--text-muted)', maxWidth: '240px' }}>
                    {app.nextSteps || '-'}
                  </td>
                  <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                    <div style={{ display: 'inline-flex', gap: '8px' }}>
                      <button
                        onClick={() => setEditingApp(app)}
                        className="btn btn-outline btn-sm"
                        style={{ padding: '4px 8px' }}
                      >
                        <Edit2 size={13} />
                      </button>
                      <button
                        onClick={(e) => handleDeleteApplication(app.id, e)}
                        className="btn btn-outline btn-sm"
                        style={{ padding: '4px 8px', color: '#f87171' }}
                      >
                        <Trash2 size={13} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Edit Modal */}
      {editingApp && (
        <ApplicationModal
          application={editingApp}
          onClose={() => setEditingApp(null)}
          onSave={handleUpdateApplication}
        />
      )}
    </div>
  );
}
