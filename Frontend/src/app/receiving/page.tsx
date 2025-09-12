
'use client';

import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { PackageCheck, PackageX, ArrowDownToLine, ArrowUpFromLine } from 'lucide-react';
import type { StockMovement, Product, Location, Supplier, Customer } from '@/types';

// Mock data - En producción esto vendría de tu API
const mockProducts = [
  { id: '1', name: 'Aspirin 100mg', sku: 'ASP-100-TAB' },
  { id: '2', name: 'Ibuprofen 200mg', sku: 'IBU-200-TAB' },
  { id: '3', name: 'Paracetamol 500mg', sku: 'PAR-500-CAP' },
  { id: '4', name: 'Amoxicillin 250mg', sku: 'AMX-250-SUS' },
];

const mockLocations = [
  { id: '1', code: 'A1-01-B', name: 'Zona A - Pasillo 1 - Estante B' },
  { id: '2', code: 'A1-02-C', name: 'Zona A - Pasillo 1 - Estante C' },
  { id: '3', code: 'B2-04-A', name: 'Zona B - Pasillo 2 - Estante A' },
  { id: '4', code: 'C3-01-D', name: 'Zona C - Pasillo 3 - Estante D' },
];

const mockSuppliers = [
  { id: '1', name: 'Laboratorios ABC', code: 'LAB-ABC' },
  { id: '2', name: 'Farmacéutica XYZ', code: 'FARM-XYZ' },
  { id: '3', name: 'Distribuidora 123', code: 'DIST-123' },
];

const mockCustomers = [
  { id: '1', name: 'Farmacia Central', code: 'FARM-001' },
  { id: '2', name: 'Hospital San Juan', code: 'HOSP-002' },
  { id: '3', name: 'Clínica Norte', code: 'CLIN-003' },
];

// Mock recent movements data
const mockRecentMovements = [
  {
    id: '1',
    type: 'IN',
    product: 'Aspirin 100mg',
    quantity: 500,
    location: 'A1-01-B',
    reference: 'PO-12345',
    performedBy: 'Juan Pérez',
    date: '2025-01-11 14:30',
  },
  {
    id: '2',
    type: 'OUT',
    product: 'Ibuprofen 200mg',
    quantity: 200,
    location: 'A1-02-C',
    reference: 'SO-67890',
    performedBy: 'María García',
    date: '2025-01-11 13:15',
  },
  {
    id: '3',
    type: 'IN',
    product: 'Paracetamol 500mg',
    quantity: 300,
    location: 'B2-04-A',
    reference: 'PO-54321',
    performedBy: 'Carlos López',
    date: '2025-01-11 11:45',
  },
];

