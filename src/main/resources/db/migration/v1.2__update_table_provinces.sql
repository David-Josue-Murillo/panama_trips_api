ALTER TABLE regions RENAME COLUMN province TO old_province;

-- Agregar la nueva columna province_id referenciada a la tabla provinces
ALTER TABLE regions ADD COLUMN province_id INT NOT NULL REFERENCES provinces(id) ON DELETE CASCADE;

-- Eliminar la columna vieja si no es necesaria
ALTER TABLE regions DROP COLUMN old_province;
