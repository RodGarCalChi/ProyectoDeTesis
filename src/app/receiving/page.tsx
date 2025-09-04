
'use client';

import React from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/hooks/use-toast";

export default function ReceivingPage() {
  const { toast } = useToast();

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // Here you would typically handle form submission, e.g., send data to an API
    console.log("Form submitted");
    toast({
      title: "Recepción Registrada",
      description: "El pedido ha sido registrado exitosamente.",
    });
    // Optionally, reset the form
    event.currentTarget.reset();
  };

  return (
    <div className="container mx-auto p-4">
       <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Registrar Recepción de Mercadería</h1>
      </div>
      <Card className="w-full max-w-2xl mx-auto">
        <CardHeader>
          <CardTitle>Detalles del Pedido</CardTitle>
          <CardDescription>Ingrese la información del pedido que está recibiendo.</CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <Label htmlFor="order-id">Número de Orden / Guía</Label>
                <Input id="order-id" placeholder="Ej: ORD-12345" required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="client">Cliente</Label>
                <Select required>
                  <SelectTrigger id="client">
                    <SelectValue placeholder="Seleccione un cliente" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="client-a">Cliente A</SelectItem>
                    <SelectItem value="client-b">Cliente B</SelectItem>
                    <SelectItem value="client-c">Cliente C</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="reception-date">Fecha de Recepción</Label>
                <Input id="reception-date" type="date" required defaultValue={new Date().toISOString().substring(0, 10)} />
              </div>
               <div className="space-y-2">
                <Label htmlFor="received-by">Recibido por</Label>
                <Input id="received-by" placeholder="Nombre del empleado" required />
              </div>
              <div className="md:col-span-2 space-y-2">
                <Label htmlFor="notes">Notas Adicionales</Label>
                <Textarea id="notes" placeholder="Cualquier observación sobre la recepción..." />
              </div>
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full md:w-auto ml-auto">Registrar Pedido</Button>
          </CardFooter>
        </form>
      </Card>
    </div>
  );
}
