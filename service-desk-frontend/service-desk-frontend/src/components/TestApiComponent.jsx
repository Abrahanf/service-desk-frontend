import React, { useState } from 'react';
// Asegúrate de importar tu servicio
import { colaboradorService } from '../services/api';

function TestApiComponent() {
  const [apiResponse, setApiResponse] = useState(null);
  const [apiError, setApiError] = useState(null);

  const handleTestApi = async () => {
    setApiResponse(null);
    setApiError(null);

    try {
      // 1. Aquí se llamará a tu servicio
      const data = await colaboradorService.getAll();
      
      // 2. Si tiene éxito, lo mostramos
      console.log('Datos recibidos:', data);
      setApiResponse(JSON.stringify(data, null, 2)); // Muestra el JSON bonito

    } catch (error) {
      // 3. Si falla, mostramos el error
      console.error('Error en la prueba de API:', error);
      setApiError(error.message || 'Ocurrió un error');
    }
  };

  return (
    <div style={{ padding: '20px', border: '1px solid #ccc', margin: '20px' }}>
      <h3>Componente de Prueba de API</h3>
      <button onClick={handleTestApi}>
        Probar "getAll" Colaboradores (Requiere Token)
      </button>

      {/* Muestra la respuesta exitosa */}
      {apiResponse && (
        <div style={{ marginTop: '15px' }}>
          <strong>✅ Éxito:</strong>
          <pre style={{ background: '#f0f0f0', padding: '10px' }}>{apiResponse}</pre>
        </div>
      )}

      {/* Muestra el error */}
      {apiError && (
        <div style={{ marginTop: '15px' }}>
          <strong>🔴 Error:</strong>
          <pre style={{ background: '#fff0f0', color: 'red', padding: '10px' }}>{apiError}</pre>
        </div>
      )}
    </div>
  );
}

export default TestApiComponent;