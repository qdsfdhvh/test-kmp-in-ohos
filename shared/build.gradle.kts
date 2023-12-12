import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary

plugins {
    kotlin("multiplatform")
}

version = "0.0.1"

kotlin {
    js(IR) {
        moduleName = "shared"
        browser {
            testTask {
                enabled = false
            }
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(file("$projectDir/../TestDemo/entry/src/main/ets/kmp"))
            }
        }
        binaries.library()
        generateTypeScriptDefinitions()
        compilations.all {
            kotlinOptions {
                moduleKind = "commonjs"
                sourceMap = false
                metaInfo = false
            }
            binaries.withType<JsIrBinary>().all {
                linkTask.configure {
                    kotlinOptions {
                        moduleKind = "commonjs"
                        sourceMap = false
                        metaInfo = false
                    }
                }
            }
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
            }
        }
    }
}