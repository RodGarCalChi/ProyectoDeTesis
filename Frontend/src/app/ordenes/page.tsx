'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';
import { recepcionesApi } from '@/lib/api';
import { GestionLotes } from '@/components/GestionLotes';
import { RegistroInventario } from '@/components/RegistroInventario';

// --- Interfaces ---

interface Recepcion {
  id: string;
  numeroOrdenCompra: string;
  numeroGuiaRemision: string;
  clienteNombre: string;
  fechaRecepcion: string;
  responsableRecepcion: string;
  estado: string;
  totalProductos?: number;
  productosConformes?: number;
  productosNoConformes?: number;
}

const getEstadoColor = (estado: string) => {
  const colores: Record<string, string> = {
    'EN_VERIFICACION': 'bg-blue-100 text-blue-800',
    'EN_CUARENTENA': 'bg-orange-100 text-orange-800',
    'APROBADO': 'bg-green-100 text-green-800',
    'RECHAZADO': 'bg-red-100 text-red-800'
  };
  return colores[estado] || 'bg-gray-100 text-gray-800';
};

// --- Componente de Validación de Actas ---

function ValidacionActasView() {
  const router = useRouter();
  const [recepciones, setRecepciones] = useState<Recepcion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filtroEstado, setFiltroEstado] = useState('EN_CUARENTENA');

  useEffect(() => {
    cargarRecepciones();
  }, [filtroEstado]);

  const cargarRecepciones = async () => {
    setLoading(true);
    setError('');

    try {
      const params: any = {
        page: 0,
        size: 100,
        sortBy: 'fechaRecepcion',
        sortDir: 'desc'
      };

      const data = await recepcionesApi.obtenerTodas(params);

      if (data.success) {
        const recepcionesFiltradas = data.data.filter((r: Recepcion) => {
          if (filtroEstado === 'TODOS') return true;
          return r.estado === filtroEstado;
        });

        setRecepciones(recepcionesFiltradas);
      } else {
        setError('Error al cargar recepciones');
      }
    } catch (error) {
      console.error('Error al cargar recepciones:', error);
      setError('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  const handleValidar = (recepcionId: string) => {
    router.push(`/validacion-actas/detalle?id=${recepcionId}`);
  };

  const handleCambiarEstado = async (recepcionId: string, nuevoEstado: string) => {
    if (!confirm(`¿Está seguro de cambiar el estado a ${nuevoEstado.replace('_', ' ')}?`)) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/recepciones/${recepcionId}/cambiar-estado`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          estado: nuevoEstado,
          actualizadoPor: 'Usuario Admin'
        })
      });

      const data = await response.json();

      if (data.success) {
        alert('Estado actualizado exitosamente');
        cargarRecepciones();
      } else {
        alert('Error al actualizar estado: ' + data.message);
      }
    } catch (error) {
      console.error('Error al cambiar estado:', error);
      alert('Error al conectar con el servidor');
    }
  };

  const formatearFecha = (fecha: string) => {
    return new Date(fecha).toLocaleString('es-PE', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="mb-6">
        <h3 className="font-bold text-gray-900 text-2xl">Validación de Actas de Recepción</h3>
        <p className="mt-2 text-gray-600 text-sm">
          Revise y valide las actas de recepción para registrar productos en el inventario
        </p>
      </div>

      {/* Filtros */}
      <div className="bg-white shadow-sm p-6 rounded-lg">
        <div className="flex items-center gap-4">
          <label className="font-medium text-gray-700 text-sm">
            Filtrar por estado:
          </label>
          <select
            value={filtroEstado}
            onChange={(e) => setFiltroEstado(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="EN_CUARENTENA">Pendientes de Validación</option>
            <option value="EN_VERIFICACION">En Verificación</option>
            <option value="APROBADO">Aprobadas</option>
            <option value="RECHAZADO">Rechazadas</option>
            <option value="TODOS">Todas</option>
          </select>
        </div>
      </div>

      {/* Estadísticas */}
      <div className="gap-4 grid grid-cols-1 md:grid-cols-4">
        <div className="bg-white shadow-sm p-6 rounded-lg">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-orange-100 p-3 rounded-md">
              <svg className="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="font-medium text-gray-600 text-sm">Cuarentena</p>
              <p className="font-semibold text-gray-900 text-2xl">
                {recepciones.filter(r => r.estado === 'EN_CUARENTENA').length}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white shadow-sm p-6 rounded-lg">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-blue-100 p-3 rounded-md">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="font-medium text-gray-600 text-sm">En Verificación</p>
              <p className="font-semibold text-gray-900 text-2xl">
                {recepciones.filter(r => r.estado === 'EN_VERIFICACION').length}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white shadow-sm p-6 rounded-lg">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-green-100 p-3 rounded-md">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="font-medium text-gray-600 text-sm">Aprobadas</p>
              <p className="font-semibold text-gray-900 text-2xl">
                {recepciones.filter(r => r.estado === 'APROBADO').length}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white shadow-sm p-6 rounded-lg">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-red-100 p-3 rounded-md">
              <svg className="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="font-medium text-gray-600 text-sm">Rechazadas</p>
              <p className="font-semibold text-gray-900 text-2xl">
                {recepciones.filter(r => r.estado === 'RECHAZADO').length}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Tabla de Actas */}
      <div className="bg-white shadow-sm rounded-lg overflow-hidden">
        {error && (
          <div className="bg-red-50 p-4 border-red-500 border-l-4 text-red-700">
            {error}
          </div>
        )}

        {loading ? (
          <div className="p-8 text-center">
            <div className="inline-block border-blue-600 border-b-2 rounded-full w-8 h-8 animate-spin"></div>
            <p className="mt-2 text-gray-600">Cargando actas...</p>
          </div>
        ) : recepciones.length === 0 ? (
          <div className="p-8 text-gray-500 text-center">
            No hay actas {filtroEstado !== 'TODOS' ? 'en este estado' : 'registradas'}
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="divide-y divide-gray-200 min-w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Nº Orden
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Cliente
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Fecha Recepción
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Responsable
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-center uppercase tracking-wider">
                    Cambiar Estado
                  </th>
                  <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {recepciones.map((recepcion) => (
                  <tr key={recepcion.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 font-medium text-gray-900 text-sm whitespace-nowrap">
                      {recepcion.numeroOrdenCompra}
                    </td>
                    <td className="px-6 py-4 text-gray-900 text-sm whitespace-nowrap">
                      {recepcion.clienteNombre}
                    </td>
                    <td className="px-6 py-4 text-gray-500 text-sm whitespace-nowrap">
                      {formatearFecha(recepcion.fechaRecepcion)}
                    </td>
                    <td className="px-6 py-4 text-gray-500 text-sm whitespace-nowrap">
                      {recepcion.responsableRecepcion}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getEstadoColor(recepcion.estado)}`}>
                        {recepcion.estado.replace('_', ' ')}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-center whitespace-nowrap">
                      <select
                        value={recepcion.estado}
                        onChange={(e) => handleCambiarEstado(recepcion.id, e.target.value)}
                        className="px-3 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                      >
                        <option value="PENDIENTE">Pendiente</option>
                        <option value="EN_VERIFICACION">En Verificación</option>
                        <option value="EN_CUARENTENA">En Cuarentena</option>
                        <option value="APROBADO">Aprobado</option>
                        <option value="RECHAZADO">Rechazado</option>
                      </select>
                    </td>
                    <td className="px-6 py-4 font-medium text-sm whitespace-nowrap">
                      <button
                        onClick={() => handleValidar(recepcion.id)}
                        className="mr-3 text-blue-600 hover:text-blue-900"
                      >
                        {recepcion.estado === 'EN_CUARENTENA' ? 'Validar' : 'Ver Detalle'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Resumen */}
        {!loading && recepciones.length > 0 && (
          <div className="bg-gray-50 px-6 py-4 border-gray-200 border-t">
            <p className="text-gray-700 text-sm">
              Mostrando <span className="font-medium">{recepciones.length}</span> actas
            </p>
          </div>
        )}
      </div>
    </div>
  );
}

