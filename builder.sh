mvn clean
mvn install -DJDBC_DATABASE_URL=mongodb://localhost:27017/db-animal -DCNN_URL=http://localhost:8000 -DSECRET_KEY=XXXX
