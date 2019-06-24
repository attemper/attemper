FROM openjdk:8-jre-slim
MAINTAINER ldang264

ADD target/attemper-eureka-server.jar ./attemper-eureka-server.jar

EXPOSE 8761

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java $JAVA_OPTS -jar attemper-eureka-server.jar $ENV