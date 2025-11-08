import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/api';

const AdminDashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (!userData) {
      navigate('/');
      return;
    }
    setUser(JSON.parse(userData));
  }, [navigate]);

  const handleLogout = () => {
    authService.logout();
    navigate('/');
  };

  if (!user) return null;

  const isAdmin = user.rol?.nombre === 'ADMINISTRADOR_TI';

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">
                Service Desk Inteligente
              </h1>
              <p className="text-sm text-gray-500 mt-1">
                Panel de {user.rol?.nombre}
              </p>
            </div>
            <button
              onClick={handleLogout}
              className="bg-red-500 hover:bg-red-600 text-white px-6 py-2 rounded-lg transition-colors font-medium"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        
        {/* User Info Card */}
        <div className="bg-white rounded-xl shadow-sm p-6 mb-8 border border-gray-100">
          <h2 className="text-xl font-bold text-gray-900 mb-4">
            👋 Bienvenido, {user.nombre}
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Nombre completo</p>
              <p className="font-semibold text-gray-900">{user.nombre} {user.apellido}</p>
            </div>
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Email</p>
              <p className="font-semibold text-gray-900">{user.email}</p>
            </div>
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Área</p>
              <p className="font-semibold text-gray-900">{user.area}</p>
            </div>
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Cargo</p>
              <p className="font-semibold text-gray-900">{user.cargo}</p>
            </div>
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Rol</p>
              <p className="font-semibold text-cyan-600">{user.rol?.nombre}</p>
            </div>
            <div className="space-y-1">
              <p className="text-sm text-gray-500">Teléfono</p>
              <p className="font-semibold text-gray-900">{user.telefono}</p>
            </div>
          </div>
        </div>

        {/* KPIs */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div className="bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl shadow-lg p-6 text-white">
            <div className="flex items-center justify-between mb-2">
              <p className="text-sm font-medium opacity-90">Tickets Abiertos</p>
              <span className="text-2xl">📋</span>
            </div>
            <p className="text-4xl font-bold">12</p>
            <p className="text-xs opacity-75 mt-2">Pendientes de atención</p>
          </div>

          <div className="bg-gradient-to-br from-green-500 to-green-600 rounded-xl shadow-lg p-6 text-white">
            <div className="flex items-center justify-between mb-2">
              <p className="text-sm font-medium opacity-90">Resueltos Hoy</p>
              <span className="text-2xl">✅</span>
            </div>
            <p className="text-4xl font-bold">8</p>
            <p className="text-xs opacity-75 mt-2">+15% vs ayer</p>
          </div>

          <div className="bg-gradient-to-br from-yellow-500 to-orange-500 rounded-xl shadow-lg p-6 text-white">
            <div className="flex items-center justify-between mb-2">
              <p className="text-sm font-medium opacity-90">En Progreso</p>
              <span className="text-2xl">⏳</span>
            </div>
            <p className="text-4xl font-bold">5</p>
            <p className="text-xs opacity-75 mt-2">Siendo atendidos</p>
          </div>

          <div className="bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl shadow-lg p-6 text-white">
            <div className="flex items-center justify-between mb-2">
              <p className="text-sm font-medium opacity-90">SLA Cumplimiento</p>
              <span className="text-2xl">⚡</span>
            </div>
            <p className="text-4xl font-bold">94%</p>
            <p className="text-xs opacity-75 mt-2">Objetivo: 95%</p>
          </div>
        </div>

        {/* SLA Alerts - Solo para Admin */}
        {isAdmin && (
          <div className="bg-white rounded-xl shadow-sm p-6 mb-8 border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-bold text-gray-900">
                🚨 Alertas y Monitoreo SLA
              </h3>
              <span className="px-3 py-1 bg-red-100 text-red-700 text-sm font-medium rounded-full">
                2 críticos
              </span>
            </div>
            <div className="space-y-3">
              <div className="flex items-center justify-between p-4 bg-red-50 border border-red-200 rounded-lg">
                <div className="flex items-center space-x-3">
                  <span className="text-2xl">⚠️</span>
                  <div>
                    <p className="font-semibold text-gray-900">Ticket #1245 - SLA próximo a vencerse</p>
                    <p className="text-sm text-gray-600">Tiempo restante: 15 minutos | Prioridad: Alta</p>
                  </div>
                </div>
                <button className="px-4 py-2 bg-red-600 text-white text-sm font-medium rounded-lg hover:bg-red-700">
                  Ver detalles
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Management Modules */}
        <div className="mb-8">
          <h3 className="text-lg font-bold text-gray-900 mb-4">
            🛠️ Gestión del Sistema
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            
            <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all p-6 cursor-pointer border border-gray-100 group">
              <div className="flex items-center justify-between mb-4">
                <span className="text-4xl group-hover:scale-110 transition-transform">👥</span>
                {isAdmin && (
                  <span className="px-2 py-1 bg-cyan-100 text-cyan-700 text-xs font-medium rounded">
                    Admin
                  </span>
                )}
              </div>
              <h4 className="text-xl font-bold text-gray-900 mb-2">Colaboradores</h4>
              <p className="text-sm text-gray-600 mb-4">
                Gestionar usuarios del sistema
              </p>
            </div>

            <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all p-6 cursor-pointer border border-gray-100 group">
              <div className="flex items-center justify-between mb-4">
                <span className="text-4xl group-hover:scale-110 transition-transform">📊</span>
              </div>
              <h4 className="text-xl font-bold text-gray-900 mb-2">Dashboard Analítico</h4>
              <p className="text-sm text-gray-600 mb-4">
                Métricas y estadísticas avanzadas
              </p>
            </div>

            {isAdmin && (
              <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all p-6 cursor-pointer border border-gray-100 group">
                <div className="flex items-center justify-between mb-4">
                  <span className="text-4xl group-hover:scale-110 transition-transform">⚙️</span>
                </div>
                <h4 className="text-xl font-bold text-gray-900 mb-2">Configurar SLA</h4>
                <p className="text-sm text-gray-600 mb-4">
                  Definir acuerdos de nivel de servicio
                </p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default AdminDashboard;