// --- Layout Principal con Sidebar ---

function OrdenesContent() {
  const [activeTab, setActiveTab] = useState('ordenes');

  const renderContent = () => {
    switch (activeTab) {
      case 'validacion':
        return <ValidacionActasView />;
      case 'ordenes':
        return <GestionLotes />;
      case 'registro':
        return <RegistroInventario />;
      default:
        return <GestionLotes />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />

      <div className="flex h-[calc(100vh-64px)]">
        {/* Sidebar */}
        <div className="w-64 bg-white border-r border-gray-200 flex-shrink-0">
          <div className="p-6">
            <h2 className="text-xl font-bold text-gray-800">Gestión</h2>
            <p className="text-sm text-gray-500">Administración de Lotes</p>
          </div>
          <nav className="px-4 space-y-2">
            <button
              onClick={() => setActiveTab('validacion')}
              className={`w-full flex items-center gap-3 px-4 py-3 text-sm font-medium rounded-lg transition-colors ${activeTab === 'validacion'
                  ? 'bg-blue-50 text-blue-700'
                  : 'text-gray-700 hover:bg-gray-50'
                }`}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              Validación Actas
            </button>

            <button
              onClick={() => setActiveTab('ordenes')}
              className={`w-full flex items-center gap-3 px-4 py-3 text-sm font-medium rounded-lg transition-colors ${activeTab === 'ordenes'
                  ? 'bg-blue-50 text-blue-700'
                  : 'text-gray-700 hover:bg-gray-50'
                }`}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              Gestión de Lotes
            </button>

            <button
              onClick={() => setActiveTab('registro')}
              className={`w-full flex items-center gap-3 px-4 py-3 text-sm font-medium rounded-lg transition-colors ${activeTab === 'registro'
                  ? 'bg-blue-50 text-blue-700'
                  : 'text-gray-700 hover:bg-gray-50'
                }`}
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
              Registro Inventario
            </button>
          </nav>
        </div>

        {/* Main Content Area */}
        <div className="flex-1 overflow-auto p-8">
          {renderContent()}
        </div>
      </div>
    </div>
  );
}

export default function OrdenesPage() {
  return (
    <ProtectedRoute requiredRole={['AreaAdministrativa', 'DirectorTecnico', 'Operaciones']}>
      <OrdenesContent />
    </ProtectedRoute>
  );
}