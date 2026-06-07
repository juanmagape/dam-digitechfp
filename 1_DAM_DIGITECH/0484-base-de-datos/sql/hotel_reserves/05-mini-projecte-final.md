# Consultes avançades

## TASCA 1:

### Explicació:

En la tasca 1 se tendrá que hacer un select, en el que se muestre las reservas, los clientes, el importe total, el total pagado y el importe pendiente de forma descendiente.

### Codi SQL

```
SELECT 
    res.id_reserva as 'Reserva',
    CONCAT(cl.nom_client, ' ', cl.cognoms_client) as 'Client',
    res.imp_tot_reserva AS 'Import total',
    IFNULL(SUM(pag.import_paga), 0) AS 'Total Pagat',
    (res.imp_tot_reserva - IFNULL(SUM(pag.import_paga), 0)) AS 'Import Pendent'
FROM RESERVES res
JOIN CLIENTS cl ON res.id_client = cl.id_client
LEFT JOIN PAGAMENTS pag ON res.id_reserva = pag.id_reserva
GROUP BY 
    res.id_reserva, 
    cl.nom_client, 
    cl.cognoms_client, 
    res.imp_tot_reserva
ORDER BY `Import Pendent` DESC;
```

### Execució real

```
mysql> SELECT
    ->     res.id_reserva,
    ->     CONCAT(cl.nom_client, ' ', cl.cognoms_client) as 'Client',
    ->     res.imp_tot_reserva AS 'Import total',
    ->     IFNULL(SUM(pag.import_paga), 0) AS 'Total Pagat',
    ->     (res.imp_tot_reserva - IFNULL(SUM(pag.import_paga), 0)) AS 'Import Pendent'
    -> FROM RESERVES res
    -> JOIN CLIENTS cl ON res.id_client = cl.id_client
    -> LEFT JOIN PAGAMENTS pag ON res.id_reserva = pag.id_reserva
    -> GROUP BY
    ->     res.id_reserva,
    ->     cl.nom_client,
    ->     cl.cognoms_client,
    ->     res.imp_tot_reserva
    -> ORDER BY `Import Pendent` DESC;
```

#### Sortida real de MysQL

```
+---------+--------------------+--------------+-------------+----------------+
| Reserva | Client             | Import total | Total Pagat | Import Pendent |
+---------+--------------------+--------------+-------------+----------------+
|       2 | Marta Casals Riera |       763.13 |      100.00 |         663.13 |
|       3 | Pau Vidal Costa    |       917.13 |      500.00 |         417.13 |
|       1 | Joan Serra Puig    |       306.90 |      216.90 |          90.00 |
|       4 | Laia Roca Torres   |       143.00 |      143.00 |           0.00 |
+---------+--------------------+--------------+-------------+----------------+
4 rows in set (0.00 sec)

mysql>
```

----


## TASCA 2:

### Explicació:

En la tasca 2 se tendrá que hacer un select, en el que se muestre las habitaciones, el número total de las reservas, incluyendo las habitaciones sin reserva.

### Codi SQL

```
SELECT 
    hab.num_habitacio AS 'Habitació',
    COUNT(res.id_reserva) AS 'N Total reserves'
    FROM HABITACIONS hab
    LEFT JOIN RESERVES res ON hab.id_habitacio = res.id_habitacio
GROUP BY 
    hab.id_habitacio, 
    hab.num_habitacio
ORDER BY `N Total reserves`;
```

### Execució real

```
mysql> SELECT
    ->     hab.num_habitacio AS 'Habitaci',
    ->     COUNT(res.id_reserva) AS 'N Total reserves'
    ->     FROM HABITACIONS hab
    ->     LEFT JOIN RESERVES res ON hab.id_habitacio = res.id_habitacio
    -> GROUP BY
    ->     hab.id_habitacio,
    ->     hab.num_habitacio
    -> ORDER BY `N Total reserves`;

```

#### Sortida real de MysQL

