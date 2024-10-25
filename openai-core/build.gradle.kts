plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
    id("binary-compatibility-validator")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    id("build-support")
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
            languageSettings.apply{
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.io.core)
                api(libs.serialization.json)
                implementation(libs.serialization.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val wasmJsTest by getting {
            dependencies {
                implementation(kotlin("test-wasm-js"))
            }
        }
    }
}
