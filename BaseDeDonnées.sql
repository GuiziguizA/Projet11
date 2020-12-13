
--

INSERT INTO `livre` (`code_livre`, `auteur`, `date_de_retour`, `emplacement`, `nom`, `nombre_exemplaire`, `nombre_exemplaire_fixe`, `nombre_liste_dattente`, `section`, `type`) VALUES
(1, 'Alexandre DUMAS', NULL, 'emplacement1', 'les trois mousquetaires', 1, 1, 0, 'section1', 'type1'),
(2, 'Guy de Maupassant', NULL, 'emplacement1', 'Bel-ami', 1, 1, 0, 'section2', 'type2'),
(3, 'Henry Polac ', NULL, 'emplacement1', 'le bossu de notre dame', 1, 1, 0, 'section1', 'type1');

-- --------------------------------------------------------





INSERT INTO `roles` (`code_role`, `nom`) VALUES
(1, 'user'),
(2, 'employe'),
(3, 'admin');

-- --------------------------------------------------------



INSERT INTO `utilisateur` (`code_utilisateur`, `adresse`, `code_postal`, `mail`, `mot_de_passe`, `nom`, `id_roles`) VALUES
(1, ' 30 rue du Lac', '93450', 'admin@gmail.com', '$2a$10$pps9jWZcdq9DeoqytjBh2e2g1yNNPejvi5KLhd2f4uNx7RUIBAy62', 'admin', 3),
(2, '25 rue de la rose', '94520', 'jean@gmail.com', '$2a$10$.RcY56BNm29WehqAye3QaOTQS89J6NnvlBiZh5vjV9gnPMT2xgD6m', 'Jean', 2),
(3, '34 rue de chretiente', '28450', 'michel@gmail.com', '$2a$10$LMUcWAy2nwxxi8kPtR8db.yz/oc7/h.d9B8uSNGeMPzwlEC4zfMR2', 'Michel', 1),
(4, '8rue de le scandinavie', '85641', 'bob@laposte.com', '$2a$10$hBB4jFKRSb3pbmLumHsYvebjRvlDp599FLfqZRn9WQLsPIHmEaDgq', 'Bob', 1),
(5, '14 avenue du chene', '65123', 'marcel@gmail.com', '$2a$10$Rl2gNVYSvnGo9aPKBr7pxuGaf7lySXNmIueMt42LEfn1fEGoKekjG', 'Marcel', 1),
(6, '14 avenue du chene', '65123', 'julien@gmail.com', '$2a$10$evT3NzUlJv1GQNDUN5WZBO3HceZuBQJJTZnMVMeDHL/uyVJJYfR.C', 'Marcel', 1),
(7, ' adresse7', '56776', 'batch', '$2a$10$2.dsIrZ3hdA.I0.1LBVmGOHnbxQqMVkBrQRIXOo4WyW.hn179MqN.', 'batch', 1);

