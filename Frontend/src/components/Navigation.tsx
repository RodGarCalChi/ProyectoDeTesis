'use client';

import React from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

export const Navigation: React.FC = () => {
  const router = useRouter();
  const pathname = usePathname();
  const { user, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  // Definir las páginas disponibles según el rol
  const getAvailablePages = () => {
    const role = user?.role;
    const pages = [];

    // Dashboard - disponible para la mayoría de roles
    if (['Cliente', 'Operaciones', 'Despacho', 'DirectorTecnico'].includes(role || '')) {
      pages.push({ key: 'dashboard', label: 'Inventario', path: '/dashboard' });
    }

    // Movimientos - disponible para Recepcion (entrada) y Despacho (salida)
    if (['Recepcion', 'Despacho', 'DirectorTecnico'].includes(role || '')) {
      pages.push({ key: 'movimientos', label: 'Movimientos', path: '/movimientos' });
    }

    // Órdenes - principalmente para Área Administrativa
    if (['AreaAdministrativa', 'DirectorTecnico'].includes(role || '')) {
      pages.push({ key: 'ordenes', label: 'Órdenes', path: '/ordenes' });
    }

    // Control - para rol de Calidad
    if (['Calidad', 'DirectorTecnico'].includes(role || '')) {
      pages.push({ key: 'control', label: 'Control', path: '/control' });
    }

    // Registro - para gestión de inventario
    if (['Operaciones', 'AreaAdministrativa', 'DirectorTecnico'].includes(role || '')) {
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
              {pathname === '/dashboard' && 'Dashboard - Inventario'}
              {pathname === '/movimientos' && 'Movimientos de Inventario'}
              {pathname === '/ordenes' && 'Gestión de Órdenes'}
              {pathname === '/control' && 'Control y Supervisión'}
              {pathname === '/registro-inventario' && 'Registro de Inventario'}
            </span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">
              {user?.role === 'Cliente' && 'Cliente'}
              {user?.role === 'Recepcion' && 'Recepción'}
              {user?.role === 'Operaciones' && 'Operaciones'}
              {user?.role === 'Calidad' && 'Control de Calidad'}
              {user?.role === 'Despacho' && 'Despacho'}
              {user?.role === 'AreaAdministrativa' && 'Área Administrativa'}
              {user?.role === 'DirectorTecnico' && 'Director Técnico'}
              : {user?.username}
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
                  pathname === page.path
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