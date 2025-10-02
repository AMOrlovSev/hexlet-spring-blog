plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("net.datafaker:datafaker:2.4.3")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Для H2 (в памяти, для разработки)
    runtimeOnly("com.h2database:h2")

    // ИЛИ для PostgreSQL
    // runtimeOnly("org.postgresql:postgresql")

    // ИЛИ для MySQL
    // runtimeOnly("com.mysql:mysql-connector-j")

    // ИЛИ для SQL Server
    // runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
}

tasks.withType<Test> {
    useJUnitPlatform()
}