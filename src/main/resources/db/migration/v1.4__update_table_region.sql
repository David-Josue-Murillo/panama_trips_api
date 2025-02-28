ALTER TABLE regions RENAME COLUMN comarca TO old_comarca;

-- Agregar la nueva columna comarca_id referenciada a la tabla provinces
ALTER TABLE regions ADD COLUMN comarca_id INT NOT NULL REFERENCES comarcas(id) ON DELETE CASCADE;

-- Eliminar la columna vieja si no es necesaria
ALTER TABLE regions DROP COLUMN old_comarca;
