## Trigger: `trg_impedeix_sobrepagament` (Tasca 2)

### Explicació de què fa el trigger `trg_impedeix_sobrepagament`

Este trigger se activa automáticamente antes de que se inserte un nuevo registro en la tabla `PAGAMENTS` (`BEFORE INSERT`). Su función es calcular cuánto se ha pagado ya de una reserva y sumarle el importe del nuevo pago. Si este total supera el importe total de la reserva (`imp_tot_reserva`), el trigger bloquea la inserción y lanza un mensaje de error, impidiendo así que el cliente pague de más.

### Comanda creació trigger

```sql
DELIMITER $$

CREATE TRIGGER trg_impedeix_sobrepagament
BEFORE INSERT
ON PAGAMENTS
FOR EACH ROW
BEGIN
    DECLARE v_total_pagat DECIMAL(10,2);
    DECLARE v_total_reserva DECIMAL(10,2);

    SELECT SUM(import_paga) INTO v_total_pagat
    FROM PAGAMENTS
    WHERE id_reserva = NEW.id_reserva;

    IF v_total_pagat IS NULL THEN
        SET v_total_pagat = 0;
    END IF;

    SELECT imp_tot_reserva INTO v_total_reserva
    FROM RESERVES
    WHERE id_reserva = NEW.id_reserva;

    IF (v_total_pagat + NEW.import_paga) > v_total_reserva THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: L''import del pagament supera el total de la reserva.';
    END IF;
END $$

DELIMITER ;

```

### Sortida de la comanda `CREATE TRIGGER`

```sql
mysql> CREATE TRIGGER trg_impedeix_sobrepagament
    -> BEFORE INSERT
    -> ON PAGAMENTS
    -> FOR EACH ROW
    -> BEGIN
    ->     DECLARE v_total_pagat DECIMAL(10,2);
    ->     DECLARE v_total_reserva DECIMAL(10,2);
    ->
    ->     SELECT SUM(import_paga) INTO v_total_pagat
    ->     FROM PAGAMENTS
    ->     WHERE id_reserva = NEW.id_reserva;
    ->
    ->     IF v_total_pagat IS NULL THEN
    ->         SET v_total_pagat = 0;
    ->     END IF;
    ->
    ->     SELECT imp_tot_reserva INTO v_total_reserva
    ->     FROM RESERVES
    ->     WHERE id_reserva = NEW.id_reserva;
    ->
    ->     IF (v_total_pagat + NEW.import_paga) > v_total_reserva THEN
    ->         SIGNAL SQLSTATE '45000'
    ->         SET MESSAGE_TEXT = 'Error: L''import del pagament supera el total de la reserva.';
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.02 sec)

mysql>
mysql> DELIMITER ;

```

### Demostració de que el trigger `trg_impedeix_sobrepagament` funciona

Para demostrar que el trigger funciona, ejecutamos primero una consulta para ver el estado de la reserva con `id_reserva = 1`. Vemos que el importe total es de 306.90€ y que en la tabla de pagos ya se han ingresado pagos que suman exactamente esos 306.90€ (100.00 + 206.90).

### Comanda a executar

```sql
SELECT id_reserva, imp_tot_reserva
FROM RESERVES
WHERE id_reserva = 1;

...

SELECT id_paga, id_reserva, import_paga
FROM PAGAMENTS
WHERE id_reserva = 1;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_reserva, imp_tot_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 1;
+------------+-----------------+
| id_reserva | imp_tot_reserva |
+------------+-----------------+
|          1 |          306.90 |
+------------+-----------------+
1 row in set (0.00 sec)

mysql> SELECT id_paga, id_reserva, import_paga
    -> FROM PAGAMENTS
    -> WHERE id_reserva = 1;
+---------+------------+-------------+
| id_paga | id_reserva | import_paga |
+---------+------------+-------------+
|       1 |          1 |      100.00 |
|      11 |          1 |      206.90 |
+---------+------------+-------------+
2 rows in set (0.00 sec)

mysql>

```

### Comanda a executar per activar el trigger `trg_impedeix_sobrepagament`

Como la reserva 1 ya está totalmente pagada, intentaremos insertar un nuevo pago de 50€ para esa misma reserva. Esto desencadenará automáticamente el trigger antes de la inserción y debería bloquear el proceso.

### Comanda a executar

