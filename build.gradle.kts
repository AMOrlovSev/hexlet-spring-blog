plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("kapt") version "1.9.25" // важно для генерации
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Datafaker (оставить только одну версию - рекомендую новейшую)
    implementation("net.datafaker:datafaker:2.4.3")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.instancio:instancio-junit:3.3.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // Database (раскомментировать нужную)
    runtimeOnly("com.h2database:h2")
    // runtimeOnly("org.postgresql:postgresql")
    // runtimeOnly("com.mysql:mysql-connector-j")
    // runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
}

tasks.withType<Test> {
    useJUnitPlatform()
}