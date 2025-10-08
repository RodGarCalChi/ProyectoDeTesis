// Configuración de la API
const API_BASE_URL = 'http://localhost:8081/api';

// Tipos para TypeScript
export interface LoginRequest {
  email: string;
  password: string;
  rol?: string; // Rol opcional
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  role: string;
  firstName?: string;
}

export interface LoginResponse {
  token?: string;
  usuario?: Usuario;
  message: string;
}

// Clase para manejar las llamadas a la API
class ApiService {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  // Método para hacer login
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
      const response = await fetch(`${this.baseUrl}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Error en el login');
      }

      return data;
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  }

  // Método para hacer logout
  async logout(): Promise<void> {
    try {
      const token = localStorage.getItem('token');
      
      await fetch(`${this.baseUrl}/auth/logout`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : '',
        },
      });

      // Limpiar datos locales
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    } catch (error) {
      console.error('Error en logout:', error);
      // Limpiar datos locales aunque falle la llamada
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  }

  // Método para verificar si el usuario está autenticado
  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    return !!(token && user);
  }

  // Método para obtener el usuario actual
  getCurrentUser(): Usuario | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (error) {
        console.error('Error parsing user data:', error);
        return null;
      }
    }
    return null;
  }

  // Método para verificar si el usuario tiene un rol específico
  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  // Método para guardar datos de autenticación
  saveAuthData(token: string, user: Usuario): void {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
  }
}

// Instancia singleton del servicio
export const apiService = new ApiService(API_BASE_URL);

// Funciones de utilidad
export const saveAuthData = (token: string, user: Usuario) => {
  apiService.saveAuthData(token, user);
};

export const clearAuthData = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  return apiService.getCurrentUser();
};

export const isAuthenticated = () => {
  return apiService.isAuthenticated();
};

export const hasRole = (role: string) => {
  return apiService.hasRole(role);
};