FROM maven:3.9-eclipse-temurin-17-focal AS build_step
RUN mkdir /build
COPY . /build
WORKDIR /build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-focal

# Install chrome
RUN apt-get update; apt-get clean
RUN apt-get install -y wget
RUN apt-get install -y gnupg
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
RUN apt-get update && apt-get -y install google-chrome-stable

RUN mkdir /app && \
    mkdir /app/lib
COPY start.sh /app/
COPY --from=build_step /build/web/target/afl-stats-manager-web.jar \
                       /build/worker/target/afl-stats-manager-worker.jar \
                       /build/scheduler/target/afl-stats-manager-scheduler.jar \
                       /build/common/target/afl-stats-manager-common.jar \
                       /app/
COPY --from=build_step /build/common/target/dependency/*.jar \
                       /app/lib/
WORKDIR /app
CMD ./start.sh
