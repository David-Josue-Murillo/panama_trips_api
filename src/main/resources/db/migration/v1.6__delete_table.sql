ALTER TABLE providers
    DROP COLUMN street_id;

ALTER TABLE providers
    ADD COLUMN address_id INT REFERENCES address(address_id) ON DELETE SET NULL;

DROP TABLE streets;