```
+----------+------------------+
| Habitaci | N Total reserves |
+----------+------------------+
| 103      |                0 |
| 202      |                0 |
| 101      |                1 |
| 102      |                1 |
| 201      |                1 |
| 301      |                1 |
+----------+------------------+
6 rows in set (0.01 sec)

mysql>

```

----

## TASCA 3:

### Explicació:

En la tasca 3 se tendrán que mostrar los clientes que hayan reservado y que el total gastado sea superior a 500€. Si es superior a 500€, se mostrará el total gastado.  

### Codi SQL

```
SELECT 
    CONCAT(cl.nom_client, ' ', cl.cognoms_client) as 'Client',
    SUM(res.imp_tot_reserva) AS 'Import Total'
FROM CLIENTS cl
JOIN RESERVES res ON cl.id_client = res.id_client 
GROUP BY 
    cl.id_client,
    cl.nom_client,
    cl.cognoms_client
HAVING `Import total` > 500
ORDER BY `Import Total` DESC;
```

### Execució real

```
mysql> SELECT
    ->     CONCAT(cl.nom_client, ' ', cl.cognoms_client) as 'Client',
    ->     SUM(res.imp_tot_reserva) AS 'Import Total'
    -> FROM CLIENTS cl
    -> JOIN RESERVES res ON cl.id_client = res.id_client
    -> GROUP BY
    ->     cl.id_client,
    ->     cl.nom_client,
    ->     cl.cognoms_client
    -> HAVING `Import total` > 500
    -> ORDER BY `Import Total` DESC;
```

#### Sortida real de MysQL

```
+--------------------+--------------+
| Client             | Import Total |
+--------------------+--------------+
| Pau Vidal Costa    |       917.13 |
| Marta Casals Riera |       763.13 |
+--------------------+--------------+
2 rows in set (0.00 sec)

mysql>

```

# Vistes

## TASCA 4:

### Explicació: Se ha de crear la vista en la que muestre los clientes, la habitación, el estado y el importe pendiente de una reserva. Solo se pueden mostrar reservas con importes pendientes.

### Codi SQL

```

CREATE OR REPLACE VIEW v_reserves_pendents AS
SELECT 
    CONCAT(cli.nom_client, ' ', cli.cognoms_client) AS 'Client',
    hab.num_habitacio AS 'Habitacio',
    res.en_estat_reserva AS 'Estat',
    (res.imp_tot_reserva - IFNULL(SUM(pag.import_paga), 0)) AS 'Import pendent' 
FROM RESERVES res
JOIN CLIENTS cli ON res.id_client = cli.id_client
JOIN HABITACIONS hab ON res.id_habitacio = hab.id_habitacio
LEFT JOIN PAGAMENTS pag ON res.id_reserva = pag.id_reserva
GROUP BY
    res.id_reserva,
    cli.nom_client,
    cli.cognoms_client,
    hab.num_habitacio,
    res.en_estat_reserva,
    res.imp_tot_reserva
HAVING 
    `Import pendent` > 0;
```

### Execució real

```
mysql> CREATE OR REPLACE VIEW v_reserves_pendents AS
    -> SELECT
    ->     CONCAT(cli.nom_client, ' ', cli.cognoms_client) AS 'Client',
    ->     hab.num_habitacio AS 'Habitacio',
    ->     res.en_estat_reserva AS 'Estat',
    ->     (res.imp_tot_reserva - IFNULL(SUM(pag.import_paga), 0)) AS 'Import pendent'
    -> FROM RESERVES res
    -> JOIN CLIENTS cli ON res.id_client = cli.id_client
    -> JOIN HABITACIONS hab ON res.id_habitacio = hab.id_habitacio
    -> LEFT JOIN PAGAMENTS pag ON res.id_reserva = pag.id_reserva
    -> GROUP BY
    ->     res.id_reserva,
    ->     cli.nom_client,
    ->     cli.cognoms_client,
    ->     hab.num_habitacio,
    ->     res.en_estat_reserva,
    ->     res.imp_tot_reserva
    -> HAVING
    ->     `Import pendent` > 0;
Query OK, 0 rows affected (0.03 sec)

```

