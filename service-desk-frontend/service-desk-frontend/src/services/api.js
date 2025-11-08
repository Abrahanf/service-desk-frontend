import axios from 'axios';                         // librería para hacer peticiones HTTP

const API_BASE_URL = 'http://localhost:8080/api';  // define la URL base del backend


const api = axios.create({                         // crea una instancia de axios llamada api
  baseURL: API_BASE_URL,                           // todas las poeticiones usarán API_BASE_URL como prefijo (base)
  headers: {
    'Content-Type': 'application/json',            // indica que se envían JSONs
  },
});

api.interceptors.request.use(                      // registra una función que se ejecuta antes de cada petición hecha con api
  (config) => {
    const token = localStorage.getItem('token');   // lee un token guardado en localStorage. 'token' es la clave que indica el valor
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // añade la cabecera Authorization: Bearer <token> a la petición
    }
    return config;                                 // config modificado para que la petición continúe.
  },
  (error) => {
    return Promise.reject(error);                  // maneja errores en el interceptor y los propaga.
  }
);

// Interceptor para manejar respuestas (ej. token expirado)
api.interceptors.response.use(
  (response) => {
    // Si la respuesta es exitosa, solo devuélvela
    return response;
  },
  (error) => {
    // Si el error es 401 (No Autorizado)
    if (error.response && error.response.status === 401) {
      // Limpia el almacenamiento local
      authService.logout(); 
      // Recarga la página para redirigir al login (o usa react-router)
      window.location.href = '/login';
      console.error('Sesión expirada. Por favor, inicie sesión de nuevo.');
    }
    
    // Propaga el error para que otros try...catch puedan manejarlo
    return Promise.reject(error);
  }
);

// ===================================
// Servicios de Autenticación
// ===================================
export const authService = {
  login: async (email, password) => {
    try {
      // 1. Llama al endpoint de login
      const response = await api.post('/colaboradores/login', { 
        email: email,
        contraseña: password 
      });

      // 2. Verifica si la respuesta contiene el token y el colaborador
      if (response.data && response.data.token && response.data.colaborador) {
        
        // 3. ¡ESTE ES EL PASO CLAVE!
        // Guarda el token y el usuario en localStorage
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(response.data.colaborador));

        // 4. Devuelve los datos (para que el AuthContext o componente los use)
        return response.data;

      } else {
        // Si el backend no devuelve lo esperado
        throw new Error('Respuesta inesperada del servidor');
      }

    } catch (error) {
      console.error('Error en login:', error);
      // Lanza el error para que el componente (ej. el formulario de login) pueda mostrarlo
      throw error.response?.data || 'Error en el login';
    }
  },

  // Solicitar recuperación de contraseña
  requestPasswordReset: async (email) => {
    try {
      const response = await api.post('/colaboradores/forgot-password', { email });
      return response.data;
    } catch (error) {
      throw error.response?.data || 'Error al solicitar recuperación';
    }
  },

  // Restablecer contraseña con token
  resetPassword: async (token, newPassword) => {
    try {
      const response = await api.post('/colaboradores/reset-password', { 
        token: token,
        nuevaClave: newPassword,
        nuevaClaveConfirmacion: newPassword
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || 'Error al restablecer contraseña';
    }
  },

  // Cerrar sesión (limpia datos locales)
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
};

// ===================================
// Servicios de Colaboradores
// ===================================
export const colaboradorService = {
  // Obtener todos los colaboradores
  getAll: async () => {
    try {
      const response = await api.get('/colaboradores');
      return response.data;
    } catch (error) {
      console.error("Error al obtener colaboradores:", error);
      throw error; // Lanza el error para que el componente lo maneje
    }
  },

  // Obtener un colaborador por ID
  getById: async (id) => {
    try {
      const response = await api.get(`/colaboradores/${id}`);
      return response.data;
    } catch (error) {
      console.error("Error al obtener colaborador por ID:", error);
      throw error; // Lanza el error para que el componente lo maneje
    }
  },

  // Crear un nuevo colaborador
  create: async (colaborador) => {
    try {
      const response = await api.post('/colaboradores', colaborador);
      return response.data;
    } catch (error) {
      console.error("Error al crear un colaborador:", error);
      throw error; // Lanza el error para que el componente lo maneje
    }
  },

  // Actualizar un colaborador
  update: async (id, colaborador) => {
    try {
      const response = await api.put(`/colaboradores/${id}`, colaborador);
      return response.data;
    } catch (error) {
      console.error("Error al actualizar colaborador:", error);
      throw error; // Lanza el error para que el componente lo maneje
    }
  },

  // Eliminar un colaborador
  delete: async (id) => {
    try {
      const response = await api.delete(`/colaboradores/${id}`);
      return response.data;
    } catch (error) {
      console.error("Error al eliminar colaboradores:", error);
      throw error; // Lanza el error para que el componente lo maneje
    }
  }
};

export default api;