```sql
INSERT INTO PAGAMENTS (
    id_reserva,
    data_paga,
    import_paga,
    metode_paga
) VALUES (
    1,
    NOW(),
    50.00,
    'efectiu'
);

```

### Sortida de la comanda executada

```sql
mysql> INSERT INTO PAGAMENTS (
    ->     id_reserva,
    ->     data_paga,
    ->     import_paga,
    ->     metode_paga
    -> ) VALUES (
    ->     1,
    ->     NOW(),
    ->     50.00,
    ->     'efectiu'
    -> );
ERROR 1644 (45000): Error: L'import del pagament supera el total de la reserva.
mysql>

```

### Comanda a executar per veure que s'activat el trigger `trg_impedeix_sobrepagament`

Habiendo saltado el error, volvemos a consultar la tabla `PAGAMENTS`. Veremos que el pago malicioso de 50€ no se ha insertado, demostrando que el trigger ha protegido la integridad de los datos.

### Comanda a executar

```sql
SELECT id_paga, id_reserva, import_paga
FROM PAGAMENTS
WHERE id_reserva = 1;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_paga, id_reserva, import_paga
    -> FROM PAGAMENTS
    -> WHERE id_reserva = 1;
+---------+------------+-------------+
| id_paga | id_reserva | import_paga |
+---------+------------+-------------+
|       1 |          1 |      100.00 |
|      11 |          1 |      206.90 |
+---------+------------+-------------+
2 rows in set (0.00 sec)

mysql>


```

---

---

## Trigger: `trg_bloqueja_checkout_impagat` (Tasca 3)

### Explicació de què fa el trigger `trg_bloqueja_checkout_impagat`

Este trigger se activa automáticamente antes de que se actualice un registro en la tabla `RESERVES` (`BEFORE UPDATE`). Su función es verificar si el usuario intenta cambiar el estado de la reserva a 'checkout'. Si es así, calcula si el total de los pagos registrados es inferior al precio total de la reserva. Si faltan pagos por realizar, el trigger aborta la actualización lanzando un error, evitando que un cliente se vaya sin pagar.

### Comanda creació trigger

```sql
DELIMITER $$

DELIMITER $$

CREATE TRIGGER trg_bloqueja_checkout_impagat
BEFORE UPDATE
ON RESERVES
FOR EACH ROW
BEGIN
    DECLARE v_total_pagat DECIMAL(10,2);

    IF NEW.en_estat_reserva = 'checkout' THEN

        SELECT SUM(import_paga) INTO v_total_pagat
        FROM PAGAMENTS
        WHERE id_reserva = NEW.id_reserva;

        IF v_total_pagat IS NULL THEN
            SET v_total_pagat = 0;
        END IF;

        IF v_total_pagat < NEW.imp_tot_reserva THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error: No es pot fer checkout. Hi ha imports pendents de pagament.';
        END IF;

    END IF;
END $$

DELIMITER ;

```

### Sortida de la comanda `CREATE TRIGGER`

```sql
mysql> DELIMITER $$
mysql>
mysql> CREATE TRIGGER trg_bloqueja_checkout_impagat
    -> BEFORE UPDATE
    -> ON RESERVES
    -> FOR EACH ROW
    -> BEGIN
    ->     DECLARE v_total_pagat DECIMAL(10,2);
    ->
    ->     IF NEW.en_estat_reserva = 'checkout' THEN
    ->
    ->         SELECT SUM(import_paga) INTO v_total_pagat
    ->         FROM PAGAMENTS
    ->         WHERE id_reserva = NEW.id_reserva;
    ->
    ->         IF v_total_pagat IS NULL THEN
    ->             SET v_total_pagat = 0;
    ->         END IF;
    ->
    ->         IF v_total_pagat < NEW.imp_tot_reserva THEN
    ->             SIGNAL SQLSTATE '45000'
    ->             SET MESSAGE_TEXT = 'Error: No es pot fer checkout. Hi ha imports pendents de pagament.';
    ->         END IF;
    ->
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.03 sec)

mysql>
mysql> DELIMITER ;
mysql>

```

### Demostració de que el trigger `trg_bloqueja_checkout_impagat` funciona

Para demostrar que el trigger funciona, ejecutamos primero una consulta para ver el estado de la reserva `2`. Vemos que cuesta 763.13€ pero, en la tabla `PAGAMENTS`, solo consta un pago parcial de 100.00€. Por lo tanto, esta reserva tiene deuda pendiente.

