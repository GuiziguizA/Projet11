@echo on





for %%x in (
<<<<<<< HEAD
biblio-api\target\biblio-api-1.0.1-SNAPSHOT.jar
biblio-front\target\biblio-front-1.0.1-SNAPSHOT.jar
biblio-batch\target\biblio-batch-1.0.1-SNAPSHOT.jar
=======
biblio-api\target\biblio-api-1.1.1-SNAPSHOT.jar
biblio-front\target\biblio-front-1.1.1-SNAPSHOT.jar
biblio-batch\target\biblio-batch-1.1.1-SNAPSHOT.jar
>>>>>>> feature/ListeDattenteReservationLivre
  ) do (
start java -jar %%x
)