export default function InventoryMovementsPage() {
  const { toast } = useToast();
  const [activeTab, setActiveTab] = useState<'IN' | 'OUT'>('IN');

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    
    const movementData = {
      movementType: activeTab as 'IN' | 'OUT',
      productId: formData.get('productId') as string,
      quantity: parseInt(formData.get('quantity') as string),
      locationId: formData.get('locationId') as string,
      supplierId: activeTab === 'IN' ? formData.get('supplierId') as string : undefined,
      customerId: activeTab === 'OUT' ? formData.get('customerId') as string : undefined,
      referenceNumber: formData.get('referenceNumber') as string,
      reason: formData.get('notes') as string || (activeTab === 'IN' ? 'Recepción de mercadería' : 'Despacho de mercadería'),
      performedBy: formData.get('receivedBy') as string,
    };

    console.log('Movement data:', movementData);

    toast({
      title: activeTab === 'IN' ? "Entrada Registrada" : "Salida Registrada",
      description: `El movimiento de ${activeTab === 'IN' ? 'entrada' : 'salida'} ha sido registrado exitosamente.`,
    });

    // Reset form
    event.currentTarget.reset();
  };

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Movimientos de Inventario</h1>
      </div>

      <Tabs value={activeTab} onValueChange={(value) => setActiveTab(value as 'IN' | 'OUT')} className="w-full">
        <TabsList className="grid w-full grid-cols-2 mb-6">
          <TabsTrigger value="IN" className="flex items-center gap-2">
            <ArrowDownToLine className="w-4 h-4" />
            Entrada de Mercadería
          </TabsTrigger>
          <TabsTrigger value="OUT" className="flex items-center gap-2">
            <ArrowUpFromLine className="w-4 h-4" />
            Salida de Mercadería
          </TabsTrigger>
        </TabsList>

        <TabsContent value="IN">
          <Card className="w-full max-w-4xl mx-auto">
            <CardHeader>
              <div className="flex items-center gap-2">
                <PackageCheck className="w-6 h-6 text-green-600" />
                <div>
                  <CardTitle>Registrar Entrada de Mercadería</CardTitle>
                  <CardDescription>
                    Registre los productos que ingresan al almacén desde proveedores.
                  </CardDescription>
                </div>
              </div>
            </CardHeader>
            <form onSubmit={handleSubmit}>
              <CardContent>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-2">
                    <Label htmlFor="referenceNumber">Número de Referencia</Label>
                    <Input 
                      id="referenceNumber" 
                      name="referenceNumber"
                      placeholder="Ej: PO-12345, GR-67890" 
                      required 
                    />
                  </div>
                  
                  <div className="space-y-2">
                    <Label htmlFor="supplierId">Proveedor</Label>
                    <Select name="supplierId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un proveedor" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockSuppliers.map((supplier) => (
                          <SelectItem key={supplier.id} value={supplier.id}>
                            {supplier.name} ({supplier.code})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="productId">Producto</Label>
                    <Select name="productId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un producto" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockProducts.map((product) => (
                          <SelectItem key={product.id} value={product.id}>
                            {product.name} ({product.sku})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="quantity">Cantidad</Label>
                    <Input 
                      id="quantity" 
                      name="quantity"
                      type="number" 
                      min="1"
                      placeholder="0" 
                      required 
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="locationId">Ubicación</Label>
                    <Select name="locationId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione una ubicación" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockLocations.map((location) => (
                          <SelectItem key={location.id} value={location.id}>
                            {location.code} - {location.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="receivedBy">Recibido por</Label>
                    <Input 
                      id="receivedBy" 
                      name="receivedBy"
                      placeholder="Nombre del empleado" 
                      required 
                    />
                  </div>

                  <div className="md:col-span-2 space-y-2">
                    <Label htmlFor="notes">Notas Adicionales</Label>
                    <Textarea 
                      id="notes" 
                      name="notes"
                      placeholder="Observaciones sobre la recepción, estado del producto, etc..." 
                    />
                  </div>
                </div>
              </CardContent>
              <div className="px-6 pb-6">
                <Button type="submit" className="w-full md:w-auto ml-auto flex">
                  <PackageCheck className="w-4 h-4 mr-2" />
                  Registrar Entrada
                </Button>
              </div>
            </form>
          </Card>
        </TabsContent>

        <TabsContent value="OUT">
          <Card className="w-full max-w-4xl mx-auto">
            <CardHeader>
              <div className="flex items-center gap-2">
                <PackageX className="w-6 h-6 text-red-600" />
                <div>
                  <CardTitle>Registrar Salida de Mercadería</CardTitle>
                  <CardDescription>
                    Registre los productos que salen del almacén hacia clientes.
                  </CardDescription>
                </div>
              </div>
            </CardHeader>
            <form onSubmit={handleSubmit}>
              <CardContent>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-2">
                    <Label htmlFor="referenceNumber">Número de Referencia</Label>
                    <Input 
                      id="referenceNumber" 
                      name="referenceNumber"
                      placeholder="Ej: SO-12345, GD-67890" 
                      required 
                    />
                  </div>
                  
                  <div className="space-y-2">
                    <Label htmlFor="customerId">Cliente</Label>
                    <Select name="customerId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un cliente" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockCustomers.map((customer) => (
                          <SelectItem key={customer.id} value={customer.id}>
                            {customer.name} ({customer.code})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="productId">Producto</Label>
                    <Select name="productId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un producto" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockProducts.map((product) => (
                          <SelectItem key={product.id} value={product.id}>
                            {product.name} ({product.sku})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="quantity">Cantidad</Label>
                    <Input 
                      id="quantity" 
                      name="quantity"
                      type="number" 
                      min="1"
                      placeholder="0" 
                      required 
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="locationId">Ubicación de Origen</Label>
                    <Select name="locationId" required>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione la ubicación" />
                      </SelectTrigger>
                      <SelectContent>
                        {mockLocations.map((location) => (
                          <SelectItem key={location.id} value={location.id}>
                            {location.code} - {location.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="receivedBy">Despachado por</Label>
                    <Input 
                      id="receivedBy" 
                      name="receivedBy"
                      placeholder="Nombre del empleado" 
                      required 
                    />
                  </div>

                  <div className="md:col-span-2 space-y-2">
                    <Label htmlFor="notes">Notas Adicionales</Label>
                    <Textarea 
                      id="notes" 
                      name="notes"
                      placeholder="Observaciones sobre el despacho, instrucciones especiales, etc..." 
                    />
                  </div>
                </div>
              </CardContent>
              <div className="px-6 pb-6">
                <Button type="submit" className="w-full md:w-auto ml-auto flex">
                  <PackageX className="w-4 h-4 mr-2" />
                  Registrar Salida
                </Button>
              </div>
            </form>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Recent Movements Section */}
      <Card className="w-full max-w-4xl mx-auto mt-8">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <PackageCheck className="w-5 h-5" />
            Movimientos Recientes
          </CardTitle>
          <CardDescription>
            Últimos movimientos de inventario registrados en el sistema.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {mockRecentMovements.map((movement) => (
              <div
                key={movement.id}
                className="flex items-center justify-between p-4 border rounded-lg hover:bg-muted/50 transition-colors"
              >
                <div className="flex items-center gap-4">
                  <div className={`p-2 rounded-full ${
                    movement.type === 'IN' 
                      ? 'bg-green-100 text-green-600' 
                      : 'bg-red-100 text-red-600'
                  }`}>
                    {movement.type === 'IN' ? (
                      <ArrowDownToLine className="w-4 h-4" />
                    ) : (
                      <ArrowUpFromLine className="w-4 h-4" />
                    )}
                  </div>
                  <div>
                    <p className="font-medium">{movement.product}</p>
                    <p className="text-sm text-muted-foreground">
                      {movement.type === 'IN' ? 'Entrada' : 'Salida'} • {movement.location} • {movement.reference}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="font-medium">
                    {movement.type === 'IN' ? '+' : '-'}{movement.quantity}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    {movement.performedBy}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {movement.date}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
