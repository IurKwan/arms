package com.fjx.arms.util

import com.fjx.arms.mvp.IView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @author guanzhirui
 * @date 2022/10/28 10:16
 */
fun <T : Any> Observable<T>.applySchedulers(
    view: IView,
    tips: String
): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .doOnSubscribe {
            view.showLoading(
                tips
            )
        }
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { view.hideLoading() }
        .bindToLifecycle(view)
}

fun <T : Any> Observable<T>.bindToLifecycle(
    view: IView,
): Observable<T> {
    return this.compose(RxLifecycleUtils.bindToLifecycle(view))
}