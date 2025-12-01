INSERT INTO cuentas (numero_cuenta, tipo, saldo_inicial, saldo, estado, cliente_id)
VALUES ('225487', 'Corriente', 100.00, 100.00, TRUE, 1);
INSERT INTO cuentas (numero_cuenta, tipo, saldo_inicial, saldo, estado, cliente_id)
VALUES ('496825', 'Ahorros', 540.00, 540.00, TRUE, 1);

INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, numero_cuenta)
VALUES (CURRENT_TIMESTAMP(), 'DEPOSITO', 600.00, 700.00, '225487');

INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, numero_cuenta)
VALUES (CURRENT_TIMESTAMP(), 'RETIRO', -540.00, 0.00, '496825');