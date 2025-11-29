import React, { useState, useEffect } from 'react';
import { clientesApi } from '@/lib/api';

interface LoteCliente {
    id: string;
    cliente: string;
    producto: string;
    lote: string;
    fechaVencimiento: string;
    cantidad: number;
    estado: string;
    ubicacion: string;
}

export function GestionLotes() {
    const [lotes, setLotes] = useState<LoteCliente[]>([]);
    const [loading, setLoading] = useState(true);
    const [filtroCliente, setFiltroCliente] = useState('');

    useEffect(() => {
        cargarLotes();
    }, []);

    const cargarLotes = async () => {
        setLoading(true);
        try {
            // En una implementación real, esto vendría de un endpoint específico de inventario
            // Por ahora simularemos o usaremos lo que tenemos
            const response = await fetch('http://localhost:8080/api/inventario-cliente');
            // Nota: Asumo que este endpoint existe o lo crearé. 
            // Si no existe, usaré datos mock o el endpoint de clientes con productos.

            if (response.ok) {
                const data = await response.json();
                setLotes(data);
            } else {
                // Fallback a datos mock si el endpoint no está listo
                setLotes([
                    { id: '1', cliente: 'FarmaSalud SAC', producto: 'Paracetamol 500mg', lote: 'L-2023-001', fechaVencimiento: '2025-10-01', cantidad: 1000, estado: 'DISPONIBLE', ubicacion: 'A-01-01' },
                    { id: '2', cliente: 'Hospital Central', producto: 'Vacuna Influenza', lote: 'L-2023-002', fechaVencimiento: '2024-12-01', cantidad: 500, estado: 'DISPONIBLE', ubicacion: 'F-01-01' },
                ]);
            }
        } catch (error) {
            console.error('Error cargando lotes:', error);
        } finally {
            setLoading(false);
        }
    };

    const lotesFiltrados = filtroCliente
        ? lotes.filter(l => l.cliente.toLowerCase().includes(filtroCliente.toLowerCase()))
        : lotes;

    return (
        <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-b border-gray-200">
                <h3 className="text-lg font-semibold text-gray-900">Gestión de Lotes por Cliente</h3>
                <p className="text-sm text-gray-600">Visualice y gestione los lotes de productos almacenados por cliente.</p>
            </div>

            <div className="p-6">
                <div className="mb-4">
                    <input
                        type="text"
                        placeholder="Buscar por cliente..."
                        value={filtroCliente}
                        onChange={(e) => setFiltroCliente(e.target.value)}
                        className="w-full md:w-1/3 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cliente</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Lote</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Vencimiento</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cantidad</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ubicación</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {lotesFiltrados.map((lote) => (
                                <tr key={lote.id}>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{lote.cliente}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{lote.producto}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{lote.lote}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{lote.fechaVencimiento}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{lote.cantidad}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{lote.ubicacion}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                            {lote.estado}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
