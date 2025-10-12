'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface MovimientoStock {
  id: string;
  fecha: string;
  tipo: 'Entrada' | 'Salida' | 'Transferencia' | 'Ajuste';
  producto: string;
  lote: string;
  cantidad: number;
  ubicacionOrigen?: string;
  ubicacionDestino?: string;
  motivo: string;
  responsable: string;
  documento?: string;
  estado: 'Pendiente' | 'Procesado' | 'Cancelado';
}

function MovimientosStockContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('todos');
  const [movimientos, setMovimientos] = useState<MovimientoStock[]>([]);
  const [loading, setLoading] = useState(false);
  const [showNewMovementModal, setShowNewMovementModal] = useState(false);

  useEffect(() => {
    // Simular carga de movimientos
    setMovimientos([
      {
        id: '1',
        fecha: '2024-01-15 14:30',
        tipo: 'Entrada',
        producto: 'Paracetamol 500mg',
        lote: 'L2024001',
        cantidad: 1000,
        ubicacionDestino: 'A1-B2-C3',
        motivo: 'Recepción de mercadería',
        responsable: 'Juan Pérez',
        documento: 'GR-2024-001',
        estado: 'Procesado'
      },
      {
        id: '2',
        fecha: '2024-01-15 15:45',
        tipo: 'Salida',
        producto: 'Ibuprofeno 400mg',
        lote: 'L2024002',
        cantidad: 250,
        ubicacionOrigen: 'A2-B1-C2',
        motivo: 'Despacho a cliente',
        responsable: 'María García',
        documento: 'GS-2024-001',
        estado: 'Procesado'
      },
      {
        id: '3',
        fecha: '2024-01-15 16:20',
        tipo: 'Transferencia',
        producto: 'Amoxicilina 250mg',
        lote: 'L2024003',
        cantidad: 500,
        ubicacionOrigen: 'B1-A3-C1',
        ubicacionDestino: 'C2-B1-A3',
        motivo: 'Reubicación por optimización',
        responsable: 'Carlos López',
        estado: 'Pendiente'
      },
      {
        id: '4',
        fecha: '2024-01-15 17:10',
        tipo: 'Ajuste',
        producto: 'Aspirina 100mg',
        lote: 'L2024004',
        cantidad: -50,
        ubicacionOrigen: 'D1-C2-B1',
        motivo: 'Ajuste por inventario físico',
        responsable: 'Ana Rodríguez',
        documento: 'AJ-2024-001',
        estado: 'Procesado'
      }
    ]);
  }, []);

  const getTipoColor = (tipo: string) => {
    switch (tipo) {
      case 'Entrada':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Salida':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'Transferencia':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'Ajuste':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'Pendiente':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Procesado':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Cancelado':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const filteredMovimientos = movimientos.filter(mov => {
    if (activeTab === 'todos') return true;
    if (activeTab === 'entradas') return mov.tipo === 'Entrada';
    if (activeTab === 'salidas') return mov.tipo === 'Salida';
    if (activeTab === 'transferencias') return mov.tipo === 'Transferencia';
    if (activeTab === 'ajustes') return mov.tipo === 'Ajuste';
    return true;
  });

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="p-6">
        {/* Header */}
        <div className="mb-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                📦 Movimientos de Stock
              </h1>
              <p className="text-gray-600 mt-1">
                Control y seguimiento de movimientos de inventario
              </p>
            </div>
            <div className="flex gap-3">
              <button 
                onClick={() => setShowNewMovementModal(true)}
                className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2 font-medium"
              >
                ➕ Nuevo Movimiento
              </button>
              <button className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700">
                📊 Reportes
              </button>
            </div>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-green-100 rounded-lg">
                <span className="text-2xl">📥</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Entradas Hoy</p>
                <p className="text-2xl font-bold text-gray-900">
                  {movimientos.filter(m => m.tipo === 'Entrada' && m.fecha.includes('2024-01-15')).length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-red-100 rounded-lg">
                <span className="text-2xl">📤</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Salidas Hoy</p>
                <p className="text-2xl font-bold text-gray-900">
                  {movimientos.filter(m => m.tipo === 'Salida' && m.fecha.includes('2024-01-15')).length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-blue-100 rounded-lg">
                <span className="text-2xl">🔄</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Transferencias</p>
                <p className="text-2xl font-bold text-gray-900">
                  {movimientos.filter(m => m.tipo === 'Transferencia').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <span className="text-2xl">⏳</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Pendientes</p>
                <p className="text-2xl font-bold text-gray-900">
                  {movimientos.filter(m => m.estado === 'Pendiente').length}
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
                { key: 'todos', label: 'Todos los Movimientos', count: movimientos.length },
                { key: 'entradas', label: 'Entradas', count: movimientos.filter(m => m.tipo === 'Entrada').length },
                { key: 'salidas', label: 'Salidas', count: movimientos.filter(m => m.tipo === 'Salida').length },
                { key: 'transferencias', label: 'Transferencias', count: movimientos.filter(m => m.tipo === 'Transferencia').length },
                { key: 'ajustes', label: 'Ajustes', count: movimientos.filter(m => m.tipo === 'Ajuste').length }
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
                  {tab.label}
                  <span className={`px-2 py-1 text-xs rounded-full ${
                    activeTab === tab.key ? 'bg-blue-100 text-blue-600' : 'bg-gray-100 text-gray-600'
                  }`}>
                    {tab.count}
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
                    placeholder="Buscar por producto, lote o documento..."
                    className="pl-10 pr-4 py-2 w-80 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  />
                  <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">🔍</span>
                </div>
                <input
                  type="date"
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                />
                <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                  <option value="">Todos los responsables</option>
                  <option value="juan">Juan Pérez</option>
                  <option value="maria">María García</option>
                  <option value="carlos">Carlos López</option>
                </select>
              </div>
              <div className="flex gap-2">
                <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                  📊 Exportar
                </button>
                <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                  🔄 Actualizar
                </button>
              </div>
            </div>
          </div>

          {/* Movements Table */}
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Fecha/Hora
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Tipo
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Producto/Lote
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Cantidad
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ubicaciones
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Responsable
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredMovimientos.map((movimiento) => (
                  <tr key={movimiento.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {movimiento.fecha.split(' ')[0]}
                      </div>
                      <div className="text-sm text-gray-500">
                        {movimiento.fecha.split(' ')[1]}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getTipoColor(movimiento.tipo)}`}>
                        {movimiento.tipo}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{movimiento.producto}</div>
                        <div className="text-sm text-gray-500">Lote: {movimiento.lote}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`text-sm font-medium ${
                        movimiento.cantidad > 0 ? 'text-green-600' : 'text-red-600'
                      }`}>
                        {movimiento.cantidad > 0 ? '+' : ''}{movimiento.cantidad}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">
                        {movimiento.ubicacionOrigen && (
                          <div>Origen: {movimiento.ubicacionOrigen}</div>
                        )}
                        {movimiento.ubicacionDestino && (
                          <div>Destino: {movimiento.ubicacionDestino}</div>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {movimiento.responsable}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getEstadoColor(movimiento.estado)}`}>
                        {movimiento.estado}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button className="text-blue-600 hover:text-blue-900 px-2 py-1 rounded hover:bg-blue-50">
                          👁️ Ver
                        </button>
                        {movimiento.estado === 'Pendiente' && (
                          <button className="text-green-600 hover:text-green-900 px-2 py-1 rounded hover:bg-green-50">
                            ✅ Procesar
                          </button>
                        )}
                        {movimiento.documento && (
                          <button className="text-purple-600 hover:text-purple-900 px-2 py-1 rounded hover:bg-purple-50">
                            📄 Doc
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {filteredMovimientos.length === 0 && (
            <div className="text-center py-12">
              <div className="text-6xl mb-4">📦</div>
              <h3 className="text-lg font-medium text-gray-900 mb-2">No hay movimientos</h3>
              <p className="text-gray-500">No se encontraron movimientos que coincidan con los filtros seleccionados.</p>
            </div>
          )}
        </div>

        {/* Quick Actions */}
        <div className="mt-8 grid grid-cols-1 md:grid-cols-4 gap-6">
          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-green-100 rounded-lg">
                <span className="text-2xl">📥</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Entrada</h4>
            </div>
            <p className="text-gray-600 mb-4">Registrar ingreso de mercadería</p>
            <button className="w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700">
              Nueva Entrada
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-red-100 rounded-lg">
                <span className="text-2xl">📤</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Salida</h4>
            </div>
            <p className="text-gray-600 mb-4">Registrar despacho de productos</p>
            <button className="w-full bg-red-600 text-white py-2 px-4 rounded hover:bg-red-700">
              Nueva Salida
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-blue-100 rounded-lg">
                <span className="text-2xl">🔄</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Transferencia</h4>
            </div>
            <p className="text-gray-600 mb-4">Mover entre ubicaciones</p>
            <button className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700">
              Nueva Transferencia
            </button>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-center mb-4">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <span className="text-2xl">⚖️</span>
              </div>
              <h4 className="ml-3 text-lg font-semibold text-gray-900">Ajuste</h4>
            </div>
            <p className="text-gray-600 mb-4">Ajustar inventario físico</p>
            <button className="w-full bg-yellow-600 text-white py-2 px-4 rounded hover:bg-yellow-700">
              Nuevo Ajuste
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function MovimientosStockPage() {
  return (
    <ProtectedRoute requiredRole={['Operaciones', 'DirectorTecnico']}>
      <MovimientosStockContent />
    </ProtectedRoute>
  );
}