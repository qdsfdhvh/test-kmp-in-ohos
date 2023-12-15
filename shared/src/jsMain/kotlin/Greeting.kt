import ohos.process

@JsExport
fun hello(): String {
    return "hello ohos from kmp"
}

@JsExport
fun getProcessUid(): Int {
    return process.uid + 10
}

@JsExport
fun getProcessPid(): Int {
    return process.pid + 20
}