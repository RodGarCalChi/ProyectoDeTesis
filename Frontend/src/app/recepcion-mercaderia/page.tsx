'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface DetalleRecepcion {
  id?: string;
  productoId: string;
  productoNombre: string;
  productoSku: string;
  loteId: string;
  loteNumero: string;
  cantidadEsperada: number;
  cantidadRecibida: number;
  cantidadAceptada?: number;
  cantidadRechazada?: number;
  fechaVencimiento: string;
  precioUnitario: number;
  temperaturaRecepcion?: number;
  observaciones?: string;
  conforme: boolean;
  motivoRechazo?: string;
}

interface RecepcionMercaderia {
  id?: string;
  numeroOrdenCompra: string;
  numeroGuiaRemision: string;
  proveedorId: string;
  proveedorNombre?: string;
  fechaRecepcion: string;
  responsableRecepcion: string;
  estado: string;
  temperaturaLlegada?: number;
  observaciones?: string;
  verificacionDocumentos: boolean;
  verificacionFisica: boolean;
  verificacionTemperatura: boolean;
  aprobadoPorCalidad: boolean;
  inspectorCalidad?: string;
  detalles: DetalleRecepcion[];
}

function RecepcionMercaderiaContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('nueva-recepcion');
  const [recepciones, setRecepciones] = useState<RecepcionMercaderia[]>([]);
  const [loading, setLoading] = useState(false);
  
  // Estado del formulario del acta de recepción
  const [formData, setFormData] = useState({
    numeroDocumentoRecepcion: '',
    fecha: new Date().toISOString().slice(0, 10), // Solo fecha
    dia: new Date().toLocaleDateString('es-ES', { weekday: 'long' }),
    hora: new Date().toTimeString().slice(0, 5), // Solo hora
    clienteId: '',
    clienteNombre: '',
    observaciones: '',
    responsableRecepcion: user?.firstName || ''
  });

  // Estado para nuevo detalle
  const [nuevoDetalle, setNuevoDetalle] = useState<DetalleRecepcion>({
    productoId: '',
    productoNombre: '',
    productoSku: '',
    loteId: '',
    loteNumero: '',
    cantidadEsperada: 0,
    cantidadRecibida: 0,
    fechaVencimiento: '',
    precioUnitario: 0,
    conforme: false
  });

  // Datos de ejemplo para clientes
  const clientes = [
    { id: '1', nombre: 'Farmacia Central S.A.C.', ruc: '20123456789' },
    { id: '2', nombre: 'Hospital Nacional Dos de Mayo', ruc: '20111222333' },
    { id: '3', nombre: 'Botica San Juan E.I.R.L.', ruc: '20987654321' },
    { id: '4', nombre: 'Clínica San Pablo S.A.', ruc: '20444555666' },
    { id: '5', nombre: 'María Elena Rodríguez Pérez', ruc: '12345678' }
  ];

  const productos = [
    { id: '1', nombre: 'Paracetamol 500mg', sku: 'PAR500MG' },
    { id: '2', nombre: 'Ibuprofeno 400mg', sku: 'IBU400MG' },
    { id: '3', nombre: 'Amoxicilina 250mg', sku: 'AMX250MG' }
  ];

  const lotes = [
    { id: '1', numero: 'L2024001', productoId: '1' },
    { id: '2', numero: 'L2024002', productoId: '2' },
    { id: '3', numero: 'L2024003', productoId: '3' }
  ];

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }));
  };

  const handleClienteSelect = (clienteId: string) => {
    const cliente = clientes.find(c => c.id === clienteId);
    if (cliente) {
      setFormData(prev => ({
        ...prev,
        clienteId: cliente.id,
        clienteNombre: cliente.nombre
      }));
    }
  };

  const handleDetalleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    setNuevoDetalle(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : 
               (type === 'number' ? parseFloat(value) || 0 : value)
    }));
  };

  const handleProductoSelect = (productoId: string) => {
    const producto = productos.find(p => p.id === productoId);
    if (producto) {
      setNuevoDetalle(prev => ({
        ...prev,
        productoId: producto.id,
        productoNombre: producto.nombre,
        productoSku: producto.sku
      }));
    }
  };

  const agregarDetalle = () => {
    if (!nuevoDetalle.productoId || !nuevoDetalle.loteId || nuevoDetalle.cantidadEsperada <= 0) {
      alert('Por favor complete todos los campos obligatorios del detalle');
      return;
    }

    const detalle: DetalleRecepcion = {
      ...nuevoDetalle,
      id: Date.now().toString()
    };

    setFormData(prev => ({
      ...prev,
      detalles: [...prev.detalles, detalle]
    }));

    // Limpiar formulario de detalle
    setNuevoDetalle({
      productoId: '',
      productoNombre: '',
      productoSku: '',
      loteId: '',
      loteNumero: '',
      cantidadEsperada: 0,
      cantidadRecibida: 0,
      fechaVencimiento: '',
      precioUnitario: 0,
      conforme: false
    });
  };

  const eliminarDetalle = (detalleId: string) => {
    setFormData(prev => ({
      ...prev,
      detalles: prev.detalles.filter(d => d.id !== detalleId)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.detalles.length === 0) {
      alert('Debe agregar al menos un producto a la recepción');
      return;
    }

    setLoading(true);
    
    try {
      // Aquí iría la llamada al API
      console.log('Datos de recepción:', formData);
      
      // Simular guardado exitoso
      alert('Recepción de mercadería registrada exitosamente');
      
      // Limpiar formulario
      setFormData({
        numeroOrdenCompra: '',
        numeroGuiaRemision: '',
        proveedorId: '',
        fechaRecepcion: new Date().toISOString().slice(0, 16),
        responsableRecepcion: user?.firstName || '',
        estado: 'PENDIENTE',
        temperaturaLlegada: undefined,
        observaciones: '',
        verificacionDocumentos: false,
        verificacionFisica: false,
        verificacionTemperatura: false,
        aprobadoPorCalidad: false,
        detalles: []
      });
      
    } catch (error) {
      console.error('Error al registrar recepción:', error);
      alert('Error al registrar la recepción');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="p-6">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-2">PharmaFlow</h1>
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
              onClick={() => setActiveTab('recepciones-pendientes')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'recepciones-pendientes'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              ⏳ Pendientes de Verificación
            </button>
            <button
              onClick={() => setActiveTab('en-cuarentena')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'en-cuarentena'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              🔬 En Cuarentena
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
                Complete la información de la mercadería recibida siguiendo las normativas BPAs y DIGEMID
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              {/* Acta de Recepción */}
              <div className="mb-8">
                <h4 className="text-md font-semibold text-gray-800 mb-4">📋 Acta de Recepción</h4>
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
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="clienteId" className="block text-sm font-medium text-gray-700 mb-2">
                      Cliente de la Recepción *
                    </label>
                    <div className="relative">
                      <select
                        id="clienteId"
                        name="clienteId"
                        value={formData.clienteId}
                        onChange={(e) => handleClienteSelect(e.target.value)}
                        required
                        className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                      >
                        <option value="">Seleccione un cliente</option>
                        {clientes.map(cliente => (
                          <option key={cliente.id} value={cliente.id}>
                            {cliente.nombre} - {cliente.ruc}
                          </option>
                        ))}
                      </select>
                      <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                      </svg>
                    </div>
                  </div>

                  <div>
                    <label htmlFor="fecha" className="block text-sm font-medium text-gray-700 mb-2">
                      Fecha *
                    </label>
                    <input
                      type="date"
                      id="fecha"
                      name="fecha"
                      value={formData.fecha}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="dia" className="block text-sm font-medium text-gray-700 mb-2">
                      Día de la Semana
                    </label>
                    <input
                      type="text"
                      id="dia"
                      name="dia"
                      value={formData.dia}
                      onChange={handleInputChange}
                      placeholder="Ej: Lunes"
                      className="w-full px-3 py-2 bg-gray-100 border-2 border-gray-300 rounded text-gray-600 focus:outline-none"
                      readOnly
                    />
                  </div>

                  <div>
                    <label htmlFor="hora" className="block text-sm font-medium text-gray-700 mb-2">
                      Hora *
                    </label>
                    <input
                      type="time"
                      id="hora"
                      name="hora"
                      value={formData.hora}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>

                  <div>
                    <label htmlFor="responsableRecepcion" className="block text-sm font-medium text-gray-700 mb-2">
                      Responsable de Recepción *
                    </label>
                    <input
                      type="text"
                      id="responsableRecepcion"
                      name="responsableRecepcion"
                      value={formData.responsableRecepcion}
                      onChange={handleInputChange}
                      placeholder="Nombre del responsable"
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                    />
                  </div>
                </div>

                <div className="mt-6">
                  <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-2">
                    Observaciones del Acta
                  </label>
                  <textarea
                    id="observaciones"
                    name="observaciones"
                    value={formData.observaciones}
                    onChange={handleInputChange}
                    placeholder="Observaciones sobre la recepción de mercadería..."
                    rows={3}
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors resize-vertical"
                  />
                </div>
              </div>

              {/* Agregar Productos */}
              <div className="mb-8">
                <h4 className="text-md font-semibold text-gray-800 mb-4">📦 Agregar Productos</h4>
                <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Producto *
                      </label>
                      <div className="relative">
                        <select
                          value={nuevoDetalle.productoId}
                          onChange={(e) => handleProductoSelect(e.target.value)}
                          className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 focus:outline-none focus:border-blue-500 appearance-none cursor-pointer"
                        >
                          <option value="">Seleccione un producto</option>
                          {productos.map(producto => (
                            <option key={producto.id} value={producto.id}>
                              {producto.sku} - {producto.nombre}
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
                        Lote *
                      </label>
                      <div className="relative">
                        <select
                          name="loteId"
                          value={nuevoDetalle.loteId}
                          onChange={handleDetalleChange}
                          className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 focus:outline-none focus:border-blue-500 appearance-none cursor-pointer"
                        >
                          <option value="">Seleccione un lote</option>
                          {lotes
                            .filter(lote => lote.productoId === nuevoDetalle.productoId)
                            .map(lote => (
                              <option key={lote.id} value={lote.id}>
                                {lote.numero}
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
                        Cantidad Esperada *
                      </label>
                      <input
                        type="number"
                        name="cantidadEsperada"
                        value={nuevoDetalle.cantidadEsperada}
                        onChange={handleDetalleChange}
                        placeholder="0"
                        min="1"
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Cantidad Recibida *
                      </label>
                      <input
                        type="number"
                        name="cantidadRecibida"
                        value={nuevoDetalle.cantidadRecibida}
                        onChange={handleDetalleChange}
                        placeholder="0"
                        min="0"
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Fecha de Vencimiento *
                      </label>
                      <input
                        type="date"
                        name="fechaVencimiento"
                        value={nuevoDetalle.fechaVencimiento}
                        onChange={handleDetalleChange}
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 focus:outline-none focus:border-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Precio Unitario (S/)
                      </label>
                      <input
                        type="number"
                        name="precioUnitario"
                        value={nuevoDetalle.precioUnitario}
                        onChange={handleDetalleChange}
                        placeholder="0.00"
                        min="0"
                        step="0.01"
                        className="w-full px-3 py-2 bg-white border border-gray-300 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500"
                      />
                    </div>
                  </div>

                  <div className="flex justify-end">
                    <button
                      type="button"
                      onClick={agregarDetalle}
                      className="bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded transition-colors"
                    >
                      ➕ Agregar Producto
                    </button>
                  </div>
                </div>
              </div>

              {/* Lista de Productos Agregados */}
              {formData.detalles.length > 0 && (
                <div className="mb-8">
                  <h4 className="text-md font-semibold text-gray-800 mb-4">📋 Productos en esta Recepción</h4>
                  <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead className="bg-gray-50">
                          <tr>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Producto</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Lote</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Esperada</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Recibida</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Vencimiento</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Precio</th>
                            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Acciones</th>
                          </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                          {formData.detalles.map((detalle) => (
                            <tr key={detalle.id} className="hover:bg-gray-50">
                              <td className="px-4 py-3 text-sm">
                                <div>
                                  <div className="font-medium text-gray-900">{detalle.productoNombre}</div>
                                  <div className="text-gray-500">{detalle.productoSku}</div>
                                </div>
                              </td>
                              <td className="px-4 py-3 text-sm text-gray-900">{detalle.loteNumero}</td>
                              <td className="px-4 py-3 text-sm text-gray-900">{detalle.cantidadEsperada}</td>
                              <td className="px-4 py-3 text-sm">
                                <span className={`font-medium ${
                                  detalle.cantidadRecibida === detalle.cantidadEsperada 
                                    ? 'text-green-600' 
                                    : detalle.cantidadRecibida < detalle.cantidadEsperada 
                                      ? 'text-yellow-600' 
                                      : 'text-red-600'
                                }`}>
                                  {detalle.cantidadRecibida}
                                </span>
                              </td>
                              <td className="px-4 py-3 text-sm text-gray-900">{detalle.fechaVencimiento}</td>
                              <td className="px-4 py-3 text-sm text-gray-900">S/ {detalle.precioUnitario.toFixed(2)}</td>
                              <td className="px-4 py-3 text-sm">
                                <button
                                  type="button"
                                  onClick={() => eliminarDetalle(detalle.id!)}
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

              {/* Verificaciones BPAs */}
              <div className="mb-8 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                <h4 className="text-sm font-medium text-yellow-800 mb-3">🔍 Verificaciones BPAs - DIGEMID</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input 
                      type="checkbox" 
                      name="verificacionDocumentos"
                      checked={formData.verificacionDocumentos}
                      onChange={handleInputChange}
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
                    />
                    <span className="ml-2 text-sm text-gray-700">Documentación completa y conforme</span>
                  </label>
                  <label className="flex items-center">
                    <input 
                      type="checkbox" 
                      name="verificacionFisica"
                      checked={formData.verificacionFisica}
                      onChange={handleInputChange}
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
                    />
                    <span className="ml-2 text-sm text-gray-700">Verificación física de productos conforme</span>
                  </label>
                  <label className="flex items-center">
                    <input 
                      type="checkbox" 
                      name="verificacionTemperatura"
                      checked={formData.verificacionTemperatura}
                      onChange={handleInputChange}
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
                    />
                    <span className="ml-2 text-sm text-gray-700">Temperatura de transporte adecuada</span>
                  </label>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  disabled={loading || formData.detalles.length === 0}
                  className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-medium py-3 px-6 rounded transition-colors flex items-center gap-2"
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
                      numeroOrdenCompra: '',
                      numeroGuiaRemision: '',
                      proveedorId: '',
                      fechaRecepcion: new Date().toISOString().slice(0, 16),
                      responsableRecepcion: user?.firstName || '',
                      estado: 'PENDIENTE',
                      temperaturaLlegada: undefined,
                      observaciones: '',
                      verificacionDocumentos: false,
                      verificacionFisica: false,
                      verificacionTemperatura: false,
                      aprobadoPorCalidad: false,
                      detalles: []
                    });
                  }}
                  className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-medium py-3 px-6 rounded transition-colors"
                >
                  🗑️ Limpiar Formulario
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Otras tabs (placeholder) */}
        {activeTab !== 'nueva-recepcion' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-8 text-center">
            <div className="text-gray-500 mb-4">
              <svg className="w-16 h-16 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              {activeTab === 'recepciones-pendientes' && 'Recepciones Pendientes de Verificación'}
              {activeTab === 'en-cuarentena' && 'Productos en Cuarentena'}
              {activeTab === 'historial' && 'Historial de Recepciones'}
            </h3>
            <p className="text-gray-500">
              Esta funcionalidad estará disponible próximamente.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}

export default function RecepcionMercaderiaPage() {
  return (
    <ProtectedRoute requiredRole={['Recepcion', 'DirectorTecnico']}>
      <RecepcionMercaderiaContent />
    </ProtectedRoute>
  );
}