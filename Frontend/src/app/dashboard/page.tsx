'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

interface InventoryItem {
  id: string;
  codigo: string;
  producto: string;
  lote: string;
  fechaVencimiento: string;
  stock: number;
  stockMinimo: number;
  ubicacion: string;
  estado: 'Disponible' | 'Stock Bajo' | 'Próximo a Vencer' | 'Vencido' | 'Bloqueado';
  proveedor: string;
  temperatura?: number;
  valorUnitario: number;
}

interface DashboardStats {
  totalProductos: number;
  stockBajo: number;
  proximosVencer: number;
  valorTotal: number;
  alertasTemperatura: number;
  movimientosHoy: number;
}

function DashboardContent() {
  const router = useRouter();
  const { user } = useAuth();
  const [inventoryData, setInventoryData] = useState<InventoryItem[]>([]);
  const [stats, setStats] = useState<DashboardStats>({
    totalProductos: 0,
    stockBajo: 0,
    proximosVencer: 0,
    valorTotal: 0,
    alertasTemperatura: 0,
    movimientosHoy: 0
  });
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('');
  const [filterEstado, setFilterEstado] = useState('');

  useEffect(() => {
    // Simular carga de datos
    setTimeout(() => {
      setInventoryData([
        {
          id: '1',
          codigo: 'PAR500',
          producto: 'Paracetamol 500mg',
          lote: 'L2024001',
          fechaVencimiento: '2025-12-15',
          stock: 150,
          stockMinimo: 50,
          ubicacion: 'A1-B2-C3',
          estado: 'Disponible',
          proveedor: 'Lab. ABC',
          temperatura: 22.5,
          valorUnitario: 12.50
        }       
 ,
        {
          id: '2',
          codigo: 'IBU400',
          producto: 'Ibuprofeno 400mg',
          lote: 'L2024002',
          fechaVencimiento: '2025-08-20',
          stock: 25,
          stockMinimo: 30,
          ubicacion: 'A2-B1-C2',
          estado: 'Stock Bajo',
          proveedor: 'Dist. XYZ',
          temperatura: 21.8,
          valorUnitario: 18.90
        },
        {
          id: '3',
          codigo: 'AMX250',
          producto: 'Amoxicilina 250mg',
          lote: 'L2024003',
          fechaVencimiento: '2025-03-10',
          stock: 80,
          stockMinimo: 40,
          ubicacion: 'B1-A3-C1',
          estado: 'Próximo a Vencer',
          proveedor: 'Sum. DEF',
          temperatura: 23.1,
          valorUnitario: 25.00
        },
        {
          id: '4',
          codigo: 'ASP100',
          producto: 'Aspirina 100mg',
          lote: 'L2024004',
          fechaVencimiento: '2024-02-28',
          stock: 0,
          stockMinimo: 20,
          ubicacion: 'C1-A2-B1',
          estado: 'Vencido',
          proveedor: 'Med. GHI',
          temperatura: 22.0,
          valorUnitario: 8.75
        }
      ]);

      setStats({
        totalProductos: 156,
        stockBajo: 12,
        proximosVencer: 8,
        valorTotal: 125000,
        alertasTemperatura: 2,
        movimientosHoy: 15
      });

      setLoading(false);
    }, 1000);
  }, []);

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'Disponible':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Stock Bajo':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'Próximo a Vencer':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Vencido':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'Bloqueado':
        return 'bg-gray-100 text-gray-800 border-gray-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getEstadoIcon = (estado: string) => {
    switch (estado) {
      case 'Disponible':
        return '✅';
      case 'Stock Bajo':
        return '⚠️';
      case 'Próximo a Vencer':
        return '⏰';
      case 'Vencido':
        return '❌';
      case 'Bloqueado':
        return '🔒';
      default:
        return '❓';
    }
  };

  const filteredData = inventoryData.filter(item => {
    const matchesSearch = item.producto.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         item.codigo.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         item.lote.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesCategory = !filterCategory || 
                           (filterCategory === 'analgesicos' && ['Paracetamol', 'Ibuprofeno', 'Aspirina'].some(med => item.producto.includes(med))) ||
                           (filterCategory === 'antibioticos' && item.producto.includes('Amoxicilina')) ||
                           (filterCategory === 'vitaminas' && item.producto.includes('Vitamina'));
    
    const matchesEstado = !filterEstado || item.estado === filterEstado;
    
    return matchesSearch && matchesCategory && matchesEstado;
  });

  const getRoleSpecificActions = () => {
    switch (user?.role) {
      case 'DirectorTecnico':
        return [
          { label: 'Gestionar Órdenes', path: '/ordenes', icon: '📋', color: 'blue' },
          { label: 'Control de Calidad', path: '/control', icon: '🔬', color: 'green' },
          { label: 'Reportes Ejecutivos', path: '/reportes', icon: '📊', color: 'purple' },
          { label: 'Configuración', path: '/configuracion', icon: '⚙️', color: 'gray' }
        ];
      case 'Operaciones':
        return [
          { label: 'Almacenamiento', path: '/almacenamiento', icon: '🏪', color: 'indigo' },
          { label: 'Movimientos Stock', path: '/movimientos-stock', icon: '📦', color: 'teal' },
          { label: 'Registro Inventario', path: '/registro-inventario', icon: '📝', color: 'blue' }
        ];
      case 'Despacho':
        return [
          { label: 'Centro Despacho', path: '/despacho', icon: '🚚', color: 'orange' },
          { label: 'Planificar Rutas', path: '/rutas', icon: '🗺️', color: 'blue' }
        ];
      case 'Cliente':
        return [
          { label: 'Mi Portal', path: '/cliente-portal', icon: '🏥', color: 'blue' },
          { label: 'Nuevo Pedido', path: '/nuevo-pedido', icon: '🛒', color: 'green' }
        ];
      default:
        return [];
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
      <Navigation />

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white">
        <div className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold mb-2">
                🏥 Dashboard PharmaFlow
              </h1>
              <p className="text-blue-100">
                Sistema de Gestión Logística Farmacéutica - Bienvenido, {user?.username}
              </p>
              <div className="flex items-center gap-4 mt-3">
                <span className="bg-blue-500 px-3 py-1 rounded-full text-sm">
                  {user?.role}
                </span>
                <span className="text-blue-100 text-sm">
                  📅 {new Date().toLocaleDateString('es-PE', { 
                    weekday: 'long', 
                    year: 'numeric', 
                    month: 'long', 
                    day: 'numeric' 
                  })}
                </span>
              </div>
            </div>
            <div className="text-right">
              <div className="text-4xl mb-2">📊</div>
              <p className="text-sm text-blue-100">Sistema Operativo</p>
            </div>
          </div>
        </div>
      </div>

      <div className="p-6">
        {/* Enhanced Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6 mb-8">
          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Total Productos</p>
                <p className="text-3xl font-bold text-gray-900">{stats.totalProductos}</p>
                <p className="text-xs text-green-600 mt-1">↗️ +5% vs mes anterior</p>
              </div>
              <div className="p-3 bg-blue-100 rounded-full">
                <span className="text-2xl">📦</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Stock Bajo</p>
                <p className="text-3xl font-bold text-red-600">{stats.stockBajo}</p>
                <p className="text-xs text-red-600 mt-1">⚠️ Requiere atención</p>
              </div>
              <div className="p-3 bg-red-100 rounded-full">
                <span className="text-2xl">⚠️</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Próximos a Vencer</p>
                <p className="text-3xl font-bold text-yellow-600">{stats.proximosVencer}</p>
                <p className="text-xs text-yellow-600 mt-1">⏰ Próximos 30 días</p>
              </div>
              <div className="p-3 bg-yellow-100 rounded-full">
                <span className="text-2xl">⏰</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Valor Total</p>
                <p className="text-3xl font-bold text-green-600">S/ {stats.valorTotal.toLocaleString()}</p>
                <p className="text-xs text-green-600 mt-1">💰 Inventario valorizado</p>
              </div>
              <div className="p-3 bg-green-100 rounded-full">
                <span className="text-2xl">💰</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Alertas Temperatura</p>
                <p className="text-3xl font-bold text-orange-600">{stats.alertasTemperatura}</p>
                <p className="text-xs text-orange-600 mt-1">🌡️ Fuera de rango</p>
              </div>
              <div className="p-3 bg-orange-100 rounded-full">
                <span className="text-2xl">🌡️</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 mb-1">Movimientos Hoy</p>
                <p className="text-3xl font-bold text-purple-600">{stats.movimientosHoy}</p>
                <p className="text-xs text-purple-600 mt-1">🔄 Operaciones realizadas</p>
              </div>
              <div className="p-3 bg-purple-100 rounded-full">
                <span className="text-2xl">🔄</span>
              </div>
            </div>
          </div>
        </div>

        {/* Role-specific Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {getRoleSpecificActions().map((action, index) => (
            <button
              key={index}
              onClick={() => router.push(action.path)}
              className={`bg-white rounded-xl shadow-lg border border-gray-200 p-6 hover:shadow-xl transition-all hover:scale-105 text-left group`}
            >
              <div className="flex items-center justify-between mb-4">
                <span className="text-3xl">{action.icon}</span>
                <div className={`w-3 h-3 rounded-full bg-${action.color}-500 opacity-60 group-hover:opacity-100`}></div>
              </div>
              <h3 className="font-semibold text-gray-900 mb-2">{action.label}</h3>
              <p className="text-sm text-gray-600">Acceso rápido</p>
            </button>
          ))}
        </div>

        {/* Recent Inventory Table */}
        <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
          <div className="p-6 border-b border-gray-200">
            <div className="flex items-center justify-between">
              <h2 className="text-xl font-bold text-gray-900">📦 Inventario Reciente</h2>
              <div className="flex gap-3">
                <input
                  type="text"
                  placeholder="Buscar productos..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
                <select
                  value={filterEstado}
                  onChange={(e) => setFilterEstado(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Todos los estados</option>
                  <option value="Disponible">Disponible</option>
                  <option value="Stock Bajo">Stock Bajo</option>
                  <option value="Próximo a Vencer">Próximo a Vencer</option>
                  <option value="Vencido">Vencido</option>
                </select>
              </div>
            </div>
          </div>

          <div className="overflow-x-auto">
            {loading ? (
              <div className="p-8 text-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                <p className="text-gray-600">Cargando inventario...</p>
              </div>
            ) : (
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Lote</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ubicación</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Temperatura</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Valor</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredData.map((item) => (
                    <tr key={item.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div className="text-sm font-medium text-gray-900">{item.producto}</div>
                          <div className="text-sm text-gray-500">{item.codigo}</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{item.lote}</div>
                        <div className="text-sm text-gray-500">Vence: {item.fechaVencimiento}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{item.stock} unidades</div>
                        <div className="text-sm text-gray-500">Min: {item.stockMinimo}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.ubicacion}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${getEstadoColor(item.estado)}`}>
                          {getEstadoIcon(item.estado)} {item.estado}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {item.temperatura ? `${item.temperatura}°C` : 'N/A'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        S/ {(item.valorUnitario * item.stock).toFixed(2)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default function Dashboard() {
  return (
    <ProtectedRoute>
      <DashboardContent />
    </ProtectedRoute>
  );
}