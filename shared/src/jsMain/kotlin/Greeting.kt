import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ohos.process
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.seconds

@JsExport
fun hello(): String {
    return "hello ohos from kmp"
}

@JsExport
fun testFlow(callback: (Int) -> Unit) {
    CoroutineScope(EmptyCoroutineContext).launch {
        var index = 0
        while (isActive) {
            delay(1.seconds)
            callback(index++)
        }
    }
}

@JsExport
fun getProcessUid(): Int {
    return process.uid + 10
}

@JsExport
fun getProcessPid(): Int {
    return process.pid + 20
}