#### Sortida real de MysQL

```
mysql> select * from v_reserves_pendents;
+--------------------+-----------+------------+----------------+
| Client             | Habitacio | Estat      | Import pendent |
+--------------------+-----------+------------+----------------+
| Joan Serra Puig    | 102       | checkout   |          90.00 |
| Marta Casals Riera | 201       | confirmada |         663.13 |
| Pau Vidal Costa    | 301       | confirmada |         417.13 |
+--------------------+-----------+------------+----------------+
3 rows in set (0.04 sec)

```

## TASCA 5:

### Explicació: Se ha de crear una vista en la que se muestre el cliente, el número de reservas que ha realizado, el total pagado y el importe pendiente acumulado.

### Codi SQL

```

CREATE OR REPLACE VIEW v_resum_clients AS
SELECT 
    CONCAT(cli.nom_client, ' ', cli.cognoms_client) AS 'Client',
    COUNT(res.id_reserva) AS 'Nombre reserves',
    COALESCE(SUM(pag.total_pagat), 0) AS 'Total pagat',
    COALESCE(SUM(res.imp_tot_reserva), 0) - COALESCE(SUM(pag.total_pagat), 0) AS 'Import pendent acumulat'

FROM CLIENTS cli
LEFT JOIN RESERVES res ON cli.id_client = res.id_client
LEFT JOIN (
        SELECT id_reserva, SUM(import_paga) AS total_pagat
        FROM PAGAMENTS
        GROUP BY id_reserva
) pag ON res.id_reserva = pag.id_reserva
GROUP BY 
    cli.id_client, 
    cli.nom_client, 
    cli.cognoms_client;
```

### Execució real

```
mysql> CREATE OR REPLACE VIEW v_resum_clients AS
    -> SELECT
    ->     CONCAT(cli.nom_client, ' ', cli.cognoms_client) AS 'Client',
    ->     COUNT(res.id_reserva) AS 'Nombre reserves',
    ->     COALESCE(SUM(pag.total_pagat), 0) AS 'Total pagat',
    ->     COALESCE(SUM(res.imp_tot_reserva), 0) - COALESCE(SUM(pag.total_pagat), 0) AS 'Import pendent acumulat'
    ->
    -> FROM CLIENTS cli
    -> LEFT JOIN RESERVES res ON cli.id_client = res.id_client
    -> LEFT JOIN (
    ->         SELECT id_reserva, SUM(import_paga) AS total_pagat
    ->         FROM PAGAMENTS
    ->         GROUP BY id_reserva
    -> ) pag ON res.id_reserva = pag.id_reserva
    -> GROUP BY
    ->     cli.id_client,
    ->     cli.nom_client,
    ->     cli.cognoms_client;
Query OK, 0 rows affected (0.04 sec)

```

#### Sortida real de MysQL

```
mysql> select * from v_resum_clients;
+--------------------+-----------------+-------------+-------------------------+
| Client             | Nombre reserves | Total pagat | Import pendent acumulat |
+--------------------+-----------------+-------------+-------------------------+
| Joan Serra Puig    |               1 |      216.90 |                   90.00 |
| Marta Casals Riera |               1 |      100.00 |                  663.13 |
| Pau Vidal Costa    |               1 |      500.00 |                  417.13 |
| Laia Roca Torres   |               1 |      143.00 |                    0.00 |
| Nil Ferrer Soler   |               0 |        0.00 |                    0.00 |
+--------------------+-----------------+-------------+-------------------------+
5 rows in set (0.02 sec)

mysql>

```

# Triggers

## TASCA 6

### Explicació: Crear un trigger que impida el overbooking de una misma habitación en mismas fechas.

### Codi SQL

