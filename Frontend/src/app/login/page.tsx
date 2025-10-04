"use client";

import * as React from "react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();
  const [role, setRole] = React.useState("");

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    // Simulate role-based redirection
    if (role === "admin") {
      router.push("/dashboard");
    } else if (role === "receiving") {
      router.push("/movimientos");
    } else {
      // Default redirection for other roles or no role selected
      router.push("/dashboard");
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
                type="email"
                placeholder="m@example.com"
                required
                defaultValue="admin@pharmaflow.com"
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
                type="password"
                required
                defaultValue="password"
                className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
              />
            </div>

            {/* Role Selector */}
            <div>
              <div className="flex items-center gap-2 mb-2">
                <svg
                  className="w-4 h-4 text-gray-600"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path
                    fillRule="evenodd"
                    d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                    clipRule="evenodd"
                  />
                </svg>
                <span className="text-sm text-gray-600">Choose option...</span>
              </div>
              <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                required
                className="w-full px-4 py-3 bg-gray-200 border-2 border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:border-blue-500 focus:bg-white transition-colors appearance-none cursor-pointer"
              >
                <option value="" disabled>Operador</option>
                <option value="admin">Admin</option>
                <option value="manager">Manager</option>
                <option value="receiving">Recepcionista</option>
                <option value="operator">Operador</option>
              </select>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-3 px-4 rounded-lg transition-colors duration-200 flex items-center justify-center gap-2"
            >
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
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}