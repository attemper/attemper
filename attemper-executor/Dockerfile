FROM openjdk:8-jre-slim
MAINTAINER ldang264

ADD target/attemper-executor.jar ./attemper-executor.jar

EXPOSE 5212

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java $JAVA_OPTS -jar attemper-executor.jar $ENV