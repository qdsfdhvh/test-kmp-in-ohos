import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

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
        useCommonJs()
        generateTypeScriptDefinitions()
//        compilations.configureEach {
//            compilerOptions.configure {
//                moduleKind.set(JsModuleKind.MODULE_COMMONJS)
//            }
//        }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}