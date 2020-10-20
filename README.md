- Prerequis:

programme d'installation java 8

installateur maven 3.6

creer une base de données mysql

l'url de la base de données: jdbc: mysql: // localhost: 3306

avec pour nom utilisateur: root

pw: (vide)

Possibilité de modifier les paramètres de la base de données dans les fichiers applications.properties
des 3 modules
creer une table biblio

 1 Automatique:

	- Packaging + deploiement:

		cloner le projet

		Double cliquez sur appli.bat sous windows 

 2 Manuellement:

	- Packaging :

		cloner le projet

		se placer à la racine du projet

		executer la commande: maven clean install

	- Deploiement:

		se placer a la racine du projet et executer : 
		java -jar biblio-api\target\biblio-api-0.0.1-SNAPSHOT.jar
		java -jar biblio-front\target\biblio-front-0.0.1-SNAPSHOT.jar
		java -jar biblio-batch\target\biblio-batch-0.0.1-SNAPSHOT.jar

- Execution:

http://localhost:8084
