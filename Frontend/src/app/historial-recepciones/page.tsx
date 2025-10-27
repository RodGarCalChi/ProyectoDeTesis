'use client';

import React, { useState, useEffect } from 'react';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Navigation } from '@/components/Navigation';
import { recepcionesApi, clientesApi } from '@/lib/api';

interface Recepcion {
  id: string;
  numeroOrdenCompra: string;
  numeroGuiaRemision: string;
  clienteId: string;
  clienteNombre: string;
  fechaRecepcion: string;
  responsableRecepcion: string;
  estado: string;
  observaciones?: string;
  fechaCreacion: string;
}

interface Cliente {
  id: string;
  razonSocial: string;
}

const ESTADOS = [
  { value: '', label: 'Todos los estados' },
  { value: 'PENDIENTE', label: 'Pendiente' },
  { value: 'EN_VERIFICACION', label: 'En Verificación' },
  { value: 'EN_CUARENTENA', label: 'En Cuarentena' },
  { value: 'APROBADO', label: 'Aprobado' },
  { value: 'RECHAZADO', label: 'Rechazado' },
  { value: 'ALMACENADO', label: 'Almacenado' },
  { value: 'DEVUELTO', label: 'Devuelto' }
];

const getEstadoColor = (estado: string) => {
  const colores: Record<string, string> = {
    'PENDIENTE': 'bg-yellow-100 text-yellow-800',
    'EN_VERIFICACION': 'bg-blue-100 text-blue-800',
    'EN_CUARENTENA': 'bg-orange-100 text-orange-800',
    'APROBADO': 'bg-green-100 text-green-800',
    'RECHAZADO': 'bg-red-100 text-red-800',
    'ALMACENADO': 'bg-purple-100 text-purple-800',
    'DEVUELTO': 'bg-gray-100 text-gray-800'
  };
  return colores[estado] || 'bg-gray-100 text-gray-800';
};

