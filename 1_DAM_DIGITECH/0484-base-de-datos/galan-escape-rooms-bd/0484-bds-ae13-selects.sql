-- 1 - MOSTRAR TOTES LES DADES DE LES SEUS EXISTENTS
SELECT * FROM SEUS;

-- 2 MOSTRA UNICAMENT EL NOM I LA CIUTAT DE TOTES LES SEUS
SELECT nom_seu as Nom, ciutat_seu as Ciutat FROM SEUS;

-- 3 MOSTRA TOTES LES DADES DELS PARTICIPANTS REGISTRATS A LA BASE DE DADES
SELECT * FROM PARTICIPANTS;

-- 4 MOSTRA EL NOM I COFNOM DELS PARTICIPANTS NASCUTS DESPRÉS DE L'1 DE GENER DE 2000
SELECT nom_parti as Nom, cognom_parti as Cognom FROM PARTICIPANTS
WHERE data_naixement >= '2000-01-01';

-- mostra totes les dades dels equips existents
SELECT * FROM EQUIPS;

-- mostra el nom i la tematica de les sales amb dificultat "Dificil"
SELECT nom_sala, tematica FROM SALES
WHERE dificultat = 'Difícil';

-- sales amb més de 6 participants
SELECT nom_sala, max_parti FROM SALES
WHERE max_parti > 6;

-- mostra les sales que permeten més de 6 participants
SELECT nom_sala, max_parti FROM SALES
WHERE max_parti > 6;



