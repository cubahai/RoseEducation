# Bước 1: Biên dịch và đóng gói mã nguồn bằng Gradle
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY . .

# 🌟 THÊM DÒNG NÀY ĐỂ CẤP QUYỀN CHẠY CHO GRADLEW TRÊN LINUX
RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon -x test

# Bước 2: Sử dụng thư viện Eclipse Temurin JDK 17 để chạy file .jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]