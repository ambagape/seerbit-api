
FROM maven:3.8.7-openjdk-18 as builder

ENV HOME=/opt/app

RUN mkdir $HOME

WORKDIR $HOME

ADD pom.xml ./pom.xml

RUN mvn dependency:go-offline

ADD . .

COPY src/main/resources/application.sample.properties \
     src/main/resources/application.properties

RUN mvn install

EXPOSE 8080

CMD ["java", "-jar", "/opt/app/target/seerbit-api-0.0.1-SNAPSHOT.jar"]