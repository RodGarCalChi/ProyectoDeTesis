'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';

function ControlContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('calidad');

  // Datos de ejemplo para control de calidad
  const [controlCalidad, setControlCalidad] = useState([
    {
      id: '1',
      lote: 'L2024001',
      producto: 'Paracetamol 500mg',
      proveedor: 'Lab. ABC',
      fechaRecepcion: '2024-10-05',
      fechaVencimiento: '2025-12-15',
      cantidad: 1000,
      estado: 'En Cuarentena',
      inspector: 'Dr. García',
      observaciones: 'Pendiente análisis microbiológico'
    },
    {
      id: '2',
      lote: 'L2024002',
      producto: 'Ibuprofeno 400mg',
      proveedor: 'Dist. XYZ',
      fechaRecepcion: '2024-10-03',
      fechaVencimiento: '2025-08-20',
      cantidad: 500,
      estado: 'Aprobado',
      inspector: 'Dra. López',
      observaciones: 'Cumple especificaciones'
    },
    {
      id: '3',
      lote: 'L2024003',
      producto: 'Amoxicilina 250mg',
      proveedor: 'Sum. DEF',
      fechaRecepcion: '2024-10-01',
      fechaVencimiento: '2025-03-10',
      cantidad: 750,
      estado: 'Rechazado',
      inspector: 'Dr. Martínez',
      observaciones: 'Humedad excesiva en empaque'
    }
  ]);

  // Datos de auditoría
  const [auditoria, setAuditoria] = useState([
    {
      id: '1',
      fecha: '2024-10-07 14:30',
      usuario: 'admin@pharmaflow.com',
      accion: 'Modificación de Stock',
      modulo: 'Inventario',
      detalle: 'Ajuste de inventario - Paracetamol 500mg',
      ip: '192.168.1.100'
    },
    {
      id: '2',
      fecha: '2024-10-07 13:15',
      usuario: 'recepcion@pharmaflow.com',
      accion: 'Registro de Entrada',
      modulo: 'Movimientos',
      detalle: 'Nueva entrada - Lote L2024004',
      ip: '192.168.1.101'
    },
    {
      id: '3',
      fecha: '2024-10-07 12:00',
      usuario: 'jefe@pharmaflow.com',
      accion: 'Aprobación de Orden',
      modulo: 'Órdenes',
      detalle: 'Orden ORD-2024-005 aprobada',
      ip: '192.168.1.102'
    }
  ]);

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'En Cuarentena':
        return 'bg-yellow-100 text-yellow-800';
      case 'Aprobado':
        return 'bg-green-100 text-green-800';
      case 'Rechazado':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const handleAprobar = (id: string) => {
    setControlCalidad(prev => 
      prev.map(item => 
        item.id === id 
          ? { ...item, estado: 'Aprobado', observaciones: 'Aprobado por control de calidad' }
          : item
      )
    );
  };

  const handleRechazar = (id: string) => {
    const motivo = prompt('Ingrese el motivo del rechazo:');
    if (motivo) {
      setControlCalidad(prev => 
        prev.map(item => 
          item.id === id 
            ? { ...item, estado: 'Rechazado', observaciones: motivo }
            : item
        )
      );
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <h1 className="text-xl font-bold text-gray-900">PharmaFlow</h1>
            <span className="text-sm text-gray-600">Control y Supervisión</span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">Control: {user?.firstName}</span>
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
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Órdenes
          </button>
          <button 
            onClick={() => router.push('/control')}
            className="text-blue-600 border-b-2 border-blue-600 pb-2 text-sm font-medium"
          >
            Control
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <div className="p-6">
        <div className="mb-6">
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Control y Supervisión</h2>
          <p className="text-gray-600">Gestión de calidad, auditoría y cumplimiento normativo BPAs/DIGEMID</p>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('calidad')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'calidad'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Control de Calidad
            </button>
            <button
              onClick={() => setActiveTab('auditoria')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'auditoria'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Auditoría
            </button>
            <button
              onClick={() => setActiveTab('reportes')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'reportes'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Reportes Regulatorios
            </button>
            <button
              onClick={() => setActiveTab('configuracion')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'configuracion'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Configuración
            </button>
          </div>
        </div>

        {/* Control de Calidad Tab */}
        {activeTab === 'calidad' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="px-6 py-4 border-b border-gray-200">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold text-gray-900">Control de Calidad - BPAs</h3>
                <div className="flex gap-2">
                  <button className="px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700">
                    Nuevo Control
                  </button>
                  <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
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
                      Lote
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Producto
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Proveedor
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      F. Recepción
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      F. Vencimiento
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Cantidad
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Inspector
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {controlCalidad.map((item) => (
                    <tr key={item.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {item.lote}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.producto}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.proveedor}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.fechaRecepcion}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.fechaVencimiento}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.cantidad}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getEstadoColor(item.estado)}`}>
                          {item.estado}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.inspector}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex gap-2">
                          <button className="text-blue-600 hover:text-blue-900">Ver</button>
                          {item.estado === 'En Cuarentena' && (
                            <>
                              <button 
                                onClick={() => handleAprobar(item.id)}
                                className="text-green-600 hover:text-green-900"
                              >
                                Aprobar
                              </button>
                              <button 
                                onClick={() => handleRechazar(item.id)}
                                className="text-red-600 hover:text-red-900"
                              >
                                Rechazar
                              </button>
                            </>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Auditoría Tab */}
        {activeTab === 'auditoria' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="px-6 py-4 border-b border-gray-200">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold text-gray-900">Registro de Auditoría</h3>
                <div className="flex gap-2">
                  <input
                    type="date"
                    className="px-3 py-2 border border-gray-300 rounded text-sm"
                    placeholder="Fecha desde"
                  />
                  <input
                    type="date"
                    className="px-3 py-2 border border-gray-300 rounded text-sm"
                    placeholder="Fecha hasta"
                  />
                  <button className="px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700">
                    Filtrar
                  </button>
                </div>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Fecha/Hora
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Usuario
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acción
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Módulo
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Detalle
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      IP
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {auditoria.map((item) => (
                    <tr key={item.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.fecha}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.usuario}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.accion}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.modulo}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {item.detalle}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {item.ip}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Reportes Regulatorios Tab */}
        {activeTab === 'reportes' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div className="bg-white rounded-lg border border-gray-200 p-6">
              <div className="flex items-center mb-4">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                </div>
                <h3 className="ml-3 text-lg font-medium text-gray-900">Reporte DIGEMID</h3>
              </div>
              <p className="text-gray-600 mb-4">Reporte mensual para DIGEMID con movimientos de productos farmacéuticos</p>
              <button className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700">
                Generar Reporte
              </button>
            </div>

            <div className="bg-white rounded-lg border border-gray-200 p-6">
              <div className="flex items-center mb-4">
                <div className="p-2 bg-green-100 rounded-lg">
                  <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                  </svg>
                </div>
                <h3 className="ml-3 text-lg font-medium text-gray-900">Inventario BPAs</h3>
              </div>
              <p className="text-gray-600 mb-4">Reporte de cumplimiento de Buenas Prácticas de Almacenamiento</p>
              <button className="w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700">
                Generar Reporte
              </button>
            </div>

            <div className="bg-white rounded-lg border border-gray-200 p-6">
              <div className="flex items-center mb-4">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <h3 className="ml-3 text-lg font-medium text-gray-900">Vencimientos</h3>
              </div>
              <p className="text-gray-600 mb-4">Reporte de productos próximos a vencer y gestión de fechas</p>
              <button className="w-full bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700">
                Generar Reporte
              </button>
            </div>
          </div>
        )}

        {/* Configuración Tab */}
        {activeTab === 'configuracion' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-6">Configuración del Sistema</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <div>
                <h4 className="text-md font-medium text-gray-900 mb-4">Parámetros BPAs</h4>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Temperatura Mínima (°C)
                    </label>
                    <input
                      type="number"
                      defaultValue="15"
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Temperatura Máxima (°C)
                    </label>
                    <input
                      type="number"
                      defaultValue="25"
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Humedad Máxima (%)
                    </label>
                    <input
                      type="number"
                      defaultValue="60"
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>
                </div>
              </div>

              <div>
                <h4 className="text-md font-medium text-gray-900 mb-4">Alertas y Notificaciones</h4>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Días antes del vencimiento para alerta
                    </label>
                    <input
                      type="number"
                      defaultValue="30"
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Porcentaje de stock mínimo
                    </label>
                    <input
                      type="number"
                      defaultValue="20"
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>
                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      id="emailAlerts"
                      defaultChecked
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <label htmlFor="emailAlerts" className="ml-2 block text-sm text-gray-900">
                      Enviar alertas por email
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div className="mt-8 pt-6 border-t border-gray-200">
              <button className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded">
                Guardar Configuración
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default function ControlPage() {
  return (
    <ProtectedRoute requiredRole="Control">
      <ControlContent />
    </ProtectedRoute>
  );
}