### Comanda a executar

```sql
SELECT id_reserva, en_estat_reserva, imp_tot_reserva
FROM RESERVES
WHERE id_reserva = 2;

...

SELECT id_paga, import_paga
FROM PAGAMENTS
WHERE id_reserva = 2;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_reserva, en_estat_reserva, imp_tot_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 2;
+------------+------------------+-----------------+
| id_reserva | en_estat_reserva | imp_tot_reserva |
+------------+------------------+-----------------+
|          2 | confirmada       |          763.13 |
+------------+------------------+-----------------+
1 row in set (0.00 sec)

mysql>


mysql> SELECT id_paga, import_paga
    -> FROM PAGAMENTS
    -> WHERE id_reserva = 2;
+---------+-------------+
| id_paga | import_paga |
+---------+-------------+
|       6 |      100.00 |
+---------+-------------+
1 row in set (0.00 sec)

mysql>


```

### Comanda a executar per activar el trigger `trg_bloqueja_checkout_impagat`

Como la reserva tiene pagos pendientes, intentaremos forzar un `UPDATE` para cambiar su estado a 'checkout'. Esto desencadenará automáticamente el trigger.

### Comanda a executar

```sql
UPDATE RESERVES
SET en_estat_reserva = 'checkout'
WHERE id_reserva = 2;

```

### Sortida de la comanda executada

```sql
mysql> UPDATE RESERVES
    -> SET en_estat_reserva = 'checkout'
    -> WHERE id_reserva = 2;
ERROR 1644 (45000): Error: No es pot fer checkout. Hi ha imports pendents de pagament.
mysql>

```

### Comanda a executar per veure que s'activat el trigger `trg_bloqueja_checkout_impagat`

Tras haber saltado el error, consultamos nuevamente el estado de la reserva `2`. Comprobaremos que el `UPDATE` fue abortado y la reserva sigue en su estado original ('confirmada').

### Comanda a executar

```sql
SELECT id_reserva, en_estat_reserva
FROM RESERVES
WHERE id_reserva = 2;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_reserva, en_estat_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 2;
+------------+------------------+
| id_reserva | en_estat_reserva |
+------------+------------------+
|          2 | confirmada       |
+------------+------------------+
1 row in set (0.00 sec)

mysql>

```

---

## Trigger: `trg_log_canvis_estat` (Tasca 4)

### Explicació de què fa el trigger `trg_log_canvis_estat`

Este trigger se activa automáticamente después de que se actualice un registro en la tabla `RESERVES` (`AFTER UPDATE`). Su función es comparar el estado anterior de la reserva (`OLD.en_estat_reserva`) con el nuevo estado (`NEW.en_estat_reserva`). Si detecta que ha habido un cambio de estado, inserta automáticamente un nuevo registro en una tabla de auditoría (`LOG_ESTATS_RESERVA`), guardando el ID de la reserva, el estado viejo, el estado nuevo y la fecha y hora exactas del cambio.

### Comanda creació trigger

```sql
CREATE TABLE IF NOT EXISTS LOG_ESTATS_RESERVA (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT,
    estat_anterior VARCHAR(50),
    estat_nou VARCHAR(50),
    data_canvi DATETIME
);

DELIMITER $$

CREATE TRIGGER trg_log_canvis_estat
AFTER UPDATE
ON RESERVES
FOR EACH ROW
BEGIN
    IF OLD.en_estat_reserva != NEW.en_estat_reserva THEN
        INSERT INTO LOG_ESTATS_RESERVA (
            id_reserva,
            estat_anterior,
            estat_nou,
            data_canvi
        ) VALUES (
            NEW.id_reserva,
            OLD.en_estat_reserva,
            NEW.en_estat_reserva,
            NOW()
        );

    END IF;
END $$

DELIMITER ;

```

### Sortida de la comanda `CREATE TRIGGER`

