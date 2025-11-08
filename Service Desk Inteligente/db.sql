-- ==========================
-- CREACIÓN DE TABLAS
-- ==========================

-- Primero crear las tabla

-- Crea la tabla roles
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       nombre VARCHAR(50) NOT NULL,
                       descripcion VARCHAR(200)
);

-- Crea la tabla colaboradores
CREATE TABLE colaboradores (
                               id BIGSERIAL PRIMARY KEY,
                               nombre VARCHAR(100) NOT NULL,
                               apellido VARCHAR(100) NOT NULL,
                               area VARCHAR(50),
                               cargo VARCHAR(50),
                               email VARCHAR(40) NOT NULL,
                               telefono VARCHAR(9),
                               contrasena VARCHAR(40),
                               id_rol BIGINT NOT NULL,
                               CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES roles(id)
);

CREATE TABLE tokens (
                        id BIGSERIAL PRIMARY KEY,
                        token VARCHAR(255) UNIQUE NOT NULL,
                        colaborador_id BIGINT NOT NULL,
                        fecha_creacion TIMESTAMP NOT NULL,
                        fecha_expiracion TIMESTAMP NOT NULL,
                        usado BOOLEAN DEFAULT FALSE,
                        FOREIGN KEY (colaborador_id) REFERENCES colaboradores(id)
);

-- 1. Tabla Categorías
CREATE TABLE categorias (
                            id BIGSERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL,
                            id_categoria_padre BIGINT,
                            FOREIGN KEY (id_categoria_padre) REFERENCES categorias(id)
);

