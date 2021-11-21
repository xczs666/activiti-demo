# 打包
docker build -t jenkinxu/activiti-base:1.0.0-SNAPSHOT ../.. --build-arg JAR_FILE=activiti-base-1.0.0-SNAPSHOT.jar -f Dockerfile
