-- Insertar permisos
INSERT INTO permissions (name) VALUES
                                   ('BOOKING_CREATE'),
                                   ('BOOKING_READ'),
                                   ('BOOKING_UPDATE'),
                                   ('BOOKING_DELETE'),
                                   ('USER_CREATE'),
                                   ('USER_READ'),
                                   ('USER_UPDATE'),
                                   ('USER_DELETE'),
                                   ('PAYMENT_READ'),
                                   ('PAYMENT_REFUND'),
                                   ('SUPPORT_TICKET_READ'),
                                   ('SUPPORT_TICKET_RESPOND'),
                                   ('CONTENT_CREATE'),
                                   ('CONTENT_UPDATE'),
                                   ('CONTENT_DELETE'),
                                   ('SYSTEM_LOG_READ');

-- Insertar roles
INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('OPERATOR'),
                             ('CUSTOMER'),
                             ('GUEST'),
                             ('SUPPORT'),
                             ('CONTENT_MANAGER');

-- Asignar permisos a roles
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN';

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.name IN ('BOOKING_CREATE', 'BOOKING_READ', 'BOOKING_UPDATE', 'BOOKING_DELETE', 'USER_CREATE', 'USER_READ', 'USER_UPDATE', 'PAYMENT_READ')
WHERE r.name = 'OPERATOR';

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.name IN ('BOOKING_CREATE', 'BOOKING_READ', 'BOOKING_UPDATE', 'USER_READ', 'USER_UPDATE', 'PAYMENT_READ')
WHERE r.name = 'CUSTOMER';

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.name IN ('PAYMENT_READ', 'PAYMENT_REFUND', 'SUPPORT_TICKET_READ', 'SUPPORT_TICKET_RESPOND')
WHERE r.name = 'SUPPORT';

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.name IN ('CONTENT_CREATE', 'CONTENT_UPDATE', 'CONTENT_DELETE')
WHERE r.name = 'CONTENT_MANAGER';

-- Insertar usuarios de prueba
INSERT INTO users (name, lastname, dni, email, password_hash, role_id)
VALUES
    ('admin', 'admin', '1-111-1111', 'admin@example.com', 'admin', (SELECT id FROM roles WHERE name = 'ADMIN')),
    ('operator', 'operator', '2-222-2222', 'operator@example.com', 'operator', (SELECT id FROM roles WHERE name = 'OPERATOR')),
    ('customer', 'customer', '3-333-3333', 'customer@example.com', 'customer', (SELECT id FROM roles WHERE name = 'CUSTOMER')),
    ('support', 'support', '4-444-4444', 'support@example.com', 'support', (SELECT id FROM roles WHERE name = 'SUPPORT')),
    ('content', 'manager', '5-555-5555', 'contentmanager@example.com', 'contentmanager', (SELECT id FROM roles WHERE name = 'CONTENT_MANAGER'));
