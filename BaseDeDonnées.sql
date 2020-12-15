
--

INSERT INTO `livre` (`code_livre`, `auteur`, `date_de_retour`, `emplacement`, `nom`, `nombre_exemplaire`, `nombre_exemplaire_fixe`, `nombre_liste_dattente`, `section`, `type`) VALUES
(1, 'Alexandre DUMAS', NULL, 'emplacement1', 'les trois mousquetaires', 2, 2, 0, 'section1', 'type1'),
(2, 'Guy de Maupassant', NULL, 'emplacement1', 'Bel-ami', 1, 1, 0, 'section2', 'type2'),
(3, 'Henry Polac ', NULL, 'emplacement1', 'le bossu de notre dame', 1, 1, 0, 'section1', 'type1');

-- --------------------------------------------------------





INSERT INTO `roles` (`code_role`, `nom`) VALUES
(1, 'user'),
(2, 'employe'),
(3, 'admin');

-- --------------------------------------------------------



INSERT INTO `utilisateur` (`code_utilisateur`, `adresse`, `code_postal`, `mail`, `mot_de_passe`, `nom`, `id_roles`) VALUES
(1, ' 30 rue du Lac', '93450', 'admin@gmail.com', '$2a$10$xoU7VVlGXVXLfEZ7hEqCFu1uby80nHM3WQKUQvaxIVSkbXC3FmxN6', 'admin', 3),
(2, '25 rue de la rose', '94520', 'jean@gmail.com', '$2a$10$2Yx8VqT/b9Uk542a3.UKx.ueJCP5tBkxcLfeXJBs1eSYZCT2cc3oC', 'Jean', 2),
(3, '34 rue de chretiente', '28450', 'michel@gmail.com', '$2a$10$Fy.f9vnz.XwXL8m9oGpHFOHOlmDMOz7a6nAKqmkGBodET9V9rmC2K', 'Michel', 1),
(4, '8rue de le scandinavie', '85641', 'bob@laposte.com', '$2a$10$u0oxRpEC1fiKQTlcBMt5E.gbzjjMdJAUv4tNW8JhGtsJ.FSLHEbZK', 'Bob', 1),
(5, '14 avenue du chene', '65123', 'marcel@gmail.com', '$2a$10$jgCuuSTMs4P2Bi9OcX59Du7fzHlikWTkufEa3unE7eSlN/iyhBEn6', 'Marcel', 1),
(6, '14 avenue du chene', '65123', 'julien@gmail.com', '$2a$10$1EQygruQvKNvJ0M5yc30puJ1oNzVXM4ClRYOD9/oaLPdz8DHsA/ui', 'Marcel', 1),
(7, '14 avenue du chene', '65123', 'leo@gmail.com', '$2a$10$kRrvdZ2Yo9L9bnnKowWb/ORP0YnRuC2W7smy8g8qEs71dNFMtRaNq', 'Marcel', 1),
(8, '14 avenue du chene', '65123', 'paul@gmail.com', '$2a$10$xWQWQbyXiU7rJ.9VS557pugfZtH2Qb2SyBqkP6kGRGaCKMou.926C', 'Marcel', 1),
(9, ' adresse7', '56776', 'batch', '$2a$10$GasKZFwGAyCOych9oIkyQ.Y3eJB4UEHqrGsdSyV0WKZJp.Uz1Z/Ka', 'batch', 1);
