FROM openjdk:11-jre-slim as builder
WORKDIR application
#ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
ARG JAR_FILE=target/*.jar
#RUN echo ${project.build.finalName}
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:11-jre-slim
LABEL PROJECT_NAME=sb-stock-service \
      PROJECT=com.sb
LABEL "collect_logs_with_filebeat"="true"
LABEL "decode_log_event_to_json_object"="true"

EXPOSE 8080

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]