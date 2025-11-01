'use client';

import React, { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Navigation } from '@/components/Navigation';
import { recepcionesApi, productosApi } from '@/lib/api';

interface Producto {
  id: string;
  nombre: string;
  codigoSKU: string;
  descripcion: string;
}

interface DetalleRecepcion {
  id?: string;
  productoId: string;
  productoNombre?: string;
  productoSku?: string;
  loteNumero: string;
  cantidadEsperada: number;
  cantidadRecibida: number;
  cantidadAceptada?: number;
  cantidadRechazada?: number;
  fechaVencimiento: string;
  temperaturaRecepcion?: number;
  observaciones?: string;
  conforme: boolean;
  motivoRechazo?: string;
}

interface Recepcion {
  id: string;
  numeroOrdenCompra: string;
  numeroGuiaRemision: string;
  clienteNombre: string;
  fechaRecepcion: string;
  responsableRecepcion: string;
  estado: string;
  observaciones?: string;
  detalles: DetalleRecepcion[];
}

function ActaRecepcionContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const recepcionId = searchParams.get('id');

  const [recepcion, setRecepcion] = useState<Recepcion | null>(null);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Estado para agregar nuevo producto
  const [showAddForm, setShowAddForm] = useState(false);
  const [nuevoDetalle, setNuevoDetalle] = useState<DetalleRecepcion>({
    productoId: '',
    loteNumero: '',
    cantidadEsperada: 0,
    cantidadRecibida: 0,
    fechaVencimiento: '',
    conforme: true
  });

  useEffect(() => {
    if (recepcionId) {
      cargarRecepcion();
      cargarProductos();
    }
  }, [recepcionId]);

  const cargarRecepcion = async () => {
    try {
      setLoading(true);
      const data = await recepcionesApi.obtenerPorId(recepcionId!);
      
      if (data.success) {
        setRecepcion(data.data);
      } else {
        setError('No se pudo cargar la recepción');
      }
    } catch (error) {
      console.error('Error al cargar recepción:', error);
      setError('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  const cargarProductos = async () => {
    try {
      const data = await productosApi.obtenerTodos();
      if (data.success) {
        setProductos(data.data);
      }
    } catch (error) {
      console.error('Error al cargar productos:', error);
    }
  };

  const handleAgregarDetalle = async () => {
    if (!nuevoDetalle.productoId || !nuevoDetalle.loteNumero || !nuevoDetalle.fechaVencimiento) {
      alert('Por favor complete todos los campos obligatorios');
      return;
    }

    setSaving(true);
    try {
      // Aquí llamarías al endpoint para agregar el detalle
      // Por ahora lo agregamos localmente
      const producto = productos.find(p => p.id === nuevoDetalle.productoId);
      
      const detalleConInfo = {
        ...nuevoDetalle,
        productoNombre: producto?.nombre,
        productoSku: producto?.codigoSKU,
        cantidadAceptada: nuevoDetalle.conforme ? nuevoDetalle.cantidadRecibida : 0,
        cantidadRechazada: nuevoDetalle.conforme ? 0 : nuevoDetalle.cantidadRecibida
      };

      setRecepcion(prev => prev ? {
        ...prev,
        detalles: [...prev.detalles, detalleConInfo]
      } : null);

      setSuccess('Producto agregado exitosamente');
      setShowAddForm(false);
      setNuevoDetalle({
        productoId: '',
        loteNumero: '',
        cantidadEsperada: 0,
        cantidadRecibida: 0,
        fechaVencimiento: '',
        conforme: true
      });

      setTimeout(() => setSuccess(''), 3000);
    } catch (error) {
      console.error('Error al agregar detalle:', error);
      setError('Error al agregar el producto');
    } finally {
      setSaving(false);
    }
  };

  const handleActualizarDetalle = (index: number, campo: string, valor: any) => {
    setRecepcion(prev => {
      if (!prev) return null;
      
      const nuevosDetalles = [...prev.detalles];
      nuevosDetalles[index] = {
        ...nuevosDetalles[index],
        [campo]: valor
      };

      // Actualizar cantidades aceptadas/rechazadas según conforme
      if (campo === 'conforme') {
        nuevosDetalles[index].cantidadAceptada = valor ? nuevosDetalles[index].cantidadRecibida : 0;
        nuevosDetalles[index].cantidadRechazada = valor ? 0 : nuevosDetalles[index].cantidadRecibida;
      }

      return {
        ...prev,
        detalles: nuevosDetalles
      };
    });
  };

  const handleEliminarDetalle = (index: number) => {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      setRecepcion(prev => prev ? {
        ...prev,
        detalles: prev.detalles.filter((_, i) => i !== index)
      } : null);
    }
  };

  const handleGuardar = async () => {
    setSaving(true);
    try {
      // Aquí llamarías al endpoint para guardar todos los detalles
      setSuccess('Acta de recepción guardada exitosamente');
      setTimeout(() => setSuccess(''), 3000);
    } catch (error) {
      console.error('Error al guardar:', error);
      setError('Error al guardar el acta');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="bg-gray-50 min-h-screen">
        <Navigation />
        <div className="flex justify-center items-center h-96">
          <div className="text-center">
            <div className="inline-block border-blue-600 border-b-2 rounded-full w-12 h-12 animate-spin"></div>
            <p className="mt-4 text-gray-600">Cargando acta de recepción...</p>
          </div>
        </div>
      </div>
    );
  }

  if (!recepcion) {
    return (
      <div className="bg-gray-50 min-h-screen">
        <Navigation />
        <div className="mx-auto px-4 py-8 max-w-7xl">
          <div className="bg-red-50 p-4 border border-red-200 rounded-lg">
            <p className="text-red-800">No se encontró la recepción</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      <div className="mx-auto px-4 sm:px-6 lg:px-8 py-8 max-w-7xl">
        {/* Header */}
        <div className="mb-6">
          <button
            onClick={() => router.back()}
            className="flex items-center gap-2 mb-4 text-blue-600 hover:text-blue-800"
          >
            ← Volver
          </button>
          <h1 className="font-bold text-gray-900 text-3xl">Acta de Recepción</h1>
          <p className="mt-2 text-gray-600 text-sm">
            Registre los productos recibidos y sus observaciones
          </p>
        </div>

        {/* Mensajes */}
        {success && (
          <div className="bg-green-50 mb-6 p-4 border border-green-200 rounded-lg">
            <p className="text-green-800">{success}</p>
          </div>
        )}

        {error && (
          <div className="bg-red-50 mb-6 p-4 border border-red-200 rounded-lg">
            <p className="text-red-800">{error}</p>
          </div>
        )}

        {/* Información de la Recepción */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <h2 className="mb-4 font-semibold text-gray-900 text-lg">Información General</h2>
          <div className="gap-4 grid grid-cols-1 md:grid-cols-3">
            <div>
              <p className="text-gray-600 text-sm">Nº Orden</p>
              <p className="font-medium">{recepcion.numeroOrdenCompra}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Cliente</p>
              <p className="font-medium">{recepcion.clienteNombre}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Fecha Recepción</p>
              <p className="font-medium">
                {new Date(recepcion.fechaRecepcion).toLocaleString('es-PE')}
              </p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Responsable</p>
              <p className="font-medium">{recepcion.responsableRecepcion}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Estado</p>
              <span className="inline-flex bg-yellow-100 px-2 py-1 rounded-full font-semibold text-yellow-800 text-xs">
                {recepcion.estado}
              </span>
            </div>
          </div>
        </div>

        {/* Lista de Productos */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-semibold text-gray-900 text-lg">Productos Recibidos</h2>
            <button
              onClick={() => setShowAddForm(true)}
              className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-md text-white transition-colors"
            >
              + Agregar Producto
            </button>
          </div>

          {/* Formulario para agregar producto */}
          {showAddForm && (
            <div className="bg-gray-50 mb-6 p-4 border border-gray-200 rounded-lg">
              <h3 className="mb-4 font-medium text-gray-900">Nuevo Producto</h3>
              <div className="gap-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Producto *
                  </label>
                  <select
                    value={nuevoDetalle.productoId}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, productoId: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                  >
                    <option value="">Seleccione un producto</option>
                    {productos.map(p => (
                      <option key={p.id} value={p.id}>{p.nombre} - {p.codigoSKU}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Nº Lote *
                  </label>
                  <input
                    type="text"
                    value={nuevoDetalle.loteNumero}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, loteNumero: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    placeholder="Ej: L2024001"
                  />
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Cantidad Esperada *
                  </label>
                  <input
                    type="number"
                    value={nuevoDetalle.cantidadEsperada}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, cantidadEsperada: parseInt(e.target.value)})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    min="0"
                  />
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Cantidad Recibida *
                  </label>
                  <input
                    type="number"
                    value={nuevoDetalle.cantidadRecibida}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, cantidadRecibida: parseInt(e.target.value)})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    min="0"
                  />
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Fecha Vencimiento *
                  </label>
                  <input
                    type="date"
                    value={nuevoDetalle.fechaVencimiento}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, fechaVencimiento: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                  />
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Temperatura (°C)
                  </label>
                  <input
                    type="number"
                    step="0.1"
                    value={nuevoDetalle.temperaturaRecepcion || ''}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, temperaturaRecepcion: parseFloat(e.target.value)})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Observaciones
                  </label>
                  <textarea
                    value={nuevoDetalle.observaciones || ''}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, observaciones: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                    rows={2}
                  />
                </div>

                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Estado
                  </label>
                  <select
                    value={nuevoDetalle.conforme ? 'conforme' : 'no-conforme'}
                    onChange={(e) => setNuevoDetalle({...nuevoDetalle, conforme: e.target.value === 'conforme'})}
                    className="px-3 py-2 border border-gray-300 rounded-md w-full"
                  >
                    <option value="conforme">Conforme</option>
                    <option value="no-conforme">No Conforme</option>
                  </select>
                </div>

                {!nuevoDetalle.conforme && (
                  <div className="md:col-span-2">
                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                      Motivo de Rechazo *
                    </label>
                    <input
                      type="text"
                      value={nuevoDetalle.motivoRechazo || ''}
                      onChange={(e) => setNuevoDetalle({...nuevoDetalle, motivoRechazo: e.target.value})}
                      className="px-3 py-2 border border-gray-300 rounded-md w-full"
                      placeholder="Describa el motivo del rechazo"
                    />
                  </div>
                )}
              </div>

              <div className="flex gap-3 mt-4">
                <button
                  onClick={handleAgregarDetalle}
                  disabled={saving}
                  className="bg-green-600 hover:bg-green-700 disabled:bg-gray-400 px-4 py-2 rounded-md text-white"
                >
                  {saving ? 'Guardando...' : 'Agregar'}
                </button>
                <button
                  onClick={() => setShowAddForm(false)}
                  className="bg-gray-200 hover:bg-gray-300 px-4 py-2 rounded-md text-gray-700"
                >
                  Cancelar
                </button>
              </div>
            </div>
          )}

          {/* Tabla de productos */}
          {recepcion.detalles.length === 0 ? (
            <div className="py-8 text-gray-500 text-center">
              No hay productos registrados. Haga clic en "Agregar Producto" para comenzar.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="divide-y divide-gray-200 min-w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Producto</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Lote</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Esperada</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Recibida</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Vencimiento</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Estado</th>
                    <th className="px-4 py-3 font-medium text-gray-500 text-xs text-left uppercase">Acciones</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {recepcion.detalles.map((detalle, index) => (
                    <tr key={index} className="hover:bg-gray-50">
                      <td className="px-4 py-3 text-sm">
                        <div>
                          <p className="font-medium text-gray-900">{detalle.productoNombre}</p>
                          <p className="text-gray-500">{detalle.productoSku}</p>
                        </div>
                      </td>
                      <td className="px-4 py-3 text-gray-900 text-sm">{detalle.loteNumero}</td>
                      <td className="px-4 py-3 text-gray-900 text-sm">{detalle.cantidadEsperada}</td>
                      <td className="px-4 py-3 text-gray-900 text-sm">{detalle.cantidadRecibida}</td>
                      <td className="px-4 py-3 text-gray-900 text-sm">
                        {new Date(detalle.fechaVencimiento).toLocaleDateString('es-PE')}
                      </td>
                      <td className="px-4 py-3 text-sm">
                        <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                          detalle.conforme 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {detalle.conforme ? 'Conforme' : 'No Conforme'}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-sm">
                        <button
                          onClick={() => handleEliminarDetalle(index)}
                          className="text-red-600 hover:text-red-900"
                        >
                          Eliminar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Botones de acción */}
        <div className="flex gap-4">
          <button
            onClick={handleGuardar}
            disabled={saving || recepcion.detalles.length === 0}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 px-6 py-3 rounded-md text-white transition-colors"
          >
            {saving ? 'Guardando...' : 'Guardar Acta'}
          </button>
          <button
            onClick={() => router.back()}
            className="bg-gray-200 hover:bg-gray-300 px-6 py-3 rounded-md text-gray-700 transition-colors"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}

export default function ActaRecepcionPage() {
  return (
    <ProtectedRoute>
      <ActaRecepcionContent />
    </ProtectedRoute>
  );
}
