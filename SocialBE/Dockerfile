# Stage 1: Build ứng dụng
FROM openjdk:17-jdk-slim AS builder

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file Gradle và mã nguồn
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

# Cấp quyền chạy gradlew và build ứng dụng
RUN chmod +x gradlew
RUN ./gradlew build -x test

# Stage 2: Tạo image chạy ứng dụng
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR từ stage build
COPY --from=builder /app/build/libs/SocialBE-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]