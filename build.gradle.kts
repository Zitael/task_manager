plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
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
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}