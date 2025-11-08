import React, { useState } from 'react';

const TicketDetalle = ({ ticket, volver }) => {
  const [respuesta, setRespuesta] = useState('');
  const [cerrado, setCerrado] = useState(false);
  const [mostrarCalificar, setMostrarCalificar] = useState(false);
  const [calificacion, setCalificacion] = useState(0);

  const comentarioTecnico = {
    texto: 'El problema fue solucionado, por favor confirmar que se solucionó.',
    fecha: '19/10/2025 10:00 am'
  };

  const handleResponder = () => {
    setCerrado(true); // marco el ticket como cerrado
  };

  const handleCalificar = (valor) => {
    setCalificacion(valor);
  };

  const handleEnviarCalificacion = () => {
    setMostrarCalificar(false);
    alert('¡Gracias por tu calificación!');
    volver(); // regresar al dashboard después de evaluar
  };

  return (
    <div className="min-h-screen bg-slate-700 flex flex-col items-center justify-center">
      <header className="bg-slate-800 shadow-lg border-b border-slate-600 w-full">
        <div className="max-w-full mx-auto px-6 py-4 flex justify-between items-center">
          <h1 className="text-xl font-bold text-white">Conexión - wifi</h1>
          <button 
            onClick={volver}
            className="text-white hover:text-cyan-400 text-sm"
          >
            Regresar
          </button>
        </div>
      </header>
      <main className="p-6 flex flex-col items-start justify-center w-full max-w-xl">
        {/* Panel datos del ticket */}
        <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6 w-full max-w-lg mb-8">
          <table className="text-white w-full mb-6">
            <tbody>
              <tr>
                <td className="font-bold">Ticket</td>
                <td>{ticket.id}</td>
              </tr>
              <tr>
                <td className="font-bold">Problema</td>
                <td>{ticket.problema}</td>
              </tr>
              <tr>
                <td className="font-bold">Estado</td>
                <td>
                  {cerrado ? 
                    <span className="inline-block px-3 py-1 text-xs font-medium bg-green-200 text-green-900 rounded-full">
                      Cerrado por el administrador
                    </span>
                  :
                    <span className="inline-block px-3 py-1 text-xs font-medium bg-yellow-100 text-yellow-800 rounded-full">
                      {ticket.estado}
                    </span>
                  }
                </td>
              </tr>
              <tr>
                <td className="font-bold">Fecha</td>
                <td>{ticket.fecha}</td>
              </tr>
            </tbody>
          </table>
          <div className="mb-6">
            <h2 className="text-md font-bold text-white mb-2">Comentarios del técnico TI</h2>
            <textarea
              className="w-full p-3 rounded bg-slate-700 text-white border border-slate-600 mb-2"
              value={comentarioTecnico.texto}
              readOnly
            />
            <div className="text-sm text-gray-300 mb-2">{comentarioTecnico.fecha}</div>
            <textarea
              placeholder="Escribe tu respuesta..."
              className="w-full p-3 rounded bg-slate-700 text-white border border-slate-600 mb-2"
              value={respuesta}
              onChange={e => setRespuesta(e.target.value)}
              disabled={cerrado}
            />
            {!cerrado && (
              <button
                className="bg-slate-800 hover:bg-cyan-600 text-white px-6 py-2 rounded-lg"
                onClick={handleResponder}
              >
                Responder
              </button>
            )}
          </div>
        </div>
        {/* Si está cerrado, muestra opción de calificar */}
        {cerrado && !mostrarCalificar && (
          <button
            onClick={() => setMostrarCalificar(true)}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg shadow-lg text-lg"
          >
            Calificar experiencia
          </button>
        )}
        {/* Modal de calificación */}
        {mostrarCalificar && (
          <div 
            className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50"
          >
            <div className="bg-indigo-600 rounded-3xl p-10 flex flex-col items-center">
              <h3 className="text-white text-lg font-bold mb-6 text-center">
                Califique su experiencia en la solución del ticket {ticket.id}
              </h3>
              <div className="flex gap-6 mb-8">
                {[1,2,3,4,5].map(star => (
                  <button
                    key={star}
                    onClick={() => handleCalificar(star)}
                    className="text-5xl"
                    style={{ color: calificacion >= star ? 'white' : '#DDD' }}
                  >
                    ★
                  </button>
                ))}
              </div>
              <button
                className="bg-white text-indigo-800 px-6 py-2 font-semibold rounded-2xl"
                onClick={handleEnviarCalificacion}
              >
                Enviar
              </button>
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default TicketDetalle;
