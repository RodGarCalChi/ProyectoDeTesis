const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081/api';

export interface Usuario {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface UsuarioCreate {
  username: string;
  email: string;
  password: string;
}

class ApiService {
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Usuarios
  async getUsuarios(): Promise<Usuario[]> {
    return this.request<Usuario[]>('/usuarios');
  }

  async getUsuario(id: number): Promise<Usuario> {
    return this.request<Usuario>(`/usuarios/${id}`);
  }

  async createUsuario(usuario: UsuarioCreate): Promise<Usuario> {
    return this.request<Usuario>('/usuarios', {
      method: 'POST',
      body: JSON.stringify(usuario),
    });
  }

  async updateUsuario(id: number, usuario: Partial<UsuarioCreate>): Promise<Usuario> {
    return this.request<Usuario>(`/usuarios/${id}`, {
      method: 'PUT',
      body: JSON.stringify(usuario),
    });
  }

  async deleteUsuario(id: number): Promise<void> {
    return this.request<void>(`/usuarios/${id}`, {
      method: 'DELETE',
    });
  }
}

export const apiService = new ApiService();