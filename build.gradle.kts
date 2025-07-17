plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator") // 액츄에이터 (상태 모니터링 등)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA (데이터베이스 연동)
    implementation("org.springframework.boot:spring-boot-starter-web") // 웹 애플리케이션 (REST API)

    // SpringDoc OpenAPI UI (Swagger UI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // Lombok (어노테이션 인식을 위해 annotationProcessor 설정 필수)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // DB Driver (JPA와 함께 사용)
    runtimeOnly("com.mysql:mysql-connector-j")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") // JUnit 5, Mockito 등 포함
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher") // spring-boot-starter-test에 포함되어 있을 가능성 높으므로 제거 또는 주석 처리 고려
    
    // Testcontainers (통합 테스트용)
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}
