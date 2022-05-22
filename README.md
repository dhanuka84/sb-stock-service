# sb-stock-service
Spring Boot Sample Application

mvn clean install

mvn clean package


docker build -f src/main/docker/Dockerfile -t sb-stock-service .


docker run -p 8081:8081 -t  sb-stock-service:latest

#Testing
mvn clean install

mvn spring-boot:run

#Open API Doc
http://localhost:8085/swagger-ui/index.html
http://localhost:8085/api-docs
