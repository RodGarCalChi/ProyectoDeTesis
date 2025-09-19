import { useState, useEffect } from 'react';
import { apiService, Usuario, UsuarioCreate } from '@/lib/api';

export function useUsuarios() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchUsuarios = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await apiService.getUsuarios();
      setUsuarios(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al cargar usuarios');
    } finally {
      setLoading(false);
    }
  };

  const createUsuario = async (usuario: UsuarioCreate) => {
    try {
      const nuevoUsuario = await apiService.createUsuario(usuario);
      setUsuarios(prev => [...prev, nuevoUsuario]);
      return nuevoUsuario;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al crear usuario');
      throw err;
    }
  };

  const updateUsuario = async (id: number, usuario: Partial<UsuarioCreate>) => {
    try {
      const usuarioActualizado = await apiService.updateUsuario(id, usuario);
      setUsuarios(prev => prev.map(u => u.id === id ? usuarioActualizado : u));
      return usuarioActualizado;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al actualizar usuario');
      throw err;
    }
  };

  const deleteUsuario = async (id: number) => {
    try {
      await apiService.deleteUsuario(id);
      setUsuarios(prev => prev.filter(u => u.id !== id));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al eliminar usuario');
      throw err;
    }
  };

  useEffect(() => {
    fetchUsuarios();
  }, []);

  return {
    usuarios,
    loading,
    error,
    fetchUsuarios,
    createUsuario,
    updateUsuario,
    deleteUsuario,
  };
}