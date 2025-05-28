plugins {
    id("java")
    id("application") // Apply the application plugin
    id ("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.7"
    id ("com.github.node-gradle.node") version "7.1.0"
}

val npmPath = file("/opt/homebrew/bin/npm")
if (npmPath.exists()) {
    the<com.github.gradle.node.NodeExtension>().npmCommand.set(npmPath.absolutePath)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter")
    developmentOnly ("org.springframework.boot:spring-boot-devtools")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("org.webjars:webjars-locator-core:0.48")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
    implementation ("org.postgresql:postgresql:42.7.5")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation ("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.6.3")

    implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation ("org.springframework.boot:spring-boot-starter-security")

    testImplementation ("org.springframework.security:spring-security-test")

}

application {
    mainClass.set("org.example.Runner")
}

tasks.named("processResources") {
    dependsOn("npm_run_build")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}