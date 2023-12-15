package app.cash.molecule

// ohos.systemTime.getCurrentTime is callback, so can't use RecompositionMode.Immediate
internal fun nanoTime(): Long {
    return 0
}