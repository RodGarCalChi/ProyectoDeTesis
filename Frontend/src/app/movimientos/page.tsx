'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';

function MovimientosContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
  
  // Determinar qué tabs mostrar según el rol
  const getAvailableTabs = () => {
    if (user?.role === 'Recepcion') {
      return ['entrada']; // Solo entrada para Recepción
    } else if (user?.role === 'Despacho') {
      return ['salida']; // Solo salida para Despacho
    }
    return ['entrada', 'salida']; // Ambos para otros roles
  };

  const availableTabs = getAvailableTabs();
  const [activeTab, setActiveTab] = useState(availableTabs[0]);
  const [formData, setFormData] = useState({
    referencia: '',
    proveedor: '',
    producto: '',
    cantidad: '',
    ubicacion: '',
    recibidoPor: '',
    dia: '',
    hora: '',
    notas: ''
  });

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Datos del formulario:', formData);
    // Aquí iría la lógica para enviar los datos
    alert('Entrada registrada exitosamente');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="text-sm text-gray-600">
            Movimiento-Entrada
          </div>
          <div className="flex items-center gap-4">
            {/* Notification Bell */}
            <svg className="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-5 5v-5zM4 19h6v-2H4v2zM4 15h8v-2H4v2zM4 11h10V9H4v2z" />
            </svg>
            {/* Document Icon */}
            <svg className="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="p-6">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-2">PharmaFlow</h1>
          <h2 className="text-xl font-semibold text-gray-800">Movimientos de Inventario</h2>
        </div>

        {/* Tabs - Solo mostrar tabs disponibles según el rol */}
        {availableTabs.length > 1 && (
          <div className="mb-6">
            <div className="flex w-full max-w-md">
              {availableTabs.includes('entrada') && (
                <button
                  onClick={() => setActiveTab('entrada')}
                  className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 ${
                    availableTabs.length === 1 ? 'rounded-lg' : 'rounded-l-lg'
                  } border-2 transition-colors ${
                    activeTab === 'entrada'
                      ? 'bg-gray-200 border-gray-400 text-gray-900'
                      : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                  </svg>
                  Entrada
                </button>
              )}
              {availableTabs.includes('salida') && (
                <button
                  onClick={() => setActiveTab('salida')}
                  className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 ${
                    availableTabs.length === 1 ? 'rounded-lg' : 'rounded-r-lg'
                  } border-2 ${
                    availableTabs.length > 1 ? 'border-l-0' : ''
                  } transition-colors ${
                    activeTab === 'salida'
                      ? 'bg-gray-200 border-gray-400 text-gray-900'
                      : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                  </svg>
                  Salida
                </button>
              )}
            </div>
          </div>
        )}

        {/* Mostrar información del rol si solo tiene acceso a una funcionalidad */}
        {availableTabs.length === 1 && (
          <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
            <div className="flex items-center gap-2">
              <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p className="text-sm text-blue-800">
                <strong>Rol {user?.role}:</strong> {
                  user?.role === 'Recepcion' 
                    ? 'Tienes acceso solo a registrar entradas de mercadería.' 
                    : 'Tienes acceso solo a registrar salidas de mercadería.'
                }
              </p>
            </div>
          </div>
        )}

        {/* Entrada Form */}
        {activeTab === 'entrada' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <div className="flex items-center gap-3 mb-2">
                <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
                  <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-green-700">Registrar Entrada de Mercadería</h3>
              </div>
              <p className="text-sm text-gray-600">
                Registre los productos que ingresan al almacén desde proveedores
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                {/* Número de Referencia */}
                <div>
                  <label htmlFor="referencia" className="block text-sm font-medium text-gray-700 mb-2">
                    Número de Referencia
                  </label>
                  <input
                    type="text"
                    id="referencia"
                    name="referencia"
                    value={formData.referencia}
                    onChange={handleInputChange}
                    placeholder="Ej: PO-12345, GR-67890"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Proveedor */}
                <div>
                  <label htmlFor="proveedor" className="block text-sm font-medium text-gray-700 mb-2">
                    Proveedor
                  </label>
                  <div className="relative">
                    <select
                      id="proveedor"
                      name="proveedor"
                      value={formData.proveedor}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione un proveedor</option>
                      <option value="proveedor1">Laboratorio Farmacéutico ABC</option>
                      <option value="proveedor2">Distribuidora Médica XYZ</option>
                      <option value="proveedor3">Suministros Hospitalarios DEF</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Producto */}
                <div>
                  <label htmlFor="producto" className="block text-sm font-medium text-gray-700 mb-2">
                    Producto
                  </label>
                  <div className="relative">
                    <select
                      id="producto"
                      name="producto"
                      value={formData.producto}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione un producto</option>
                      <option value="producto1">Paracetamol 500mg</option>
                      <option value="producto2">Ibuprofeno 400mg</option>
                      <option value="producto3">Amoxicilina 250mg</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Cantidad */}
                <div>
                  <label htmlFor="cantidad" className="block text-sm font-medium text-gray-700 mb-2">
                    Cantidad
                  </label>
                  <input
                    type="number"
                    id="cantidad"
                    name="cantidad"
                    value={formData.cantidad}
                    onChange={handleInputChange}
                    placeholder="0"
                    min="0"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Ubicación */}
                <div>
                  <label htmlFor="ubicacion" className="block text-sm font-medium text-gray-700 mb-2">
                    Ubicación
                  </label>
                  <div className="relative">
                    <select
                      id="ubicacion"
                      name="ubicacion"
                      value={formData.ubicacion}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione una ubicación</option>
                      <option value="almacen1">Almacén Principal - Estante A1</option>
                      <option value="almacen2">Almacén Secundario - Estante B2</option>
                      <option value="almacen3">Área de Cuarentena - Estante C3</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Recibido por */}
                <div>
                  <label htmlFor="recibidoPor" className="block text-sm font-medium text-gray-700 mb-2">
                    Recibido por
                  </label>
                  <input
                    type="text"
                    id="recibidoPor"
                    name="recibidoPor"
                    value={formData.recibidoPor}
                    onChange={handleInputChange}
                    placeholder="Nombre del empleado"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Día */}
                <div>
                  <label htmlFor="dia" className="block text-sm font-medium text-gray-700 mb-2">
                    Día
                  </label>
                  <input
                    type="date"
                    id="dia"
                    name="dia"
                    value={formData.dia}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Hora */}
                <div>
                  <label htmlFor="hora" className="block text-sm font-medium text-gray-700 mb-2">
                    Hora
                  </label>
                  <input
                    type="time"
                    id="hora"
                    name="hora"
                    value={formData.hora}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>
              </div>

              {/* Notas Adicionales */}
              <div className="mb-6">
                <label htmlFor="notas" className="block text-sm font-medium text-gray-700 mb-2">
                  Notas Adicionales
                </label>
                <textarea
                  id="notas"
                  name="notas"
                  value={formData.notas}
                  onChange={handleInputChange}
                  placeholder="Ingrese cualquier observación o comentario adicional..."
                  rows={4}
                  className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors resize-vertical"
                />
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-6 rounded transition-colors"
                >
                  Registrar Entrada
                </button>
                <button
                  type="button"
                  onClick={() => setFormData({
                    referencia: '',
                    proveedor: '',
                    producto: '',
                    cantidad: '',
                    ubicacion: '',
                    recibidoPor: '',
                    dia: '',
                    hora: '',
                    notas: ''
                  })}
                  className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-medium py-2 px-6 rounded transition-colors"
                >
                  Limpiar
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Salida Form */}
        {activeTab === 'salida' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <div className="flex items-center gap-3 mb-2">
                <div className="w-8 h-8 rounded-full bg-red-100 flex items-center justify-center">
                  <svg className="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-red-700">Registrar Salida de Mercadería</h3>
              </div>
              <p className="text-sm text-gray-600">
                Registre los productos que salen del almacén para despacho o transferencia
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                {/* Número de Orden */}
                <div>
                  <label htmlFor="numeroOrden" className="block text-sm font-medium text-gray-700 mb-2">
                    Número de Orden/Guía
                  </label>
                  <input
                    type="text"
                    id="numeroOrden"
                    name="numeroOrden"
                    placeholder="Ej: ORD-12345, GR-67890"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Tipo de Salida */}
                <div>
                  <label htmlFor="tipoSalida" className="block text-sm font-medium text-gray-700 mb-2">
                    Tipo de Salida
                  </label>
                  <div className="relative">
                    <select
                      id="tipoSalida"
                      name="tipoSalida"
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione tipo de salida</option>
                      <option value="venta">Venta a Cliente</option>
                      <option value="transferencia">Transferencia entre Almacenes</option>
                      <option value="devolucion">Devolución a Proveedor</option>
                      <option value="ajuste">Ajuste de Inventario</option>
                      <option value="merma">Merma/Vencimiento</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Cliente/Destino */}
                <div>
                  <label htmlFor="destino" className="block text-sm font-medium text-gray-700 mb-2">
                    Cliente/Destino
                  </label>
                  <div className="relative">
                    <select
                      id="destino"
                      name="destino"
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione destino</option>
                      <option value="farmacia1">Farmacia Central</option>
                      <option value="farmacia2">Botica San Juan</option>
                      <option value="hospital1">Hospital Nacional</option>
                      <option value="almacen2">Almacén Secundario</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Producto */}
                <div>
                  <label htmlFor="productoSalida" className="block text-sm font-medium text-gray-700 mb-2">
                    Producto
                  </label>
                  <div className="relative">
                    <select
                      id="productoSalida"
                      name="productoSalida"
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione un producto</option>
                      <option value="producto1">Paracetamol 500mg - Stock: 150</option>
                      <option value="producto2">Ibuprofeno 400mg - Stock: 25</option>
                      <option value="producto3">Amoxicilina 250mg - Stock: 80</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Lote */}
                <div>
                  <label htmlFor="lote" className="block text-sm font-medium text-gray-700 mb-2">
                    Lote
                  </label>
                  <div className="relative">
                    <select
                      id="lote"
                      name="lote"
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione un lote</option>
                      <option value="lote1">L2024001 - Vence: 2025-12-15</option>
                      <option value="lote2">L2024002 - Vence: 2025-08-20</option>
                      <option value="lote3">L2024003 - Vence: 2025-03-10</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Cantidad */}
                <div>
                  <label htmlFor="cantidadSalida" className="block text-sm font-medium text-gray-700 mb-2">
                    Cantidad a Despachar
                  </label>
                  <input
                    type="number"
                    id="cantidadSalida"
                    name="cantidadSalida"
                    placeholder="0"
                    min="0"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Ubicación Origen */}
                <div>
                  <label htmlFor="ubicacionOrigen" className="block text-sm font-medium text-gray-700 mb-2">
                    Ubicación Origen
                  </label>
                  <div className="relative">
                    <select
                      id="ubicacionOrigen"
                      name="ubicacionOrigen"
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione ubicación</option>
                      <option value="almacen1">Almacén Principal - Estante A1</option>
                      <option value="almacen2">Almacén Secundario - Estante B2</option>
                      <option value="almacen3">Área de Cuarentena - Estante C3</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Despachado por */}
                <div>
                  <label htmlFor="despachadoPor" className="block text-sm font-medium text-gray-700 mb-2">
                    Despachado por
                  </label>
                  <input
                    type="text"
                    id="despachadoPor"
                    name="despachadoPor"
                    placeholder="Nombre del empleado"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Día */}
                <div>
                  <label htmlFor="diaSalida" className="block text-sm font-medium text-gray-700 mb-2">
                    Día
                  </label>
                  <input
                    type="date"
                    id="diaSalida"
                    name="diaSalida"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Hora */}
                <div>
                  <label htmlFor="horaSalida" className="block text-sm font-medium text-gray-700 mb-2">
                    Hora
                  </label>
                  <input
                    type="time"
                    id="horaSalida"
                    name="horaSalida"
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>
              </div>

              {/* Motivo/Observaciones */}
              <div className="mb-6">
                <label htmlFor="motivoSalida" className="block text-sm font-medium text-gray-700 mb-2">
                  Motivo/Observaciones
                </label>
                <textarea
                  id="motivoSalida"
                  name="motivoSalida"
                  placeholder="Ingrese el motivo de la salida y cualquier observación relevante..."
                  rows={4}
                  className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors resize-vertical"
                />
              </div>

              {/* Verificación BPAs */}
              <div className="mb-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                <h4 className="text-sm font-medium text-yellow-800 mb-3">Verificación BPAs - DIGEMID</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Producto verificado y en condiciones óptimas</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Fecha de vencimiento verificada</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Documentación de salida completa</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Trazabilidad registrada correctamente</span>
                  </label>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-red-600 hover:bg-red-700 text-white font-medium py-2 px-6 rounded transition-colors"
                >
                  Registrar Salida
                </button>
                <button
                  type="button"
                  className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-medium py-2 px-6 rounded transition-colors"
                >
                  Limpiar
                </button>
                <button
                  type="button"
                  className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded transition-colors"
                >
                  Imprimir Guía
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Logout Button */}
        <div className="fixed bottom-6 left-6">
          <button
            onClick={handleLogout}
            className="flex items-center gap-2 px-4 py-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            Logout
          </button>
        </div>
      </div>
    </div>
  );
}

export default function MovimientosPage() {
  return (
    <ProtectedRoute requiredRole="Recepcion">
      <MovimientosContent />
    </ProtectedRoute>
  );
}