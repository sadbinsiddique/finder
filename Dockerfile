# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the pom.xml file first to cache dependencies
COPY pom.xml .

# Pre-fetch Maven dependencies
RUN mvn dependency:go-offline -B || true

# Copy the source code and build the application package
COPY src ./src
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root system user and pre-create the logs directory with correct ownership
RUN addgroup --system spring && adduser --system --ingroup spring spring \
    && mkdir -p /app/logs \
    && chown -R spring:spring /app/logs

USER spring:spring

# Copy the built jar artifact from the builder stage
COPY --from=builder /app/target/finder-1.0.0.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Configure JVM optimizations matching development settings
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-Dsun.misc.Unsafe=supported", "--enable-native-access=ALL-UNNAMED", "-jar", "app.jar"]
