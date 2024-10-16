FROM adoptopenjdk:11
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} ms-auth.jar
EXPOSE ${SLA_AUTH_PORT}
ENTRYPOINT [ "java", "-jar", "/ms-auth.jar" ]