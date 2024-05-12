FROM node:lts-alpine

LABEL org.opencontainers.image.source https://github.com/roelofr/k8s-experiment
LABEL dev.roelofr.app frontend
LABEL dev.roelofr.bundle k8s

RUN mkdir /opt/app

WORKDIR /opt/app

COPY . /opt/app/

EXPOSE 8081
ENV NITRO_PORT 8081

CMD ["/opt/app/server/index.mjs"]
