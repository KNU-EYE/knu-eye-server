FROM eclipse-temurin:21-jre
WORKDIR /app
# 빌드된 JAR 파일만 복사 (서버에서 빌드 안 함)
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
