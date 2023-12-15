import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import internal.OhosAnimationFrameClock
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@JsExport
class CounterPresenter {

    private val scope = CoroutineScope(OhosAnimationFrameClock + Dispatchers.Main)

    fun collectPresenter(block: (CounterState) -> Unit) {
        // nanoTime of molecule-js is not support in ohos, so can't use RecompositionMode.Immediate
        scope.launchMolecule(
            mode = RecompositionMode.ContextClock,
            body = { content() },
            emitter = { block(it) }
        )
    }

    @Composable
    private fun content(): CounterState {
        var count by remember { mutableStateOf(0) }
        return CounterState(count) { event ->
            when (event) {
                is CounterEventIncrement -> count++
                is CounterEventDecrement -> count--
            }
        }
    }
}

@JsExport
interface CounterEvent

@JsExport
class CounterEventIncrement : CounterEvent

@JsExport
class CounterEventDecrement : CounterEvent

@JsExport
data class CounterState(
    val count: Int,
    val event: (CounterEvent) -> Unit,
) {
    companion object {
        fun default() = CounterState(0) {}
    }
}