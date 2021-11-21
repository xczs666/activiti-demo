FROM java:8

EXPOSE 8080

ARG JAR_FILE

ADD target/${JAR_FILE} /activiti-base.jar

ENTRYPOINT ["java", "-jar", "/activiti-base.jar"]