```sql
mysql>
mysql> CREATE TABLE IF NOT EXISTS LOG_ESTATS_RESERVA (
    ->     id_log INT AUTO_INCREMENT PRIMARY KEY,
    ->     id_reserva INT,
    ->     estat_anterior VARCHAR(50),
    ->     estat_nou VARCHAR(50),
    ->     data_canvi DATETIME
    -> );
Query OK, 0 rows affected (0.04 sec)

mysql>
mysql> DELIMITER $$
mysql>
mysql> CREATE TRIGGER trg_log_canvis_estat
    -> AFTER UPDATE
    -> ON RESERVES
    -> FOR EACH ROW
    -> BEGIN
    ->     IF OLD.en_estat_reserva != NEW.en_estat_reserva THEN
    ->         INSERT INTO LOG_ESTATS_RESERVA (
    ->             id_reserva,
    ->             estat_anterior,
    ->             estat_nou,
    ->             data_canvi
    ->         ) VALUES (
    ->             NEW.id_reserva,
    ->             OLD.en_estat_reserva,
    ->             NEW.en_estat_reserva,
    ->             NOW()
    ->         );
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.02 sec)

mysql>
mysql> DELIMITER ;
mysql>

```

### Demostració de que el trigger `trg_log_canvis_estat` funciona

Para demostrar que el trigger funciona, ejecutamos primero una consulta para ver el estado actual de la reserva `1`. Observamos que tiene el estado 'pendent'. También consultamos la tabla `LOG_ESTATS_RESERVA` para comprobar que actualmente está vacía.

### Comanda a executar

```sql
SELECT id_reserva, en_estat_reserva
FROM RESERVES
WHERE id_reserva = 1;

...

SELECT *
FROM LOG_ESTATS_RESERVA;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_reserva, en_estat_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 1;
+------------+------------------+
| id_reserva | en_estat_reserva |
+------------+------------------+
|          1 | pendent          |
+------------+------------------+
1 row in set (0.01 sec)

mysql>

mysql> SELECT *
    -> FROM LOG_ESTATS_RESERVA;
Empty set (0.00 sec)

mysql>

```

### Comanda a executar per activar el trigger `trg_log_canvis_estat`

Imaginando que la reserva `1` ya está totalmente pagada, haremos un `UPDATE` para pasar su estado a 'checkout'. Esto activara el trigger por el `AFTER UPDATE` en segundo plano para guardar el registro del cambio.

### Comanda a executar

```sql
UPDATE RESERVES
SET en_estat_reserva = 'checkout'
WHERE id_reserva = 1;

```

### Sortida de la comanda executada

```sql
mysql> UPDATE RESERVES
    -> SET en_estat_reserva = 'checkout'
    -> WHERE id_reserva = 1;
Query OK, 1 row affected (0.02 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql>

```

### Comanda a executar per veure que s'activat el trigger `trg_log_canvis_estat`

Habiendo cambiado el estado de la reserva, consultamos de nuevo la tabla `LOG_ESTATS_RESERVA`. Comprobaremos que el trigger ha insertado automáticamente una fila registrando que la reserva pasó de 'confirmada' a 'checkout' con su fecha y hora correspondientes.

### Comanda a executar

```sql
SELECT *
FROM LOG_ESTATS_RESERVA;

```

### Sortida de la comanda executada

```sql
mysql> SELECT *
    -> FROM LOG_ESTATS_RESERVA;
+--------+------------+----------------+-----------+---------------------+
| id_log | id_reserva | estat_anterior | estat_nou | data_canvi          |
+--------+------------+----------------+-----------+---------------------+
|      1 |          1 | pendent        | checkout  | 2026-05-19 11:09:10 |
+--------+------------+----------------+-----------+---------------------+
1 row in set (0.00 sec)

mysql>

```

---

## Trigger: `trg_log_modificacions_sospitoses` (Tasca 5)

### Explicació de què fa el trigger `trg_log_modificacions_sospitoses`

Este trigger se activa automáticamente después de que se actualice un registro en la tabla `PAGAMENTS` con (`AFTER UPDATE`). Su función principal es la seguridad: compara el importe anterior del pago (`OLD.import_paga`) con el importe modificado (`NEW.import_paga`). Si detecta que alguien ha alterado la cantidad de dinero, guarda automáticamente un registro en la tabla `LOG_MODIFICACIONS_PAGAMENTS` indicando el ID del pago, el importe viejo, el importe nuevo y la fecha de la alteración.

### Comanda creació trigger