```
DELIMITER $$

CREATE TRIGGER trg_impedir_overbooking
BEFORE INSERT ON RESERVES
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM RESERVES
        WHERE id_habitacio = NEW.id_habitacio
          AND en_estat_reserva NOT IN ('cancel_lada', 'checkout')
          AND NEW.data_entra_reserva < data_surt_reserva
          AND NEW.data_surt_reserva > data_entra_reserva
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Habitacio ocupada en aquestes dates';
    END IF;
END $$

DELIMITER ;
```

### Execució real

```
mysql> DELIMITER $$
mysql>
mysql> CREATE TRIGGER trg_impedir_overbooking
    -> BEFORE INSERT ON RESERVES
    -> FOR EACH ROW
    -> BEGIN
    ->     IF EXISTS (
    ->         SELECT 1
    ->         FROM RESERVES
    ->         WHERE id_habitacio = NEW.id_habitacio
    ->           AND en_estat_reserva NOT IN ('cancel_lada', 'checkout')
    ->           AND NEW.data_entra_reserva < data_surt_reserva
    ->           AND NEW.data_surt_reserva > data_entra_reserva
    ->     ) THEN
    ->         SIGNAL SQLSTATE '45000'
    ->         SET MESSAGE_TEXT = 'Error: Habitacio ocupada en aquestes dates';
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.04 sec)

mysql>
mysql> DELIMITER ;

```

#### Sortida real de MysQL

```
mysql> SELECT id_reserva, id_habitacio, data_entra_reserva, data_surt_reserva, en_estat_reserva
    -> FROM RESERVES
    -> WHERE id_habitacio = 1;
+------------+--------------+--------------------+-------------------+------------------+
| id_reserva | id_habitacio | data_entra_reserva | data_surt_reserva | en_estat_reserva |
+------------+--------------+--------------------+-------------------+------------------+
|          4 |            1 | 2026-04-28         | 2026-04-30        | confirmada       |
+------------+--------------+--------------------+-------------------+------------------+
1 row in set (0.02 sec)

mysql> INSERT INTO RESERVES (id_client, id_habitacio, data_entra_reserva, data_surt_reserva, adults_reserva, menors_reserva)
    -> VALUES (1, 1, '2026-04-29', '2026-05-02', 2, 0);
ERROR 1644 (45000): Error: Habitacio ocupada en aquestes dates
mysql>

```

## TASCA 7

### Explicació: Crear un trigger que impida el pago de un valor superior al importe pendiente.

### Codi SQL

```
DELIMITER $$

CREATE TRIGGER trg_impedeix_sobrepagament
BEFORE INSERT ON PAGAMENTS
FOR EACH ROW
BEGIN
    DECLARE v_pendent DECIMAL(10,2);

    SELECT imp_tot_reserva - IFNULL((SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = NEW.id_reserva), 0)
    INTO v_pendent
    FROM RESERVES
    WHERE id_reserva = NEW.id_reserva;

    IF NEW.import_paga > v_pendent THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: L''import del pagament supera el deute pendent.';
    END IF;
END $$

DELIMITER ;
```

### Execució real

```
mysql> DELIMITER $$
mysql>
mysql> CREATE TRIGGER trg_impedeix_sobrepagament
    -> BEFORE INSERT ON PAGAMENTS
    -> FOR EACH ROW
    -> BEGIN
    ->     DECLARE v_pendent DECIMAL(10,2);
    ->
    ->     SELECT imp_tot_reserva - IFNULL((SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = NEW.id_reserva), 0)
    ->     INTO v_pendent
    ->     FROM RESERVES
    ->     WHERE id_reserva = NEW.id_reserva;
    ->
    ->     IF NEW.import_paga > v_pendent THEN
    ->         SIGNAL SQLSTATE '45000'
    ->         SET MESSAGE_TEXT = 'Error: L''import del pagament supera el deute pendent.';
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.04 sec)

mysql>
mysql> DELIMITER ;

```

