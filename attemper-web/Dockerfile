FROM openjdk:8-jre-slim
MAINTAINER ldang264

ADD target/attemper-web.jar ./attemper-web.jar

EXPOSE 5210

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java $JAVA_OPTS -jar attemper-web.jar $ENV