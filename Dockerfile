# Этап 1 - сборка проекта в jar
FROM maven:3.9-amazoncorretto-21-debian AS maven
WORKDIR /auth
COPY . /auth
RUN mvn install

# Этап 2 - указание как запустить проект
FROM amazoncorretto:21
WORKDIR /auth
COPY --from=maven /auth/target/auth.jar auth.jar
CMD ["java", "-jar", "auth.jar"]