#### Sortida real de MysQL

```
mysql> SELECT * FROM v_reserves_pendents WHERE `Client` LIKE '%Joan Serra%';
+-----------------+-----------+----------+----------------+
| Client          | Habitacio | Estat    | Import pendent |
+-----------------+-----------+----------+----------------+
| Joan Serra Puig | 102       | checkout |          90.00 |
+-----------------+-----------+----------+----------------+
1 row in set (0.00 sec)

mysql> INSERT INTO PAGAMENTS (id_reserva, import_paga)
    -> VALUES (1, 150.00);
ERROR 1644 (45000): Error: L'import del pagament supera el deute pendent.
mysql>

```

# Triggers

## TASCA 8

### Explicació: Crear un trigger que registre un log en cada vez que se hace un cambio de un estado de una reserva.

### Codi SQL

```
DELIMITER $$
CREATE TRIGGER trg_log_canvi_estat_reserva
AFTER UPDATE ON RESERVES
FOR EACH ROW
BEGIN
    IF OLD.en_estat_reserva != NEW.en_estat_reserva THEN
        INSERT INTO LOG_ESTATS_RESERVA (id_reserva, estat_anterior, estat_nou, data_canvi) 
        VALUES (NEW.id_reserva, OLD.en_estat_reserva, NEW.en_estat_reserva, NOW());
    END IF;
END $$
DELIMITER ;
```

### Execució real

```
mysql> DELIMITER $$
mysql> CREATE TRIGGER trg_log_canvi_estat_reserva
    -> AFTER UPDATE ON RESERVES
    -> FOR EACH ROW
    -> BEGIN
    ->     IF OLD.en_estat_reserva != NEW.en_estat_reserva THEN
    ->         INSERT INTO LOG_ESTATS_RESERVA (id_reserva, estat_anterior, estat_nou, data_canvi)
    ->         VALUES (NEW.id_reserva, OLD.en_estat_reserva, NEW.en_estat_reserva, NOW());
    ->     END IF;
    -> END $$
Query OK, 0 rows affected (0.04 sec)

mysql> DELIMITER ;
mysql>

```

#### Sortida real de MysQL

```
mysql> SELECT id_reserva, en_estat_reserva FROM RESERVES WHERE id_reserva = 4;
+------------+------------------+
| id_reserva | en_estat_reserva |
+------------+------------------+
|          4 | confirmada       |
+------------+------------------+
1 row in set (0.01 sec)

mysql> SELECT * FROM LOG_ESTATS_RESERVA WHERE id_reserva = 4;
Empty set (0.01 sec)

mysql> UPDATE RESERVES SET en_estat_reserva = 'checkin' WHERE id_reserva = 4;
Query OK, 1 row affected (0.03 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> SELECT * FROM LOG_ESTATS_RESERVA WHERE id_reserva = 4;
+--------+------------+----------------+-----------+---------------------+
| id_log | id_reserva | estat_anterior | estat_nou | data_canvi          |
+--------+------------+----------------+-----------+---------------------+
|      2 |          4 | confirmada     | checkin   | 2026-05-28 08:31:12 |
+--------+------------+----------------+-----------+---------------------+
1 row in set (0.01 sec)


```


# Procedures

## TASCA 9

### Explicació: Tenemos que crear un procedure, el cual no se pueda hacer un importe menor o igual a 0, y en caso de que se haga la llamada y funcione, simplemente se hará el insert automaticamente en los pagos, juntamente con en caso de que no esté confirmada, se confirmará la reserva.

### Codi SQL

