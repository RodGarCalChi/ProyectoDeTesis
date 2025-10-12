'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface PedidoCliente {
  id: string;
  numeroPedido: string;
  fechaPedido: string;
  fechaEntrega?: string;
  estado: 'Solicitado' | 'Confirmado' | 'Preparando' | 'Despachado' | 'Entregado' | 'Cancelado';
  productos: number;
  valorTotal: number;
  observaciones?: string;
}

interface ProductoDisponible {
  id: string;
  codigo: string;
  nombre: string;
  laboratorio: string;
  presentacion: string;
  precio: number;
  stock: number;
  fechaVencimiento: string;
  requiereReceta: boolean;
}

interface FacturaCliente {
  id: string;
  numeroFactura: string;
  fecha: string;
  monto: number;
  estado: 'Pendiente' | 'Pagada' | 'Vencida';
  fechaVencimiento: string;
}

function ClientePortalContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('pedidos');
  const [pedidos, setPedidos] = useState<PedidoCliente[]>([]);
  const [productos, setProductos] = useState<ProductoDisponible[]>([]);
  const [facturas, setFacturas] = useState<FacturaCliente[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Simular carga de datos del cliente
    setPedidos([
      {
        id: '1',
        numeroPedido: 'PED-2024-001',
        fechaPedido: '2024-01-15',
        fechaEntrega: '2024-01-18',
        estado: 'Confirmado',
        productos: 5,
        valorTotal: 2500.00,
        observaciones: 'Entrega en horario de mañana'
      },
      {
        id: '2',
        numeroPedido: 'PED-2024-002',
        fechaPedido: '2024-01-12',
        fechaEntrega: '2024-01-15',
        estado: 'Entregado',
        productos: 3,
        valorTotal: 1200.00
      },
      {
        id: '3',
        numeroPedido: 'PED-2024-003',
        fechaPedido: '2024-01-10',
        estado: 'Solicitado',
        productos: 8,
        valorTotal: 4500.00,
        observaciones: 'Pendiente de confirmación de stock'
      }
    ]);

    setProductos([
      {
        id: '1',
        codigo: 'PAR500',
        nombre: 'Paracetamol 500mg',
        laboratorio: 'Laboratorio ABC',
        presentacion: 'Caja x 20 tabletas',
        precio: 12.50,
        stock: 150,
        fechaVencimiento: '2025-12-15',
        requiereReceta: false
      },
      {
        id: '2',
        codigo: 'IBU400',
        nombre: 'Ibuprofeno 400mg',
        laboratorio: 'Distribuidora XYZ',
        presentacion: 'Caja x 10 cápsulas',
        precio: 18.90,
        stock: 75,
        fechaVencimiento: '2025-08-20',
        requiereReceta: false
      },
      {
        id: '3',
        codigo: 'AMX250',
        nombre: 'Amoxicilina 250mg',
        laboratorio: 'Suministros DEF',
        presentacion: 'Frasco x 60ml',
        precio: 25.00,
        stock: 30,
        fechaVencimiento: '2025-03-10',
        requiereReceta: true
      }
    ]);

    setFacturas([
      {
        id: '1',
        numeroFactura: 'F001-00001234',
        fecha: '2024-01-15',
        monto: 2500.00,
        estado: 'Pendiente',
        fechaVencimiento: '2024-02-14'
      },
      {
        id: '2',
        numeroFactura: 'F001-00001235',
        fecha: '2024-01-12',
        monto: 1200.00,
        estado: 'Pagada',
        fechaVencimiento: '2024-02-11'
      }
    ]);
  }, []);

  const getEstadoPedidoColor = (estado: string) => {
    switch (estado) {
      case 'Solicitado':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Confirmado':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'Preparando':
        return 'bg-purple-100 text-purple-800 border-purple-200';
      case 'Despachado':
        return 'bg-orange-100 text-orange-800 border-orange-200';
      case 'Entregado':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Cancelado':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getEstadoFacturaColor = (estado: string) => {
    switch (estado) {
      case 'Pendiente':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Pagada':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Vencida':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="p-6">
        {/* Header */}
        <div className="mb-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                🏥 Portal del Cliente
              </h1>
              <p className="text-gray-600 mt-1">
                Bienvenido, {user?.username} - Gestiona tus pedidos y consulta productos disponibles
              </p>
            </div>
            <div className="flex gap-3">
              <button className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2 font-medium">
                ➕ Nuevo Pedido
              </button>
              <button className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700">
                💬 Soporte
              </button>
            </div>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-blue-100 rounded-lg">
                <span className="text-2xl">📋</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Pedidos Activos</p>
                <p className="text-2xl font-bold text-gray-900">
                  {pedidos.filter(p => !['Entregado', 'Cancelado'].includes(p.estado)).length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-green-100 rounded-lg">
                <span className="text-2xl">✅</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Entregados</p>
                <p className="text-2xl font-bold text-gray-900">
                  {pedidos.filter(p => p.estado === 'Entregado').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <span className="text-2xl">💰</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Facturas Pendientes</p>
                <p className="text-2xl font-bold text-gray-900">
                  {facturas.filter(f => f.estado === 'Pendiente').length}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg border border-gray-200 p-6">
            <div className="flex items-center">
              <div className="p-3 bg-purple-100 rounded-lg">
                <span className="text-2xl">📦</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Productos Disponibles</p>
                <p className="text-2xl font-bold text-gray-900">{productos.length}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className="bg-white rounded-lg shadow-sm">
          {/* Tabs */}
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex space-x-8 px-6">
              {[
                { key: 'pedidos', label: 'Mis Pedidos', icon: '📋' },
                { key: 'catalogo', label: 'Catálogo de Productos', icon: '💊' },
                { key: 'facturas', label: 'Facturas', icon: '💰' },
                { key: 'perfil', label: 'Mi Perfil', icon: '👤' }
              ].map(tab => (
                <button
                  key={tab.key}
                  onClick={() => setActiveTab(tab.key)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm flex items-center gap-2 ${
                    activeTab === tab.key
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  <span>{tab.icon}</span>
                  {tab.label}
                </button>
              ))}
            </nav>
          </div>

          <div className="p-6">
            {activeTab === 'pedidos' && (
              <div>
                <div className="flex justify-between items-center mb-6">
                  <h3 className="text-xl font-semibold text-gray-900">Mis Pedidos</h3>
                  <div className="flex gap-4">
                    <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                      <option value="">Todos los estados</option>
                      <option value="solicitado">Solicitados</option>
                      <option value="confirmado">Confirmados</option>
                      <option value="entregado">Entregados</option>
                    </select>
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Pedido
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Fecha Pedido
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Fecha Entrega
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Estado
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Productos
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Total
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Acciones
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {pedidos.map((pedido) => (
                        <tr key={pedido.id} className="hover:bg-gray-50">
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-sm font-medium text-gray-900">{pedido.numeroPedido}</div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {pedido.fechaPedido}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {pedido.fechaEntrega || 'Por programar'}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getEstadoPedidoColor(pedido.estado)}`}>
                              {pedido.estado}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {pedido.productos}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            S/ {pedido.valorTotal.toLocaleString('es-PE', { minimumFractionDigits: 2 })}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                            <div className="flex gap-2">
                              <button className="text-blue-600 hover:text-blue-900 px-2 py-1 rounded hover:bg-blue-50">
                                👁️ Ver Detalle
                              </button>
                              {pedido.estado === 'Solicitado' && (
                                <button className="text-red-600 hover:text-red-900 px-2 py-1 rounded hover:bg-red-50">
                                  ❌ Cancelar
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {activeTab === 'catalogo' && (
              <div>
                <div className="flex justify-between items-center mb-6">
                  <h3 className="text-xl font-semibold text-gray-900">Catálogo de Productos</h3>
                  <div className="flex gap-4">
                    <div className="relative">
                      <input
                        type="text"
                        placeholder="Buscar productos..."
                        className="pl-10 pr-4 py-2 w-64 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                      />
                      <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">🔍</span>
                    </div>
                    <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                      <option value="">Todos los laboratorios</option>
                      <option value="abc">Laboratorio ABC</option>
                      <option value="xyz">Distribuidora XYZ</option>
                      <option value="def">Suministros DEF</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {productos.map((producto) => (
                    <div key={producto.id} className="bg-white border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                      <div className="flex justify-between items-start mb-4">
                        <div>
                          <h4 className="text-lg font-semibold text-gray-900">{producto.nombre}</h4>
                          <p className="text-sm text-gray-500">{producto.codigo}</p>
                        </div>
                        {producto.requiereReceta && (
                          <span className="bg-red-100 text-red-800 text-xs px-2 py-1 rounded-full">
                            Receta
                          </span>
                        )}
                      </div>
                      
                      <div className="space-y-2 mb-4">
                        <div className="flex justify-between">
                          <span className="text-sm text-gray-600">Laboratorio:</span>
                          <span className="text-sm text-gray-900">{producto.laboratorio}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-sm text-gray-600">Presentación:</span>
                          <span className="text-sm text-gray-900">{producto.presentacion}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-sm text-gray-600">Stock:</span>
                          <span className={`text-sm font-medium ${
                            producto.stock > 50 ? 'text-green-600' : 
                            producto.stock > 10 ? 'text-yellow-600' : 'text-red-600'
                          }`}>
                            {producto.stock} unidades
                          </span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-sm text-gray-600">Vencimiento:</span>
                          <span className="text-sm text-gray-900">{producto.fechaVencimiento}</span>
                        </div>
                      </div>
                      
                      <div className="flex justify-between items-center pt-4 border-t">
                        <span className="text-2xl font-bold text-blue-600">
                          S/ {producto.precio.toFixed(2)}
                        </span>
                        <button 
                          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:bg-gray-300"
                          disabled={producto.stock === 0}
                        >
                          {producto.stock > 0 ? '🛒 Agregar' : 'Sin Stock'}
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'facturas' && (
              <div>
                <div className="flex justify-between items-center mb-6">
                  <h3 className="text-xl font-semibold text-gray-900">Mis Facturas</h3>
                  <div className="flex gap-4">
                    <select className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500">
                      <option value="">Todos los estados</option>
                      <option value="pendiente">Pendientes</option>
                      <option value="pagada">Pagadas</option>
                      <option value="vencida">Vencidas</option>
                    </select>
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Número Factura
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Fecha
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Monto
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Estado
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Vencimiento
                        </th>
                        <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Acciones
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {facturas.map((factura) => (
                        <tr key={factura.id} className="hover:bg-gray-50">
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {factura.numeroFactura}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {factura.fecha}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            S/ {factura.monto.toLocaleString('es-PE', { minimumFractionDigits: 2 })}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <span className={`inline-flex px-3 py-1 text-xs font-semibold rounded-full border ${getEstadoFacturaColor(factura.estado)}`}>
                              {factura.estado}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {factura.fechaVencimiento}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                            <div className="flex gap-2">
                              <button className="text-blue-600 hover:text-blue-900 px-2 py-1 rounded hover:bg-blue-50">
                                📄 Ver PDF
                              </button>
                              {factura.estado === 'Pendiente' && (
                                <button className="text-green-600 hover:text-green-900 px-2 py-1 rounded hover:bg-green-50">
                                  💳 Pagar
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {activeTab === 'perfil' && (
              <div>
                <h3 className="text-xl font-semibold text-gray-900 mb-6">Mi Perfil</h3>
                <div className="max-w-2xl">
                  <div className="bg-gray-50 rounded-lg p-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Razón Social
                        </label>
                        <input
                          type="text"
                          defaultValue="Farmacia Central S.A.C."
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          readOnly
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          RUC
                        </label>
                        <input
                          type="text"
                          defaultValue="20123456789"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          readOnly
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Dirección
                        </label>
                        <input
                          type="text"
                          defaultValue="Av. Arequipa 1234"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Distrito
                        </label>
                        <input
                          type="text"
                          defaultValue="Lima"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Teléfono
                        </label>
                        <input
                          type="text"
                          defaultValue="(01) 234-5678"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Email
                        </label>
                        <input
                          type="email"
                          defaultValue="contacto@farmaciacentral.com"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>
                    <div className="mt-6">
                      <button className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md">
                        Actualizar Información
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default function ClientePortalPage() {
  return (
    <ProtectedRoute requiredRole={['Cliente']}>
      <ClientePortalContent />
    </ProtectedRoute>
  );
}