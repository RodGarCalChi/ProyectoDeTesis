'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';
import { clientesApi, recepcionesApi } from '@/lib/api';

interface Cliente {
  id: string;
  razonSocial: string;
  rucDni: string;
  direccionEntrega: string;
  distrito: string;
  telefono: string;
  email: string;
  activo: boolean;
}

interface RecepcionFormData {
  numeroDocumentoRecepcion: string;
  clienteId: string;
  clienteNombre: string;
  fechaLlegada: string;
  horaLlegada: string;
  responsableRecepcion: string;
  observaciones: string;
}

function RecepcionMercaderiaContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('nueva-recepcion');
  const [loading, setLoading] = useState(false);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Estado del formulario simplificado (SIN productos)
  const [formData, setFormData] = useState<RecepcionFormData>({
    numeroDocumentoRecepcion: '',
    clienteId: '',
    clienteNombre: '',
    fechaLlegada: new Date().toISOString().slice(0, 10),
    horaLlegada: new Date().toTimeString().slice(0, 5),
    responsableRecepcion: user?.username || '',
    observaciones: ''
  });

  // Cargar datos iniciales
  useEffect(() => {
    cargarClientes();
  }, []);

  const cargarClientes = async () => {
    try {
      console.log('🔄 Iniciando carga de clientes...');

      const data = await clientesApi.obtenerActivos();
      console.log('📊 Datos recibidos:', data);

      if (data.success) {
        console.log('✅ Clientes cargados:', data.data);
        console.log('📈 Cantidad de clientes:', data.data.length);
        setClientes(data.data);

        if (data.data.length === 0) {
          setErrorMessage('⚠️ No hay clientes activos en el sistema. Por favor, cree clientes primero.');
        }
      } else {
        console.error('❌ Error en respuesta:', data);
        setErrorMessage('❌ Error al cargar clientes: ' + (data.message || 'Error desconocido'));
      }
    } catch (error) {
      console.error('💥 Error al cargar clientes:', error);
      console.error('📋 Detalles del error:', error instanceof Error ? error.message : String(error));
      setErrorMessage('❌ Error al conectar con el servidor. Verifique que el backend esté corriendo.');
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleClienteSelect = (clienteId: string) => {
    const cliente = clientes.find(c => c.id === clienteId);
    if (cliente) {
      setFormData(prev => ({
        ...prev,
        clienteId: cliente.id,
        clienteNombre: cliente.razonSocial
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.clienteId || !formData.numeroDocumentoRecepcion) {
      alert('Por favor complete los campos obligatorios');
      return;
    }

    setLoading(true);

    try {
      console.log('📤 Enviando datos de recepción...');

      // Preparar datos para la API (SIN productos)
      const recepcionData = {
        numeroOrdenCompra: formData.numeroDocumentoRecepcion,
        numeroGuiaRemision: formData.numeroDocumentoRecepcion,
        clienteId: formData.clienteId, // Ahora el backend acepta clienteId
        fechaRecepcion: `${formData.fechaLlegada}T${formData.horaLlegada}:00`,
        responsableRecepcion: formData.responsableRecepcion,
        estado: 'PENDIENTE',
        observaciones: formData.observaciones,
        verificacionDocumentos: false,
        verificacionFisica: false,
        verificacionTemperatura: false,
        aprobadoPorCalidad: false,
        detalles: [] // Sin productos
      };

      console.log('📦 Datos a enviar:', recepcionData);

      const result = await recepcionesApi.crear(recepcionData);

      console.log('📥 Respuesta del servidor:', result);

      if (result.success) {
        setSuccessMessage('✅ Recepción de mercadería registrada exitosamente. Ahora puede agregar productos en el Acta de Recepción.');
        setErrorMessage('');

        // Limpiar formulario
        setFormData({
          numeroDocumentoRecepcion: '',
          clienteId: '',
          clienteNombre: '',
          fechaLlegada: new Date().toISOString().slice(0, 10),
          horaLlegada: new Date().toTimeString().slice(0, 5),
          responsableRecepcion: user?.username || '',
          observaciones: ''
        });

        // Limpiar mensaje después de 5 segundos
        setTimeout(() => setSuccessMessage(''), 5000);

        // Scroll al inicio
        window.scrollTo({ top: 0, behavior: 'smooth' });
      } else {
        console.error('❌ Error en respuesta:', result);
        setErrorMessage(`❌ Error: ${result.message || 'Error desconocido'}`);
        setSuccessMessage('');
      }

    } catch (error: any) {
      console.error('💥 Error al registrar recepción:', error);
      console.error('�n Detalles:', error.message);
      setErrorMessage(`❌ Error al conectar con el servidor: ${error.message || 'Error desconocido'}`);
      setSuccessMessage('');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-50 min-h-screen">
      <Navigation />

      <div className="p-6">
        <div className="mb-6">
          <h1 className="mb-2 font-bold text-gray-900 text-2xl">🏥 PharmaFlow</h1>
          <h2 className="font-semibold text-gray-800 text-xl">Recepción de Mercadería</h2>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex border-gray-200 border-b">
            <button
              onClick={() => setActiveTab('nueva-recepcion')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${activeTab === 'nueva-recepcion'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
                }`}
            >
              📦 Nueva Recepción
            </button>
            <button
              onClick={() => setActiveTab('historial')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${activeTab === 'historial'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
                }`}
            >
              📋 Historial
            </button>
          </div>
        </div>

        {/* Mensajes de éxito/error */}
        {successMessage && (
          <div className="bg-green-50 mb-6 p-4 border border-green-200 rounded-lg">
            <div className="flex items-center">
              <svg className="mr-3 w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p className="font-medium text-green-800">{successMessage}</p>
            </div>
          </div>
        )}

        {errorMessage && (
          <div className="bg-red-50 mb-6 p-4 border border-red-200 rounded-lg">
            <div className="flex items-center">
              <svg className="mr-3 w-5 h-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p className="font-medium text-red-800">{errorMessage}</p>
            </div>
          </div>
        )}

        {/* Nueva Recepción */}
        {activeTab === 'nueva-recepcion' && (
          <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-gray-200 border-b">
              <div className="flex items-center gap-3 mb-2">
                <div className="flex justify-center items-center bg-blue-100 rounded-full w-8 h-8">
                  <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                  </svg>
                </div>
                <h3 className="font-semibold text-blue-700 text-lg">Registrar Nueva Recepción de Mercadería</h3>
              </div>
              <p className="text-gray-600 text-sm">
                Complete la información de la mercadería recibida del cliente (los productos se agregarán después en el Acta de Recepción)
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              {/* Información de Recepción */}
              <div className="mb-8">
                <h4 className="mb-4 font-semibold text-gray-800 text-md">📋 Información de Recepción</h4>
                <div className="gap-6 grid grid-cols-1 md:grid-cols-2">
                  <div>
                    <label htmlFor="numeroDocumentoRecepcion" className="block mb-2 font-medium text-gray-700 text-sm">
                      Número de Documento de Recepción *
                    </label>
                    <input
                      type="text"
                      id="numeroDocumentoRecepcion"
                      name="numeroDocumentoRecepcion"
                      value={formData.numeroDocumentoRecepcion}
                      onChange={handleInputChange}
                      placeholder="Ej: REC-2024-001"
                      required
                      className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  <div>
                    <label htmlFor="clienteId" className="block mb-2 font-medium text-gray-700 text-sm">
                      Cliente (Dueño de la Mercadería) *
                    </label>
                    <div className="relative">
                      <select
                        id="clienteId"
                        name="clienteId"
                        value={formData.clienteId}
                        onChange={(e) => handleClienteSelect(e.target.value)}
                        required
                        className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                      >
                        <option value="">Seleccione un cliente</option>
                        {clientes.map((cliente) => (
                          <option key={cliente.id} value={cliente.id}>
                            {cliente.razonSocial} - {cliente.rucDni}
                          </option>
                        ))}
                      </select>
                      <svg className="top-1/2 right-3 absolute w-4 h-4 text-gray-500 -translate-y-1/2 pointer-events-none transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                      </svg>
                    </div>
                    {clientes.length > 0 && (
                      <p className="mt-1 text-green-600 text-xs">✅ {clientes.length} cliente(s) disponible(s)</p>
                    )}
                    {clientes.length === 0 && (
                      <p className="mt-1 text-red-600 text-xs">⚠️ No hay clientes disponibles</p>
                    )}
                  </div>

                  <div>
                    <label htmlFor="fechaLlegada" className="block mb-2 font-medium text-gray-700 text-sm">
                      Fecha de Llegada *
                    </label>
                    <input
                      type="date"
                      id="fechaLlegada"
                      name="fechaLlegada"
                      value={formData.fechaLlegada}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="horaLlegada" className="block mb-2 font-medium text-gray-700 text-sm">
                      Hora de Llegada *
                    </label>
                    <input
                      type="time"
                      id="horaLlegada"
                      name="horaLlegada"
                      value={formData.horaLlegada}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors"
                    />
                  </div>

                  <div className="md:col-span-2">
                    <label htmlFor="responsableRecepcion" className="block mb-2 font-medium text-gray-700 text-sm">
                      Recepcionista que lo Registró *
                    </label>
                    <input
                      type="text"
                      id="responsableRecepcion"
                      name="responsableRecepcion"
                      value={formData.responsableRecepcion}
                      onChange={handleInputChange}
                      placeholder="Nombre del recepcionista"
                      required
                      className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>
                </div>
              </div>

              {/* Observaciones Generales */}
              <div className="mb-8">
                <label htmlFor="observaciones" className="block mb-2 font-medium text-gray-700 text-sm">
                  Observaciones Generales
                </label>
                <textarea
                  id="observaciones"
                  name="observaciones"
                  value={formData.observaciones}
                  onChange={handleInputChange}
                  placeholder="Observaciones sobre la recepción de mercadería..."
                  rows={3}
                  className="bg-gray-50 focus:bg-white px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-gray-700 transition-colors resize-vertical placeholder-gray-500"
                />
              </div>

              {/* Nota informativa */}
              <div className="bg-blue-50 mb-8 p-4 border border-blue-200 rounded-lg">
                <div className="flex items-start">
                  <svg className="mt-0.5 mr-3 w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <div>
                    <p className="font-medium text-blue-900 text-sm">ℹ️ Nota Importante</p>
                    <p className="mt-1 text-blue-800 text-sm">
                      Los productos se agregarán posteriormente en el <strong>Acta de Recepción</strong>.
                      Este registro solo guarda la información general de la recepción.
                    </p>
                  </div>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  disabled={loading}
                  className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 px-6 py-3 rounded-lg font-medium text-white transition-colors"
                >
                  {loading ? (
                    <>
                      <div className="border-white border-b-2 rounded-full w-4 h-4 animate-spin"></div>
                      Registrando...
                    </>
                  ) : (
                    <>
                      💾 Registrar Recepción
                    </>
                  )}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setFormData({
                      numeroDocumentoRecepcion: '',
                      clienteId: '',
                      clienteNombre: '',
                      fechaLlegada: new Date().toISOString().slice(0, 10),
                      horaLlegada: new Date().toTimeString().slice(0, 5),
                      responsableRecepcion: user?.username || '',
                      observaciones: ''
                    });
                  }}
                  className="bg-gray-500 hover:bg-gray-600 px-6 py-3 rounded-lg font-medium text-white transition-colors"
                >
                  🔄 Limpiar Formulario
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Historial */}
        {activeTab === 'historial' && (
          <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-gray-200 border-b">
              <h3 className="font-semibold text-gray-800 text-lg">📋 Historial de Recepciones</h3>
              <p className="mt-1 text-gray-600 text-sm">
                Próximamente: Lista de todas las recepciones registradas
              </p>
            </div>
            <div className="p-6">
              <div className="py-8 text-center">
                <div className="mb-4 text-4xl">📋</div>
                <p className="text-gray-500">Funcionalidad en desarrollo</p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default function RecepcionMercaderia() {
  return (
    <ProtectedRoute>
      <RecepcionMercaderiaContent />
    </ProtectedRoute>
  );
}
