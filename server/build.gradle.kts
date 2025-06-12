plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //spring boot設定
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    //DB Driver
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    testRuntimeOnly("com.h2database:h2")

    //kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //開発
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks {
    //build時にclient側のpackage.json コマンドを起動.
    val npmBuild by registering(Exec::class) {
        workingDir = file("../client")

        //Docker内でnpm存在しないとエラー出るので存在チェック
        onlyIf { file("/usr/local/bin/npm").exists() }
        commandLine("/usr/local/bin/npm", "run", "build:deploy")
    }

    named("processResources") {
        dependsOn(npmBuild)
    }
}


tasks.test {
    //build時testスキップ。本番では消す
    onlyIf { false }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