```
DELIMITER $$

CREATE PROCEDURE sp_afegir_pagament(
    IN p_id_reserva INT, 
    IN p_import DECIMAL(10,2), 
    IN p_metode VARCHAR(50), 
    IN p_descripcio VARCHAR(255)
)
BEGIN
    IF p_import <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: L''import ha de ser superior a 0.';
    END IF;

    INSERT INTO PAGAMENTS (id_reserva, import_paga, metode_paga, concepte_paga) 
    VALUES (p_id_reserva, p_import, p_metode, p_descripcio);

    UPDATE RESERVES 
    SET en_estat_reserva = 'confirmada' 
    WHERE id_reserva = p_id_reserva 
      AND en_estat_reserva = 'pendent'
      AND imp_tot_reserva <= (
          SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = p_id_reserva
      );

END $$

DELIMITER ;

```

### Execució real

```
DELIMITER ;mysql>
mysql> CREATE PROCEDURE sp_afegir_pagament(
    ->     IN p_id_reserva INT,
    ->     IN p_import DECIMAL(10,2),
    ->     IN p_metode VARCHAR(50),
    ->     IN p_descripcio VARCHAR(255)
    -> )
    -> BEGIN
    ->     IF p_import <= 0 THEN
    ->         SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: L''import ha de ser superior a 0.';
    ->     END IF;
    ->
    ->     INSERT INTO PAGAMENTS (id_reserva, import_paga, metode_paga, concepte_paga)
    ->     VALUES (p_id_reserva, p_import, p_metode, p_descripcio);
    ->
    ->     UPDATE RESERVES
    ->     SET en_estat_reserva = 'confirmada'
    ->     WHERE id_reserva = p_id_reserva
    ->       AND en_estat_reserva = 'pendent'
    ->       AND imp_tot_reserva <= (
    ->           SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = p_id_reserva
    ->       );
    ->
    -> END $$
Query OK, 0 rows affected (0.04 sec)

mysql>
mysql> DELIMITER ;
mysql>


```

#### Sortida real de MysQL

```
mysql> SELECT id_reserva, en_estat_reserva, imp_tot_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 2;
+------------+------------------+-----------------+
| id_reserva | en_estat_reserva | imp_tot_reserva |
+------------+------------------+-----------------+
|          2 | confirmada       |          763.13 |
+------------+------------------+-----------------+
1 row in set (0.00 sec)

mysql> SELECT id_paga, import_paga, metode_paga, concepte_paga
    -> FROM PAGAMENTS
    -> WHERE id_reserva = 2;
+---------+-------------+-------------+------------------+
| id_paga | import_paga | metode_paga | concepte_paga    |
+---------+-------------+-------------+------------------+
|       6 |      100.00 | bizum       | Pagament parcial |
|      12 |      100.00 | bizum       | Pagament parcial |
|      13 |      100.00 | bizum       | Pagament parcial |
+---------+-------------+-------------+------------------+
3 rows in set (0.00 sec)

mysql> CALL sp_afegir_pagament(
    ->     2,
    ->     100,
    ->     'bizum',
    ->     'Pagament parcial'
    -> );
Query OK, 0 rows affected (0.01 sec)

mysql> SELECT id_reserva, en_estat_reserva, imp_tot_reserva
    -> FROM RESERVES
    -> WHERE id_reserva = 2;
+------------+------------------+-----------------+
| id_reserva | en_estat_reserva | imp_tot_reserva |
+------------+------------------+-----------------+
|          2 | confirmada       |          763.13 |
+------------+------------------+-----------------+
1 row in set (0.01 sec)

mysql> SELECT id_paga, import_paga, metode_paga, concepte_paga
    -> FROM PAGAMENTS
    -> WHERE id_reserva = 2;
+---------+-------------+-------------+------------------+
| id_paga | import_paga | metode_paga | concepte_paga    |
+---------+-------------+-------------+------------------+
|       6 |      100.00 | bizum       | Pagament parcial |
|      12 |      100.00 | bizum       | Pagament parcial |
|      13 |      100.00 | bizum       | Pagament parcial |
|      14 |      100.00 | bizum       | Pagament parcial |
+---------+-------------+-------------+------------------+
4 rows in set (0.00 sec)

mysql>
```

## TASCA 10

