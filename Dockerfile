# ---- Stage 1: build the WAR with Maven -------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copy the POM first so dependency downloads are cached between builds
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# ---- Stage 2: run on Tomcat 10.1 -------------------------------------------
FROM tomcat:10.1-jdk17-temurin
# Remove the default Tomcat webapps and deploy ours as the root application
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/straytostay.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
