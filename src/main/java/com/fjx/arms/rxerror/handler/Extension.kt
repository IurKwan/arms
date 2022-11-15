package com.fjx.arms.rxerror.handler

import com.fjx.arms.rxerror.core.RxErrorHandler
import io.reactivex.rxjava3.core.Observable
import retrofit2.HttpException

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
