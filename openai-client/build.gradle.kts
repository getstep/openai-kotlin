import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
    id("binary-compatibility-validator")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    id("build-support")
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/getstep/openai-kotlin")
        credentials(PasswordCredentials::class)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/getstep/openai-kotlin")
            credentials(PasswordCredentials::class)
        }
    }
}

kotlin {
    explicitApi()
    jvm()
    jsNode()
    jsWasm()
    native()

    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("okio.ExperimentalFileSystem")
                optIn("com.aallam.openai.api.ExperimentalOpenAI")
                optIn("com.aallam.openai.api.BetaOpenAI")
                optIn("com.aallam.openai.api.InternalOpenAI")
                optIn("com.aallam.openai.api.LegacyOpenAI")
            }
        }
        val commonMain by getting {
            dependencies {
                api("com.step.openai:openai-core-jvm:4.0.0")
                api(libs.coroutines.core)
                api(libs.kotlinx.io.core)
                implementation(libs.kotlinx.io.bytestring)
                implementation(libs.serialization.json)
                api(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.openaiCore)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.coroutines.test)
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.ktor.client.okhttp)
                implementation(libs.logback.classic)
            }
        }

        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val wasmJsMain by getting {
            dependencies {
            }
        }
        val wasmJsTest by getting {
            dependencies {
                implementation(kotlin("test-wasm-js"))
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }
        if (HostManager.hostIsMac) {
            val darwinTest by getting {
                dependencies {
                    implementation(libs.ktor.client.darwin)
                }
            }
        }
    }
}
