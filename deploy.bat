@echo on





for %%x in (
biblio-api\target\biblio-api-0.0.1-SNAPSHOT.jar
biblio-front\target\biblio-front-0.0.1-SNAPSHOT.jar
biblio-batch\target\biblio-batch-0.0.1-SNAPSHOT.jar
  ) do (
start java -jar %%x
)

