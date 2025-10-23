FROM maven:3.9-amazoncorretto-21-debian

RUN mkdir auth

WORKDIR auth

COPY . .

RUN mvn install

CMD ["java", "-jar", "target/auth.jar"]
