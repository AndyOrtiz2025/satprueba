-- Script para permitir que archivos no requieran un trámite asociado
-- Esto permite subir archivos directamente a clientes sin necesidad de un trámite

-- Modificar la columna id_tramites para permitir valores NULL
ALTER TABLE archivos
ALTER COLUMN id_tramites DROP NOT NULL;

-- Verificar el cambio
SELECT
    column_name,
    is_nullable,
    data_type
FROM information_schema.columns
WHERE table_name = 'archivos'
AND column_name = 'id_tramites';
