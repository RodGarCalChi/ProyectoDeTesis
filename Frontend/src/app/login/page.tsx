"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";

export default function LoginPage() {
  const router = useRouter();
  const { login } = useAuth();
  const [formData, setFormData] = React.useState({
    email: "",
    password: "",
    rol: ""
  });
  const [error, setError] = React.useState("");
  const [isLoading, setIsLoading] = React.useState(false);

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

    console.log('=== LOGIN DEBUG ===');
    console.log('formData:', formData);

    // Validar campos requeridos
    if (!formData.email || !formData.password) {
      setError("Email y contraseña son obligatorios");
      return;
    }

    // Validar que se seleccione un rol
    if (!formData.rol) {
      setError("Seleccione un tipo de usuario");
      return;
    }

    setIsLoading(true);

    try {
      // Usar la validación del API del backend
      const result = await login(formData.email, formData.password, formData.rol);

      if (result.success && result.user) {
        // Redirigir según el rol del usuario autenticado
        const rolRedirectMap: { [key: string]: string } = {
          "Cliente": "/dashboard",
          "Recepcion": "/recepcion-mercaderia",
          "Operaciones": "/dashboard",
          "Calidad": "/control",
          "Despacho": "/dashboard",
          "AreaAdministrativa": "/ordenes",
          "DirectorTecnico": "/dashboard"
        };

        const redirectPath = rolRedirectMap[result.user.role] || "/dashboard";
        router.push(redirectPath);
      } else {
        setError(result.message || 'Credenciales inválidas');
      }

    } catch (error) {
      console.error('Login error:', error);
      setError('Error de al registrar datos. Intente nuevamente.');
    } finally {
      setIsLoading(false);
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
              <div className="relative">
                <select
                  id="rol"
                  name="rol"
                  required
                  value={formData.rol}
                  onChange={handleInputChange}
                  className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
                >
                  <option value="">Seleccione su rol</option>
                  <option value="Recepcion">👥 Recepción</option>
                  <option value="Operaciones">⚙️ Operaciones</option>
                  <option value="Calidad">🔍 Control de Calidad</option>
                  <option value="Despacho">🚚 Despacho</option>
                  <option value="AreaAdministrativa">👔 Área Administrativa</option>
                  <option value="DirectorTecnico">👨‍⚕️ Director Técnico</option>
                </select>
                {/* Flecha del select */}
                <div className="absolute right-3 top-1/2 transform -translate-y-1/2 pointer-events-none">
                  <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                  </svg>
                </div>
              </div>
            </div>

            {/* Info sobre el sistema 
                • Puede usar cualquier email y contraseña
                • Seleccione el tipo de usuario para acceder a diferentes pantallas
                • Cada rol tiene acceso a funcionalidades específicas del sistema*/}
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