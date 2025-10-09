'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

function RegistroInventarioContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('productos');
  const [formData, setFormData] = useState({
    codigo: '',
    nombre: '',
    categoria: '',
    proveedor: '',
    precio: '',
    stock: '',
    stockMinimo: '',
    ubicacion: '',
    requiereReceta: false,
    descripcion: ''
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Datos del producto:', formData);
    alert('Producto registrado exitosamente');
  };

  // Datos de ejemplo para la tabla
  const productos = [
    {
      id: 1,
      codigo: 'MED001',
      nombre: 'Paracetamol 500mg',
      categoria: 'Analgésicos',
      stock: 150,
      stockMinimo: 20,
      precio: 12.50,
      ubicacion: 'A1-E2',
      estado: 'Disponible'
    },
    {
      id: 2,
      codigo: 'MED002',
      nombre: 'Ibuprofeno 400mg',
      categoria: 'Antiinflamatorios',
      stock: 25,
      stockMinimo: 30,
      precio: 18.75,
      ubicacion: 'A2-E1',
      estado: 'Stock Bajo'
    },
    {
      id: 3,
      codigo: 'MED003',
      nombre: 'Amoxicilina 250mg',
      categoria: 'Antibióticos',
      stock: 80,
      stockMinimo: 15,
      precio: 25.00,
      ubicacion: 'B1-E3',
      estado: 'Disponible'
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      {/* Main Content */}
      <div className="p-6">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-2">PharmaFlow</h1>
          <h2 className="text-xl font-semibold text-gray-800">Registro de Inventario</h2>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex w-full max-w-lg">
            <button
              onClick={() => setActiveTab('productos')}
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-l-lg border-2 transition-colors ${
                activeTab === 'productos'
                  ? 'bg-gray-200 border-gray-400 text-gray-900'
                  : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              Catálogo de Productos
            </button>
            <button
              onClick={() => setActiveTab('registro')}
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-r-lg border-2 border-l-0 transition-colors ${
                activeTab === 'registro'
                  ? 'bg-gray-200 border-gray-400 text-gray-900'
                  : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
              Registrar Producto
            </button>
          </div>
        </div>

        {/* Catálogo de Productos */}
        {activeTab === 'productos' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                    <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                    </svg>
                  </div>
                  <h3 className="text-lg font-semibold text-blue-700">Catálogo de Productos Farmacéuticos</h3>
                </div>
                <div className="flex gap-2">
                  <input
                    type="text"
                    placeholder="Buscar producto..."
                    className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                  />
                  <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm">
                    Buscar
                  </button>
                </div>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Código</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Categoría</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precio</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ubicación</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Acciones</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {productos.map((producto) => (
                    <tr key={producto.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {producto.codigo}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {producto.nombre}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {producto.categoria}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        <div className="flex items-center gap-2">
                          <span>{producto.stock}</span>
                          {producto.stock <= producto.stockMinimo && (
                            <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800">
                              ⚠️ Bajo
                            </span>
                          )}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        S/ {producto.precio.toFixed(2)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {producto.ubicacion}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${
                          producto.estado === 'Disponible' 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-yellow-100 text-yellow-800'
                        }`}>
                          {producto.estado}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex gap-2">
                          <button className="text-blue-600 hover:text-blue-900">Editar</button>
                          <button className="text-green-600 hover:text-green-900">Ver</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Registro de Producto */}
        {activeTab === 'registro' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="p-6 border-b border-gray-200">
              <div className="flex items-center gap-3 mb-2">
                <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
                  <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-green-700">Registrar Nuevo Producto</h3>
              </div>
              <p className="text-sm text-gray-600">
                Registre un nuevo producto farmacéutico en el sistema de inventario
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                {/* Código del Producto */}
                <div>
                  <label htmlFor="codigo" className="block text-sm font-medium text-gray-700 mb-2">
                    Código del Producto *
                  </label>
                  <input
                    type="text"
                    id="codigo"
                    name="codigo"
                    value={formData.codigo}
                    onChange={handleInputChange}
                    placeholder="Ej: MED001, ANT002"
                    required
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Nombre del Producto */}
                <div>
                  <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-2">
                    Nombre del Producto *
                  </label>
                  <input
                    type="text"
                    id="nombre"
                    name="nombre"
                    value={formData.nombre}
                    onChange={handleInputChange}
                    placeholder="Ej: Paracetamol 500mg"
                    required
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Categoría */}
                <div>
                  <label htmlFor="categoria" className="block text-sm font-medium text-gray-700 mb-2">
                    Categoría *
                  </label>
                  <div className="relative">
                    <select
                      id="categoria"
                      name="categoria"
                      value={formData.categoria}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione una categoría</option>
                      <option value="analgesicos">Analgésicos</option>
                      <option value="antibioticos">Antibióticos</option>
                      <option value="antiinflamatorios">Antiinflamatorios</option>
                      <option value="vitaminas">Vitaminas y Suplementos</option>
                      <option value="cardiovasculares">Cardiovasculares</option>
                      <option value="respiratorios">Sistema Respiratorio</option>
                      <option value="digestivos">Sistema Digestivo</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Proveedor */}
                <div>
                  <label htmlFor="proveedor" className="block text-sm font-medium text-gray-700 mb-2">
                    Proveedor *
                  </label>
                  <div className="relative">
                    <select
                      id="proveedor"
                      name="proveedor"
                      value={formData.proveedor}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione un proveedor</option>
                      <option value="proveedor1">Laboratorio Farmacéutico ABC</option>
                      <option value="proveedor2">Distribuidora Médica XYZ</option>
                      <option value="proveedor3">Suministros Hospitalarios DEF</option>
                      <option value="proveedor4">Farmacéutica Nacional GHI</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

                {/* Precio */}
                <div>
                  <label htmlFor="precio" className="block text-sm font-medium text-gray-700 mb-2">
                    Precio Unitario (S/) *
                  </label>
                  <input
                    type="number"
                    id="precio"
                    name="precio"
                    value={formData.precio}
                    onChange={handleInputChange}
                    placeholder="0.00"
                    min="0"
                    step="0.01"
                    required
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Stock Inicial */}
                <div>
                  <label htmlFor="stock" className="block text-sm font-medium text-gray-700 mb-2">
                    Stock Inicial *
                  </label>
                  <input
                    type="number"
                    id="stock"
                    name="stock"
                    value={formData.stock}
                    onChange={handleInputChange}
                    placeholder="0"
                    min="0"
                    required
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Stock Mínimo */}
                <div>
                  <label htmlFor="stockMinimo" className="block text-sm font-medium text-gray-700 mb-2">
                    Stock Mínimo *
                  </label>
                  <input
                    type="number"
                    id="stockMinimo"
                    name="stockMinimo"
                    value={formData.stockMinimo}
                    onChange={handleInputChange}
                    placeholder="0"
                    min="0"
                    required
                    className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
                  />
                </div>

                {/* Ubicación */}
                <div>
                  <label htmlFor="ubicacion" className="block text-sm font-medium text-gray-700 mb-2">
                    Ubicación en Almacén *
                  </label>
                  <div className="relative">
                    <select
                      id="ubicacion"
                      name="ubicacion"
                      value={formData.ubicacion}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione una ubicación</option>
                      <option value="A1-E1">Almacén Principal - A1-E1</option>
                      <option value="A1-E2">Almacén Principal - A1-E2</option>
                      <option value="A2-E1">Almacén Principal - A2-E1</option>
                      <option value="B1-E1">Almacén Secundario - B1-E1</option>
                      <option value="B1-E2">Almacén Secundario - B1-E2</option>
                      <option value="C1-E1">Área de Cuarentena - C1-E1</option>
                    </select>
                    <svg className="absolute right-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>
              </div>

              {/* Requiere Receta Médica */}
              <div className="mb-6">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    name="requiereReceta"
                    checked={formData.requiereReceta}
                    onChange={handleInputChange}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <span className="ml-2 text-sm text-gray-700">
                    Este producto requiere receta médica (Normativa DIGEMID)
                  </span>
                </label>
              </div>

              {/* Descripción */}
              <div className="mb-6">
                <label htmlFor="descripcion" className="block text-sm font-medium text-gray-700 mb-2">
                  Descripción del Producto
                </label>
                <textarea
                  id="descripcion"
                  name="descripcion"
                  value={formData.descripcion}
                  onChange={handleInputChange}
                  placeholder="Ingrese una descripción detallada del producto, indicaciones, contraindicaciones, etc."
                  rows={4}
                  className="w-full px-3 py-2 bg-gray-200 border-2 border-gray-400 rounded text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors resize-vertical"
                />
              </div>

              {/* Verificación BPAs */}
              <div className="mb-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                <h4 className="text-sm font-medium text-yellow-800 mb-3">Verificación BPAs - DIGEMID</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" required className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Producto cumple con normativas DIGEMID</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Documentación de registro sanitario completa</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    <span className="ml-2 text-sm text-gray-700">Condiciones de almacenamiento verificadas</span>
                  </label>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-6 rounded transition-colors"
                >
                  Registrar Producto
                </button>
                <button
                  type="button"
                  onClick={() => setFormData({
                    codigo: '',
                    nombre: '',
                    categoria: '',
                    proveedor: '',
                    precio: '',
                    stock: '',
                    stockMinimo: '',
                    ubicacion: '',
                    requiereReceta: false,
                    descripcion: ''
                  })}
                  className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-medium py-2 px-6 rounded transition-colors"
                >
                  Limpiar
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}

export default function RegistroInventarioPage() {
  return (
    <ProtectedRoute requiredRole={['Operaciones', 'AreaAdministrativa', 'DirectorTecnico']}>
      <RegistroInventarioContent />
    </ProtectedRoute>
  );
}