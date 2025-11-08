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