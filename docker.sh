mvn clean
mvn package -DPROFILE_ACTIVE=default -DJDBC_DATABASE_URL=mongodb://localhost:27017/db-animal -DCNN_URL=http://localhost:8000
docker-compose -f aplicacao.yaml build
docker-compose -f aplicacao.yaml up
mvn clean
