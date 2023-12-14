package ohos

external interface Process {
    val uid: Int
    val pid: Int
}

@JsModule("@ohos.process")
@JsNonModule
external val process: Process
