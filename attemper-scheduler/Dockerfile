FROM openjdk:8-jre-slim
MAINTAINER ldang264

ADD target/attemper-scheduler.jar ./attemper-scheduler.jar

EXPOSE 5211

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java $JAVA_OPTS -jar attemper-scheduler.jar $ENV