'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface Cliente {
  id: string;
  razonSocial: string;
}

interface Producto {
  id: string;
  codigoSKU: string;
  nombre: string;
  tipo: string;
  requiereCadenaFrio: boolean;
  unidadMedida: string;
  condicionAlmacen: string;
}

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

  // Estados para el catálogo
  const [productos, setProductos] = useState<Producto[]>([]);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(false);
  const [filtros, setFiltros] = useState({
    clienteId: '',
    nombre: '',
    codigoSKU: ''
  });
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,
    size: 10
  });

  // Cargar clientes al montar el componente
  useEffect(() => {
    cargarClientes();
  }, []);

  // Cargar productos cuando cambian los filtros
  useEffect(() => {
    if (activeTab === 'productos') {
      cargarProductos();
    }
  }, [activeTab, filtros, pagination.currentPage]);

  const cargarClientes = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/clientes');
      const data = await response.json();
      if (data.success) {
        setClientes(data.data);
      }
    } catch (error) {
      console.error('Error al cargar clientes:', error);
    }
  };

  const cargarProductos = async () => {
    setLoading(true);
    try {
      let url = `http://localhost:8080/api/productos/buscar?page=${pagination.currentPage}&size=${pagination.size}`;
      
      if (filtros.clienteId) {
        url += `&clienteId=${filtros.clienteId}`;
      }
      if (filtros.nombre) {
        url += `&nombre=${encodeURIComponent(filtros.nombre)}`;
      }
      if (filtros.codigoSKU) {
        url += `&codigoSKU=${encodeURIComponent(filtros.codigoSKU)}`;
      }

      const response = await fetch(url);
      const data = await response.json();
      
      if (data.success) {
        setProductos(data.data);
        setPagination(prev => ({
          ...prev,
          totalPages: data.totalPages,
          totalElements: data.totalElements,
          currentPage: data.currentPage
        }));
      }
    } catch (error) {
      console.error('Error al cargar productos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFiltroChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFiltros(prev => ({
      ...prev,
      [name]: value
    }));
    setPagination(prev => ({ ...prev, currentPage: 0 })); // Reset a primera página
  };

  const limpiarFiltros = () => {
    setFiltros({
      clienteId: '',
      nombre: '',
      codigoSKU: ''
    });
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

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
 

  return (
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      {/* Main Content */}
      <div className="p-6">
        <div className="mb-6">
          <h1 className="mb-2 font-bold text-gray-900 text-2xl">PharmaFlow</h1>
          <h2 className="font-semibold text-gray-800 text-xl">Registro de Inventario</h2>
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
          <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-gray-200 border-b">
              <div className="flex items-center gap-3 mb-4">
                <div className="flex justify-center items-center bg-blue-100 rounded-full w-8 h-8">
                  <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                  </svg>
                </div>
                <h3 className="font-semibold text-blue-700 text-lg">Catálogo de Productos Farmacéuticos</h3>
              </div>

              {/* Filtros */}
              <div className="gap-4 grid grid-cols-1 md:grid-cols-4 mb-4">
                {/* Filtro por Cliente */}
                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Cliente
                  </label>
                  <select
                    name="clienteId"
                    value={filtros.clienteId}
                    onChange={handleFiltroChange}
                    className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                  >
                    <option value="">Todos los clientes</option>
                    {clientes.map(cliente => (
                      <option key={cliente.id} value={cliente.id}>
                        {cliente.razonSocial}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Filtro por Nombre */}
                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Nombre del Producto
                  </label>
                  <input
                    type="text"
                    name="nombre"
                    value={filtros.nombre}
                    onChange={handleFiltroChange}
                    placeholder="Buscar por nombre..."
                    className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                  />
                </div>

                {/* Filtro por Código SKU */}
                <div>
                  <label className="block mb-2 font-medium text-gray-700 text-sm">
                    Código SKU
                  </label>
                  <input
                    type="text"
                    name="codigoSKU"
                    value={filtros.codigoSKU}
                    onChange={handleFiltroChange}
                    placeholder="Buscar por código..."
                    className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                  />
                </div>

                {/* Botón Limpiar */}
                <div className="flex items-end">
                  <button
                    onClick={limpiarFiltros}
                    className="bg-gray-200 hover:bg-gray-300 px-4 py-2 rounded-lg w-full text-gray-700 text-sm transition-colors"
                  >
                    Limpiar Filtros
                  </button>
                </div>
              </div>

              {/* Información de resultados */}
              <div className="text-gray-600 text-sm">
                Mostrando {productos.length} de {pagination.totalElements} productos
              </div>
            </div>

            <div className="overflow-x-auto">
              {loading ? (
                <div className="flex justify-center items-center py-12">
                  <div className="border-4 border-blue-200 border-t-blue-600 rounded-full w-12 h-12 animate-spin"></div>
                </div>
              ) : productos.length === 0 ? (
                <div className="py-12 text-center text-gray-500">
                  No se encontraron productos con los filtros seleccionados
                </div>
              ) : (
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Código</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Producto</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Categoría</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Unidad</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Condición</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Estado</th>
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Acciones</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {productos.map((producto) => (
                      <tr key={producto.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {producto.codigoSKU}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {producto.nombre}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <span className="inline-flex px-2 py-1 bg-blue-100 rounded-full font-semibold text-blue-800 text-xs">
                            {producto.tipo}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {producto.unidadMedida}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {producto.condicionAlmacen}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm">
                          {producto.requiereCadenaFrio ? (
                            <span className="inline-flex items-center gap-1 px-2 py-1 bg-cyan-100 rounded-full font-semibold text-cyan-800 text-xs">
                              <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                              </svg>
                              Cadena Frío
                            </span>
                          ) : (
                            <span className="inline-flex px-2 py-1 bg-green-100 rounded-full font-semibold text-green-800 text-xs">
                              Normal
                            </span>
                          )}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <button className="text-blue-600 hover:text-blue-900">
                            Ver Detalle
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>

            {/* Paginación */}
            {!loading && productos.length > 0 && (
              <div className="flex justify-between items-center px-6 py-4 border-gray-200 border-t">
                <div className="text-gray-700 text-sm">
                  Página {pagination.currentPage + 1} de {pagination.totalPages}
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => setPagination(prev => ({ ...prev, currentPage: Math.max(0, prev.currentPage - 1) }))}
                    disabled={pagination.currentPage === 0}
                    className="disabled:opacity-50 bg-gray-200 hover:bg-gray-300 disabled:hover:bg-gray-200 px-4 py-2 rounded-lg text-gray-700 text-sm transition-colors disabled:cursor-not-allowed"
                  >
                    Anterior
                  </button>
                  <button
                    onClick={() => setPagination(prev => ({ ...prev, currentPage: Math.min(prev.totalPages - 1, prev.currentPage + 1) }))}
                    disabled={pagination.currentPage >= pagination.totalPages - 1}
                    className="disabled:opacity-50 bg-gray-200 hover:bg-gray-300 disabled:hover:bg-gray-200 px-4 py-2 rounded-lg text-gray-700 text-sm transition-colors disabled:cursor-not-allowed"
                  >
                    Siguiente
                  </button>
                </div>
              </div>
            )}
          </div>
        )}

        {/* Registro de Producto */}
        {activeTab === 'registro' && (
          <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-gray-200 border-b">
              <div className="flex items-center gap-3 mb-2">
                <div className="flex justify-center items-center bg-green-100 rounded-full w-8 h-8">
                  <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                </div>
                <h3 className="font-semibold text-green-700 text-lg">Registrar Nuevo Producto</h3>
              </div>
              <p className="text-gray-600 text-sm">
                Registre un nuevo producto farmacéutico en el sistema de inventario
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              <div className="gap-6 grid grid-cols-1 md:grid-cols-2 mb-6">
                {/* Código del Producto */}
                <div>
                  <label htmlFor="codigo" className="block mb-2 font-medium text-gray-700 text-sm">
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
                    className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                  />
                </div>

                {/* Nombre del Producto */}
                <div>
                  <label htmlFor="nombre" className="block mb-2 font-medium text-gray-700 text-sm">
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
                    className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                  />
                </div>

                {/* Categoría */}
                <div>
                  <label htmlFor="categoria" className="block mb-2 font-medium text-gray-700 text-sm">
                    Categoría *
                  </label>
                  <div className="relative">
                    <select
                      id="categoria"
                      name="categoria"
                      value={formData.categoria}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
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
                    <svg className="top-1/2 right-3 absolute w-4 h-4 text-gray-500 -translate-y-1/2 pointer-events-none transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>

             

                {/* Stock Inicial */}
                <div>
                  <label htmlFor="stock" className="block mb-2 font-medium text-gray-700 text-sm">
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
                    className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                  />
                </div>

                {/* Stock Mínimo */}
                <div>
                  <label htmlFor="stockMinimo" className="block mb-2 font-medium text-gray-700 text-sm">
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
                    className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                  />
                </div>

                {/* Ubicación */}
                <div>
                  <label htmlFor="ubicacion" className="block mb-2 font-medium text-gray-700 text-sm">
                    Ubicación en Almacén *
                  </label>
                  <div className="relative">
                    <select
                      id="ubicacion"
                      name="ubicacion"
                      value={formData.ubicacion}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione una ubicación</option>
                      <option value="A1-E1">Almacén Principal - A1-E1</option>
                      <option value="A1-E2">Almacén Principal - A1-E2</option>
                      <option value="A2-E1">Almacén Principal - A2-E1</option>
                      <option value="B1-E1">Almacén Secundario - B1-E1</option>
                      <option value="B1-E2">Almacén Secundario - B1-E2</option>
                      <option value="C1-E1">Área de Cuarentena - C1-E1</option>
                    </select>
                    <svg className="top-1/2 right-3 absolute w-4 h-4 text-gray-500 -translate-y-1/2 pointer-events-none transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
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
                    className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600"
                  />
                  <span className="ml-2 text-gray-700 text-sm">
                    Este producto requiere receta médica (Normativa DIGEMID)
                  </span>
                </label>
              </div>

              {/* Descripción */}
              <div className="mb-6">
                <label htmlFor="descripcion" className="block mb-2 font-medium text-gray-700 text-sm">
                  Descripción del Producto
                </label>
                <textarea
                  id="descripcion"
                  name="descripcion"
                  value={formData.descripcion}
                  onChange={handleInputChange}
                  placeholder="Ingrese una descripción detallada del producto, indicaciones, contraindicaciones, etc."
                  rows={4}
                  className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors resize-vertical placeholder-gray-500"
                />
              </div>

              {/* Verificación BPAs */}
              <div className="bg-yellow-50 mb-6 p-4 border border-yellow-200 rounded-lg">
                <h4 className="mb-3 font-medium text-yellow-800 text-sm">Verificación BPAs - DIGEMID</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Producto cumple con normativas DIGEMID</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Documentación de registro sanitario completa</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Condiciones de almacenamiento verificadas</span>
                  </label>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-green-600 hover:bg-green-700 px-6 py-2 rounded font-medium text-white transition-colors"
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
                  className="bg-gray-300 hover:bg-gray-400 px-6 py-2 rounded font-medium text-gray-700 transition-colors"
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