FROM openjdk:15-jdk-slim as build

RUN addgroup sigo adduser  --ingroup sigo --disabled-password sigo
USER demo

WORKDIR application

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ["chmod", "u+x", "mvnw"]

RUN ./mvnw install -DskipTests

RUN cp /application/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:15-jdk-slim
WORKDIR application
COPY --from=bulid application/dependencies/ ./
COPY --from=bulid application/spring-boot-loader/ ./
COPY --from=bulid application/snapshot-dependencies/ ./
COPY --from=bulid application/application/ ./
EXPOSE 8001
ENV JAVA_TOOL_OPTIONS "-Xms256m -Xmx512m"
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]