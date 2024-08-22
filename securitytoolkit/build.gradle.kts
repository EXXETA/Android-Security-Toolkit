plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    compileSdk = 35
    namespace = "com.exxeta.securitytoolkit"
    buildToolsVersion = "35.0.0"

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("com.scottyab:rootbeer-lib:0.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.exxeta"
            artifactId = "security-toolkit"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

/**
 * Disables the execution of specific tasks of type AbstractArchiveTask.
 * It iterates over all tasks of this type and checks if their names are
 * either "releaseSourcesJar" or "debugSourcesJar". If a task has one of these names,
 * its enabled property is set to false, effectively preventing it from executing.
 */
tasks.withType<AbstractArchiveTask> {
    if (this.name == "releaseSourcesJar" || this.name == "debugSourcesJar") {
        enabled = false
    }
}