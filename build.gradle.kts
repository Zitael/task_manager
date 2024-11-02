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
    implementation("org.springframework.boot:spring-boot:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.liquibase:liquibase-core:4.29.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.mapstruct:mapstruct:1.6.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    kapt("org.mapstruct:mapstruct-processor:1.6.0")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("com.h2database:h2:2.3.232")

    //TODO update or change, there are vulnerabilities
    testImplementation("org.jeasy:easy-random-core:5.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}