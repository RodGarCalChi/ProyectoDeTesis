'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';
import { clientesApi, productosApi, recepcionesApi } from '@/lib/api';

interface Cliente {
  id: string;
  razonSocial: string;
  rucDni: string;
  tipoCliente: string;
  activo: boolean;
}

interface Producto {
  id: string;
  nombre: string;
  codigoSKU: string;
  tipo: string;
  requiereCadenaFrio: boolean;
}

interface RecepcionFormData {
  numeroDocumentoRecepcion: string;
  clienteId: string;
  clienteNombre: string;
  fechaLlegada: string;
  horaLlegada: string;
  responsableRecepcion: string;
  productos: ProductoRecepcion[];
  observaciones: string;
}

interface ProductoRecepcion {
  id?: string;
  productoId: string;
  productoNombre: string;
  cantidad: number;
  observaciones?: string;
}

function RecepcionMercaderiaContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('nueva-recepcion');
  const [loading, setLoading] = useState(false);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [recepciones, setRecepciones] = useState([]);
  
  // Estado del formulario simplificado
  const [formData, setFormData] = useState<RecepcionFormData>({
    numeroDocumentoRecepcion: '',
    clienteId: '',
    clienteNombre: '',
    fechaLlegada: new Date().toISOString().slice(0, 10),
    horaLlegada: new Date().toTimeString().slice(0, 5),
    responsableRecepcion: user?.username || '',
    productos: [],
    observaciones: ''
  });

  // Estado para nuevo producto
  const [nuevoProducto, setNuevoProducto] = useState<ProductoRecepcion>({
    productoId: '',
    productoNombre: '',
    cantidad: 0,
    observaciones: ''
  });

  // Cargar datos iniciales
  useEffect(() => {
    cargarClientes();
    cargarProductos();
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

  const cargarProductos = async () => {
    try {
      const data = await productosApi.obtenerTodos({ size: 100 });
      if (data.success) {
        setProductos(data.data);
      }
    } catch (error) {
      console.error('Error al cargar productos:', error);
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

  const handleProductoChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    setNuevoProducto(prev => ({
      ...prev,
      [name]: type === 'number' ? parseFloat(value) || 0 : value
    }));
  };

  const handleProductoSelect = (productoId: string) => {
    const producto = productos.find(p => p.id === productoId);
    if (producto) {
      setNuevoProducto(prev => ({
        ...prev,
        productoId: producto.id,
        productoNombre: producto.nombre
      }));
    }
  };

  const agregarProducto = () => {
    if (!nuevoProducto.productoId || nuevoProducto.cantidad <= 0) {
      alert('Por favor seleccione un producto y especifique la cantidad');
      return;
    }

    const producto: ProductoRecepcion = {
      ...nuevoProducto,
      id: Date.now().toString()
    };

    setFormData(prev => ({
      ...prev,
      productos: [...prev.productos, producto]
    }));

    // Limpiar formulario de producto
    setNuevoProducto({
      productoId: '',
      productoNombre: '',
      cantidad: 0,
      observaciones: ''
    });
  };

  const eliminarProducto = (productoId: string) => {
    setFormData(prev => ({
      ...prev,
      productos: prev.productos.filter(p => p.id !== productoId)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.clienteId || !formData.numeroDocumentoRecepcion) {
      alert('Por favor complete los campos obligatorios');
      return;
    }

    if (formData.productos.length === 0) {
      alert('Debe agregar al menos un producto a la recepción');
      return;
    }

    setLoading(true);
    
    try {
      // Preparar datos para la API
      const recepcionData = {
        numeroOrdenCompra: formData.numeroDocumentoRecepcion,
        numeroGuiaRemision: formData.numeroDocumentoRecepcion,
        proveedorId: formData.clienteId, // Usando cliente como proveedor por simplicidad
        fechaRecepcion: `${formData.fechaLlegada}T${formData.horaLlegada}:00`,
        responsableRecepcion: formData.responsableRecepcion,
        estado: 'PENDIENTE',
        observaciones: formData.observaciones,
        verificacionDocumentos: false,
        verificacionFisica: false,
        verificacionTemperatura: false,
        aprobadoPorCalidad: false,
        detalles: formData.productos.map(producto => ({
          productoId: producto.productoId,
          cantidadEsperada: producto.cantidad,
          cantidadRecibida: producto.cantidad,
          conforme: true,
          observaciones: producto.observaciones
        }))
      };

      const result = await recepcionesApi.crear(recepcionData);

      if (result.success) {
        alert('Recepción de mercadería registrada exitosamente');
        
        // Limpiar formulario
        setFormData({
          numeroDocumentoRecepcion: '',
          clienteId: '',
          clienteNombre: '',
          fechaLlegada: new Date().toISOString().slice(0, 10),
          horaLlegada: new Date().toTimeString().slice(0, 5),
          responsableRecepcion: user?.username || '',
          productos: [],
          observaciones: ''
        });
      } else {
        alert(`Error: ${result.message}`);
      }
      
    } catch (error) {
      console.error('Error al registrar recepción:', error);
      alert('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="p-6">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-2">🏥 PharmaFlow</h1>
          <h2 className="text-xl font-semibold text-gray-800">Recepción de Mercadería</h2>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('nueva-recepcion')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'nueva-recepcion'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              📦 Nueva Recepción
            </button>
            <button
              onClick={() => setActiveTab('historial')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'historial'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              📋 Historial
            </button>
          </div>
        </div>

        {/* Nueva Recepción */}
        {activeTab === 'nueva-recepcion' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <div className="flex items-center gap-3 mb-2">
                <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                  <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-blue-700">Registrar Nueva Recepción de Mercadería</h3>
              </div>
              <p className="text-sm text-gray-600">
                Complete la información de la mercadería recibida del cliente
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              {/* Información de Recepción */}
              <div className="mb-8">
                <h4 className="text-md font-semibold text-gray-800 mb-4">📋 Información de Recepción</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label htmlFor="numeroDocumentoRecepcion" className="block text-sm font-medium text-gray-700 mb-2">
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
                      className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="clienteId" className="block text-sm font-medium text-gray-700 mb-2">
                      Cliente *
                    </label>
                    <div className="relative">
                      <select
                        id="clienteId"
                        name="clienteId"
                        value={formData.clienteId}
                        onChange={(e) => handleClienteSelect(e.target.value)}
                        required
                        className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                      >
                        <option value="">Seleccione un cliente</option>
                        {clientes.map(cliente => (
                          <option key={cliente.id} value={cliente.id}>
                            {cliente.razonSocial} - {cliente.rucDni}
                          </option>
                        ))}
                      </select>
                      <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                      </svg>
                    </div>
                  </div>

                  <div>
                    <label htmlFor="fechaLlegada" className="block text-sm font-medium text-gray-700 mb-2">
                      Fecha de Llegada *
                    </label>
                    <input
                      type="date"
                      id="fechaLlegada"
                      name="fechaLlegada"
                      value={formData.fechaLlegada}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="horaLlegada" className="block text-sm font-medium text-gray-700 mb-2">
                      Hora de Llegada *
                    </label>
                    <input
                      type="time"
                      id="horaLlegada"
                      name="horaLlegada"
                      value={formData.horaLlegada}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div className="md:col-span-2">
                    <label htmlFor="responsableRecepcion" className="block text-sm font-medium text-gray-700 mb-2">
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
                      className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>
                </div>
              </div>

              {/* Agregar Productos */}
              <div className="mb-8">
                <h4 className="text-md font-semibold text-gray-800 mb-4">📦 Agregar Productos</h4>
                <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Producto *
                      </label>
                      <div className="relative">
                        <select
                          value={nuevoProducto.productoId}
                          onChange={(e) => handleProductoSelect(e.target.value)}
                          className="w-full px-3 py-2 bg-white border border-gray-300 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 appearance-none cursor-pointer"
                        >
                          <option value="">Seleccione un producto</option>
                          {productos.map(producto => (
                            <option key={producto.id} value={producto.id}>
                              {producto.codigoSKU} - {producto.nombre}
                            </option>
                          ))}
                        </select>
                        <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                        </svg>
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Cantidad *
                      </label>
                      <input
                        type="number"
                        name="cantidad"
                        value={nuevoProducto.cantidad}
                        onChange={handleProductoChange}
                        placeholder="0"
                        min="1"
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Observaciones
                      </label>
                      <input
                        type="text"
                        name="observaciones"
                        value={nuevoProducto.observaciones}
                        onChange={handleProductoChange}
                        placeholder="Observaciones del producto"
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500"
                      />
                    </div>

                    <div className="flex items-end">
                      <button
                        type="button"
                        onClick={agregarProducto}
                        className="w-full bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded-lg transition-colors"
                      >
                        ➕ Agregar
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              {/* Lista de Productos Agregados */}
              {formData.productos.length > 0 && (
                <div className="mb-8">
                  <h4 className="text-md font-semibold text-gray-800 mb-4">📋 Productos en esta Recepción</h4>
                  <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead className="bg-gray-50">
                          <tr>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Producto</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Cantidad</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Observaciones</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Acciones</th>
                          </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                          {formData.productos.map((producto) => (
                            <tr key={producto.id} className="hover:bg-gray-50">
                              <td className="px-4 py-3 text-sm">
                                <div className="font-medium text-gray-900">{producto.productoNombre}</div>
                              </td>
                              <td className="px-4 py-3 text-sm text-gray-900">{producto.cantidad}</td>
                              <td className="px-4 py-3 text-sm text-gray-900">{producto.observaciones || '-'}</td>
                              <td className="px-4 py-3 text-sm">
                                <button
                                  type="button"
                                  onClick={() => eliminarProducto(producto.id!)}
                                  className="text-red-600 hover:text-red-900 font-medium"
                                >
                                  Eliminar
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              )}

              {/* Observaciones Generales */}
              <div className="mb-8">
                <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-2">
                  Observaciones Generales
                </label>
                <textarea
                  id="observaciones"
                  name="observaciones"
                  value={formData.observaciones}
                  onChange={handleInputChange}
                  placeholder="Observaciones sobre la recepción de mercadería..."
                  rows={3}
                  className="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors resize-vertical"
                />
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  disabled={loading || formData.productos.length === 0}
                  className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-medium py-3 px-6 rounded-lg transition-colors flex items-center gap-2"
                >
                  {loading ? (
                    <>
                      <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
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
                      productos: [],
                      observaciones: ''
                    });
                  }}
                  className="bg-gray-500 hover:bg-gray-600 text-white font-medium py-3 px-6 rounded-lg transition-colors"
                >
                  🔄 Limpiar Formulario
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Historial */}
        {activeTab === 'historial' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-800">📋 Historial de Recepciones</h3>
              <p className="text-sm text-gray-600 mt-1">
                Próximamente: Lista de todas las recepciones registradas
              </p>
            </div>
            <div className="p-6">
              <div className="text-center py-8">
                <div className="text-4xl mb-4">📋</div>
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