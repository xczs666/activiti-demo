FROM java:8

## Add the wait script to the image
ADD src/docker/wait /wait
RUN chmod +x /wait

ARG JAR_FILE
ADD target/${JAR_FILE} /activiti-base.jar

CMD /wait && java -jar /activiti-base.jar
