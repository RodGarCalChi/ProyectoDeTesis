'use client';

import React, { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Navigation } from '@/components/Navigation';
import { recepcionesApi } from '@/lib/api';

interface DetalleRecepcion {
  id: string;
  productoId: string;
  productoNombre: string;
  productoSku: string;
  loteNumero: string;
  cantidadEsperada: number;
  cantidadRecibida: number;
  cantidadAceptada: number;
  cantidadRechazada: number;
  fechaVencimiento: string;
  temperaturaRecepcion?: number;
  observaciones?: string;
  conforme: boolean;
  motivoRechazo?: string;
  // Campos para registro en inventario
  registrado?: boolean;
  ubicacionAlmacen?: string;
  codigoBarras?: string;
}

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
  detalles: DetalleRecepcion[];
}

function ValidacionDetalleContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const recepcionId = searchParams.get('id');

  const [recepcion, setRecepcion] = useState<Recepcion | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [observacionesValidacion, setObservacionesValidacion] = useState('');

  useEffect(() => {
    if (recepcionId) {
      cargarRecepcion();
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

  const handleActualizarDetalle = (index: number, campo: string, valor: any) => {
    setRecepcion(prev => {
      if (!prev) return null;
      
      const nuevosDetalles = [...prev.detalles];
      nuevosDetalles[index] = {
        ...nuevosDetalles[index],
        [campo]: valor
      };

      return {
        ...prev,
        detalles: nuevosDetalles
      };
    });
  };

  const handleAprobar = async () => {
    if (!recepcion) return;

    const productosConformes = recepcion.detalles.filter(d => d.conforme);

    if (productosConformes.length === 0) {
      setError('Debe haber al menos un producto conforme para aprobar el acta');
      return;
    }

    const mensaje = `¿Está seguro de aprobar esta acta y registrar ${productosConformes.length} producto(s) en el inventario?\n\nLa ubicación física será asignada posteriormente por el personal de Operaciones.`;

    if (confirm(mensaje)) {
      setSaving(true);
      try {
        // Aquí llamarías al endpoint para aprobar y registrar
        console.log('Aprobando acta:', {
          recepcionId: recepcion.id,
          productosConformes: productosConformes.length,
          observaciones: observacionesValidacion
        });

        setSuccess('Acta aprobada y productos registrados exitosamente. El personal de Operaciones asignará las ubicaciones.');
        setTimeout(() => {
          router.push('/validacion-actas');
        }, 3000);
      } catch (error) {
        console.error('Error al aprobar:', error);
        setError('Error al aprobar el acta');
      } finally {
        setSaving(false);
      }
    }
  };

  const handleRechazar = async () => {
    if (!observacionesValidacion.trim()) {
      setError('Debe ingresar el motivo del rechazo');
      return;
    }

    if (confirm('¿Está seguro de rechazar esta acta? Esta acción no se puede deshacer.')) {
      setSaving(true);
      try {
        // Aquí llamarías al endpoint para rechazar
        console.log('Rechazando acta:', {
          recepcionId: recepcion?.id,
          motivo: observacionesValidacion
        });

        setSuccess('Acta rechazada');
        setTimeout(() => {
          router.push('/validacion-actas');
        }, 2000);
      } catch (error) {
        console.error('Error al rechazar:', error);
        setError('Error al rechazar el acta');
      } finally {
        setSaving(false);
      }
    }
  };

  if (loading) {
    return (
      <div className="bg-gray-50 min-h-screen">
        <Navigation />
        <div className="flex justify-center items-center h-96">
          <div className="text-center">
            <div className="inline-block border-blue-600 border-b-2 rounded-full w-12 h-12 animate-spin"></div>
            <p className="mt-4 text-gray-600">Cargando acta...</p>
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

  const productosConformes = recepcion.detalles.filter(d => d.conforme).length;
  const productosNoConformes = recepcion.detalles.filter(d => !d.conforme).length;
  const totalProductos = recepcion.detalles.length;

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
          <h1 className="font-bold text-gray-900 text-3xl">Validación de Acta de Recepción</h1>
          <p className="mt-2 text-gray-600 text-sm">
            Revise los productos y asigne ubicaciones de almacén para registrarlos en el inventario
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

        {/* Información General */}
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
              <p className="text-gray-600 text-sm">Responsable Recepción</p>
              <p className="font-medium">{recepcion.responsableRecepcion}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Estado</p>
              <span className="inline-flex bg-orange-100 px-2 py-1 rounded-full font-semibold text-orange-800 text-xs">
                {recepcion.estado}
              </span>
            </div>
          </div>
        </div>

        {/* Estadísticas */}
        <div className="gap-4 grid grid-cols-1 md:grid-cols-3 mb-6">
          <div className="bg-white shadow-sm p-4 rounded-lg">
            <p className="text-gray-600 text-sm">Total Productos</p>
            <p className="font-bold text-gray-900 text-2xl">{totalProductos}</p>
          </div>
          <div className="bg-white shadow-sm p-4 rounded-lg">
            <p className="text-gray-600 text-sm">Conformes</p>
            <p className="font-bold text-green-600 text-2xl">{productosConformes}</p>
          </div>
          <div className="bg-white shadow-sm p-4 rounded-lg">
            <p className="text-gray-600 text-sm">No Conformes</p>
            <p className="font-bold text-red-600 text-2xl">{productosNoConformes}</p>
          </div>
        </div>

        {/* Lista de Productos */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <h2 className="mb-4 font-semibold text-gray-900 text-lg">Productos Recibidos</h2>
          
          <div className="space-y-4">
            {recepcion.detalles.map((detalle, index) => (
              <div key={detalle.id} className={`border rounded-lg p-4 ${
                detalle.conforme ? 'border-green-200 bg-green-50' : 'border-red-200 bg-red-50'
              }`}>
                <div className="gap-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4">
                  {/* Información del Producto */}
                  <div className="lg:col-span-2">
                    <p className="font-medium text-gray-900 text-sm">{detalle.productoNombre}</p>
                    <p className="text-gray-500 text-xs">SKU: {detalle.productoSku}</p>
                    <p className="text-gray-500 text-xs">Lote: {detalle.loteNumero}</p>
                    <p className="text-gray-500 text-xs">
                      Vencimiento: {new Date(detalle.fechaVencimiento).toLocaleDateString('es-PE')}
                    </p>
                  </div>

                  {/* Cantidades */}
                  <div>
                    <p className="text-gray-600 text-xs">Cantidades</p>
                    <p className="text-sm">Esperada: <span className="font-medium">{detalle.cantidadEsperada}</span></p>
                    <p className="text-sm">Recibida: <span className="font-medium">{detalle.cantidadRecibida}</span></p>
                    {detalle.conforme ? (
                      <p className="text-green-600 text-sm">Aceptada: <span className="font-medium">{detalle.cantidadAceptada}</span></p>
                    ) : (
                      <p className="text-red-600 text-sm">Rechazada: <span className="font-medium">{detalle.cantidadRechazada}</span></p>
                    )}
                  </div>

                  {/* Estado */}
                  <div>
                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                      detalle.conforme ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {detalle.conforme ? 'Conforme' : 'No Conforme'}
                    </span>
                    {detalle.temperaturaRecepcion && (
                      <p className="mt-2 text-gray-600 text-xs">
                        Temp: {detalle.temperaturaRecepcion}°C
                      </p>
                    )}
                  </div>
                </div>

                {/* Observaciones */}
                {detalle.observaciones && (
                  <div className="mt-3 pt-3 border-gray-200 border-t">
                    <p className="text-gray-600 text-xs">Observaciones:</p>
                    <p className="text-gray-900 text-sm">{detalle.observaciones}</p>
                  </div>
                )}

                {/* Motivo de Rechazo */}
                {!detalle.conforme && detalle.motivoRechazo && (
                  <div className="mt-3 pt-3 border-red-200 border-t">
                    <p className="font-medium text-red-600 text-xs">Motivo de Rechazo:</p>
                    <p className="text-red-900 text-sm">{detalle.motivoRechazo}</p>
                  </div>
                )}

                {/* Información de Registro (solo productos conformes) */}
                {detalle.conforme && (
                  <div className="bg-green-50 mt-4 p-3 pt-4 border-green-200 border-t rounded">
                    <div className="flex items-start gap-2">
                      <svg className="flex-shrink-0 mt-0.5 w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <div>
                        <p className="font-medium text-green-900 text-sm">
                          Este producto será registrado en el inventario
                        </p>
                        <p className="mt-1 text-green-700 text-xs">
                          La ubicación física en el almacén será asignada posteriormente por el personal de Operaciones
                        </p>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Observaciones de Validación */}
        <div className="bg-white shadow-sm mb-6 p-6 rounded-lg">
          <h2 className="mb-4 font-semibold text-gray-900 text-lg">Observaciones de Validación</h2>
          <textarea
            value={observacionesValidacion}
            onChange={(e) => setObservacionesValidacion(e.target.value)}
            placeholder="Ingrese observaciones sobre la validación del acta (obligatorio si rechaza)"
            rows={4}
            className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
          />
        </div>

        {/* Botones de Acción */}
        <div className="flex gap-4">
          <button
            onClick={handleAprobar}
            disabled={saving || productosConformes === 0}
            className="flex items-center gap-2 bg-green-600 hover:bg-green-700 disabled:bg-gray-400 px-6 py-3 rounded-md text-white transition-colors"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
            {saving ? 'Procesando...' : 'Aprobar y Registrar Productos'}
          </button>
          
          <button
            onClick={handleRechazar}
            disabled={saving}
            className="flex items-center gap-2 bg-red-600 hover:bg-red-700 disabled:bg-gray-400 px-6 py-3 rounded-md text-white transition-colors"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
            {saving ? 'Procesando...' : 'Rechazar Acta'}
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

export default function ValidacionDetallePage() {
  return (
    <ProtectedRoute>
      <ValidacionDetalleContent />
    </ProtectedRoute>
  );
}
