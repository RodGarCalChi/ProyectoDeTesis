"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";

export default function LoginPage() {
  const router = useRouter();
  const { login, isLoading } = useAuth();
  const [formData, setFormData] = React.useState({
    email: "",
    password: "",
    rol: ""
  });
  const [error, setError] = React.useState("");

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Limpiar error cuando el usuario empiece a escribir
    if (error) setError("");
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    // Validar que se hayan ingresado todos los campos
    if (!formData.email || !formData.password || !formData.rol) {
      setError("Todos los campos son obligatorios");
      return;
    }

    // Mapeo de roles a datos de usuario y pantallas
    const rolMap: { [key: string]: { firstName: string; redirect: string } } = {
      "Recepcion": { firstName: "María", redirect: "/movimientos" },
      "Jefe_Ejecutivas": { firstName: "Patricia", redirect: "/ordenes" },
      "Control": { firstName: "Ana", redirect: "/control" },
      "Inventario": { firstName: "Luis", redirect: "/dashboard" },
      "Operaciones": { firstName: "Juan", redirect: "/dashboard" },
      "Despacho": { firstName: "Carlos", redirect: "/dashboard" },
      "DirectorTecnico": { firstName: "Director", redirect: "/dashboard" },
      "Administrador": { firstName: "Admin", redirect: "/dashboard" },
      "Cliente": { firstName: "Pedro", redirect: "/dashboard" }
    };

    const userData = rolMap[formData.rol];

    if (!userData) {
      setError("Rol seleccionado inválido.");
      return;
    }

    try {
      // Simular usuario autenticado sin llamar al backend
      const mockUser = {
        id: Math.floor(Math.random() * 1000),
        username: formData.email.split('@')[0],
        email: formData.email,
        role: formData.rol,
        firstName: userData.firstName
      };

      // Guardar datos de autenticación simulados
      const mockToken = `mock_token_${formData.rol}_${Date.now()}`;
      localStorage.setItem('token', mockToken);
      localStorage.setItem('user', JSON.stringify(mockUser));

      // Redirigir según el rol
      router.push(userData.redirect);

    } catch (error) {
      setError('Error interno. Intente nuevamente.');
      console.error('Login error:', error);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md mx-4">
        <div className="bg-white rounded-lg border-2 border-gray-400 p-8 shadow-sm">
          {/* Header */}
          <div className="text-center mb-8">
            <div className="flex items-center justify-center gap-3 mb-6">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <svg className="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                </svg>
              </div>
              <h1 className="text-4xl font-bold text-gray-900">PharmaFlow</h1>
            </div>
            <h2 className="text-2xl font-semibold text-gray-900 mb-2">
              Iniciar Sesión
            </h2>
            <p className="text-gray-500 text-sm">
              Ingresa tus credenciales para acceder a tu cuenta.
            </p>
          </div>

          <form onSubmit={handleLogin} className="space-y-6">
            {/* Error Message */}
            {error && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-3">
                <p className="text-sm text-red-600">{error}</p>
              </div>
            )}

            {/* Email Field */}
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="m@example.com"
                required
                value={formData.email}
                onChange={handleInputChange}
                className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
              />
            </div>

            {/* Password Field */}
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Contraseña
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleInputChange}
                className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
              />
            </div>

            {/* Selector de Rol */}
            <div>
              <label
                htmlFor="rol"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Tipo de Usuario
              </label>
              <select
                id="rol"
                name="rol"
                required
                value={formData.rol}
                onChange={handleInputChange}
                className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
              >
                <option value="">Seleccione su rol</option>
                <option value="Recepcion">👥 Recepción - María</option>
                <option value="Jefe_Ejecutivas">👔 Jefe de Ejecutivas - Patricia</option>
                <option value="Control">🔍 Control de Calidad - Ana</option>
                <option value="Inventario">📦 Inventario - Luis</option>
                <option value="Operaciones">⚙️ Operaciones - Juan</option>
                <option value="Despacho">🚚 Despacho - Carlos</option>
                <option value="DirectorTecnico">👨‍⚕️ Director Técnico</option>
                <option value="Administrador">🔧 Administrador</option>
                <option value="Cliente">👤 Cliente - Pedro</option>
              </select>
              {/* Flecha del select */}
              <div className="absolute right-3 top-1/2 transform -translate-y-1/2 pointer-events-none">
                <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </div>
            </div>

            {/* Info sobre el sistema */}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
              <p className="text-xs text-blue-600 mb-2">
                <strong>Sistema de Demostración:</strong>
              </p>
              <p className="text-xs text-blue-600 mb-1">
                • Puede usar cualquier email y contraseña
              </p>
              <p className="text-xs text-blue-600 mb-1">
                • Seleccione el tipo de usuario para acceder a diferentes pantallas
              </p>
              <p className="text-xs text-blue-600">
                • Cada rol tiene acceso a funcionalidades específicas del sistema
              </p>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-blue-500 hover:bg-blue-600 disabled:bg-blue-300 text-white font-medium py-3 px-4 rounded-lg transition-colors duration-200 flex items-center justify-center gap-2"
            >
              {isLoading ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                  Iniciando sesión...
                </>
              ) : (
                <>
                  Iniciar Sesión
                  <div className="w-6 h-6 bg-white rounded-full flex items-center justify-center">
                    <svg
                      className="w-3 h-3 text-blue-500"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path
                        fillRule="evenodd"
                        d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z"
                        clipRule="evenodd"
                      />
                    </svg>
                  </div>
                </>
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}