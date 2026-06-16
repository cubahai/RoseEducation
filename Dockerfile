# Bước 1: Sử dụng hình ảnh Gradle để biên dịch và đóng gói code Java
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY . .
# Chạy lệnh bootJar của Gradle để tạo ra file .jar chạy ứng dụng
RUN ./gradlew bootJar --no-daemon -x test

# Bước 2: Sử dụng hình ảnh OpenJDK nhỏ gọn để chạy file .jar sau khi build
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]