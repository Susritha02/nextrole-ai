import api from './client';

export const authApi = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
  getMe: () => api.get('/auth/me'),
};

export const jobsApi = {
  getJobs: (params) => api.get('/jobs', { params }),
  getJobById: (id) => api.get(`/jobs/${id}`),
  saveJob: (id) => api.post(`/jobs/${id}/save`),
  unsaveJob: (id) => api.delete(`/jobs/${id}/save`),
  getSavedJobs: () => api.get('/jobs/saved'),
};

export const recommendationApi = {
  getRecommendations: (limit = 10) => api.get('/recommendations', { params: { limit } }),
  evaluateJob: (jobId) => api.get(`/recommendations/job/${jobId}`),
};

export const resumeApi = {
  uploadResume: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/resume/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  getLatest: () => api.get('/resume/latest'),
};

export const skillGapApi = {
  getSkillGap: (jobId) => api.get(`/skill-gap/${jobId}`),
  saveGoal: (goalData) => api.post('/skill-gap/goal', goalData),
  getGoals: () => api.get('/skill-gap/goals'),
  deleteGoal: (id) => api.delete(`/skill-gap/goal/${id}`),
};

export const applicationApi = {
  getApplications: () => api.get('/applications'),
  applyOrTrack: (jobId, data) => api.post(`/applications/job/${jobId}`, data),
  updateApplication: (id, data) => api.patch(`/applications/${id}`, data),
  deleteApplication: (id) => api.delete(`/applications/${id}`),
  getStats: () => api.get('/applications/stats'),
};

export const profileApi = {
  getProfile: () => api.get('/profile'),
  updateProfile: (data) => api.put('/profile', data),
  addSkill: (skillName, proficiencyLevel) => api.post('/profile/skills', { skillName, proficiencyLevel }),
  removeSkill: (skillId) => api.delete(`/profile/skills/${skillId}`),
};

export const dashboardApi = {
  getOverview: () => api.get('/dashboard/overview'),
};
