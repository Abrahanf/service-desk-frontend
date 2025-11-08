import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AdminDashboard from './AdminDashboard';
import UsuarioDashboard from './UsuarioDashboard';

const Dashboard = () => {
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

  if (!user) return null;

  // Determinar qué dashboard mostrar según el rol
  const rolNombre = user.rol?.nombre;

  // Si es USUARIO normal, mostrar UsuarioDashboard
  if (rolNombre === 'USUARIO') {
    return <UsuarioDashboard />;
  }

  // Si es ADMINISTRADOR_TI, COORDINADOR_TI, TECNICO_TI, GERENCIA_TI
  // mostrar AdminDashboard
  return <AdminDashboard />;
};

export default Dashboard;