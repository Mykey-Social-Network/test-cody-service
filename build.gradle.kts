import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.io.FileInputStream
import java.util.*

// Version management
group = "com.mykeyapi"
version = "0.0"

// Variables
val schemaRegistryUrl: String by project
val schemaRegistryUser: String by project
val schemaRegistryPassword: String by project
val protobufVersion: String by project

val userName: String? by project
val token: String? by project


repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
    maven {
        url = uri("https://maven.pkg.github.com/mykey-social-network/mykey-maven")
        credentials {
            username = userName
            password = token
        }
    }

}


plugins {
    id("idea")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.7.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.22"
    id("com.google.protobuf") version "0.8.18"
    id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "1.9.1"
}


// Dependencies
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-streams")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.1")
    implementation("io.projectreactor:reactor-core:3.5.6")
    implementation("io.confluent:kafka-streams-protobuf-serde:7.3.1")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("com.mykeyapi.utils:mykey-backend-utils:1.0.2")
    implementation("org.json:json:20230227")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    runtimeOnly ("io.micrometer:micrometer-registry-prometheus")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.apache.kafka:kafka-streams-test-utils")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation ("org.mockito:mockito-inline:4.10.0")
}

// Protobuf build configuration
protobuf {
    protobuf.apply {
        protoc {
            artifact = "com.google.protobuf:protoc:$protobufVersion"
        }
        generateProtoTasks {
            all().forEach {
                it.builtins {
                    id("kotlin")
                }
            }
        }
    }
}


// Tasks
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.register<Copy>("copyApplicationYml") {
    from(layout.projectDirectory.file("src/main/resources/application.yml"),
        layout.projectDirectory.file("src/main/resources/app-properties.properties"))

    into(layout.projectDirectory.dir("k8s"))
}

tasks.register<Exec>("updateK8s") {
    workingDir(projectDir)
    commandLine("sh", "-c", "sed -i '' 's/service-template/${project.name}/g' k8s/*.yml")
}


tasks.withType<Test> {
    useJUnitPlatform()
}


// Load app-properties.properties file for getting the full topic name for the schema registry
val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "src/main/resources/app-properties.properties")))
}

val appName: String = prop.getProperty("spring.application.name")!!

fun getTopicVersion(topicName: String): String = prop.getProperty("app-properties.topics.$topicName.version")!!
fun getTopicServiceName(topicName: String): String = prop.getProperty("app-properties.topics.$topicName.serviceName")!!
fun getTopicName(topicName: String): String = prop.getProperty("app-properties.topics.$topicName.name")!!

fun getRemoteTopicFullName(topicName: String): String = "${getTopicServiceName(topicName)}-${getTopicName(topicName)}-v${getTopicVersion(topicName)}"
fun getTopicFullName(topicName: String): String = "$appName-${getTopicName(topicName)}-v${getTopicVersion(topicName)}"

// Schema Registry configuration
schemaRegistry {
    url.set(schemaRegistryUrl)
    credentials {
        username.set(schemaRegistryUser)
        password.set(schemaRegistryPassword)
    }

    download {
        // Output path should match the structure of the service that registered thees schemas
        subject(getRemoteTopicFullName("someTopic") + "-value", "src/main/proto/in")
    }

    register {
        // Register your output topic models schemas here
        subject(
            getTopicFullName("otherTopic")+ "-value",
            "src/main/proto/out/EventTemplate.proto",
            type = "PROTOBUF"
        )
    }
}



