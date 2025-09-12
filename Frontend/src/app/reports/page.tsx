'use client';

import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { 
  BarChart3, 
  Download, 
  Calendar, 
  FileText, 
  TrendingUp, 
  Package,
  AlertTriangle,
  DollarSign,
  Users,
  Clock,
  Filter
} from 'lucide-react';
import { 
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { 
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line, PieChart, Pie, Cell } from 'recharts';
import { useToast } from "@/hooks/use-toast";

// Mock data
const reportTemplates = [
  {
    id: "inventory",
    name: "Reporte de Inventario",
    description: "Estado actual del inventario por producto y ubicación",
    icon: Package,
    category: "Inventario",
    lastGenerated: "2024-01-15",
  },
  {
    id: "sales",
    name: "Reporte de Ventas",
    description: "Análisis de ventas por período y cliente",
    icon: TrendingUp,
    category: "Ventas",
    lastGenerated: "2024-01-14",
  },
  {
    id: "expiration",
    name: "Productos por Vencer",
    description: "Productos próximos a vencer en los próximos 30 días",
    icon: AlertTriangle,
    category: "Inventario",
    lastGenerated: "2024-01-15",
  },
  {
    id: "movements",
    name: "Movimientos de Stock",
    description: "Historial de movimientos de entrada y salida",
    icon: BarChart3,
    category: "Movimientos",
    lastGenerated: "2024-01-13",
  },
  {
    id: "financial",
    name: "Reporte Financiero",
    description: "Análisis financiero de compras y ventas",
    icon: DollarSign,
    category: "Financiero",
    lastGenerated: "2024-01-12",
  },
  {
    id: "users",
    name: "Actividad de Usuarios",
    description: "Reporte de actividad y accesos de usuarios",
    icon: Users,
    category: "Sistema",
    lastGenerated: "2024-01-10",
  },
];

const recentReports = [
  {
    id: "1",
    name: "Inventario Enero 2024",
    type: "Inventario",
    generatedAt: "2024-01-15 14:30",
    generatedBy: "Juan Pérez",
    status: "Completado",
    size: "2.3 MB",
  },
  {
    id: "2",
    name: "Ventas Diciembre 2023",
    type: "Ventas",
    generatedAt: "2024-01-14 09:15",
    generatedBy: "María González",
    status: "Completado",
    size: "1.8 MB",
  },
  {
    id: "3",
    name: "Productos por Vencer",
    type: "Inventario",
    generatedAt: "2024-01-13 16:45",
    generatedBy: "Carlos Rodríguez",
    status: "Procesando",
    size: "0.5 MB",
  },
];

// Chart data
const salesData = [
  { month: 'Ene', ventas: 45000, compras: 32000 },
  { month: 'Feb', ventas: 52000, compras: 38000 },
  { month: 'Mar', ventas: 48000, compras: 35000 },
  { month: 'Abr', ventas: 61000, compras: 42000 },
  { month: 'May', ventas: 55000, compras: 40000 },
  { month: 'Jun', ventas: 67000, compras: 45000 },
];

const inventoryData = [
  { category: 'Analgésicos', value: 35, color: '#0088FE' },
  { category: 'Antibióticos', value: 25, color: '#00C49F' },
  { category: 'Antiinflamatorios', value: 20, color: '#FFBB28' },
  { category: 'Antihistamínicos', value: 15, color: '#FF8042' },
  { category: 'Otros', value: 5, color: '#8884D8' },
];

const movementsData = [
  { day: 'Lun', entradas: 120, salidas: 80 },
  { day: 'Mar', entradas: 150, salidas: 95 },
  { day: 'Mié', entradas: 180, salidas: 110 },
  { day: 'Jue', entradas: 90, salidas: 130 },
  { day: 'Vie', entradas: 200, salidas: 85 },
  { day: 'Sáb', entradas: 60, salidas: 45 },
  { day: 'Dom', entradas: 40, salidas: 30 },
];

export default function ReportsPage() {
  const [selectedReport, setSelectedReport] = useState<typeof reportTemplates[0] | null>(null);
  const [isGenerateDialogOpen, setIsGenerateDialogOpen] = useState(false);
  const [reportParams, setReportParams] = useState({
    dateFrom: "",
    dateTo: "",
    category: "",
    format: "PDF",
  });
  const { toast } = useToast();

  const handleGenerateReport = () => {
    console.log("Generating report:", selectedReport?.id, reportParams);
    toast({
      title: "Reporte Generado",
      description: `El reporte "${selectedReport?.name}" se está generando.`,
    });
    setIsGenerateDialogOpen(false);
    setSelectedReport(null);
  };

  const openGenerateDialog = (report: typeof reportTemplates[0]) => {
    setSelectedReport(report);
    setIsGenerateDialogOpen(true);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Reportes y Análisis</h1>
          <p className="text-muted-foreground">Genera reportes y visualiza métricas del sistema</p>
        </div>
      </div>

      {/* Analytics Dashboard */}
      <Tabs defaultValue="dashboard" className="space-y-6">
        <TabsList>
          <TabsTrigger value="dashboard">Dashboard</TabsTrigger>
          <TabsTrigger value="templates">Plantillas</TabsTrigger>
          <TabsTrigger value="history">Historial</TabsTrigger>
        </TabsList>

        <TabsContent value="dashboard" className="space-y-6">
          {/* Quick Stats */}
          <div className="grid gap-4 md:grid-cols-4">
            <Card>
              <CardContent className="p-6">
                <div className="flex items-center space-x-2">
                  <Package className="h-4 w-4 text-blue-500" />
                  <span className="text-sm font-medium">Total Productos</span>
                </div>
                <div className="text-2xl font-bold mt-2">1,247</div>
                <p className="text-xs text-muted-foreground">+12% vs mes anterior</p>
              </CardContent>
            </Card>
            <Card>
              <CardContent className="p-6">
                <div className="flex items-center space-x-2">
                  <TrendingUp className="h-4 w-4 text-green-500" />
                  <span className="text-sm font-medium">Ventas del Mes</span>
                </div>
                <div className="text-2xl font-bold mt-2">$67,000</div>
                <p className="text-xs text-muted-foreground">+8% vs mes anterior</p>
              </CardContent>
            </Card>
            <Card>
              <CardContent className="p-6">
                <div className="flex items-center space-x-2">
                  <AlertTriangle className="h-4 w-4 text-orange-500" />
                  <span className="text-sm font-medium">Stock Bajo</span>
                </div>
                <div className="text-2xl font-bold mt-2">23</div>
                <p className="text-xs text-muted-foreground">Productos críticos</p>
              </CardContent>
            </Card>
            <Card>
              <CardContent className="p-6">
                <div className="flex items-center space-x-2">
                  <Clock className="h-4 w-4 text-red-500" />
                  <span className="text-sm font-medium">Por Vencer</span>
                </div>
                <div className="text-2xl font-bold mt-2">8</div>
                <p className="text-xs text-muted-foreground">Próximos 30 días</p>
              </CardContent>
            </Card>
          </div>

          {/* Charts */}
          <div className="grid gap-6 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Ventas vs Compras</CardTitle>
                <CardDescription>Comparación mensual de ingresos y egresos</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={salesData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="month" />
                    <YAxis />
                    <Tooltip />
                    <Bar dataKey="ventas" fill="#0088FE" name="Ventas" />
                    <Bar dataKey="compras" fill="#FF8042" name="Compras" />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Distribución de Inventario</CardTitle>
                <CardDescription>Porcentaje por categoría de productos</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <PieChart>
                    <Pie
                      data={inventoryData}
                      cx="50%"
                      cy="50%"
                      outerRadius={80}
                      fill="#8884d8"
                      dataKey="value"
                      label={({ category, value }) => `${category}: ${value}%`}
                    >
                      {inventoryData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip />
                  </PieChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            <Card className="md:col-span-2">
              <CardHeader>
                <CardTitle>Movimientos Semanales</CardTitle>
                <CardDescription>Entradas y salidas de productos por día</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <LineChart data={movementsData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="day" />
                    <YAxis />
                    <Tooltip />
                    <Line type="monotone" dataKey="entradas" stroke="#00C49F" name="Entradas" />
                    <Line type="monotone" dataKey="salidas" stroke="#FF8042" name="Salidas" />
                  </LineChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="templates" className="space-y-6">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {reportTemplates.map((template) => (
              <Card key={template.id} className="cursor-pointer hover:shadow-md transition-shadow">
                <CardHeader>
                  <div className="flex items-center space-x-3">
                    <div className="p-2 bg-primary/10 rounded-lg">
                      <template.icon className="h-5 w-5 text-primary" />
                    </div>
                    <div>
                      <CardTitle className="text-lg">{template.name}</CardTitle>
                      <Badge variant="outline" className="mt-1">
                        {template.category}
                      </Badge>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <CardDescription className="mb-4">
                    {template.description}
                  </CardDescription>
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">
                      Último: {new Date(template.lastGenerated).toLocaleDateString()}
                    </span>
                    <Button size="sm" onClick={() => openGenerateDialog(template)}>
                      <FileText className="h-4 w-4 mr-2" />
                      Generar
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="history" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Reportes Recientes</CardTitle>
              <CardDescription>Historial de reportes generados</CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Nombre del Reporte</TableHead>
                    <TableHead>Tipo</TableHead>
                    <TableHead>Generado</TableHead>
                    <TableHead>Por</TableHead>
                    <TableHead>Estado</TableHead>
                    <TableHead>Tamaño</TableHead>
                    <TableHead className="w-[100px]">Acciones</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {recentReports.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell className="font-medium">{report.name}</TableCell>
                      <TableCell>{report.type}</TableCell>
                      <TableCell>{report.generatedAt}</TableCell>
                      <TableCell>{report.generatedBy}</TableCell>
                      <TableCell>
                        <Badge variant={report.status === "Completado" ? "default" : "secondary"}>
                          {report.status}
                        </Badge>
                      </TableCell>
                      <TableCell>{report.size}</TableCell>
                      <TableCell>
                        <Button size="sm" variant="outline">
                          <Download className="h-4 w-4" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Generate Report Dialog */}
      <Dialog open={isGenerateDialogOpen} onOpenChange={setIsGenerateDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Generar Reporte</DialogTitle>
            <DialogDescription>
              Configura los parámetros para generar "{selectedReport?.name}"
            </DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="dateFrom">Fecha Desde</Label>
                <Input
                  id="dateFrom"
                  type="date"
                  value={reportParams.dateFrom}
                  onChange={(e) => setReportParams({...reportParams, dateFrom: e.target.value})}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="dateTo">Fecha Hasta</Label>
                <Input
                  id="dateTo"
                  type="date"
                  value={reportParams.dateTo}
                  onChange={(e) => setReportParams({...reportParams, dateTo: e.target.value})}
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="category">Categoría (Opcional)</Label>
              <Select value={reportParams.category} onValueChange={(value) => setReportParams({...reportParams, category: value})}>
                <SelectTrigger>
                  <SelectValue placeholder="Todas las categorías" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="analgesicos">Analgésicos</SelectItem>
                  <SelectItem value="antibioticos">Antibióticos</SelectItem>
                  <SelectItem value="antiinflamatorios">Antiinflamatorios</SelectItem>
                  <SelectItem value="antihistaminicos">Antihistamínicos</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="format">Formato</Label>
              <Select value={reportParams.format} onValueChange={(value) => setReportParams({...reportParams, format: value})}>
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PDF">PDF</SelectItem>
                  <SelectItem value="Excel">Excel</SelectItem>
                  <SelectItem value="CSV">CSV</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsGenerateDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleGenerateReport}>
              <FileText className="h-4 w-4 mr-2" />
              Generar Reporte
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
