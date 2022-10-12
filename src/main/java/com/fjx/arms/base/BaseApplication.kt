package com.fjx.arms.base

import android.app.Application
import android.content.Context
import com.fjx.arms.base.delegate.AppDelegate
import com.fjx.arms.base.delegate.AppLifecycles
import com.fjx.arms.di.component.AppComponent

/**
 * @author guanzhirui
 * @date 2022/7/30 11:08
 */
class BaseApplication : Application(), App {

    private var mAppDelegate: AppLifecycles? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) {
            mAppDelegate = AppDelegate(base)
        }
        mAppDelegate?.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate?.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }

    override fun getAppComponent(): AppComponent {
        return (mAppDelegate as App).getAppComponent()
    }

}