import ohos.DeviceInfo
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

@JsExport
fun getDeviceType1(): String {
    return DeviceInfo.deviceType + "44444"
}