```sql
CREATE TABLE IF NOT EXISTS LOG_MODIFICACIONS_PAGAMENTS (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_paga INT,
    import_antic DECIMAL(10,2),
    import_nou DECIMAL(10,2),
    data_modificacio DATETIME
);

DELIMITER $$

CREATE TRIGGER trg_log_modificacions_sospitoses
AFTER UPDATE
ON PAGAMENTS
FOR EACH ROW
BEGIN
    IF OLD.import_paga != NEW.import_paga THEN

        INSERT INTO LOG_MODIFICACIONS_PAGAMENTS (
            id_paga,
            import_antic,
            import_nou,
            data_modificacio
        ) VALUES (
            NEW.id_paga,
            OLD.import_paga,
            NEW.import_paga,
            NOW()
        );

    END IF;
END $$

DELIMITER ;

```

### Sortida de la comanda `CREATE TRIGGER`

```sql
mysql> CREATE TABLE IF NOT EXISTS LOG_MODIFICACIONS_PAGAMENTS (
    ->     id_log INT AUTO_INCREMENT PRIMARY KEY,
    ->     id_paga INT,
    ->     import_antic DECIMAL(10,2),
    ->     import_nou DECIMAL(10,2),
    ->     data_modificacio DATETIME
    -> );
Query OK, 0 rows affected (0.02 sec)

mysql>
mysql> DELIMITER $$
mysql>
mysql> CREATE TRIGGER trg_log_modificacions_sospitoses
    -> AFTER UPDATE
    -> ON PAGAMENTS
    -> FOR EACH ROW
    -> BEGIN
    ->     IF OLD.import_paga != NEW.import_paga THEN
    ->         INSERT INTO LOG_MODIFICACIONS_PAGAMENTS (
    ->             id_paga, import_antic, import_nou, data_modificacio
    ->         ) VALUES (
    ->             NEW.id_paga, OLD.import_paga, NEW.import_paga, NOW()
    ->         );
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.01 sec)

mysql>
mysql> DELIMITER ;
mysql>

```

### Demostració de que el trigger `trg_log_modificacions_sospitoses` funciona

Para demostrar que el trigger funciona, ejecutamos primero una consulta para ver un pago existente (el pago `1`), que actualmente tiene un importe de 100.00€. También consultamos nuestra nueva tabla de auditoría para comprobar que está vacía.

### Comanda a executar

```sql
SELECT id_paga, import_paga
FROM PAGAMENTS
WHERE id_paga = 1;

...

SELECT *
FROM LOG_MODIFICACIONS_PAGAMENTS;

```

### Sortida de la comanda executada

```sql
mysql> SELECT id_paga, import_paga
    -> FROM PAGAMENTS
    -> WHERE id_paga = 1;
+---------+-------------+
| id_paga | import_paga |
+---------+-------------+
|       1 |      100.00 |
+---------+-------------+
1 row in set (0.00 sec)

mysql>

mysql> SELECT *
    -> FROM LOG_MODIFICACIONS_PAGAMENTS;
Empty set (0.00 sec)

```

### Comanda a executar per activar el trigger `trg_log_modificacions_sospitoses`

Simularemos una modificación sospechosa: alguien intenta modificar el pago en la base de datos, cambiando el importe original de 100.00€ a solo 10.00€ utilizando `UPDATE`.

### Comanda a executar

```sql
UPDATE PAGAMENTS
SET import_paga = 10.00
WHERE id_paga = 1;

```

### Sortida de la comanda executada

```sql
mysql> UPDATE PAGAMENTS
    -> SET import_paga = 10.00
    -> WHERE id_paga = 1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

```

### Comanda a executar per veure que s'activat el trigger `trg_log_modificacions_sospitoses`

Habiendo realizado el cambio, consultamos la tabla `LOG_MODIFICACIONS_PAGAMENTS`. Comprobaremos que el trigger ha detectado la diferencia en los importes y ha guardado un registro exacto de la "trampa".

### Comanda a executar

```sql
SELECT *
FROM LOG_MODIFICACIONS_PAGAMENTS;

```

### Sortida de la comanda executada

```sql
mysql> SELECT *
    -> FROM LOG_MODIFICACIONS_PAGAMENTS;
+--------+---------+--------------+------------+---------------------+
| id_log | id_paga | import_antic | import_nou | data_modificacio    |
+--------+---------+--------------+------------+---------------------+
|      1 |       1 |       100.00 |      10.00 | 2026-05-19 11:21:36 |
+--------+---------+--------------+------------+---------------------+
1 row in set (0.00 sec)

```