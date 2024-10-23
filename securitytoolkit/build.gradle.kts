plugins {
    libs.plugins.android.library
    libs.plugins.jetbrains.kotlin.android
    id("maven-publish")
    signing
}

android {
    namespace = "com.exxeta.securitytoolkit"

    compileSdk =
        libs.versions.android.compile.sdk
            .get()
            .toInt()
    buildToolsVersion =
        libs.versions.android.build.tools
            .get()
    ndkVersion =
        libs.versions.android.ndk
            .get()

    defaultConfig {
        minSdk =
            libs.versions.android.min.sdk
                .get()
                .toInt()

        ndk {
            abiFilters.addAll(
                setOf(
                    "x86",
                    "x86_64",
                    "armeabi-v7a",
                    "arm64-v8a"
                )
            )
        }
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

    publishing {
        singleVariant("release") {
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.rootbeer.lib)
    implementation(libs.kotlinx.coroutines.core)
}

// MARK: - Release management

// helper method to ensure we have a non null string for a property
fun getPropertyOrEmpty(propertyName: String): String =
    project.findProperty(propertyName)?.toString().orEmpty()


project.version = getPropertyOrEmpty("VERSION_NAME")
project.group = getPropertyOrEmpty("GROUP")


fun isReleaseBuild(): Boolean =
    !getPropertyOrEmpty("VERSION_NAME").contains("SNAPSHOT")

fun getReleaseRepositoryUrl(): String =
    getPropertyOrEmpty("RELEASE_REPOSITORY_URL")

fun getSnapshotRepositoryUrl(): String =
    getPropertyOrEmpty("SNAPSHOT_REPOSITORY_URL")

fun getRepositoryUsername(): String = getPropertyOrEmpty("NEXUS_USERNAME")

fun getRepositoryPassword(): String = getPropertyOrEmpty("NEXUS_PASSWORD")

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = getPropertyOrEmpty("GROUP")
            artifactId = getPropertyOrEmpty("POM_ARTIFACT_ID")
            version = getPropertyOrEmpty("VERSION_NAME")
            afterEvaluate {
                from(components["release"])
            }

            pom {
                name = getPropertyOrEmpty("POM_NAME")
                packaging = getPropertyOrEmpty("POM_PACKAGING")
                description = getPropertyOrEmpty("POM_DESCRIPTION")
                url = getPropertyOrEmpty("POM_URL")

                scm {
                    url = getPropertyOrEmpty("POM_SCM_URL")
                    connection = getPropertyOrEmpty("POM_SCM_CONNECTION")
                    developerConnection =
                        getPropertyOrEmpty("POM_SCM_DEV_CONNECTION")
                }

                licenses {
                    license {
                        name = getPropertyOrEmpty("POM_LICENCE_NAME")
                        url = getPropertyOrEmpty("POM_LICENCE_URL")
                        distribution = getPropertyOrEmpty("POM_LICENCE_DIST")
                    }
                }

                developers {
                    developer {
                        id = getPropertyOrEmpty("POM_DEVELOPER_ID")
                        name = getPropertyOrEmpty("POM_DEVELOPER_NAME")
                        organizationUrl = getPropertyOrEmpty("POM_URL")
                    }
                }
            }
        }
    }
    repositories {
        maven(url = if (isReleaseBuild()) getReleaseRepositoryUrl() else getSnapshotRepositoryUrl()) {
            credentials {
                username = getRepositoryUsername()
                password = getRepositoryPassword()
            }
        }
    }
}

signing {
    setRequired({ isReleaseBuild() })
    sign(publishing.publications["release"])
}
