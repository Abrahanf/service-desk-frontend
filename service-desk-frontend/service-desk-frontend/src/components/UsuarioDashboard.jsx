import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import TicketDetalle from './TicketDetalle';

const UsuarioDashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [tickets, setTickets] = useState([
    { id: 1005, problema: 'Conexión - Wifi', estado: 'En proceso', fecha: '19/10/2025' },
    { id: 1006, problema: 'Hardware - Laptop', estado: 'En proceso', fecha: '19/10/2025' }
  ]);
  const [chatMessages, setChatMessages] = useState([
    { sender: 'bot', text: 'Hola👋, soy tu asistente de TI. ¿En qué puedo ayudarte?' }
  ]);
  const [inputMessage, setInputMessage] = useState('');
  const [dateFrom, setDateFrom] = useState('');
  const [dateTo, setDateTo] = useState('');
  const [filterType, setFilterType] = useState('');
  const [ticketActivo, setTicketActivo] = useState(null); // <- Mueve aquí

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

  const handleSendMessage = () => {
    if (!inputMessage.trim()) return;

    setChatMessages([...chatMessages, { sender: 'user', text: inputMessage }]);
    setTimeout(() => {
      setChatMessages(prev => [...prev, {
        sender: 'bot',
        text: '¿Podrías darme más detalles sobre tu problema?'
      }]);
    }, 1000);

    setInputMessage('');
  };

  const handleQuickOption = (option) => {
    setChatMessages([...chatMessages, { sender: 'user', text: option }]);
    setTimeout(() => {
      setChatMessages(prev => [...prev, {
        sender: 'bot',
        text: 'Entiendo, déjame ayudarte con eso. Por favor describe el problema con más detalle.'
      }]);
    }, 1000);
  };

  if (!user) return null;

  return (
    ticketActivo ? (
      <TicketDetalle
        ticket={ticketActivo}
        volver={() => setTicketActivo(null)}
      />
    ) : (
      <div className="min-h-screen bg-gradient-to-br from-slate-600 via-slate-700 to-slate-800">
        {/* Header */}
        <header className="bg-slate-800 shadow-lg border-b border-slate-600">
          <div className="max-w-full mx-auto px-6 py-4">
            <div className="flex justify-between items-center">
              <div className="flex items-center space-x-8">
                <h1 className="text-xl font-bold text-white">Asistente TI</h1>
                <nav className="flex space-x-6">
                  <button className="text-white hover:text-cyan-400 transition-colors font-medium border-b-2 border-cyan-400 pb-1">
                    Mis solicitudes
                  </button>
                  <button className="text-gray-300 hover:text-white transition-colors">
                    {user.nombre} {user.apellido}
                  </button>
                </nav>
              </div>
              <button
                onClick={handleLogout}
                className="text-white hover:text-red-400 transition-colors text-sm"
              >
                Cerrar sesión
              </button>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <div className="flex max-w-full mx-auto h-[calc(100vh-73px)]">
          {/* Left Panel - Tickets List */}
          <div className="flex-1 p-6 overflow-y-auto">
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6 mb-6">
              <h2 className="text-2xl font-bold text-white mb-6">Mis solicitudes</h2>
              {/* Filters */}
              <div className="flex items-center gap-4 mb-6">
                <div className="flex items-center gap-2">
                  <label className="text-white text-sm">Rango de fecha</label>
                  <input
                    type="date"
                    value={dateFrom}
                    onChange={(e) => setDateFrom(e.target.value)}
                    className="px-3 py-2 rounded bg-slate-700 text-white border border-slate-600 focus:outline-none focus:border-cyan-400"
                  />
                  <span className="text-white">-</span>
                  <input
                    type="date"
                    value={dateTo}
                    onChange={(e) => setDateTo(e.target.value)}
                    className="px-3 py-2 rounded bg-slate-700 text-white border border-slate-600 focus:outline-none focus:border-cyan-400"
                  />
                </div>
                <div className="flex items-center gap-2">
                  <select
                    value={filterType}
                    onChange={(e) => setFilterType(e.target.value)}
                    className="px-4 py-2 rounded bg-slate-700 text-white border border-slate-600 focus:outline-none focus:border-cyan-400"
                  >
                    <option value="">Tipo</option>
                    <option value="hardware">Hardware</option>
                    <option value="software">Software</option>
                    <option value="conexion">Conexión</option>
                  </select>
                </div>
              </div>
              {/* Tickets Table */}
              <div className="bg-white rounded-lg overflow-hidden shadow-lg">
                <table className="w-full">
                  <thead className="bg-slate-100 border-b-2 border-slate-200">
                    <tr>
                      <th className="text-left px-6 py-4 text-sm font-semibold text-slate-700">Ticket</th>
                      <th className="text-left px-6 py-4 text-sm font-semibold text-slate-700">Problema</th>
                      <th className="text-left px-6 py-4 text-sm font-semibold text-slate-700">Estado</th>
                      <th className="text-left px-6 py-4 text-sm font-semibold text-slate-700">Fecha</th>
                      <th className="px-6 py-4"></th>
                    </tr>
                  </thead>
                  <tbody>
                    {tickets.map((ticket) => (
                      <tr key={ticket.id} className="border-b border-slate-100 hover:bg-slate-50 transition-colors">
                        <td className="px-6 py-4 text-sm font-medium text-slate-900">{ticket.id}</td>
                        <td className="px-6 py-4 text-sm text-slate-700">{ticket.problema}</td>
                        <td className="px-6 py-4">
                          <span className="inline-block px-3 py-1 text-xs font-medium bg-yellow-100 text-yellow-800 rounded-full">
                            {ticket.estado}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-sm text-slate-600">{ticket.fecha}</td>
                        <td className="px-6 py-4 text-right">
                          <button
                            className="px-4 py-2 bg-slate-800 hover:bg-slate-700 text-white text-sm font-medium rounded transition-colors"
                            onClick={() => setTicketActivo(ticket)}>
                            Ver detalle
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                {tickets.length === 0 && (
                  <div className="text-center py-12 text-slate-500">
                    No tienes tickets registrados
                  </div>
                )}
              </div>
            </div>
          </div>
          {/* Right Panel - Chatbot */}
          <div className="w-96 bg-slate-200 shadow-2xl flex flex-col">
            {/* Chat Header */}
            <div className="bg-slate-300 px-6 py-4 border-b border-slate-400">
              <h3 className="text-xl font-bold text-slate-800">Chat bot</h3>
            </div>
            {/* Chat Messages */}
            <div className="flex-1 overflow-y-auto p-6 space-y-4">
              {chatMessages.map((msg, index) => (
                <div key={index} className={`flex ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
                  {msg.sender === 'bot' && (
                    <div className="flex items-start space-x-2">
                      <div className="w-10 h-10 rounded-full bg-slate-700 flex items-center justify-center text-white font-bold flex-shrink-0">
                        🤖
                      </div>
                      <div className="bg-white rounded-2xl rounded-tl-none px-4 py-3 max-w-xs shadow-md">
                        <p className="text-sm text-slate-800">{msg.text}</p>
                      </div>
                    </div>
                  )}
                  {msg.sender === 'user' && (
                    <div className="bg-slate-700 text-white rounded-2xl rounded-tr-none px-4 py-3 max-w-xs shadow-md">
                      <p className="text-sm">{msg.text}</p>
                    </div>
                  )}
                </div>
              ))}
            </div>
            {/* Quick Options */}
            <div className="px-6 py-3 bg-slate-100 border-t border-slate-300">
              <div className="flex flex-wrap gap-2">
                <button
                  onClick={() => handleQuickOption('Problema con Wi-Fi')}
                  className="px-3 py-2 bg-slate-300 hover:bg-slate-400 text-slate-800 text-xs rounded-lg transition-colors"
                >
                  Problema con Wi-Fi
                </button>
                <button
                  onClick={() => handleQuickOption('Problema con el proyector')}
                  className="px-3 py-2 bg-slate-300 hover:bg-slate-400 text-slate-800 text-xs rounded-lg transition-colors"
                >
                  Problema con el proyector
                </button>
                <button
                  onClick={() => handleQuickOption('Problema con VPN')}
                  className="px-3 py-2 bg-slate-300 hover:bg-slate-400 text-slate-800 text-xs rounded-lg transition-colors"
                >
                  Problema con VPN
                </button>
                <button
                  onClick={() => handleQuickOption('Problema con correo corporativo')}
                  className="px-3 py-2 bg-slate-300 hover:bg-slate-400 text-slate-800 text-xs rounded-lg transition-colors"
                >
                  Problema con correo corporativo
                </button>
              </div>
            </div>
            {/* Chat Input */}
            <div className="p-4 bg-slate-100 border-t border-slate-300">
              <div className="flex items-center space-x-2">
                <button className="w-10 h-10 rounded-full bg-slate-300 hover:bg-slate-400 flex items-center justify-center transition-colors">
                  <span className="text-xl">+</span>
                </button>
                <input
                  type="text"
                  value={inputMessage}
                  onChange={(e) => setInputMessage(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                  placeholder="Escribe tu mensaje..."
                  className="flex-1 px-4 py-3 rounded-full bg-white border border-slate-300 focus:outline-none focus:border-slate-500 text-sm"
                />
                <button
                  onClick={handleSendMessage}
                  className="w-10 h-10 rounded-full bg-slate-700 hover:bg-slate-600 flex items-center justify-center transition-colors"
                >
                  <span className="text-white text-xl">→</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  );
};

export default UsuarioDashboard;
