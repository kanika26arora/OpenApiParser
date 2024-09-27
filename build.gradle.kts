plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.chat"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("mysql:mysql-connector-java:8.0.28")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("io.swagger.core.v3:swagger-annotations-jakarta")
	implementation("io.swagger.parser.v3:swagger-parser")
	runtimeOnly("org.liquibase:liquibase-core")
	implementation("com.vladmihalcea:hibernate-types-60:2.20.0")
	implementation("org.postgresql:postgresql:42.7.2")
	implementation("org.hibernate.orm:hibernate-core:6.2.1.Final")
	implementation("software.amazon.awssdk:s3:2.20.153")
}

dependencyManagement {

	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.1")
	}

	dependencies {

		dependency("io.swagger.parser.v3:swagger-parser:2.1.5")
		dependency ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
		dependency ("io.swagger.core.v3:swagger-annotations-jakarta:2.2.8")


		dependency ("io.github.openfeign:feign-core:12.2")
		dependency ("io.github.openfeign:feign-jackson:12.2")
		dependency ("io.github.openfeign:feign-jaxrs:12.2")
		dependency ("io.github.openfeign:feign-slf4j:12.2")
		dependency ("io.github.openfeign:feign-httpclient:12.2")

		dependency("io.awspring.cloud:spring-cloud-aws-messaging:2.4.4")

		// stick to this version until spring-cloud-aws-messaging implements support of Spring Boot 3
		dependency("org.springframework:spring-messaging:5.3.25")

		dependency("org.mapstruct:mapstruct:1.5.3.Final")

		dependency("io.mockk:mockk:1.13.4")
		dependency("io.kotest:kotest-assertions-core:5.5.5")
		dependency("org.jetbrains.dokka:dokka-gradle-plugin:1.7.20")

		dependency("net.javacrumbs.shedlock:shedlock-spring:5.0.1")
		dependency("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.0.1")


		dependency("org.apache.commons:commons-text:1.10.0")


	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
