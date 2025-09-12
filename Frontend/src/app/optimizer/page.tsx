'use client';

import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { 
  Zap, 
  BarChart3, 
  MapPin, 
  TrendingUp, 
  AlertTriangle,
  CheckCircle,
  RefreshCw,
  Settings,
  Lightbulb,
  Target
} from 'lucide-react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useToast } from "@/hooks/use-toast";

// Mock data
const optimizationMetrics = {
  spaceUtilization: 78,
  pickingEfficiency: 85,
  storageCapacity: 92,
  accessibilityScore: 73,
};

const recommendations = [
  {
    id: "1",
    type: "HIGH",
    title: "Reubicación de Productos de Alta Rotación",
    description: "Mover productos de alta rotación más cerca de las áreas de picking para reducir tiempo de desplazamiento.",
    impact: "Reducción del 15% en tiempo de picking",
    effort: "Medio",
    savings: "$2,400/mes",
    status: "pending",
  },
  {
    id: "2",
    type: "MEDIUM",
    title: "Optimización de Altura de Estanterías",
    description: "Reorganizar productos por peso y frecuencia de acceso en diferentes niveles de altura.",
    impact: "Mejora del 20% en ergonomía",
    effort: "Alto",
    savings: "$1,800/mes",
    status: "pending",
  },
  {
    id: "3",
    type: "LOW",
    title: "Consolidación de Espacios Vacíos",
    description: "Identificar y consolidar espacios subutilizados para maximizar la capacidad de almacenamiento.",
    impact: "Aumento del 12% en capacidad",
    effort: "Bajo",
    savings: "$900/mes",
    status: "implemented",
  },
  {
    id: "4",
    type: "HIGH",
    title: "Implementación de Zonas ABC",
    description: "Crear zonas basadas en la clasificación ABC de productos para optimizar el flujo de trabajo.",
    impact: "Reducción del 25% en distancia recorrida",
    effort: "Alto",
    savings: "$3,200/mes",
    status: "in-progress",
  },
];

const warehouseZones = [
  {
    zone: "A - Alta Rotación",
    utilization: 95,
    products: 245,
    efficiency: 92,
    status: "optimal",
  },
  {
    zone: "B - Media Rotación",
    utilization: 78,
    products: 456,
    efficiency: 85,
    status: "good",
  },
  {
    zone: "C - Baja Rotación",
    utilization: 45,
    products: 789,
    efficiency: 65,
    status: "needs-attention",
  },
  {
    zone: "D - Productos Especiales",
    utilization: 88,
    products: 123,
    efficiency: 90,
    status: "optimal",
  },
];

const optimizationHistory = [
  {
    date: "2024-01-15",
    action: "Reubicación Zona A",
    improvement: "+12% eficiencia",
    status: "completed",
  },
  {
    date: "2024-01-10",
    action: "Análisis de Layout",
    improvement: "Identificadas 8 oportunidades",
    status: "completed",
  },
  {
    date: "2024-01-05",
    action: "Optimización ABC",
    improvement: "+18% en picking",
    status: "completed",
  },
];

