'use client';

import React, { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: string;
  redirectTo?: string;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  children,
  requiredRole,
  redirectTo = '/login'
}) => {
  const { user, isLoading, isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading) {
      // Si no está autenticado, redirigir al login
      if (!isAuthenticated) {
        router.push(redirectTo);
        return;
      }

      // Si se requiere un rol específico y el usuario no lo tiene
      if (requiredRole && user?.role !== requiredRole) {
        // Redirigir según el rol del usuario
        switch (user?.role) {
          case 'Recepcion':
            router.push('/movimientos');
            break;
          case 'DirectorTecnico':
          case 'Administrador':
            router.push('/dashboard');
            break;
          default:
            router.push('/unauthorized');
            break;
        }
        return;
      }
    }
  }, [user, isLoading, isAuthenticated, requiredRole, router, redirectTo]);

  // Mostrar loading mientras se verifica la autenticación
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"></div>
          <p className="text-gray-600">Verificando acceso...</p>
        </div>
      </div>
    );
  }

  // Si no está autenticado o no tiene el rol requerido, no mostrar nada
  // (la redirección se maneja en el useEffect)
  if (!isAuthenticated || (requiredRole && user?.role !== requiredRole)) {
    return null;
  }

  // Si todo está bien, mostrar el contenido
  return <>{children}</>;
};