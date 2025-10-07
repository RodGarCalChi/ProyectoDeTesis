'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

interface NavigationProps {
  currentPage: string;
}

export const Navigation: React.FC<NavigationProps> = ({ currentPage }) => {
  const router = useRouter();
  const { user, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  // Definir las páginas disponibles según el rol
  const getAvailablePages = () => {
    const role = user?.role;
    const pages = [];

    // Páginas comunes para roles con acceso al sistema
    if (['Inventario', 'Jefe_Ejecutivas', 'Control', 'DirectorTecnico', 'Administrador'].includes(role || '')) {
      pages.push({ key: 'dashboard', label: 'Inventario', path: '/dashboard' });
    }

    // Movimientos - disponible para Recepcion y otros roles
    if (['Recepcion', 'Inventario', 'Jefe_Ejecutivas', 'DirectorTecnico', 'Administrador'].includes(role || '')) {
      pages.push({ key: 'movimientos', label: 'Movimientos', path: '/movimientos' });
    }

    // Órdenes - principalmente para Jefe de Ejecutivas
    if (['Jefe_Ejecutivas', 'DirectorTecnico', 'Administrador'].includes(role || '')) {
      pages.push({ key: 'ordenes', label: 'Órdenes', path: '/ordenes' });
    }

    // Control - para rol de Control
    if (['Control', 'DirectorTecnico', 'Administrador'].includes(role || '')) {
      pages.push({ key: 'control', label: 'Control', path: '/control' });
    }

    // Registro - para gestión de inventario
    if (['Inventario', 'Jefe_Ejecutivas', 'DirectorTecnico', 'Administrador'].includes(role || '')) {
      pages.push({ key: 'registro-inventario', label: 'Registro', path: '/registro-inventario' });
    }

    return pages;
  };

  const availablePages = getAvailablePages();

  return (
    <>
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <h1 className="text-xl font-bold text-gray-900">PharmaFlow</h1>
            <span className="text-sm text-gray-600">
              {currentPage === 'dashboard' && 'Dashboard - Inventario'}
              {currentPage === 'movimientos' && 'Movimientos de Inventario'}
              {currentPage === 'ordenes' && 'Gestión de Órdenes'}
              {currentPage === 'control' && 'Control y Supervisión'}
              {currentPage === 'registro-inventario' && 'Registro de Inventario'}
            </span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">
              {user?.role === 'Recepcion' && 'Recepción'}
              {user?.role === 'Jefe_Ejecutivas' && 'Jefe de Ejecutivas'}
              {user?.role === 'Control' && 'Control'}
              {user?.role === 'Inventario' && 'Inventario'}
              {user?.role === 'DirectorTecnico' && 'Director Técnico'}
              {user?.role === 'Administrador' && 'Administrador'}
              : {user?.firstName}
            </span>
            <button
              onClick={handleLogout}
              className="text-gray-600 hover:text-gray-900 transition-colors"
              title="Cerrar sesión"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>
        </div>
      </header>

      {/* Navigation */}
      {availablePages.length > 1 && (
        <nav className="bg-white border-b border-gray-200 px-6 py-2">
          <div className="flex gap-6">
            {availablePages.map((page) => (
              <button
                key={page.key}
                onClick={() => router.push(page.path)}
                className={`pb-2 text-sm transition-colors ${
                  currentPage === page.key
                    ? 'text-blue-600 border-b-2 border-blue-600 font-medium'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                {page.label}
              </button>
            ))}
          </div>
        </nav>
      )}
    </>
  );
};