'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface OrdenDespacho {
  id: string;
  numeroOrden: string;
  cliente: string;
  ruc: string;
  direccion: string;
  distrito: string;
  fechaOrden: string;
  fechaDespacho?: string;
  estado: 'Pendiente' | 'Preparando' | 'Listo' | 'Despachado' | 'Entregado';
  prioridad: 'Alta' | 'Media' | 'Baja';
  productos: number;
  valorTotal: number;
  transportista?: string;
  vehiculo?: string;
  guiaRemision?: string;
  observaciones?: string;
}

interface ProductoDespacho {
  id: string;
  producto: string;
  lote: string;
  cantidadSolicitada: number;
  cantidadDespachada: number;
  ubicacion: string;
  fechaVencimiento: string;
}

function DespachoContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('pendientes');
  const [ordenes, setOrdenes] = useState<OrdenDespacho[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Simular carga de órdenes de despacho
    setOrdenes([
      {
        id: '1',
        numeroOrden: 'ORD-2024-001',
        cliente: 'Farmacia Central S.A.C.',
        ruc: '20123456789',
        direccion: 'Av. Arequipa 1234',
        distrito: 'Lima',
        fechaOrden: '2024-01-15',
        fechaDespacho: '2024-01-16',
        estado: 'Pendiente',
        prioridad: 'Alta',
        productos: 5,
        valorTotal: 2500.00,
        observaciones: 'Entrega urgente antes de las 2 PM'
      },
      {
        id: '2',
        numeroOrden: 'ORD-2024-002',
        cliente: 'Hospital Nacional Dos de Mayo',
        ruc: '20111222333',
        direccion: 'Av. Grau 13',
        distrito: 'Cercado de Lima',
        fechaOrden: '2024-01-14',
        fechaDespacho: '2024-01-16',
        estado: 'Preparando',
        prioridad: 'Media',
        productos: 12,
        valorTotal: 8750.00,
        transportista: 'Transportes Rápidos S.A.',
        vehiculo: 'ABC-123'
      },
      {
        id: '3',
        numeroOrden: 'ORD-2024-003',
        cliente: 'Botica San Juan E.I.R.L.',
        ruc: '20987654321',
        direccion: 'Jr. Huancavelica 456',
        distrito: 'Breña',
        fechaOrden: '2024-01-13',
        fechaDespacho: '2024-01-15',
        estado: 'Listo',
        prioridad: 'Baja',
        productos: 3,
        valorTotal: 1200.00,
        transportista: 'Delivery Express',
        vehiculo: 'DEF-456',
        guiaRemision: 'GR-2024-001'
      },
      {
        id: '4',
        numeroOrden: 'ORD-2024-004',
        cliente: 'Clínica San Pablo S.A.',
        ruc: '20444555666',
        direccion: 'Av. El Derby 254',
        distrito: 'Surco',
        fechaOrden: '2024-01-12',
        fechaDespacho: '2024-01-14',
        estado: 'Despachado',
        prioridad: 'Media',
        productos: 8,
        valorTotal: 4500.00,
        transportista: 'Logística Total',
        vehiculo: 'GHI-789',
        guiaRemision: 'GR-2024-002'
      }
    ]);
  }, []);

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'Pendiente':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Preparando':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'Listo':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Despachado':
        return 'bg-purple-100 text-purple-800 border-purple-200';
      case 'Entregado':
        return 'bg-gray-100 text-gray-800 border-gray-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getPrioridadColor = (prioridad: string) => {
    switch (prioridad) {
      case 'Alta':
        return 'bg-red-50 text-red-700 border-red-200';
      case 'Media':
        return 'bg-yellow-50 text-yellow-700 border-yellow-200';
      case 'Baja':
        return 'bg-green-50 text-green-700 border-green-200';
      default:
        return 'bg-gray-50 text-gray-700 border-gray-200';
    }
  };

  const filteredOrdenes = ordenes.filter(orden => {
    if (activeTab === 'pendientes') return orden.estado === 'Pendiente';
    if (activeTab === 'preparando') return orden.estado === 'Preparando';
    if (activeTab === 'listos') return orden.estado === 'Listo';
    if (activeTab === 'despachados') return orden.estado === 'Despachado';
    if (activeTab === 'entregados') return orden.estado === 'Entregado';
    return true;
  });

  const handleCambiarEstado = (ordenId: string, nuevoEstado: OrdenDespacho['estado']) => {
    setOrdenes(prev => 
      prev.map(orden => 
        orden.id === ordenId 
          ? { ...orden, estado: nuevoEstado }
          : orden
      )
    );
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="p-6">
        {/* Header */}
        <div className="mb-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                🚚 Centro de Despacho
              </h1>
              <p className="text-gray-600 mt-1">
                Gestión de despachos, preparación de pedidos y logística de entrega
              </p>
            </div>
            <div className="flex gap-3">
           
              <button className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700">
                📊 Reportes
              </button>
            </div>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-6 mb-6">
          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <span className="text-2xl">⏳</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Pendientes</p>
                <p className="text-2xl font-bold text-gray-900">
                  {ordenes.filter(o => o.estado === 'Pendiente').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-blue-100 rounded-lg">
                <span className="text-2xl">📦</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Preparando</p>
                <p className="text-2xl font-bold text-gray-900">
                  {ordenes.filter(o => o.estado === 'Preparando').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-green-100 rounded-lg">
                <span className="text-2xl">✅</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Listos</p>
                <p className="text-2xl font-bold text-gray-900">
                  {ordenes.filter(o => o.estado === 'Listo').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-purple-100 rounded-lg">
                <span className="text-2xl">🚚</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Despachados</p>
                <p className="text-2xl font-bold text-gray-900">
                  {ordenes.filter(o => o.estado === 'Despachado').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-gray-100 rounded-lg">
                <span className="text-2xl">📍</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Entregados</p>
                <p className="text-2xl font-bold text-gray-900">
                  {ordenes.filter(o => o.estado === 'Entregado').length}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className="bg-white rounded-lg shadow-sm">
          {/* Tabs */}
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex space-x-8 px-6">
              {[
                { key: 'pendientes', label: 'Pendientes', icon: '⏳' },
                { key: 'preparando', label: 'Preparando', icon: '📦' },
                { key: 'listos', label: 'Listos para Despacho', icon: '✅' },
                { key: 'despachados', label: 'Despachados', icon: '🚚' },
                { key: 'entregados', label: 'Entregados', icon: '📍' }
              ].map(tab => (
                <button
                  key={tab.key}
                  onClick={() => setActiveTab(tab.key)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm flex items-center gap-2 ${
                    activeTab === tab.key
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  <span>{tab.icon}</span>
                  {tab.label}
                  <span className={`px-2 py-1 text-xs rounded-full ${
                    activeTab === tab.key ? 'bg-blue-100 text-blue-600' : 'bg-gray-100 text-gray-600'
                  }`}>
                    {ordenes.filter(o => {
                      if (tab.key === 'pendientes') return o.estado === 'Pendiente';
                      if (tab.key === 'preparando') return o.estado === 'Preparando';
                      if (tab.key === 'listos') return o.estado === 'Listo';
                      if (tab.key === 'despachados') return o.estado === 'Despachado';
                      if (tab.key === 'entregados') return o.estado === 'Entregado';
                      return false;
                    }).length}
                  </span>
                </button>
              ))}
            </nav>
          </div>

          {/* Filters and Search */}
          <div className="p-6 border-b border-gray-200">
            <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
              <div className="flex gap-4 flex-1">
                <div className="relative">
                  <input
                    type="text"
                    placeholder="Buscar por orden, cliente o RUC..."
                    className="pl-10 pr-4 py-2 w-80 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  />
                  <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">🔍</span>
                </div>
                <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                  <option value="">Todos los distritos</option>
                  <option value="lima">Lima</option>
                  <option value="miraflores">Miraflores</option>
                  <option value="surco">Surco</option>
                  <option value="san-isidro">San Isidro</option>
                </select>
                <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                  <option value="">Todas las prioridades</option>
                  <option value="alta">Alta</option>
                  <option value="media">Media</option>
                  <option value="baja">Baja</option>
                </select>
              </div>
              <div className="flex gap-2">
                <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                  🗺️ Ver Mapa
                </button>
                <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                  📊 Exportar
                </button>
              </div>
            </div>
          </div>

          {/* Orders Table */}
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Orden
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Cliente
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Dirección
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Fecha Despacho
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Prioridad
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Valor
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredOrdenes.map((orden) => (
                  <tr key={orden.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{orden.numeroOrden}</div>
                        <div className="text-sm text-gray-500">{orden.productos} productos</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{orden.cliente}</div>
                        <div className="text-sm text-gray-500">RUC: {orden.ruc}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm text-gray-900">{orden.direccion}</div>
                        <div className="text-sm text-gray-500">{orden.distrito}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {orden.fechaDespacho || 'Por programar'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getEstadoColor(orden.estado)}`}>
                        {orden.estado}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getPrioridadColor(orden.prioridad)}`}>
                        {orden.prioridad}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      S/ {orden.valorTotal.toLocaleString('es-PE', { minimumFractionDigits: 2 })}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button className="text-blue-600 hover:text-blue-900 px-2 py-1 rounded hover:bg-blue-50">
                          👁️ Ver
                        </button>
                        {orden.estado === 'Pendiente' && (
                          <button 
                            onClick={() => handleCambiarEstado(orden.id, 'Preparando')}
                            className="text-green-600 hover:text-green-900 px-2 py-1 rounded hover:bg-green-50"
                          >
                            📦 Preparar
                          </button>
                        )}
                        {orden.estado === 'Preparando' && (
                          <button 
                            onClick={() => handleCambiarEstado(orden.id, 'Listo')}
                            className="text-purple-600 hover:text-purple-900 px-2 py-1 rounded hover:bg-purple-50"
                          >
                            ✅ Listo
                          </button>
                        )}
                        {orden.estado === 'Listo' && (
                          <button 
                            onClick={() => handleCambiarEstado(orden.id, 'Despachado')}
                            className="text-orange-600 hover:text-orange-900 px-2 py-1 rounded hover:bg-orange-50"
                          >
                            🚚 Despachar
                          </button>
                        )}
                        {orden.guiaRemision && (
                          <button className="text-gray-600 hover:text-gray-900 px-2 py-1 rounded hover:bg-gray-50">
                            📄 Guía
                          </button>
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
              <div className="text-6xl mb-4">📦</div>
              <h3 className="text-lg font-medium text-gray-900 mb-2">No hay órdenes</h3>
              <p className="text-gray-500">No se encontraron órdenes en esta categoría.</p>
            </div>
          )}
        </div>

        {/* Quick Actions */}
        <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
  

          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-green-100 rounded-lg">
                <span className="text-2xl">📋</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Lista de Picking</h4>
            </div>
            <p className="text-gray-600 mb-4">Generar listas de preparación</p>
            <button className="w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700">
              Generar Lista
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-purple-100 rounded-lg">
                <span className="text-2xl">📄</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Guías de Remisión</h4>
            </div>
            <p className="text-gray-600 mb-4">Generar documentos de despacho</p>
            <button className="w-full bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700">
              Nueva Guía
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function DespachoPage() {
  return (
    <ProtectedRoute requiredRole={['Despacho', 'DirectorTecnico']}>
      <DespachoContent />
    </ProtectedRoute>
  );
}