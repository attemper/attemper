FROM openjdk:8-jre-slim
MAINTAINER ldang264

ARG JAR_FILE

ADD target/${JAR_FILE} app.jar

EXPOSE 5212

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java -jar /app.jar $JAVA_OPTS