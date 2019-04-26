FROM openjdk:8-jre

ADD attemper-web-*.jar ./attemper-web.jar

EXPOSE 8055

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT java $JAVA_OPTS -jar attemper-web.jar $ENV