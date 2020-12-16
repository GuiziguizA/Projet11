@echo on





for %%x in (

biblio-api\target\biblio-api-1.2.4-SNAPSHOT.jar
biblio-front\target\biblio-front-1.2.4-SNAPSHOT.jar
biblio-batch\target\biblio-batch-1.2.4-SNAPSHOT.jar


  ) do (
start java -jar %%x
)

