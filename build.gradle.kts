plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.jooq.jooq-codegen-gradle") version "3.19.15"
}

group = "com.gleb"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

sourceSets {
	main {
		java {
			srcDir("build/generated/source/jooq/main")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	jooqCodegen("org.postgresql:postgresql:42.6.1")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jooq {
	configuration {
		jdbc {
			driver = "org.postgresql.Driver"
			url = "jdbc:postgresql://localhost:5432/stage0_db"
			user = "postgres"
			password = "password"
		}
		generator {
			database {
				name = "org.jooq.meta.postgres.PostgresDatabase"
				inputSchema = "public"
				includes = ".*"
				excludes = "flyway_schema_history"
			}
			target {
				packageName = "com.gleb.jooq"
				directory = "build/generated/source/jooq/main"
			}
		}
	}
}

tasks.named<JavaCompile>("compileJava") {
	dependsOn("jooqCodegen")
}
