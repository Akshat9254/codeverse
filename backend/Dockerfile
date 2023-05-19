FROM maven:3.8.5-openjdk-18
WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]