export default function OptimizerPage() {
  const [isOptimizing, setIsOptimizing] = useState(false);
  const { toast } = useToast();

  const handleRunOptimization = () => {
    setIsOptimizing(true);
    // Simular proceso de optimización
    setTimeout(() => {
      setIsOptimizing(false);
      toast({
        title: "Optimización Completada",
        description: "Se han generado nuevas recomendaciones para mejorar el layout del almacén.",
      });
    }, 3000);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "optimal": return "text-green-600";
      case "good": return "text-blue-600";
      case "needs-attention": return "text-orange-600";
      default: return "text-gray-600";
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "pending": return <Badge variant="secondary">Pendiente</Badge>;
      case "in-progress": return <Badge variant="outline">En Progreso</Badge>;
      case "implemented": return <Badge variant="default">Implementado</Badge>;
      default: return <Badge variant="outline">Desconocido</Badge>;
    }
  };

  const getPriorityColor = (type: string) => {
    switch (type) {
      case "HIGH": return "border-red-200 bg-red-50";
      case "MEDIUM": return "border-yellow-200 bg-yellow-50";
      case "LOW": return "border-green-200 bg-green-50";
      default: return "border-gray-200 bg-gray-50";
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Optimizador de Espacios</h1>
          <p className="text-muted-foreground">Optimiza el layout y la eficiencia del almacén con IA</p>
        </div>
        <Button 
          onClick={handleRunOptimization} 
          disabled={isOptimizing}
          className="gap-2"
        >
          {isOptimizing ? (
            <>
              <RefreshCw className="h-4 w-4 animate-spin" />
              Optimizando...
            </>
          ) : (
            <>
              <Zap className="h-4 w-4" />
              Ejecutar Optimización
            </>
          )}
        </Button>
      </div>

      {/* Metrics Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Utilización del Espacio</p>
                <p className="text-2xl font-bold">{optimizationMetrics.spaceUtilization}%</p>
              </div>
              <BarChart3 className="h-8 w-8 text-blue-500" />
            </div>
            <Progress value={optimizationMetrics.spaceUtilization} className="mt-3" />
          </CardContent>
        </Card>
        
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Eficiencia de Picking</p>
                <p className="text-2xl font-bold">{optimizationMetrics.pickingEfficiency}%</p>
              </div>
              <Target className="h-8 w-8 text-green-500" />
            </div>
            <Progress value={optimizationMetrics.pickingEfficiency} className="mt-3" />
          </CardContent>
        </Card>
        
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Capacidad de Almacenamiento</p>
                <p className="text-2xl font-bold">{optimizationMetrics.storageCapacity}%</p>
              </div>
              <MapPin className="h-8 w-8 text-purple-500" />
            </div>
            <Progress value={optimizationMetrics.storageCapacity} className="mt-3" />
          </CardContent>
        </Card>
        
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Score de Accesibilidad</p>
                <p className="text-2xl font-bold">{optimizationMetrics.accessibilityScore}%</p>
              </div>
              <TrendingUp className="h-8 w-8 text-orange-500" />
            </div>
            <Progress value={optimizationMetrics.accessibilityScore} className="mt-3" />
          </CardContent>
        </Card>
      </div>

      {/* Main Content */}
      <Tabs defaultValue="recommendations" className="space-y-6">
        <TabsList>
          <TabsTrigger value="recommendations">Recomendaciones</TabsTrigger>
          <TabsTrigger value="zones">Análisis por Zonas</TabsTrigger>
          <TabsTrigger value="history">Historial</TabsTrigger>
        </TabsList>

        <TabsContent value="recommendations" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Lightbulb className="h-5 w-5" />
                Recomendaciones de Optimización
              </CardTitle>
              <CardDescription>
                Sugerencias basadas en IA para mejorar la eficiencia del almacén
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {recommendations.map((rec) => (
                  <div key={rec.id} className={`p-4 rounded-lg border-2 ${getPriorityColor(rec.type)}`}>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <h3 className="font-semibold">{rec.title}</h3>
                          <Badge variant={rec.type === "HIGH" ? "destructive" : rec.type === "MEDIUM" ? "secondary" : "outline"}>
                            {rec.type === "HIGH" ? "Alta Prioridad" : rec.type === "MEDIUM" ? "Media Prioridad" : "Baja Prioridad"}
                          </Badge>
                          {getStatusBadge(rec.status)}
                        </div>
                        <p className="text-sm text-muted-foreground mb-3">{rec.description}</p>
                        <div className="grid grid-cols-3 gap-4 text-sm">
                          <div>
                            <span className="font-medium">Impacto:</span>
                            <p className="text-muted-foreground">{rec.impact}</p>
                          </div>
                          <div>
                            <span className="font-medium">Esfuerzo:</span>
                            <p className="text-muted-foreground">{rec.effort}</p>
                          </div>
                          <div>
                            <span className="font-medium">Ahorro Estimado:</span>
                            <p className="text-green-600 font-medium">{rec.savings}</p>
                          </div>
                        </div>
                      </div>
                      <div className="flex gap-2 ml-4">
                        {rec.status === "pending" && (
                          <>
                            <Button size="sm" variant="outline">
                              Ver Detalles
                            </Button>
                            <Button size="sm">
                              Implementar
                            </Button>
                          </>
                        )}
                        {rec.status === "in-progress" && (
                          <Button size="sm" variant="outline">
                            Ver Progreso
                          </Button>
                        )}
                        {rec.status === "implemented" && (
                          <div className="flex items-center text-green-600">
                            <CheckCircle className="h-4 w-4 mr-1" />
                            <span className="text-sm">Completado</span>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="zones" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Análisis por Zonas del Almacén</CardTitle>
              <CardDescription>
                Rendimiento y utilización de cada zona del almacén
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {warehouseZones.map((zone, index) => (
                  <div key={index} className="p-4 border rounded-lg">
                    <div className="flex items-center justify-between mb-3">
                      <h3 className="font-semibold">{zone.zone}</h3>
                      <div className={`flex items-center gap-1 ${getStatusColor(zone.status)}`}>
                        {zone.status === "optimal" && <CheckCircle className="h-4 w-4" />}
                        {zone.status === "needs-attention" && <AlertTriangle className="h-4 w-4" />}
                        <span className="text-sm font-medium capitalize">
                          {zone.status === "optimal" ? "Óptimo" : 
                           zone.status === "good" ? "Bueno" : "Necesita Atención"}
                        </span>
                      </div>
                    </div>
                    
                    <div className="grid grid-cols-3 gap-6">
                      <div>
                        <p className="text-sm text-muted-foreground">Utilización</p>
                        <div className="flex items-center gap-2 mt-1">
                          <Progress value={zone.utilization} className="flex-1" />
                          <span className="text-sm font-medium">{zone.utilization}%</span>
                        </div>
                      </div>
                      
                      <div>
                        <p className="text-sm text-muted-foreground">Productos</p>
                        <p className="text-lg font-semibold">{zone.products}</p>
                      </div>
                      
                      <div>
                        <p className="text-sm text-muted-foreground">Eficiencia</p>
                        <div className="flex items-center gap-2 mt-1">
                          <Progress value={zone.efficiency} className="flex-1" />
                          <span className="text-sm font-medium">{zone.efficiency}%</span>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="history" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Historial de Optimizaciones</CardTitle>
              <CardDescription>
                Registro de optimizaciones anteriores y sus resultados
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {optimizationHistory.map((item, index) => (
                  <div key={index} className="flex items-center justify-between p-4 border rounded-lg">
                    <div className="flex items-center gap-4">
                      <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                      <div>
                        <p className="font-medium">{item.action}</p>
                        <p className="text-sm text-muted-foreground">{item.date}</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium text-green-600">{item.improvement}</p>
                      <Badge variant="outline" className="mt-1">
                        {item.status === "completed" ? "Completado" : "En Progreso"}
                      </Badge>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
