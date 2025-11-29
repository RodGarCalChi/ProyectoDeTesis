'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface Cliente {
  id: string;
  razonSocial: string;
  rucDni: string;
  email: string;
  activo: boolean;
}

interface Producto {
  id: string;
  codigoSKU: string;
  nombre: string;
  tipo: string;
  unidadMedida: string;
  requiereCadenaFrio: boolean;
  condicionAlmacen: string;
}

interface InventarioCliente {
  id: string;
  clienteId: string;
  productoId: string;
  loteId: string;
  cantidadDespachada: number;
  cantidadDisponible: number;
  cantidadReservada: number;
  codigoBarras: string;
  estado: string;
  fechaIngreso: string;
  fechaVencimiento: string;
  fechaCreacion: string;
  fechaActualizacion: string;
  observaciones: string;
  temperaturaAlmacenamiento: number;
  ubicacionId: string;
  recepcionId: string;
  detalleRecepcionId: string;
  usuarioRegistroId: string;
  usuarioUbicacionId: string;
  producto?: Producto;
}

interface Ubicacion {
  id: string;
  nombre: string;
  zona: string;
  tipo: string;
}

interface FormularioInventario {
  clienteId: string;
  productoId: string;
  lote: string;
  cantidadDisponible: string;
  cantidadReservada: string;
  codigoBarras: string;
  fechaIngreso: string;
  fechaVencimiento: string;
  ubicacionId: string;
  temperaturaAlmacenamiento: string;
  observaciones: string;
}

function InventarioClienteContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('consultar');
  
  // Estados principales
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [ubicaciones, setUbicaciones] = useState<Ubicacion[]>([]);
  const [inventarios, setInventarios] = useState<InventarioCliente[]>([]);
  const [loading, setLoading] = useState(false);
  
  // Estados de filtros
  const [clienteSeleccionado, setClienteSeleccionado] = useState('');
  const [productoSeleccionado, setProductoSeleccionado] = useState('');
  
  // Estado del formulario
  const [formulario, setFormulario] = useState<FormularioInventario>({
    clienteId: '',
    productoId: '',
    lote: '',
    cantidadDisponible: '',
    cantidadReservada: '',
    codigoBarras: '',
    fechaIngreso: new Date().toISOString().split('T')[0],
    fechaVencimiento: '',
    ubicacionId: '',
    temperaturaAlmacenamiento: '',
    observaciones: ''
  });

  // Cargar datos iniciales
  useEffect(() => {
    cargarClientes();
    cargarUbicaciones();
  }, []);

  // Cargar productos cuando se selecciona un cliente
  useEffect(() => {
    if (clienteSeleccionado) {
      cargarProductosDelCliente(clienteSeleccionado);
      cargarInventarioDelCliente(clienteSeleccionado);
    }
  }, [clienteSeleccionado]);

  const cargarClientes = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/clientes/activos');
      if (response.ok) {
        const data = await response.json();
        setClientes(Array.isArray(data) ? data : []);
      }
    } catch (error) {
      console.error('Error al cargar clientes:', error);
    } finally {
      setLoading(false);
    }
  };

  const cargarProductosDelCliente = async (clienteId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/clientes/${clienteId}/productos`);
      if (response.ok) {
        const data = await response.json();
        if (data.success && data.productos) {
          setProductos(data.productos);
        }
      }
    } catch (error) {
      console.error('Error al cargar productos del cliente:', error);
    }
  };

  const cargarInventarioDelCliente = async (clienteId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/inventario-cliente/cliente/${clienteId}`);
      if (response.ok) {
        const data = await response.json();
        setInventarios(Array.isArray(data) ? data : data.data || []);
      }
    } catch (error) {
      console.error('Error al cargar inventario:', error);
      setInventarios([]);
    }
  };

  const cargarUbicaciones = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/ubicaciones');
      if (response.ok) {
        const data = await response.json();
        setUbicaciones(Array.isArray(data) ? data : data.data || []);
      }
    } catch (error) {
      console.error('Error al cargar ubicaciones:', error);
    }
  };

  const handleClienteChange = (clienteId: string) => {
    setClienteSeleccionado(clienteId);
    setProductoSeleccionado('');
    setFormulario(prev => ({ ...prev, clienteId, productoId: '' }));
  };

  const handleFormularioChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormulario(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmitInventario = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const inventarioData = {
        clienteId: formulario.clienteId,
        productoId: formulario.productoId,
        lote: formulario.lote,
        cantidadDisponible: parseInt(formulario.cantidadDisponible),
        cantidadReservada: parseInt(formulario.cantidadReservada) || 0,
        codigoBarras: formulario.codigoBarras,
        fechaIngreso: formulario.fechaIngreso,
        fechaVencimiento: formulario.fechaVencimiento,
        ubicacionId: formulario.ubicacionId,
        temperaturaAlmacenamiento: parseFloat(formulario.temperaturaAlmacenamiento) || null,
        observaciones: formulario.observaciones,
        estado: 'ALMACENADO',
        usuarioRegistroId: user?.id || 'system'
      };

      const response = await fetch('http://localhost:8080/api/inventario-cliente', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(inventarioData)
      });

      if (response.ok) {
        const result = await response.json();
        alert('✅ Inventario registrado exitosamente');
        
        // Limpiar formulario
        setFormulario({
          clienteId: formulario.clienteId,
          productoId: '',
          lote: '',
          cantidadDisponible: '',
          cantidadReservada: '',
          codigoBarras: '',
          fechaIngreso: new Date().toISOString().split('T')[0],
          fechaVencimiento: '',
          ubicacionId: '',
          temperaturaAlmacenamiento: '',
          observaciones: ''
        });

        // Recargar inventario
        cargarInventarioDelCliente(formulario.clienteId);
      } else {
        const error = await response.json();
        throw new Error(error.message || 'Error al registrar inventario');
      }
    } catch (error) {
      console.error('Error:', error);
      alert(`❌ Error: ${error instanceof Error ? error.message : 'Error desconocido'}`);
    } finally {
      setLoading(false);
    }
  };

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'ALMACENADO': return 'bg-green-100 text-green-800';
      case 'RESERVADO': return 'bg-yellow-100 text-yellow-800';
      case 'DESPACHADO': return 'bg-blue-100 text-blue-800';
      case 'VENCIDO': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const clienteActual = clientes.find(c => c.id === clienteSeleccionado);

  return (
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      <div className="p-6">
        <div className="mb-6">
          <h1 className="mb-2 font-bold text-gray-900 text-2xl">PharmaFlow</h1>
          <h2 className="font-semibold text-gray-800 text-xl">Inventario de Cliente</h2>
          <p className="text-gray-600 text-sm mt-1">
            Gestione el inventario de productos almacenados para cada cliente
          </p>
        </div>

        {/* Selección de Cliente */}
        <div className="bg-white shadow-sm border border-gray-200 rounded-lg mb-6">
          <div className="p-6">
            <div className="flex items-center gap-3 mb-4">
              <div className="flex justify-center items-center bg-blue-100 rounded-full w-8 h-8">
                <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <h3 className="font-semibold text-blue-700 text-lg">Seleccionar Cliente</h3>
            </div>
            
            <div className="gap-4 grid grid-cols-1 md:grid-cols-2">
              <div>
                <label className="block mb-2 font-medium text-gray-700 text-sm">
                  Cliente *
                </label>
                <select
                  value={clienteSeleccionado}
                  onChange={(e) => handleClienteChange(e.target.value)}
                  className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                >
                  <option value="">Seleccione un cliente</option>
                  {clientes.map(cliente => (
                    <option key={cliente.id} value={cliente.id}>
                      {cliente.razonSocial} - {cliente.rucDni}
                    </option>
                  ))}
                </select>
              </div>
              
              {clienteActual && (
                <div className="flex items-end">
                  <div className="bg-blue-50 px-4 py-2 rounded-lg w-full">
                    <div className="font-medium text-blue-900 text-sm">{clienteActual.razonSocial}</div>
                    <div className="text-blue-600 text-xs">{clienteActual.email}</div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {clienteSeleccionado && (
          <>
            {/* Tabs */}
            <div className="mb-6">
              <div className="flex w-full max-w-lg">
                <button
                  onClick={() => setActiveTab('consultar')}
                  className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-l-lg border-2 transition-colors ${
                    activeTab === 'consultar'
                      ? 'bg-gray-200 border-gray-400 text-gray-900'
                      : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                  Consultar Inventario
                </button>
                <button
                  onClick={() => setActiveTab('crear')}
                  className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-r-lg border-2 border-l-0 transition-colors ${
                    activeTab === 'crear'
                      ? 'bg-gray-200 border-gray-400 text-gray-900'
                      : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                  Crear Inventario
                </button>
              </div>
            </div>

            {/* Consultar Inventario */}
            {activeTab === 'consultar' && (
              <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
                <div className="p-6 border-gray-200 border-b">
                  <div className="flex items-center gap-3 mb-2">
                    <div className="flex justify-center items-center bg-green-100 rounded-full w-8 h-8">
                      <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                      </svg>
                    </div>
                    <h3 className="font-semibold text-green-700 text-lg">Inventario Actual</h3>
                  </div>
                  <p className="text-gray-600 text-sm">
                    Productos almacenados para {clienteActual?.razonSocial}
                  </p>
                </div>

                <div className="overflow-x-auto">
                  {loading ? (
                    <div className="flex justify-center items-center py-12">
                      <div className="border-4 border-blue-200 border-t-blue-600 rounded-full w-12 h-12 animate-spin"></div>
                    </div>
                  ) : inventarios.length === 0 ? (
                    <div className="py-12 text-center text-gray-500">
                      <svg className="mx-auto mb-4 w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                      </svg>
                      <p>No hay inventario registrado para este cliente</p>
                      <p className="text-gray-400 text-sm mt-1">Use la pestaña "Crear Inventario" para agregar productos</p>
                    </div>
                  ) : (
                    <table className="w-full">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Producto</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Lote</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Disponible</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Reservado</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Estado</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Vencimiento</th>
                          <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Ubicación</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {inventarios.map((inventario) => (
                          <tr key={inventario.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div>
                                <div className="font-medium text-gray-900 text-sm">
                                  {inventario.producto?.nombre || 'Producto no encontrado'}
                                </div>
                                <div className="text-gray-500 text-sm">
                                  {inventario.producto?.codigoSKU}
                                </div>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {inventario.loteId || 'N/A'}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              <span className="font-medium text-green-600">
                                {inventario.cantidadDisponible}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              <span className="font-medium text-yellow-600">
                                {inventario.cantidadReservada}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className={`inline-flex px-2 py-1 rounded-full font-semibold text-xs ${getEstadoColor(inventario.estado)}`}>
                                {inventario.estado}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {new Date(inventario.fechaVencimiento).toLocaleDateString()}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                              {inventario.ubicacionId || 'Sin asignar'}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  )}
                </div>
              </div>
            )}

            {/* Crear Inventario */}
            {activeTab === 'crear' && (
              <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
                <div className="p-6 border-gray-200 border-b">
                  <div className="flex items-center gap-3 mb-2">
                    <div className="flex justify-center items-center bg-purple-100 rounded-full w-8 h-8">
                      <svg className="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                      </svg>
                    </div>
                    <h3 className="font-semibold text-purple-700 text-lg">Crear Registro de Inventario</h3>
                  </div>
                  <p className="text-gray-600 text-sm">
                    Registre productos en el inventario de {clienteActual?.razonSocial}
                  </p>
                </div>

                <form onSubmit={handleSubmitInventario} className="p-6">
                  <div className="gap-6 grid grid-cols-1 md:grid-cols-2 mb-6">
                    {/* Producto */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Producto *
                      </label>
                      <select
                        name="productoId"
                        value={formulario.productoId}
                        onChange={handleFormularioChange}
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      >
                        <option value="">Seleccione un producto</option>
                        {productos.map(producto => (
                          <option key={producto.id} value={producto.id}>
                            {producto.nombre} - {producto.codigoSKU}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Lote */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Número de Lote *
                      </label>
                      <input
                        type="text"
                        name="lote"
                        value={formulario.lote}
                        onChange={handleFormularioChange}
                        placeholder="L2024001"
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Cantidad Disponible */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Cantidad Disponible *
                      </label>
                      <input
                        type="number"
                        name="cantidadDisponible"
                        value={formulario.cantidadDisponible}
                        onChange={handleFormularioChange}
                        placeholder="100"
                        min="0"
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Cantidad Reservada */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Cantidad Reservada
                      </label>
                      <input
                        type="number"
                        name="cantidadReservada"
                        value={formulario.cantidadReservada}
                        onChange={handleFormularioChange}
                        placeholder="0"
                        min="0"
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Código de Barras */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Código de Barras
                      </label>
                      <input
                        type="text"
                        name="codigoBarras"
                        value={formulario.codigoBarras}
                        onChange={handleFormularioChange}
                        placeholder="7501234567890"
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Fecha de Ingreso */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Fecha de Ingreso *
                      </label>
                      <input
                        type="date"
                        name="fechaIngreso"
                        value={formulario.fechaIngreso}
                        onChange={handleFormularioChange}
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Fecha de Vencimiento */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Fecha de Vencimiento *
                      </label>
                      <input
                        type="date"
                        name="fechaVencimiento"
                        value={formulario.fechaVencimiento}
                        onChange={handleFormularioChange}
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>

                    {/* Ubicación */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Ubicación *
                      </label>
                      <select
                        name="ubicacionId"
                        value={formulario.ubicacionId}
                        onChange={handleFormularioChange}
                        required
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      >
                        <option value="">Seleccione ubicación</option>
                        {ubicaciones.map(ubicacion => (
                          <option key={ubicacion.id} value={ubicacion.id}>
                            {ubicacion.nombre} - {ubicacion.zona}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Temperatura */}
                    <div>
                      <label className="block mb-2 font-medium text-gray-700 text-sm">
                        Temperatura de Almacenamiento (°C)
                      </label>
                      <input
                        type="number"
                        name="temperaturaAlmacenamiento"
                        value={formulario.temperaturaAlmacenamiento}
                        onChange={handleFormularioChange}
                        placeholder="22.5"
                        step="0.1"
                        className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm"
                      />
                    </div>
                  </div>

                  {/* Observaciones */}
                  <div className="mb-6">
                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                      Observaciones
                    </label>
                    <textarea
                      name="observaciones"
                      value={formulario.observaciones}
                      onChange={handleFormularioChange}
                      placeholder="Observaciones adicionales sobre el inventario..."
                      rows={3}
                      className="px-3 py-2 border border-gray-300 focus:border-purple-500 rounded-lg focus:outline-none w-full text-sm resize-none"
                    />
                  </div>

                  {/* Botones */}
                  <div className="flex justify-end gap-4 pt-6 border-gray-200 border-t">
                    <button
                      type="button"
                      onClick={() => setFormulario({
                        clienteId: formulario.clienteId,
                        productoId: '',
                        lote: '',
                        cantidadDisponible: '',
                        cantidadReservada: '',
                        codigoBarras: '',
                        fechaIngreso: new Date().toISOString().split('T')[0],
                        fechaVencimiento: '',
                        ubicacionId: '',
                        temperaturaAlmacenamiento: '',
                        observaciones: ''
                      })}
                      className="bg-gray-200 hover:bg-gray-300 px-6 py-2 rounded-lg text-gray-700 transition-colors"
                    >
                      Limpiar
                    </button>
                    <button
                      type="submit"
                      disabled={loading}
                      className="flex items-center gap-2 bg-purple-600 hover:bg-purple-700 disabled:opacity-50 px-6 py-2 rounded-lg text-white transition-colors disabled:cursor-not-allowed"
                    >
                      {loading ? (
                        <div className="border-2 border-white border-t-transparent rounded-full w-4 h-4 animate-spin"></div>
                      ) : (
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                        </svg>
                      )}
                      {loading ? 'Registrando...' : 'Registrar Inventario'}
                    </button>
                  </div>
                </form>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default function InventarioClientePage() {
  return (
    <ProtectedRoute allowedRoles={['Admin', 'Recepcion']}>
      <InventarioClienteContent />
    </ProtectedRoute>
  );
}