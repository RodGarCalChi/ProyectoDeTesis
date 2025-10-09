'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import { Usuario, apiService, getCurrentUser, clearAuthData } from '@/lib/api';

interface AuthContextType {
  user: Usuario | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (email: string, password: string, rol?: string) => Promise<{ success: boolean; message: string; user?: Usuario }>;
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

  const login = async (email: string, password: string, rol?: string) => {
    try {
      setIsLoading(true);
      
      // Llamar al backend para autenticación real
      const loginRequest: any = { email, password };
      if (rol) {
        loginRequest.rol = rol;
      }
      
      const response = await apiService.login(loginRequest);
      
      if (response.token && response.usuario) {
        // Mapear el usuario del backend al formato del frontend
        const user: Usuario = {
          id: response.usuario.id,
          username: response.usuario.username,
          email: response.usuario.email,
          role: response.usuario.role,
          firstName: response.usuario.username // Usar username como firstName por defecto
        };
        
        // Guardar datos de autenticación
        apiService.saveAuthData(response.token, user);
        setUser(user);
        
        return {
          success: true,
          message: response.message || 'Login exitoso',
          user: user
        };
      } else {
        return {
          success: false,
          message: response.message || 'Error en el login'
        };
      }
    } catch (error) {
      console.error('Login error:', error);
      // Propagar el error para que el componente de login pueda manejarlo
      throw error;
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