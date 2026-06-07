USE escape_room;

INSERT INTO SEUS (nom_seu, ciutat_seu, adressa_seu, telef_contacte_seu)
VALUES
    ('Seu Canterlot', 'Canterlot', 'Carrer del Castell Reial 1', '900111222'),
    ('Seu Ponyville', 'Ponyville', 'Plaça de l''Ajuntament', '900333444'),
    ('Seu Cloudsdale', 'Cloudsdale', 'Avinguda del Cumulonimbus 7', '900555666');

INSERT INTO PARTICIPANTS (nom_parti, cognom_parti, email_parti, data_naixement)
VALUES
    ('Twilight', 'Sparkle', 'twilight@equestria.com', '1995-12-03'),
    ('Spike', 'El Drac', 'spike@equestria.com', '1998-05-12'),
    ('Rainbow', 'Dash', 'rainbow@equestria.com', '1995-02-01'),
    ('Fluttershy', 'Posey', 'fluttershy@equestria.com', '1995-03-15'),
    ('Pinkie', 'Pie', 'pinkie@equestria.com', '1996-06-07'),
    ('Cheese', 'Sandwich', 'cheese@equestria.com', '1996-01-20'),
    ('Rarity', 'Belle', 'rarity@equestria.com', '1994-09-08'),
    ('Sweetie', 'Belle', 'sweetie@equestria.com', '2005-11-10'),
    ('Apple', 'Jack', 'aj@equestria.com', '1994-10-05'),
    ('Big', 'Macintosh', 'bigmac@equestria.com', '1992-08-25');

INSERT INTO EQUIPS (nom_equip, data_creacio, qtat_parti)
VALUES
    ('Elements de l''Harmonia', '2023-01-10', 2),
    ('The Wonderbolts', '2023-02-15', 2),
    ('Party Animals', '2023-03-20', 2),
    ('Fashionistas', '2023-04-05', 2),
    ('Sweet Apple Acres', '2023-05-12', 2);

INSERT INTO SALES (nom_sala, tematica, dificultat, durada_max, max_parti, id_seu)
VALUES
    ('Biblioteca de Celestia', 'Màgia', 'Difícil', 60, 6,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Canterlot' LIMIT 1)),

    ('Saló del Tron', 'Reialesa', 'Mitjà', 75, 5,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Canterlot' LIMIT 1)),

    ('Bosc Everfree', 'Aventures', 'Fàcil', 60, 4,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Ponyville' LIMIT 1)),

    ('Sugarcube Corner', 'Cuina', 'Mitjà', 60, 5,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Ponyville' LIMIT 1)),

    ('Fàbrica d''Arcs de Sant Martí', 'Clima', 'Difícil', 90, 6,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Cloudsdale' LIMIT 1)),

    ('Acadèmia Wonderbolt', 'Entrenament', 'Mitjà', 60, 4,
    (SELECT id_seu FROM SEUS WHERE nom_seu = 'Seu Cloudsdale' LIMIT 1));

INSERT INTO EQUIPS_PARTICIPANTS (id_equip, id_parti, data_incorp)
VALUES
    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Elements de l''Harmonia' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'twilight@equestria.com' LIMIT 1), '2023-01-10'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Elements de l''Harmonia' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'spike@equestria.com' LIMIT 1), '2023-01-10'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'The Wonderbolts' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'rainbow@equestria.com' LIMIT 1), '2023-02-15'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'The Wonderbolts' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'fluttershy@equestria.com' LIMIT 1), '2023-02-15'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Party Animals' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'pinkie@equestria.com' LIMIT 1), '2023-03-20'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Party Animals' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'cheese@equestria.com' LIMIT 1), '2023-03-20'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Fashionistas' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'rarity@equestria.com' LIMIT 1), '2023-04-05'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Fashionistas' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'sweetie@equestria.com' LIMIT 1), '2023-04-05'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Sweet Apple Acres' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'aj@equestria.com' LIMIT 1), '2023-05-12'),

    ((SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Sweet Apple Acres' LIMIT 1),
     (SELECT id_parti FROM PARTICIPANTS WHERE email_parti = 'bigmac@equestria.com' LIMIT 1), '2023-05-12');

INSERT INTO RESERVES (data_hora, estat_reserva, qtat_participants, id_sala, id_equip)
VALUES
    ('2023-10-01 17:00:00', 'Realitzada', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Biblioteca de Celestia' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Elements de l''Harmonia' LIMIT 1)),

    ('2023-10-02 18:30:00', 'Realitzada', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Bosc Everfree' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Sweet Apple Acres' LIMIT 1)),

    ('2023-10-05 16:00:00', 'Cancel·lada', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Fàbrica d''Arcs de Sant Martí' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Fashionistas' LIMIT 1)),

    ('2023-10-10 20:00:00', 'Realitzada', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Acadèmia Wonderbolt' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'The Wonderbolts' LIMIT 1)),

    ('2023-12-01 10:00:00', 'Pendent', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Sugarcube Corner' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Party Animals' LIMIT 1)),

    ('2023-10-15 19:00:00', 'Realitzada', 2,
    (SELECT id_sala FROM SALES WHERE nom_sala = 'Saló del Tron' LIMIT 1),
    (SELECT id_equip FROM EQUIPS WHERE nom_equip = 'Elements de l''Harmonia' LIMIT 1));

INSERT INTO RESULTATS (temps_utilitzat, exit_fracas, puntuacio, qtat_pistes, id_reserva)
VALUES
    (45, TRUE, 1000, 0,
    (SELECT id_reserva FROM RESERVES WHERE data_hora = '2023-10-01 17:00:00' LIMIT 1)),

    (60, FALSE, 400, 3,
    (SELECT id_reserva FROM RESERVES WHERE data_hora = '2023-10-02 18:30:00' LIMIT 1)),

    (40, TRUE, 950, 1,
    (SELECT id_reserva FROM RESERVES WHERE data_hora = '2023-10-10 20:00:00' LIMIT 1)),

    (55, TRUE, 850, 2,
    (SELECT id_reserva FROM RESERVES WHERE data_hora = '2023-10-15 19:00:00' LIMIT 1));