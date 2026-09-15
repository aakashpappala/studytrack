import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('studytrack_token'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      const savedToken = localStorage.getItem('studytrack_token');
      const savedUser = localStorage.getItem('studytrack_user');

      if (savedToken && savedUser) {
        try {
          setUser(JSON.parse(savedUser));
          setToken(savedToken);
          // Verify with backend
          const res = await api.get('/auth/me');
          if (res.data && res.data.data) {
            setUser(res.data.data);
            localStorage.setItem('studytrack_user', JSON.stringify(res.data.data));
          }
        } catch (err) {
          console.error('Auth validation failed', err);
          logout();
        }
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  const login = async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    const authData = response.data.data;
    
    setToken(authData.token);
    const userData = {
      id: authData.id,
      email: authData.email,
      fullName: authData.fullName,
      role: authData.role,
      studentId: authData.studentId,
    };
    setUser(userData);

    localStorage.setItem('studytrack_token', authData.token);
    localStorage.setItem('studytrack_user', JSON.stringify(userData));

    return userData;
  };

  const register = async (data) => {
    const response = await api.post('/auth/register', data);
    const authData = response.data.data;

    setToken(authData.token);
    const userData = {
      id: authData.id,
      email: authData.email,
      fullName: authData.fullName,
      role: authData.role,
      studentId: authData.studentId,
    };
    setUser(userData);

    localStorage.setItem('studytrack_token', authData.token);
    localStorage.setItem('studytrack_user', JSON.stringify(userData));

    return userData;
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('studytrack_token');
    localStorage.removeItem('studytrack_user');
  };

  const isAdmin = user?.role === 'ROLE_ADMIN';
  const isStudent = user?.role === 'ROLE_STUDENT';

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, isAdmin, isStudent }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
