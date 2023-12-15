package ohos

external interface SystemDateTime {
    fun getCurrentTime(isNano: Boolean, callback: (Long) -> Unit)
}

@JsModule("@ohos.systemTime")
@JsNonModule
external val systemDateTime: SystemDateTime
