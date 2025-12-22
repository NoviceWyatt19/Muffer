plugins {
	java
	id("org.springframework.boot") version "3.5.8"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.wyatt"
version = "0.0.1-SNAPSHOT"
description = "Personal project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Database
    implementation("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    // Authentication / Token (PASETO)
    implementation("dev.paseto:jpaseto-api:0.7.0")
    runtimeOnly("dev.paseto:jpaseto-impl:0.7.0")  // implementation -> runtimeOnly
    implementation("dev.paseto:jpaseto-jackson:0.7.0")
    runtimeOnly("dev.paseto:jpaseto-bouncy-castle:0.7.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")

    // Build / Utils
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
