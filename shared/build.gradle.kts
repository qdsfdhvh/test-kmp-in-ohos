import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.5.11"
}

version = "0.0.1"

val generatedLibName = "shared"
val generatedKmpFileDir get() = "$projectDir/dist"
val ohosJsDir get() = "$projectDir/../TestDemo/entry/src/main/ets/kmp"

kotlin {
    js(IR) {
        moduleName = generatedLibName
        browser {
            testTask {
                enabled = false
            }
            @OptIn(ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory.set(file(generatedKmpFileDir))
            }
        }
        binaries.library()
        generateTypeScriptDefinitions()
        useEsModules()
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
        commonMain {
            dependencies {
                // TODO crash on ProductionLibrary, error: Signal:SIGSEGV(SEGV_MAPERR)@0x000000000000000a
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
                implementation("app.cash.molecule:molecule-runtime:1.3.1")
                implementation(compose.runtime)
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
    delete(generatedKmpFileDir)
}

val ohosDeleteLibrary by tasks.register<Delete>("ohosDeleteLibrary") {
    group = "ohos"
    delete(ohosJsDir)
}

listOf(
    "jsBrowserDevelopmentLibraryDistribution",
    "jsBrowserProductionLibraryDistribution",
).forEach { taskName ->
    tasks.getByName(taskName).dependsOn(jsBrowserDeleteLibrary)

    tasks.register<Copy>(taskName.replace("jsBrowser", "ohosJs")) {
        group = "ohos"
        dependsOn(taskName)
        dependsOn(ohosDeleteLibrary)

        val targetDir = ohosJsDir

        from(generatedKmpFileDir)
        into(targetDir)

        include("*.mjs")
        include("*.ts")

        // *.mjs -> *.js
        rename("(.*).mjs", "$1.js")
        filter { line ->
            line.replace(".mjs'", ".js'")
        }

        // remove follow codes in compose-multiplatform-core-runtime.mjs
        filterContent(
            "function MovableContent(content) {",
            "illegalDecoyCallException('<init>');",
            "}",
        )
        // remove follow codes in compose-multiplatform-core-ui.mjs
        filterContent(
            "function ComposedModifier(inspectorInfo, factory) {",
            "illegalDecoyCallException('<init>');",
            "}"
        )
        filterContent(
            "function NodeState(slotId, content, composition) {",
            "composition = composition === VOID ? null : composition;",
            "illegalDecoyCallException('<init>');",
            "}"
        )
    }
}

// file: -> filter("b", "c") -> file:
//   a                            a
//   b                            d
//   c
//   d
fun Copy.filterContent(vararg content: String) {
    var checkIndex = 0
    filter { line ->
        if (line.contains(content[checkIndex])) {
            checkIndex++
            if (checkIndex == content.size) {
                checkIndex = 0
                return@filter null
            }
            return@filter null
        }
        if (checkIndex > 0) {
            if (checkIndex == content.size) {
                checkIndex = 0
                return@filter null
            } else {
                return@filter buildString {
                    content.take(checkIndex).forEach {
                        append(it)
                        append('\n')
                    }
                    checkIndex = 0
                    append(line)
                }
            }
        }
        line
    }
}

// run follow task to build lib
// ./gradlew :shared:ohosJsDevelopmentLibraryDistribution
