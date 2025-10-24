-- Script para cambiar la contraseña del usuario "paola" a "Paola1234"
-- Ejecutar este script en PostgreSQL (base de datos: gestor_sat)

UPDATE usuario
SET password = '8yJcJGhp7DFgWAxy/Fibf6cTtWP+gTPj799jMX5Ggqs=',
    updated_at = CURRENT_TIMESTAMP
WHERE usuario = 'paola';

-- Verificar el cambio
SELECT id_usuario, usuario, email, password, estado
FROM usuario
WHERE usuario = 'paola';
