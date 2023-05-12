FROM maven:3.9-eclipse-temurin-17-focal AS build_step
RUN mkdir /build
COPY . /build
WORKDIR /build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-focal
RUN mkdir /app
COPY start.sh /app/
COPY --from=build_step /build/web/target/afl-stats-manager-web.jar \
                       /build/worker/target/afl-stats-manager-worker.jar \
                       /build/scheduler/target/afl-stats-manager-scheduler.jar \
                       /build/common/target/afl-stats-manager-common.jar \
                       /app/
WORKDIR /app
CMD ./start.sh
