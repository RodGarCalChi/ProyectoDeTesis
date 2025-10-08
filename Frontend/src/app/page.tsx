"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";

export default function Home() {
  const router = useRouter();
  const { user, isLoading, isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isLoading) {
      if (!isAuthenticated) {
        // Si no está autenticado, redirigir al login
        router.push("/login");
      } else {
        // Si está autenticado, redirigir según el rol
        switch (user?.role) {
          case 'Recepcion':
            router.push('/movimientos');
            break;
          case 'AreaAdministrativa':
            router.push('/ordenes');
            break;
          case 'Calidad':
            router.push('/control');
            break;
          case 'Operaciones':
          case 'Despacho':
          case 'DirectorTecnico':
          case 'Cliente':
            router.push('/dashboard');
            break;
          default:
            router.push('/login');
            break;
        }
      }
    }
  }, [user, isLoading, isAuthenticated, router]);

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"></div>
        <p className="text-gray-600">
          {isLoading ? "Verificando sesión..." : "Redirigiendo..."}
        </p>
      </div>
    </div>
  );
}