### Explicació: se ha creado un procedure que comprueba automáticamente si el cliente tiene pagos pendientes calculando la diferencia entre el precio de la reserva y el total de sus pagos. Si la deuda es mayor a 0, bloquea la acción lanzando un error. Si no hay deuda, actualiza el estado de la reserva a 'checkout'.

### Codi SQL

```
DELIMITER $$

CREATE PROCEDURE sp_checkout_reserva(IN p_id_reserva INT)
BEGIN
    IF (SELECT imp_tot_reserva FROM RESERVES WHERE id_reserva = p_id_reserva) > 
       IFNULL((SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = p_id_reserva), 0) THEN
       
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Hi ha pagaments pendents. No es pot fer el checkout.';
    END IF;

    UPDATE RESERVES SET en_estat_reserva = 'checkout' WHERE id_reserva = p_id_reserva;

END $$

DELIMITER ;
```

### Execució real

```
mysql> DELIMITER $$
mysql>
mysql> CREATE PROCEDURE sp_checkout_reserva(IN p_id_reserva INT)
    -> BEGIN
    ->     IF (SELECT imp_tot_reserva FROM RESERVES WHERE id_reserva = p_id_reserva) >
    ->        IFNULL((SELECT SUM(import_paga) FROM PAGAMENTS WHERE id_reserva = p_id_reserva), 0) THEN
    ->
    ->         SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Hi ha pagaments pendents. No es pot fer el checkout.';
    ->     END IF;
    ->
    ->     UPDATE RESERVES SET en_estat_reserva = 'checkout' WHERE id_reserva = p_id_reserva;
    ->
    -> END $$
Query OK, 0 rows affected (0.04 sec)

mysql>
mysql> DELIMITER ;
mysql>

```

#### Sortida real de MysQL

```
mysql> SELECT id_reserva, en_estat_reserva, imp_tot_reserva FROM RESERVES WHERE id_reserva = 3;
+------------+------------------+-----------------+
| id_reserva | en_estat_reserva | imp_tot_reserva |
+------------+------------------+-----------------+
|          3 | confirmada       |          917.13 |
+------------+------------------+-----------------+
1 row in set (0.00 sec)

mysql>


mysql> CALL sp_checkout_reserva(3);
ERROR 1644 (45000): Error: Hi ha pagaments pendents. No es pot fer el checkout.
mysql>

-------------

mysql> SELECT id_reserva, en_estat_reserva FROM RESERVES WHERE id_reserva = 2;
+------------+------------------+
| id_reserva | en_estat_reserva |
+------------+------------------+
|          2 | confirmada       |
+------------+------------------+
1 row in set (0.00 sec)

mysql> CALL sp_checkout_reserva(2);
Query OK, 1 row affected (0.04 sec)

mysql> SELECT id_reserva, en_estat_reserva FROM RESERVES WHERE id_reserva = 2;
+------------+------------------+
| id_reserva | en_estat_reserva |
+------------+------------------+
|          2 | checkout         |
+------------+------------------+
1 row in set (0.00 sec)

mysql>

```

# Reflexió final

En general lo que he aprendido es que la automatización de un negocio se puede hacer facilmente con SQL puro, creando los triggers, procedures, en la propia base de datos.
Asi tambien se garantiza la seguridad de los datos que entran y no hace falta estar dependiendo de la seguridad o integridad de datos que se aplique en la aplicación que se desarrolle.
He aprendido que el procedure es como una función en bloque, que sirve para agrupar consultas, en el que se puede ejecutar aceptando parametros cuando se llama.
También he aprendido que el trigger es un "gatillo", en el que se puede estar trabajando y en caso de que algo no coincida con las reglas de negocio, salte error, por ejemplo con las fechas en el overbooking.

La diferencia principal entre estos dos, es que un procedure tiene que ser llamado de forma manual para ejecutarse, mientras que el trigger, esta a la espera de alguna ejecución, para poder proceder.
