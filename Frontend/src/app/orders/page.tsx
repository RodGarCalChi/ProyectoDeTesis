'use client';

import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { 
  MoreHorizontal, 
  Search, 
  PlusCircle, 
  ListFilter, 
  Eye, 
  Edit, 
  Trash2,
  Package,
  Calendar,
  DollarSign
} from 'lucide-react';
import { 
  DropdownMenu, 
  DropdownMenuContent, 
  DropdownMenuItem, 
  DropdownMenuLabel, 
  DropdownMenuTrigger,
  DropdownMenuSeparator,
  DropdownMenuCheckboxItem
} from '@/components/ui/dropdown-menu';
import { 
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

// Mock data
const orders = [
  {
    id: "ORD-001",
    orderNumber: "ORD-2024-001",
    type: "DISPATCH" as const,
    status: "PENDING" as const,
    supplier: "Farmacéutica ABC",
    customer: null,
    orderDate: "2024-01-15",
    expectedDate: "2024-01-20",
    totalAmount: 15750.00,
    itemsCount: 5,
  },
  {
    id: "ORD-002",
    orderNumber: "ORD-2024-002",
    type: "DISPATCH" as const,
    status: "CONFIRMED" as const,
    supplier: null,
    customer: "Hospital Central",
    orderDate: "2024-01-14",
    expectedDate: "2024-01-18",
    totalAmount: 8920.50,
    itemsCount: 3,
  },
  {
    id: "ORD-003",
    orderNumber: "ORD-2024-003",
    type: "DISPATCH" as const,
    status: "PROCESSING" as const,
    supplier: "Laboratorios XYZ",
    customer: null,
    orderDate: "2024-01-13",
    expectedDate: "2024-01-19",
    totalAmount: 22340.75,
    itemsCount: 8,
  },
  {
    id: "ORD-004",
    orderNumber: "ORD-2024-004",
    type: "DISPATCH" as const,
    status: "SHIPPED" as const,
    supplier: null,
    customer: "Clínica San José",
    orderDate: "2024-01-12",
    expectedDate: "2024-01-16",
    totalAmount: 5680.25,
    itemsCount: 2,
  },
];

const orderItems = [
  { id: "1", productName: "Aspirin 100mg", sku: "ASP-100", quantity: 500, unitPrice: 12.50, totalPrice: 6250.00 },
  { id: "2", productName: "Ibuprofen 200mg", sku: "IBU-200", quantity: 300, unitPrice: 18.75, totalPrice: 5625.00 },
  { id: "3", productName: "Paracetamol 500mg", sku: "PAR-500", quantity: 200, unitPrice: 19.25, totalPrice: 3850.00 },
];

export default function OrdersPage() {
  const [selectedTab, setSelectedTab] = useState("all");
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedOrder, setSelectedOrder] = useState<typeof orders[0] | null>(null);

  const getStatusVariant = (status: string) => {
    switch (status) {
      case "PENDING": return "secondary";
      case "CONFIRMED": return "default";
      case "PROCESSING": return "outline";
      case "SHIPPED": return "default";
      case "DELIVERED": return "default";
      case "CANCELLED": return "destructive";
      default: return "outline";
    }
  };

  const getTypeVariant = (type: string) => {
    return "default";
  };

  const filteredOrders = orders.filter(order => {
    const matchesSearch = order.orderNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (order.supplier?.toLowerCase().includes(searchTerm.toLowerCase())) ||
                         (order.customer?.toLowerCase().includes(searchTerm.toLowerCase()));
    
    if (selectedTab === "all") return matchesSearch;
    if (selectedTab === "dispatch") return matchesSearch && order.type === "DISPATCH";
    return matchesSearch;
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Gestión de Órdenes</h1>
          <p className="text-muted-foreground">Administra órdenes de despacho de mercadería</p>
        </div>
        <Button>
          <PlusCircle className="h-4 w-4 mr-2" />
          Nueva Orden
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-2">
              <Package className="h-4 w-4 text-muted-foreground" />
              <span className="text-sm font-medium">Total Órdenes</span>
            </div>
            <div className="text-2xl font-bold mt-2">{orders.length}</div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-2">
              <Calendar className="h-4 w-4 text-orange-500" />
              <span className="text-sm font-medium">Pendientes</span>
            </div>
            <div className="text-2xl font-bold mt-2 text-orange-600">
              {orders.filter(o => o.status === "PENDING").length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-2">
              <Package className="h-4 w-4 text-blue-500" />
              <span className="text-sm font-medium">En Proceso</span>
            </div>
            <div className="text-2xl font-bold mt-2 text-blue-600">
              {orders.filter(o => o.status === "PROCESSING").length}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-2">
              <DollarSign className="h-4 w-4 text-green-500" />
              <span className="text-sm font-medium">Valor Total</span>
            </div>
            <div className="text-2xl font-bold mt-2 text-green-600">
              ${orders.reduce((sum, order) => sum + order.totalAmount, 0).toLocaleString()}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters and Search */}
      <div className="flex items-center justify-between">
        <Tabs value={selectedTab} onValueChange={setSelectedTab}>
          <TabsList>
            <TabsTrigger value="all">Todas</TabsTrigger>
            <TabsTrigger value="dispatch">Despachos</TabsTrigger>
          </TabsList>
        </Tabs>
        
        <div className="flex items-center space-x-2">
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Buscar órdenes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-8 w-[300px]"
            />
          </div>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" size="sm">
                <ListFilter className="h-4 w-4 mr-2" />
                Filtros
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Filtrar por estado</DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuCheckboxItem checked>Pendiente</DropdownMenuCheckboxItem>
              <DropdownMenuCheckboxItem checked>Confirmado</DropdownMenuCheckboxItem>
              <DropdownMenuCheckboxItem checked>En Proceso</DropdownMenuCheckboxItem>
              <DropdownMenuCheckboxItem checked>Enviado</DropdownMenuCheckboxItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>

      {/* Orders Table */}
      <Card>
        <CardContent className="p-0">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Número de Orden</TableHead>
                <TableHead>Tipo</TableHead>
                <TableHead>Cliente/Proveedor</TableHead>
                <TableHead>Fecha</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Monto</TableHead>
                <TableHead className="text-center">Items</TableHead>
                <TableHead className="w-[50px]"></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredOrders.map((order) => (
                <TableRow key={order.id}>
                  <TableCell className="font-medium">{order.orderNumber}</TableCell>
                  <TableCell>
                    <Badge variant={getTypeVariant(order.type)}>
                      Despacho
                    </Badge>
                  </TableCell>
                  <TableCell>{order.supplier || order.customer}</TableCell>
                  <TableCell>{new Date(order.orderDate).toLocaleDateString()}</TableCell>
                  <TableCell>
                    <Badge variant={getStatusVariant(order.status)}>
                      {order.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right font-medium">
                    ${order.totalAmount.toLocaleString()}
                  </TableCell>
                  <TableCell className="text-center">{order.itemsCount}</TableCell>
                  <TableCell>
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon">
                          <MoreHorizontal className="h-4 w-4" />
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem onClick={() => setSelectedOrder(order)}>
                          <Eye className="h-4 w-4 mr-2" />
                          Ver Detalles
                        </DropdownMenuItem>
                        <DropdownMenuItem>
                          <Edit className="h-4 w-4 mr-2" />
                          Editar
                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem className="text-red-600">
                          <Trash2 className="h-4 w-4 mr-2" />
                          Eliminar
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Order Details Dialog */}
      <Dialog open={!!selectedOrder} onOpenChange={() => setSelectedOrder(null)}>
        <DialogContent className="max-w-4xl">
          <DialogHeader>
            <DialogTitle>Detalles de la Orden</DialogTitle>
            <DialogDescription>
              {selectedOrder?.orderNumber} - Orden de Despacho
            </DialogDescription>
          </DialogHeader>
          
          {selectedOrder && (
            <div className="space-y-6">
              {/* Order Info */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Estado</label>
                  <div className="mt-1">
                    <Badge variant={getStatusVariant(selectedOrder.status)}>
                      {selectedOrder.status}
                    </Badge>
                  </div>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Fecha de Orden</label>
                  <div className="mt-1">{new Date(selectedOrder.orderDate).toLocaleDateString()}</div>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Fecha Esperada</label>
                  <div className="mt-1">{new Date(selectedOrder.expectedDate).toLocaleDateString()}</div>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Monto Total</label>
                  <div className="mt-1 font-medium">${selectedOrder.totalAmount.toLocaleString()}</div>
                </div>
              </div>

              {/* Order Items */}
              <div>
                <h3 className="text-lg font-medium mb-4">Items de la Orden</h3>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Producto</TableHead>
                      <TableHead>SKU</TableHead>
                      <TableHead className="text-right">Cantidad</TableHead>
                      <TableHead className="text-right">Precio Unit.</TableHead>
                      <TableHead className="text-right">Total</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {orderItems.map((item) => (
                      <TableRow key={item.id}>
                        <TableCell className="font-medium">{item.productName}</TableCell>
                        <TableCell>{item.sku}</TableCell>
                        <TableCell className="text-right">{item.quantity}</TableCell>
                        <TableCell className="text-right">${item.unitPrice}</TableCell>
                        <TableCell className="text-right font-medium">${item.totalPrice.toLocaleString()}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
