package com.fjx.arms.rxerror.handler

import com.fjx.arms.rxerror.core.RxErrorHandler
import io.reactivex.rxjava3.core.Observable

/**
 * @author guanzhirui
 * @date 2022/10/27 14:42
 */
fun <T : Any> Observable<T>.subscribe(
    rxErrorHandler: RxErrorHandler,
    onNext: (t: T) -> Unit,
): Any {
    return subscribe(object : ErrorHandleSubscriber<T>(rxErrorHandler) {
        override fun onNext(t: T) {
            onNext(t)
        }
    })
}

fun <T : Any> Observable<T>.subscribe(
    rxErrorHandler: RxErrorHandler,
    onNext: (t: T) -> Unit,
    onError: (t: Throwable) -> Unit,
): Any {
    return subscribe(object : ErrorHandleSubscriber<T>(rxErrorHandler) {
        override fun onNext(t: T) {
            onNext(t)
        }

        override fun onError(t: Throwable) {
            super.onError(t)
            onError(t)
        }
    })
}

