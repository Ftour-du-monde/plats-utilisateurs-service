INSERT INTO utilisateurs (nom, prenom, email, adresse)
SELECT 'Dupont', 'Alice', 'alice.dupont@example.com', '12 rue de Provence, Marseille'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateurs WHERE email = 'alice.dupont@example.com'
);

INSERT INTO utilisateurs (nom, prenom, email, adresse)
SELECT 'Martin', 'Lucas', 'lucas.martin@example.com', '8 avenue du Prado, Marseille'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateurs WHERE email = 'lucas.martin@example.com'
);

INSERT INTO utilisateurs (nom, prenom, email, adresse)
SELECT 'Bernard', 'Emma', 'emma.bernard@example.com', '4 boulevard Longchamp, Marseille'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateurs WHERE email = 'emma.bernard@example.com'
);

INSERT INTO utilisateurs (nom, prenom, email, adresse)
SELECT 'Robert', 'Nicolas', 'nicolas.robert@example.com', '15 rue Paradis, Marseille'
WHERE NOT EXISTS (
    SELECT 1 FROM utilisateurs WHERE email = 'nicolas.robert@example.com'
);

INSERT INTO plats (nom, description, prix)
SELECT 'Burger classique', 'Steak hache, cheddar, salade', 11.90
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Burger classique' AND prix = 11.90
);

INSERT INTO plats (nom, description, prix)
SELECT 'Salade mediterraneenne', 'Tomates, feta, olives, concombre', 9.50
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Salade mediterraneenne' AND prix = 9.50
);

INSERT INTO plats (nom, description, prix)
SELECT 'Salade nicoise', 'Thon, tomates, oeufs, olives, haricots verts', 10.50
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Salade nicoise' AND prix = 10.50
);

INSERT INTO plats (nom, description, prix)
SELECT 'Aioli', 'Morue, legumes de saison et sauce aioli', 13.80
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Aioli' AND prix = 13.80
);

INSERT INTO plats (nom, description, prix)
SELECT 'Gratin dauphinois', 'Pommes de terre, creme et muscade', 8.90
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Gratin dauphinois' AND prix = 8.90
);

INSERT INTO plats (nom, description, prix)
SELECT 'Poulet roti', 'Poulet fermier, herbes et pommes grenailles', 12.40
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Poulet roti' AND prix = 12.40
);

INSERT INTO plats (nom, description, prix)
SELECT 'Lasagnes maison', 'Boeuf, tomate, bechamel et parmesan', 11.60
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Lasagnes maison' AND prix = 11.60
);

INSERT INTO plats (nom, description, prix)
SELECT 'Ratatouille', 'Legumes mijotes a l huile d olive', 7.80
WHERE NOT EXISTS (
    SELECT 1 FROM plats WHERE nom = 'Ratatouille' AND prix = 7.80
);
