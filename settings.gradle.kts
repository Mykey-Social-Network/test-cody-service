
// todo: edit
rootProject.name = "service-template"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
        // add mavenLocal() if you are using a locally built version of the plugin
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.google.protobuf")) {
                useModule("com.google.protobuf:protobuf-gradle-plugin:${requested.version}")
            }
            if (requested.id.id.startsWith("com.github.imflog")) {
                useModule("com.github.imflog:kafka-schema-registry-gradle-plugin:${requested.version}")
            }
        }
    }
}