-- 2. Tabla SLA
CREATE TABLE slas (
                      id BIGSERIAL PRIMARY KEY,
                      id_categoria BIGINT NOT NULL,
                      prioridad VARCHAR(10) NOT NULL,
                      tiempo_max_respuesta INTEGER NOT NULL,
                      tiempo_max_resolucion INTEGER NOT NULL,
                      FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

-- 3. Tabla Tickets
CREATE TABLE tickets (
                         id BIGSERIAL PRIMARY KEY,
                         id_categoria BIGINT NOT NULL,
                         id_solicitante BIGINT NOT NULL,
                         id_agente_asignado BIGINT,
                         prioridad VARCHAR(10) NOT NULL,
                         estado VARCHAR(20) NOT NULL DEFAULT 'Abierto',
                         descripcion TEXT,
                         fuente VARCHAR(20),
                         fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         fecha_cierre TIMESTAMP,
                         id_sla BIGINT,
                         tipo VARCHAR(20) DEFAULT 'formal',
                         FOREIGN KEY (id_categoria) REFERENCES categorias(id),
                         FOREIGN KEY (id_solicitante) REFERENCES colaboradores(id),
                         FOREIGN KEY (id_agente_asignado) REFERENCES colaboradores(id),
                         FOREIGN KEY (id_sla) REFERENCES slas(id)
);

-- 4. Tabla Comentarios
CREATE TABLE comentarios (
                             id BIGSERIAL PRIMARY KEY,
                             id_ticket BIGINT NOT NULL,
                             id_autor BIGINT NOT NULL,
                             fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             texto TEXT,
                             tipo VARCHAR(30),
                             FOREIGN KEY (id_ticket) REFERENCES tickets(id) ON DELETE CASCADE,
                             FOREIGN KEY (id_autor) REFERENCES colaboradores(id)
);

-- 5. Tabla Encuestas de Satisfacción
CREATE TABLE encuestas_satisfaccion (
                                        id BIGSERIAL PRIMARY KEY,
                                        id_ticket BIGINT NOT NULL UNIQUE,
                                        calificacion INTEGER NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
                                        comentario TEXT,
                                        FOREIGN KEY (id_ticket) REFERENCES tickets(id) ON DELETE CASCADE
);




-- insertar datos en la tabla roles
INSERT INTO roles (nombre, descripcion) VALUES
                                            ('USUARIO', 'Usuario estándar del sistema'),
                                            ('ADMINISTRADOR_TI', 'Administrador del sistema con todos los permisos'),
                                            ('COORDINADOR_TI', 'Coordinador del área de TI'),
                                            ('TECNICO_TI', 'Técnico de soporte TI'),
                                            ('GERENCIA_TI', 'Gerencia del área de TI');

-- Asegúrate de que ya tienes los roles creados primero
-- Luego inserta colaboradores (asumiendo que los id_rol van del 1 al 5)

INSERT INTO colaboradores (nombre, apellido, area, cargo, email, telefono, contrasena, id_rol) VALUES
                                                                                                   ('Juan', 'Pérez', 'Tecnología', 'Administrador de Sistemas', 'juan.perez@banco.com', '987654321', 'pass123', 2),
                                                                                                   ('María', 'González', 'Tecnología', 'Coordinadora TI', 'maria.gonzalez@banco.com', '987654322', 'pass123', 3),
                                                                                                   ('Carlos', 'Rodríguez', 'Tecnología', 'Técnico de Soporte', 'carlos.rodriguez@banco.com', '987654323', 'pass123', 4),
                                                                                                   ('Ana', 'Martínez', 'Recursos Humanos', 'Asistente', 'ana.martinez@banco.com', '987654324', 'pass123', 1),
                                                                                                   ('Luis', 'Fernández', 'Tecnología', 'Técnico de Soporte', 'luis.fernandez@banco.com', '987654325', 'pass123', 4),
                                                                                                   ('Patricia', 'López', 'Finanzas', 'Analista', 'patricia.lopez@banco.com', '987654326', 'pass123', 1),
                                                                                                   ('Roberto', 'Sánchez', 'Tecnología', 'Gerente TI', 'roberto.sanchez@banco.com', '987654327', 'pass123', 5),
                                                                                                   ('Carmen', 'Torres', 'Operaciones', 'Jefe de Área', 'carmen.torres@banco.com', '987654328', 'pass123', 1),
                                                                                                   ('Diego', 'Ramírez', 'Tecnología', 'Técnico de Redes', 'diego.ramirez@banco.com', '987654329', 'pass123', 4),
                                                                                                   ('Sofía', 'Vargas', 'Marketing', 'Coordinadora', 'sofia.vargas@banco.com', '987654330', 'pass123', 1);

-- Insertar categorías
INSERT INTO categorias (nombre, id_categoria_padre) VALUES
                                                        ('Hardware', NULL),          -- id: 1
                                                        ('Software', NULL),          -- id: 2
                                                        ('Conectividad', NULL),      -- id: 3
                                                        ('Laptop', 1),               -- id: 4 (subcategoría de Hardware)
                                                        ('Impresora', 1),            -- id: 5 (subcategoría de Hardware)
                                                        ('Wi-Fi', 3),                -- id: 6 (subcategoría de Conectividad)
                                                        ('VPN', 3);                  -- id: 7 (subcategoría de Conectividad)

-- Insertar SLAs
INSERT INTO slas (id_categoria, prioridad, tiempo_max_respuesta, tiempo_max_resolucion) VALUES
                                                                                            (1, 'Alta', 30, 240),        -- Hardware Alta: 30 min respuesta, 4h resolución
                                                                                            (1, 'Media', 60, 480),       -- Hardware Media: 1h respuesta, 8h resolución
                                                                                            (1, 'Baja', 120, 1440),      -- Hardware Baja: 2h respuesta, 24h resolución
                                                                                            (3, 'Alta', 15, 120),        -- Conectividad Alta: 15 min respuesta, 2h resolución
                                                                                            (3, 'Media', 30, 240);       -- Conectividad Media: 30 min respuesta, 4h resolución

-- Insertar tickets de prueba
-- IMPORTANTE: Usar IDs de colaboradores que EXISTEN
-- Según tu db.sql:
-- ID 1: Juan (Admin) | ID 2: María (Coord) | ID 3: Carlos (Técnico)
-- ID 4: Ana (Usuario) | ID 5: Luis (Técnico) | ID 6: Patricia (Usuario)
-- ID 7: Roberto (Gerente) | ID 8: Carmen (Usuario) | ID 9: Diego (Técnico)
-- ID 10: Sofía (Usuario)

INSERT INTO tickets (id_categoria, id_solicitante, id_agente_asignado, prioridad, estado, descripcion, fuente, id_sla) VALUES
                                                                                                                           -- Ticket 1: Solicitante Ana (4), Sin asignar
                                                                                                                           (6, 4, NULL, 'Media', 'Abierto', 'Conexión Wi-Fi muy lenta en área de finanzas', 'Manual', 5),

                                                                                                                           -- Ticket 2: Solicitante Patricia (6), Sin asignar
                                                                                                                           (4, 6, NULL, 'Alta', 'Abierto', 'Laptop no enciende, se escucha pitido', 'Manual', 1),

                                                                                                                           -- Ticket 3: Solicitante Carmen (8), Sin asignar
                                                                                                                           (2, 8, NULL, 'Baja', 'Abierto', 'Solicitud de instalación de software de análisis', 'Manual', 3),

                                                                                                                           -- Ticket 4: Solicitante Sofía (10), Asignado a Carlos (3)
                                                                                                                           (7, 10, 3, 'Alta', 'En proceso', 'No puedo conectarme a la VPN corporativa', 'Manual', 4),

                                                                                                                           -- Ticket 5: Solicitante Ana (4), Asignado a Luis (5), Resuelto
                                                                                                                           (5, 4, 5, 'Media', 'Resuelto', 'Impresora del 3er piso no imprime', 'Manual', 2);

-- Insertar comentarios de ejemplo
INSERT INTO comentarios (id_ticket, id_autor, texto, tipo) VALUES
                                                               (4, 3, 'Estoy revisando la configuración de VPN. Dame 10 minutos.', 'actualización'),
                                                               (5, 5, 'Problema resuelto. Era un atasco de papel.', 'actualización'),
                                                               (5, 4, 'Confirmado. Ya funciona. Gracias!', 'retroalimentación');

-- ==========================
-- PASO 4: VERIFICAR DATOS
-- ==========================

-- Ver todos los tickets creados
SELECT
    t.id,
    c.nombre as categoria,
    s.nombre || ' ' || s.apellido as solicitante,
    COALESCE(a.nombre || ' ' || a.apellido, 'Sin asignar') as agente,
    t.prioridad,
    t.estado,
    t.descripcion
FROM tickets t
         JOIN categorias c ON t.id_categoria = c.id
         JOIN colaboradores s ON t.id_solicitante = s.id
         LEFT JOIN colaboradores a ON t.id_agente_asignado = a.id
ORDER BY t.id;