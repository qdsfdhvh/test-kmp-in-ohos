import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@JsExport
fun hello(): String {
    return "hello ohos from kmp"
}

@JsExport
fun testFlow(callback: (Int) -> Unit) {
    GlobalScope.launch {
        var index = 0
        while (isActive) {
            delay(1.seconds)
            callback(index++)
        }
    }
}
