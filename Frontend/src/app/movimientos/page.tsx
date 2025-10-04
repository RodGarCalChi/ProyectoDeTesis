'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function MovimientosPage() {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState('entrada');
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

  const handleLogout = () => {
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

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex w-full max-w-md">
            <button
              onClick={() => setActiveTab('entrada')}
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-l-lg border-2 transition-colors ${
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
            <button
              onClick={() => setActiveTab('salida')}
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-r-lg border-2 border-l-0 transition-colors ${
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
          </div>
        </div>

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

        {/* Salida Form (placeholder) */}
        {activeTab === 'salida' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
            <div className="text-center py-12">
              <svg className="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 mb-2">Registrar Salida de Mercadería</h3>
              <p className="text-gray-500">Esta funcionalidad estará disponible próximamente.</p>
            </div>
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