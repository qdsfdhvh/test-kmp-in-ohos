import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary

plugins {
    kotlin("multiplatform")
}

version = "0.0.1"

val generatedLibName = "shared"
val generatedKmpFileDir = file("$projectDir/../TestDemo/entry/src/main/ets/kmp")

kotlin {
    js(IR) {
        moduleName = generatedLibName
        browser {
            testTask {
                enabled = false
            }
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(generatedKmpFileDir)
            }
        }
        binaries.library()
        generateTypeScriptDefinitions()
        // ≈ useEsModules()
        compilations.all {
            kotlinOptions {
                moduleKind = "es"
                sourceMap = false
                metaInfo = false
            }
            binaries.withType<JsIrBinary>().all {
                linkTask.configure {
                    kotlinOptions {
                        moduleKind = "es"
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
                // TODO crash on ProductionLibrary, error: Signal:SIGSEGV(SEGV_MAPERR)@0x000000000000000a
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
            }
        }
        jsMain {
            dependencies {
            }
        }
    }
}

// rename old file when rebuild
val jsBrowserDeleteLibrary by tasks.register<Delete>("jsBrowserDeleteLibrary") {
    group = "kotlin browser"
    delete(file(generatedKmpFileDir))
}

listOf(
    "jsBrowserDevelopmentLibraryDistribution",
    "jsBrowserProductionLibraryDistribution",
).forEach { taskName ->
    tasks.getByName(taskName).dependsOn(jsBrowserDeleteLibrary)

    // ohos not support *.mjs, rename shared.mjs to shared.js
    val renameShareLib by tasks.register<Copy>("${taskName}RenameLib") {
        group = "kotlin browser"
        from(generatedKmpFileDir)
        into(generatedKmpFileDir)
        include("${generatedLibName}.mjs")
        rename("${generatedLibName}.mjs", "${generatedLibName}.js")
        dependsOn(tasks.getByName(taskName))
    }

    // rename task will not delete old file, so run this task
    tasks.register<Delete>("${taskName}Fix") {
        group = "kotlin browser"
        delete(File(generatedKmpFileDir, "${generatedLibName}.mjs"))
        dependsOn(renameShareLib)
    }
}

// run follow task to build lib
// ./gradlew :shared:jsBrowserDevelopmentLibraryDistributionFix
