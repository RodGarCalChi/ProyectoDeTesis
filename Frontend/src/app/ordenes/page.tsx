'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';

function OrdenesContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('pendientes');
  const [showNewOrderModal, setShowNewOrderModal] = useState(false);

  const [ordenes, setOrdenes] = useState([
    {
      id: '1',
      numero: 'ORD-2024-001',
      tipo: 'Compra',
      proveedor: 'Laboratorio ABC',
      fecha: '2024-10-05',
      estado: 'Pendiente',
      total: 15000,
      items: 5,
      fechaEntrega: '2024-10-12'
    },
    {
      id: '2',
      numero: 'ORD-2024-002',
      tipo: 'Venta',
      cliente: 'Farmacia Central',
      fecha: '2024-10-06',
      estado: 'Confirmada',
      total: 8500,
      items: 3,
      fechaEntrega: '2024-10-08'
    },
    {
      id: '3',
      numero: 'ORD-2024-003',
      tipo: 'Transferencia',
      destino: 'Almacén Secundario',
      fecha: '2024-10-07',
      estado: 'En Proceso',
      total: 12000,
      items: 8,
      fechaEntrega: '2024-10-09'
    }
  ]);

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'Pendiente':
        return 'bg-yellow-100 text-yellow-800';
      case 'Confirmada':
        return 'bg-green-100 text-green-800';
      case 'En Proceso':
        return 'bg-blue-100 text-blue-800';
      case 'Completada':
        return 'bg-gray-100 text-gray-800';
      case 'Cancelada':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const filteredOrdenes = ordenes.filter(orden => {
    switch (activeTab) {
      case 'pendientes':
        return orden.estado === 'Pendiente';
      case 'proceso':
        return orden.estado === 'En Proceso' || orden.estado === 'Confirmada';
      case 'completadas':
        return orden.estado === 'Completada';
      default:
        return true;
    }
  });

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <h1 className="text-xl font-bold text-gray-900">PharmaFlow</h1>
            <span className="text-sm text-gray-600">Gestión de Órdenes</span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">Jefe de Ejecutivas: {user?.firstName}</span>
            <button
              onClick={handleLogout}
              className="text-gray-600 hover:text-gray-900"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white border-b border-gray-200 px-6 py-2">
        <div className="flex gap-6">
          <button 
            onClick={() => router.push('/dashboard')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Inventario
          </button>
          <button 
            onClick={() => router.push('/movimientos')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Movimientos
          </button>
          <button 
            onClick={() => router.push('/ordenes')}
            className="text-blue-600 border-b-2 border-blue-600 pb-2 text-sm font-medium"
          >
            Órdenes
          </button>
          <button 
            onClick={() => router.push('/control')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Control
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <div className="p-6">
        {/* Header Actions */}
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-gray-900">Gestión de Órdenes</h2>
          <button
            onClick={() => setShowNewOrderModal(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center gap-2"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nueva Orden
          </button>
        </div>

        {/* Cards de Resumen - Estilo Figma */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600">Órdenes Pendientes</h3>
                <p className="text-2xl font-bold text-blue-600">4</p>
              </div>
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600">En Proceso</h3>
                <p className="text-2xl font-bold text-yellow-600">1</p>
              </div>
              <div className="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
                <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-medium text-gray-600">Completadas</h3>
                <p className="text-2xl font-bold text-green-600">1</p>
              </div>
              <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('pendientes')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'pendientes'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Pendientes ({ordenes.filter(o => o.estado === 'Pendiente').length})
            </button>
            <button
              onClick={() => setActiveTab('proceso')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'proceso'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              En Proceso ({ordenes.filter(o => o.estado === 'En Proceso' || o.estado === 'Confirmada').length})
            </button>
            <button
              onClick={() => setActiveTab('completadas')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'completadas'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Completadas ({ordenes.filter(o => o.estado === 'Completada').length})
            </button>
            <button
              onClick={() => setActiveTab('todas')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'todas'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Todas ({ordenes.length})
            </button>
          </div>
        </div>

        {/* Orders Table */}
        <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
          <div className="px-6 py-4 border-b border-gray-200">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">
                {activeTab === 'pendientes' && 'Órdenes Pendientes'}
                {activeTab === 'proceso' && 'Órdenes en Proceso'}
                {activeTab === 'completadas' && 'Órdenes Completadas'}
                {activeTab === 'todas' && 'Todas las Órdenes'}
              </h3>
              <div className="flex gap-2">
                <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                  Filtros
                </button>
                <button className="px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700">
                  Exportar
                </button>
              </div>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Número
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Tipo
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Cliente/Proveedor
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Fecha
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Entrega
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Items
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Total
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredOrdenes.map((orden) => (
                  <tr key={orden.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-blue-600">
                      {orden.numero}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {orden.tipo}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {orden.proveedor || orden.cliente || orden.destino}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {orden.fecha}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {orden.fechaEntrega}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {orden.items}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-medium">
                      S/ {orden.total.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getEstadoColor(orden.estado)}`}>
                        {orden.estado}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button className="text-blue-600 hover:text-blue-900">Ver</button>
                        <button className="text-green-600 hover:text-green-900">Editar</button>
                        {orden.estado === 'Pendiente' && (
                          <button className="text-purple-600 hover:text-purple-900">Confirmar</button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {filteredOrdenes.length === 0 && (
            <div className="text-center py-12">
              <svg className="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 mb-2">No hay órdenes</h3>
              <p className="text-gray-500">No se encontraron órdenes en esta categoría.</p>
            </div>
          )}
        </div>

        {/* Quick Actions */}
        <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center mb-4">
              <div className="p-2 bg-blue-100 rounded-lg">
                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <h3 className="ml-3 text-lg font-medium text-gray-900">Orden de Compra</h3>
            </div>
            <p className="text-gray-600 mb-4">Crear nueva orden de compra a proveedores</p>
            <button className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700">
              Crear Orden
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center mb-4">
              <div className="p-2 bg-green-100 rounded-lg">
                <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                </svg>
              </div>
              <h3 className="ml-3 text-lg font-medium text-gray-900">Orden de Venta</h3>
            </div>
            <p className="text-gray-600 mb-4">Procesar pedidos de clientes</p>
            <button className="w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700">
              Nueva Venta
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center mb-4">
              <div className="p-2 bg-purple-100 rounded-lg">
                <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                </svg>
              </div>
              <h3 className="ml-3 text-lg font-medium text-gray-900">Transferencia</h3>
            </div>
            <p className="text-gray-600 mb-4">Transferir productos entre almacenes</p>
            <button className="w-full bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700">
              Nueva Transferencia
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function OrdenesPage() {
  return (
    <ProtectedRoute requiredRole="Jefe_Ejecutivas">
      <OrdenesContent />
    </ProtectedRoute>
  );
}