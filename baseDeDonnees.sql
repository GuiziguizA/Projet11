
INSERT INTO `livre` (`code_livre`, `auteur`, `date_de_retour`, `emplacement`, `nom`, `nombre_exemplaire`, `nombre_exemplaire_fixe`, `nombre_liste_dattente`, `section`, `type`) VALUES
(1, 'Alexandre DUMAS', '2021-01-15 22:31:03', 'emplacement1', 'les trois mousquetaires', 0, 2, 3, 'section1', 'type1'),
(2, 'Guy de Maupassant', NULL, 'emplacement1', 'Bel-ami', 1, 1, 0, 'section2', 'type2'),
(3, 'Henry Polac ', NULL, 'emplacement1', 'le bossu de notre dame', 1, 1, 0, 'section1', 'type1');

-- --------------------------------------------------------


-- Déchargement des données de la table `livre_liste_dattente`
--

INSERT INTO `livre_liste_dattente` (`livre_code_livre`, `liste_dattente`) VALUES
(1, 'marcel@gmail.com'),
(1, 'leo@gmail.com'),
(1, 'paul@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `pret`
--


--
-- Déchargement des données de la table `pret`
--

INSERT INTO `pret` (`id`, `date_de_debut`, `date_de_fin`, `date_de_rendu`, `nombre_livres`, `position`, `statut`, `id_livre`, `id_utilisateur`) VALUES
(1, '2020-12-16 22:31:03', '2021-01-15 22:31:03', NULL, 1, 0, 'encours', 1, 4),
(2, '2020-12-16 22:31:21', '2021-01-15 22:31:21', NULL, 1, 0, 'encours', 1, 3),
(3, NULL, NULL, NULL, 0, 1, 'enattente', 1, 5),
(4, NULL, NULL, NULL, 0, 2, 'enattente', 1, 7),
(5, NULL, NULL, NULL, 0, 3, 'enattente', 1, 8);

-- --------------------------------------------------------


-- Déchargement des données de la table `roles`
--

INSERT INTO `roles` (`code_role`, `nom`) VALUES
(1, 'user'),
(2, 'employe'),
(3, 'admin');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--


-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`code_utilisateur`, `adresse`, `code_postal`, `mail`, `mot_de_passe`, `nom`, `id_roles`) VALUES
(1, ' 30 rue du Lac', '93450', 'admin@gmail.com', '$2a$10$xoU7VVlGXVXLfEZ7hEqCFu1uby80nHM3WQKUQvaxIVSkbXC3FmxN6', 'admin', 3),
(2, '25 rue de la rose', '94520', 'jean@gmail.com', '$2a$10$2Yx8VqT/b9Uk542a3.UKx.ueJCP5tBkxcLfeXJBs1eSYZCT2cc3oC', 'Jean', 2),
(3, '34 rue de chretiente', '28450', 'michel@gmail.com', '$2a$10$Fy.f9vnz.XwXL8m9oGpHFOHOlmDMOz7a6nAKqmkGBodET9V9rmC2K', 'Michel', 1),
(4, '8rue de le scandinavie', '85641', 'bob@gmail.com', '$2a$10$u0oxRpEC1fiKQTlcBMt5E.gbzjjMdJAUv4tNW8JhGtsJ.FSLHEbZK', 'Bob', 1),
(5, '14 avenue du chene', '65123', 'marcel@gmail.com', '$2a$10$jgCuuSTMs4P2Bi9OcX59Du7fzHlikWTkufEa3unE7eSlN/iyhBEn6', 'Marcel', 1),
(6, '14 avenue du chene', '65123', 'julien@gmail.com', '$2a$10$1EQygruQvKNvJ0M5yc30puJ1oNzVXM4ClRYOD9/oaLPdz8DHsA/ui', 'julien', 1),
(7, '14 avenue du chene', '65123', 'leo@gmail.com', '$2a$10$kRrvdZ2Yo9L9bnnKowWb/ORP0YnRuC2W7smy8g8qEs71dNFMtRaNq', 'leo', 1),
(8, '14 avenue du chene', '65123', 'paul@gmail.com', '$2a$10$xWQWQbyXiU7rJ.9VS557pugfZtH2Qb2SyBqkP6kGRGaCKMou.926C', 'paul', 1),
(9, ' adresse7', '56776', 'batch', '$2a$10$GasKZFwGAyCOych9oIkyQ.Y3eJB4UEHqrGsdSyV0WKZJp.Uz1Z/Ka', 'batch', 1);

