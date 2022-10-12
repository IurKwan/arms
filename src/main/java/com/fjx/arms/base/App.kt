package com.fjx.arms.base

import androidx.annotation.NonNull
import com.fjx.arms.di.component.AppComponent

/**
 * @author guanzhirui
 * @date 2022/7/29 17:06
 */
interface App {

    @NonNull
    fun getAppComponent(): AppComponent

}