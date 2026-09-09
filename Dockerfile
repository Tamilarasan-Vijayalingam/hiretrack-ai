# ==========================================
# Stage 1: Build the backend application
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy backend pom and source code
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

COPY backend/src ./src
RUN mvn clean package -DskipTests -B

# ==========================================
# Stage 2: Production JRE runtime
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root system user for security
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Copy built artifact from builder stage
COPY --from=builder /app/target/backend-*.jar app.jar

# Set ownership
RUN chown appuser:appgroup app.jar

USER appuser

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