function HistorialRecepcionesContent() {
  const [recepciones, setRecepciones] = useState<Recepcion[]>([]);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Filtros
  const [fechaInicio, setFechaInicio] = useState(new Date().toISOString().slice(0, 10));
  const [fechaFin, setFechaFin] = useState(new Date().toISOString().slice(0, 10));
  const [estadoFiltro, setEstadoFiltro] = useState('');
  const [clienteFiltro, setClienteFiltro] = useState('');
  const [numeroOrdenFiltro, setNumeroOrdenFiltro] = useState('');

  useEffect(() => {
    cargarClientes();
    cargarRecepciones();
  }, []);

  const cargarClientes = async () => {
    try {
      const data = await clientesApi.obtenerActivos();
      if (data.success) {
        setClientes(data.data);
      }
    } catch (error) {
      console.error('Error al cargar clientes:', error);
    }
  };

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
        // Filtrar por fecha del día actual por defecto
        const recepcionesFiltradas = filtrarRecepciones(data.data);
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

  const filtrarRecepciones = (data: Recepcion[]) => {
    return data.filter(recepcion => {
      const fechaRecepcion = new Date(recepcion.fechaRecepcion).toISOString().slice(0, 10);
      
      // Filtro por rango de fechas
      if (fechaInicio && fechaRecepcion < fechaInicio) return false;
      if (fechaFin && fechaRecepcion > fechaFin) return false;
      
      // Filtro por estado
      if (estadoFiltro && recepcion.estado !== estadoFiltro) return false;
      
      // Filtro por cliente
      if (clienteFiltro && recepcion.clienteId !== clienteFiltro) return false;
      
      // Filtro por número de orden
      if (numeroOrdenFiltro && !recepcion.numeroOrdenCompra.toLowerCase().includes(numeroOrdenFiltro.toLowerCase())) {
        return false;
      }
      
      return true;
    });
  };

  const handleBuscar = () => {
    cargarRecepciones();
  };

  const handleLimpiarFiltros = () => {
    setFechaInicio(new Date().toISOString().slice(0, 10));
    setFechaFin(new Date().toISOString().slice(0, 10));
    setEstadoFiltro('');
    setClienteFiltro('');
    setNumeroOrdenFiltro('');
    cargarRecepciones();
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
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      <div className="mx-auto px-4 sm:px-6 lg:px-8 py-8 max-w-7xl">
        <div className="mb-6">
          <h1 className="font-bold text-gray-900 text-3xl">Historial de Recepciones</h1>
          <p className="mt-2 text-gray-600 text-sm">
            Consulta y filtra las recepciones de mercadería registradas
          </p>
        </div>

        {/* Filtros */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <h2 className="mb-4 font-semibold text-gray-900 text-lg">Filtros de Búsqueda</h2>
          
          <div className="gap-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
            {/* Fecha Inicio */}
            <div>
              <label className="block mb-2 font-medium text-gray-700 text-sm">
                Fecha Inicio
              </label>
              <input
                type="date"
                value={fechaInicio}
                onChange={(e) => setFechaInicio(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
              />
            </div>

            {/* Fecha Fin */}
            <div>
              <label className="block mb-2 font-medium text-gray-700 text-sm">
                Fecha Fin
              </label>
              <input
                type="date"
                value={fechaFin}
                onChange={(e) => setFechaFin(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
              />
            </div>

            {/* Estado */}
            <div>
              <label className="block mb-2 font-medium text-gray-700 text-sm">
                Estado
              </label>
              <select
                value={estadoFiltro}
                onChange={(e) => setEstadoFiltro(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
              >
                {ESTADOS.map(estado => (
                  <option key={estado.value} value={estado.value}>
                    {estado.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Cliente */}
            <div>
              <label className="block mb-2 font-medium text-gray-700 text-sm">
                Cliente
              </label>
              <select
                value={clienteFiltro}
                onChange={(e) => setClienteFiltro(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
              >
                <option value="">Todos los clientes</option>
                {clientes.map(cliente => (
                  <option key={cliente.id} value={cliente.id}>
                    {cliente.razonSocial}
                  </option>
                ))}
              </select>
            </div>

            {/* Número de Orden */}
            <div>
              <label className="block mb-2 font-medium text-gray-700 text-sm">
                Número de Orden
              </label>
              <input
                type="text"
                value={numeroOrdenFiltro}
                onChange={(e) => setNumeroOrdenFiltro(e.target.value)}
                placeholder="Buscar por número..."
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
              />
            </div>
          </div>

          {/* Botones */}
          <div className="flex gap-3 mt-4">
            <button
              onClick={handleBuscar}
              className="bg-blue-600 hover:bg-blue-700 px-6 py-2 rounded-md text-white transition-colors"
            >
              Buscar
            </button>
            <button
              onClick={handleLimpiarFiltros}
              className="bg-gray-200 hover:bg-gray-300 px-6 py-2 rounded-md text-gray-700 transition-colors"
            >
              Limpiar Filtros
            </button>
          </div>
        </div>

        {/* Tabla de Recepciones */}
        <div className="bg-white shadow-sm rounded-lg overflow-hidden">
          {error && (
            <div className="bg-red-50 p-4 border-red-500 border-l-4 text-red-700">
              {error}
            </div>
          )}

          {loading ? (
            <div className="p-8 text-center">
              <div className="inline-block border-blue-600 border-b-2 rounded-full w-8 h-8 animate-spin"></div>
              <p className="mt-2 text-gray-600">Cargando recepciones...</p>
            </div>
          ) : recepciones.length === 0 ? (
            <div className="p-8 text-gray-500 text-center">
              No se encontraron recepciones para los filtros seleccionados
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
                      <td className="px-6 py-4 font-medium text-sm whitespace-nowrap">
                        <button
                          onClick={() => window.location.href = `/acta-recepcion?id=${recepcion.id}`}
                          className="mr-3 text-blue-600 hover:text-blue-900"
                        >
                          Ver Detalles
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
                Mostrando <span className="font-medium">{recepciones.length}</span> recepciones
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default function HistorialRecepcionesPage() {
  return (
    <ProtectedRoute>
      <HistorialRecepcionesContent />
    </ProtectedRoute>
  );
}
