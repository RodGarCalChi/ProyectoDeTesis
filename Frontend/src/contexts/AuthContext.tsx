'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import { Usuario, apiService, getCurrentUser, clearAuthData } from '@/lib/api';

interface AuthContextType {
  user: Usuario | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<{ success: boolean; message: string; user?: Usuario }>;
  logout: () => Promise<void>;
  hasRole: (role: string) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<Usuario | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Cargar usuario al inicializar
  useEffect(() => {
    const loadUser = () => {
      try {
        const currentUser = getCurrentUser();
        setUser(currentUser);
      } catch (error) {
        console.error('Error loading user:', error);
        clearAuthData();
      } finally {
        setIsLoading(false);
      }
    };

    loadUser();
  }, []);

  const login = async (email: string, password: string) => {
    try {
      setIsLoading(true);
      
      // COMENTADO: Acceso original del personal de recepción
      // const response = await apiService.login({ email, password });
      
      // NUEVO: Sistema de códigos - el login real se maneja en LoginPage
      // Este método ahora solo verifica si ya hay un usuario autenticado
      const currentUser = getCurrentUser();
      
      if (currentUser) {
        setUser(currentUser);
        return {
          success: true,
          message: 'Login exitoso',
          user: currentUser
        };
      } else {
        return {
          success: false,
          message: 'No se encontró usuario autenticado'
        };
      }
    } catch (error) {
      console.error('Login error:', error);
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Error de autenticación'
      };
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      setIsLoading(true);
      await apiService.logout();
      setUser(null);
    } catch (error) {
      console.error('Logout error:', error);
      // Limpiar estado local aunque falle la llamada
      setUser(null);
      clearAuthData();
    } finally {
      setIsLoading(false);
    }
  };

  const hasRole = (role: string): boolean => {
    return user?.role === role;
  };

  const isAuthenticated = !!user;

  const value: AuthContextType = {
    user,
    isLoading,
    isAuthenticated,
    login,
    logout,
    hasRole,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};