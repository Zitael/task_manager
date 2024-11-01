plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("kapt") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}

group = "org.task_manager"
version = "1.0"

repositories {
    mavenCentral()
}

val springVersion = "3.3.5"
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.liquibase:liquibase-core:4.29.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.mapstruct:mapstruct:1.6.2")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    kapt("org.mapstruct:mapstruct-processor:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}