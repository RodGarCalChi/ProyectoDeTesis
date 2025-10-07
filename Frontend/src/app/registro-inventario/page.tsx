'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';

function RegistroInventarioContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('productos');
  const [showNewProductModal, setShowNewProductModal] = useState(false);

  const [productos, setProductos] = useState([
    {
      id: '1',
      codigo: 'PAR500',
      nombre: 'Paracetamol 500mg',
      categoria: 'Analgésicos',
      proveedor: 'Lab. ABC',
      stockMinimo: 50,
      stockMaximo: 500,
      unidadMedida: 'Tabletas',
      requiereReceta: false,
      activo: true
    },
    {
      id: '2',
      codigo: 'IBU400',
      nombre: 'Ibuprofeno 400mg',
      categoria: 'Antiinflamatorios',
      proveedor: 'Dist. XYZ',
      stockMinimo: 30,
      stockMaximo: 300,
      unidadMedida: 'Tabletas',
      requiereReceta: false,
      activo: true
    }
  ]);

  const [newProduct, setNewProduct] = useState({
    codigo: '',
    nombre: '',
    categoria: '',
    proveedor: '',
    stockMinimo: '',
    stockMaximo: '',
    unidadMedida: '',
    requiereReceta: false,
    descripcion: ''
  });

  const handleLogout = async () => {
    await logout();
    router.push('/login');
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    if (type === 'checkbox') {
      const checked = (e.target as HTMLInputElement).checked;
      setNewProduct(prev => ({ ...prev, [name]: checked }));
    } else {
      setNewProduct(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const producto = {
      id: Date.now().toString(),
      ...newProduct,
      stockMinimo: parseInt(newProduct.stockMinimo),
      stockMaximo: parseInt(newProduct.stockMaximo),
      activo: true
    };
    setProductos(prev => [...prev, producto]);
    setNewProduct({
      codigo: '',
      nombre: '',
      categoria: '',
      proveedor: '',
      stockMinimo: '',
      stockMaximo: '',
      unidadMedida: '',
      requiereReceta: false,
      descripcion: ''
    });
    setShowNewProductModal(false);
    alert('Producto registrado exitosamente');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <h1 className="text-xl font-bold text-gray-900">PharmaFlow</h1>
            <span className="text-sm text-gray-600">Registro de Inventario</span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">Usuario: {user?.firstName}</span>
            <button
              onClick={handleLogout}
              className="text-gray-600 hover:text-gray-900"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>
        </div>
      </header>     
 {/* Navigation */}
      <nav className="bg-white border-b border-gray-200 px-6 py-2">
        <div className="flex gap-6">
          <button 
            onClick={() => router.push('/dashboard')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Inventario
          </button>
          <button 
            onClick={() => router.push('/movimientos')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Movimientos
          </button>
          <button 
            onClick={() => router.push('/ordenes')}
            className="text-gray-600 hover:text-gray-900 pb-2 text-sm"
          >
            Órdenes
          </button>
          <button 
            onClick={() => router.push('/registro-inventario')}
            className="text-blue-600 border-b-2 border-blue-600 pb-2 text-sm font-medium"
          >
            Registro
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <div className="p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Registro de Inventario</h2>
            <p className="text-gray-600">Gestión de productos farmacéuticos según normativas DIGEMID</p>
          </div>
          <button
            onClick={() => setShowNewProductModal(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg flex items-center gap-2"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nuevo Producto
          </button>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('productos')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'productos'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Productos ({productos.length})
            </button>
            <button
              onClick={() => setActiveTab('categorias')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'categorias'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Categorías
            </button>
            <button
              onClick={() => setActiveTab('proveedores')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'proveedores'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Proveedores
            </button>
            <button
              onClick={() => setActiveTab('ubicaciones')}
              className={`py-2 px-4 text-sm font-medium border-b-2 ${
                activeTab === 'ubicaciones'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Ubicaciones
            </button>
          </div>
        </div>

        {/* Products Tab */}
        {activeTab === 'productos' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm">
            <div className="px-6 py-4 border-b border-gray-200">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold text-gray-900">Catálogo de Productos</h3>
                <div className="flex gap-2">
                  <input
                    type="text"
                    placeholder="Buscar productos..."
                    className="px-3 py-2 border border-gray-300 rounded text-sm"
                  />
                  <button className="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200">
                    Filtros
                  </button>
                  <button className="px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700">
                    Exportar
                  </button>
                </div>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Código
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Producto
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Categoría
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Proveedor
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Stock Min/Max
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Unidad
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Receta
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
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
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {producto.proveedor}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {producto.stockMinimo} / {producto.stockMaximo}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {producto.unidadMedida}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {producto.requiereReceta ? (
                          <span className="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                            Sí
                          </span>
                        ) : (
                          <span className="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                            No
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                          producto.activo ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {producto.activo ? 'Activo' : 'Inactivo'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex gap-2">
                          <button className="text-blue-600 hover:text-blue-900">Ver</button>
                          <button className="text-green-600 hover:text-green-900">Editar</button>
                          <button className="text-red-600 hover:text-red-900">
                            {producto.activo ? 'Desactivar' : 'Activar'}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}  
      {/* Categories Tab */}
        {activeTab === 'categorias' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
            <div className="text-center py-12">
              <svg className="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 mb-2">Gestión de Categorías</h3>
              <p className="text-gray-500">Funcionalidad en desarrollo para gestionar categorías de productos.</p>
            </div>
          </div>
        )}

        {/* Suppliers Tab */}
        {activeTab === 'proveedores' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
            <div className="text-center py-12">
              <svg className="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 mb-2">Gestión de Proveedores</h3>
              <p className="text-gray-500">Funcionalidad en desarrollo para gestionar proveedores.</p>
            </div>
          </div>
        )}

        {/* Locations Tab */}
        {activeTab === 'ubicaciones' && (
          <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
            <div className="text-center py-12">
              <svg className="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 mb-2">Gestión de Ubicaciones</h3>
              <p className="text-gray-500">Funcionalidad en desarrollo para gestionar ubicaciones de almacén.</p>
            </div>
          </div>
        )}

        {/* New Product Modal */}
        {showNewProductModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
              <div className="px-6 py-4 border-b border-gray-200">
                <div className="flex items-center justify-between">
                  <h3 className="text-lg font-semibold text-gray-900">Nuevo Producto</h3>
                  <button
                    onClick={() => setShowNewProductModal(false)}
                    className="text-gray-400 hover:text-gray-600"
                  >
                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
              </div>

              <form onSubmit={handleSubmit} className="p-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                  {/* Código */}
                  <div>
                    <label htmlFor="codigo" className="block text-sm font-medium text-gray-700 mb-2">
                      Código del Producto *
                    </label>
                    <input
                      type="text"
                      id="codigo"
                      name="codigo"
                      value={newProduct.codigo}
                      onChange={handleInputChange}
                      placeholder="Ej: PAR500"
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>

                  {/* Nombre */}
                  <div>
                    <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-2">
                      Nombre del Producto *
                    </label>
                    <input
                      type="text"
                      id="nombre"
                      name="nombre"
                      value={newProduct.nombre}
                      onChange={handleInputChange}
                      placeholder="Ej: Paracetamol 500mg"
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>

                  {/* Categoría */}
                  <div>
                    <label htmlFor="categoria" className="block text-sm font-medium text-gray-700 mb-2">
                      Categoría *
                    </label>
                    <select
                      id="categoria"
                      name="categoria"
                      value={newProduct.categoria}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    >
                      <option value="">Seleccione una categoría</option>
                      <option value="Analgésicos">Analgésicos</option>
                      <option value="Antiinflamatorios">Antiinflamatorios</option>
                      <option value="Antibióticos">Antibióticos</option>
                      <option value="Antihistamínicos">Antihistamínicos</option>
                      <option value="Vitaminas">Vitaminas y Suplementos</option>
                    </select>
                  </div>

                  {/* Proveedor */}
                  <div>
                    <label htmlFor="proveedor" className="block text-sm font-medium text-gray-700 mb-2">
                      Proveedor *
                    </label>
                    <select
                      id="proveedor"
                      name="proveedor"
                      value={newProduct.proveedor}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    >
                      <option value="">Seleccione un proveedor</option>
                      <option value="Lab. ABC">Laboratorio Farmacéutico ABC</option>
                      <option value="Dist. XYZ">Distribuidora Médica XYZ</option>
                      <option value="Sum. DEF">Suministros Hospitalarios DEF</option>
                    </select>
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
                      value={newProduct.stockMinimo}
                      onChange={handleInputChange}
                      placeholder="0"
                      min="0"
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>

                  {/* Stock Máximo */}
                  <div>
                    <label htmlFor="stockMaximo" className="block text-sm font-medium text-gray-700 mb-2">
                      Stock Máximo *
                    </label>
                    <input
                      type="number"
                      id="stockMaximo"
                      name="stockMaximo"
                      value={newProduct.stockMaximo}
                      onChange={handleInputChange}
                      placeholder="0"
                      min="0"
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    />
                  </div>

                  {/* Unidad de Medida */}
                  <div>
                    <label htmlFor="unidadMedida" className="block text-sm font-medium text-gray-700 mb-2">
                      Unidad de Medida *
                    </label>
                    <select
                      id="unidadMedida"
                      name="unidadMedida"
                      value={newProduct.unidadMedida}
                      onChange={handleInputChange}
                      required
                      className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                    >
                      <option value="">Seleccione unidad</option>
                      <option value="Tabletas">Tabletas</option>
                      <option value="Cápsulas">Cápsulas</option>
                      <option value="ml">Mililitros (ml)</option>
                      <option value="mg">Miligramos (mg)</option>
                      <option value="Frascos">Frascos</option>
                      <option value="Cajas">Cajas</option>
                    </select>
                  </div>

                  {/* Requiere Receta */}
                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      id="requiereReceta"
                      name="requiereReceta"
                      checked={newProduct.requiereReceta}
                      onChange={handleInputChange}
                      className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <label htmlFor="requiereReceta" className="ml-2 block text-sm text-gray-900">
                      Requiere Receta Médica
                    </label>
                  </div>
                </div>

                {/* Descripción */}
                <div className="mb-6">
                  <label htmlFor="descripcion" className="block text-sm font-medium text-gray-700 mb-2">
                    Descripción
                  </label>
                  <textarea
                    id="descripcion"
                    name="descripcion"
                    value={newProduct.descripcion}
                    onChange={handleInputChange}
                    placeholder="Descripción detallada del producto..."
                    rows={3}
                    className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500 resize-vertical"
                  />
                </div>

                {/* Botones */}
                <div className="flex gap-4 justify-end">
                  <button
                    type="button"
                    onClick={() => setShowNewProductModal(false)}
                    className="px-4 py-2 text-gray-700 bg-gray-200 rounded hover:bg-gray-300"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    Registrar Producto
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default function RegistroInventarioPage() {
  return (
    <ProtectedRoute requiredRole={['Inventario', 'Jefe_Ejecutivas']}>
      <RegistroInventarioContent />
    </ProtectedRoute>
  );
}