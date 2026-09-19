import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('studytrack_token');

    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    // File uploads must be sent as multipart/form-data.
    // Let the browser/Axios set the correct boundary automatically.
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      const isAuthUrl =
        error.config.url.includes('/auth/login') ||
        error.config.url.includes('/auth/register');

      if (!isAuthUrl) {
        localStorage.removeItem('studytrack_token');
        localStorage.removeItem('studytrack_user');
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;
