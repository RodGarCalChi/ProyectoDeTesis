'use client';

import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { 
  Package, 
  AlertTriangle, 
  Clock, 
  ShoppingCart, 
  TrendingUp, 
  TrendingDown,
  Truck,
  Send,
  DollarSign
} from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

// Mock data - En producción esto vendría de tu API
const dashboardStats = {
  totalProducts: 1247,
  lowStockProducts: 23,
  expiringSoonProducts: 8,
  pendingOrders: 15,
  todayReceivings: 7,
  todayDispatches: 12,
  totalValue: 2847650,
};

const topProducts = [
  { name: 'Aspirin 100mg', quantity: 1500, value: 45000, trend: 'up' },
  { name: 'Ibuprofen 200mg', quantity: 800, value: 32000, trend: 'up' },
  { name: 'Paracetamol 500mg', quantity: 200, value: 8000, trend: 'down' },
  { name: 'Amoxicillin 250mg', quantity: 0, value: 0, trend: 'down' },
];

const monthlyData = [
  { month: 'Ene', ingresos: 45000, egresos: 32000 },
  { month: 'Feb', ingresos: 52000, egresos: 38000 },
  { month: 'Mar', ingresos: 48000, egresos: 35000 },
  { month: 'Abr', ingresos: 61000, egresos: 42000 },
  { month: 'May', ingresos: 55000, egresos: 40000 },
  { month: 'Jun', ingresos: 67000, egresos: 45000 },
];

const categoryData = [
  { name: 'Analgésicos', value: 35, color: '#0088FE' },
  { name: 'Antibióticos', value: 25, color: '#00C49F' },
  { name: 'Antiinflamatorios', value: 20, color: '#FFBB28' },
  { name: 'Antihistamínicos', value: 15, color: '#FF8042' },
  { name: 'Otros', value: 5, color: '#8884D8' },
];

export default function DashboardPage() {
  return (
    <div className="space-y-6">
      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Productos</CardTitle>
            <Package className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{dashboardStats.totalProducts.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">
              +12% desde el mes pasado
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Stock Bajo</CardTitle>
            <AlertTriangle className="h-4 w-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-orange-600">{dashboardStats.lowStockProducts}</div>
            <p className="text-xs text-muted-foreground">
              Requieren reposición
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Por Vencer</CardTitle>
            <Clock className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{dashboardStats.expiringSoonProducts}</div>
            <p className="text-xs text-muted-foreground">
              Próximos 30 días
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Valor Total</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${dashboardStats.totalValue.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">
              Inventario actual
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Activity Cards */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Pedidos Pendientes</CardTitle>
            <ShoppingCart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{dashboardStats.pendingOrders}</div>
            <Progress value={75} className="mt-2" />
            <p className="text-xs text-muted-foreground mt-2">
              75% procesados esta semana
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Recepciones Hoy</CardTitle>
            <Truck className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{dashboardStats.todayReceivings}</div>
            <p className="text-xs text-muted-foreground">
              +2 desde ayer
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Despachos Hoy</CardTitle>
            <Send className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{dashboardStats.todayDispatches}</div>
            <p className="text-xs text-muted-foreground">
              +5 desde ayer
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Movimientos Mensuales</CardTitle>
            <CardDescription>Ingresos vs Egresos por mes</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={monthlyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="ingresos" fill="#0088FE" name="Ingresos" />
                <Bar dataKey="egresos" fill="#FF8042" name="Egresos" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Distribución por Categoría</CardTitle>
            <CardDescription>Porcentaje de productos por categoría</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={categoryData}
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                  label={({ name, value }) => `${name}: ${value}%`}
                >
                  {categoryData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Top Products */}
      <Card>
        <CardHeader>
          <CardTitle>Productos Principales</CardTitle>
          <CardDescription>Los productos con mayor movimiento</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {topProducts.map((product, index) => (
              <div key={index} className="flex items-center justify-between">
                <div className="flex items-center space-x-4">
                  <div className="w-8 h-8 bg-primary/10 rounded-full flex items-center justify-center">
                    <span className="text-sm font-medium">{index + 1}</span>
                  </div>
                  <div>
                    <p className="font-medium">{product.name}</p>
                    <p className="text-sm text-muted-foreground">
                      Stock: {product.quantity} unidades
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="font-medium">${product.value.toLocaleString()}</span>
                  {product.trend === 'up' ? (
                    <TrendingUp className="h-4 w-4 text-green-500" />
                  ) : (
                    <TrendingDown className="h-4 w-4 text-red-500" />
                  )}
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
