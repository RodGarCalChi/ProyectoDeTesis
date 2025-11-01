'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Navigation } from '@/components/Navigation';
import { recepcionesApi } from '@/lib/api';

interface ProductoPendiente {
  id: string;
  recepcionId: string;
  numeroOrdenCompra: string;
  clienteNombre: string;
  productoNombre: string;
  productoSku: string;
  loteNumero: string;
  cantidad: number;
  fechaVencimiento: string;
  fechaAprobacion: string;
  ubicacionAsignada?: string;
}

function AsignacionUbicacionesContent() {
  const router = useRouter();
  const [productos, setProductos] = useState<ProductoPendiente[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filtroCliente, setFiltroCliente] = useState('');

  useEffect(() => {
    cargarProductosPendientes();
  }, []);

  const cargarProductosPendientes = async () => {
    setLoading(true);
    setError('');
    
    try {
      // Aquí llamarías al endpoint para obtener productos aprobados sin ubicación
      // Por ahora simulamos datos
      const productosMock: ProductoPendiente[] = [
        {
          id: '1',
          recepcionId: 'rec-001',
          numeroOrdenCompra: 'ORD-2024-001',
          clienteNombre: 'Botica San Juan',
          productoNombre: 'Paracetamol 500mg',
          productoSku: 'PAR-500',
          loteNumero: 'L2024001',
          cantidad: 100,
          fechaVencimiento: '2025-12-31',
          fechaAprobacion: '2025-10-27T10:30:00'
        }
      ];
      
      setProductos(productosMock);
    } catch (error) {
      console.error('Error al cargar productos:', error);
      setError('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  const handleAsignarUbicacion = (productoId: string) => {
    router.push(`/asignacion-ubicaciones/asignar?id=${productoId}`);
  };

  const productosFiltrados = productos.filter(p => 
    !filtroCliente || p.clienteNombre.toLowerCase().includes(filtroCliente.toLowerCase())
  );

  return (
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      <div className="mx-auto px-4 sm:px-6 lg:px-8 py-8 max-w-7xl">
        {/* Header */}
        <div className="mb-6">
          <h1 className="font-bold text-gray-900 text-3xl">Asignación de Ubicaciones</h1>
          <p className="mt-2 text-gray-600 text-sm">
            Asigne ubicaciones físicas en el almacén a los productos aprobados
          </p>
        </div>

        {/* Estadísticas */}
        <div className="gap-4 grid grid-cols-1 md:grid-cols-3 mb-6">
          <div className="bg-white shadow-sm p-6 rounded-lg">
            <div className="flex items-center">
              <div className="flex-shrink-0 bg-orange-100 p-3 rounded-md">
                <svg className="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
              </div>
              <div className="ml-4">
                <p className="font-medium text-gray-600 text-sm">Pendientes</p>
                <p className="font-semibold text-gray-900 text-2xl">{productos.length}</p>
              </div>
            </div>
          </div>

          <div className="bg-white shadow-sm p-6 rounded-lg">
            <div className="flex items-center">
              <div className="flex-shrink-0 bg-blue-100 p-3 rounded-md">
                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
              </div>
              <div className="ml-4">
                <p className="font-medium text-gray-600 text-sm">Total Unidades</p>
                <p className="font-semibold text-gray-900 text-2xl">
                  {productos.reduce((sum, p) => sum + p.cantidad, 0)}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white shadow-sm p-6 rounded-lg">
            <div className="flex items-center">
              <div className="flex-shrink-0 bg-green-100 p-3 rounded-md">
                <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
              </div>
              <div className="ml-4">
                <p className="font-medium text-gray-600 text-sm">Clientes</p>
                <p className="font-semibold text-gray-900 text-2xl">
                  {new Set(productos.map(p => p.clienteNombre)).size}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Filtros */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <div className="flex items-center gap-4">
            <label className="font-medium text-gray-700 text-sm">
              Buscar por cliente:
            </label>
            <input
              type="text"
              value={filtroCliente}
              onChange={(e) => setFiltroCliente(e.target.value)}
              placeholder="Nombre del cliente..."
              className="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        {/* Tabla de Productos */}
        <div className="bg-white shadow-sm rounded-lg overflow-hidden">
          {error && (
            <div className="bg-red-50 p-4 border-red-500 border-l-4 text-red-700">
              {error}
            </div>
          )}

          {loading ? (
            <div className="p-8 text-center">
              <div className="inline-block border-blue-600 border-b-2 rounded-full w-8 h-8 animate-spin"></div>
              <p className="mt-2 text-gray-600">Cargando productos...</p>
            </div>
          ) : productosFiltrados.length === 0 ? (
            <div className="p-8 text-gray-500 text-center">
              {productos.length === 0 
                ? 'No hay productos pendientes de asignación de ubicación'
                : 'No se encontraron productos para el filtro aplicado'
              }
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="divide-y divide-gray-200 min-w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Producto
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Cliente
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Lote
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Cantidad
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Vencimiento
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Nº Orden
                    </th>
                    <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {productosFiltrados.map((producto) => (
                    <tr key={producto.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div>
                          <p className="font-medium text-gray-900 text-sm">{producto.productoNombre}</p>
                          <p className="text-gray-500 text-xs">SKU: {producto.productoSku}</p>
                        </div>
                      </td>
                      <td className="px-6 py-4 text-gray-900 text-sm whitespace-nowrap">
                        {producto.clienteNombre}
                      </td>
                      <td className="px-6 py-4 text-gray-900 text-sm whitespace-nowrap">
                        {producto.loteNumero}
                      </td>
                      <td className="px-6 py-4 text-gray-900 text-sm whitespace-nowrap">
                        {producto.cantidad}
                      </td>
                      <td className="px-6 py-4 text-gray-500 text-sm whitespace-nowrap">
                        {new Date(producto.fechaVencimiento).toLocaleDateString('es-PE')}
                      </td>
                      <td className="px-6 py-4 text-gray-500 text-sm whitespace-nowrap">
                        {producto.numeroOrdenCompra}
                      </td>
                      <td className="px-6 py-4 font-medium text-sm whitespace-nowrap">
                        <button
                          onClick={() => handleAsignarUbicacion(producto.id)}
                          className="flex items-center gap-1 text-blue-600 hover:text-blue-900"
                        >
                          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                          </svg>
                          Asignar Ubicación
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Resumen */}
          {!loading && productosFiltrados.length > 0 && (
            <div className="bg-gray-50 px-6 py-4 border-gray-200 border-t">
              <p className="text-gray-700 text-sm">
                Mostrando <span className="font-medium">{productosFiltrados.length}</span> productos pendientes
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default function AsignacionUbicacionesPage() {
  return (
    <ProtectedRoute>
      <AsignacionUbicacionesContent />
    </ProtectedRoute>
  );
}
