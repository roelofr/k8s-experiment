FROM eclipse-temurin:21

LABEL org.opencontainers.image.source https://github.com/roelofr/k8s-experiment
LABEL dev.roelofr.app backend
LABEL dev.roelofr.bundle k8s

RUN mkdir /opt/app

WORKDIR /opt/app

COPY app.jar /opt/app

EXPOSE 8000 8041

CMD ["java", "-jar", "-Dserver.port=8000", "-Dmanagement.server.port=8041", "/opt/app/app.jar"]
