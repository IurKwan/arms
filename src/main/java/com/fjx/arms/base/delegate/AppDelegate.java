package com.fjx.arms.base.delegate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.fjx.arms.base.App;
import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.di.component.DaggerAppComponent;
import com.fjx.arms.di.module.GlobalConfigModule;
import com.fjx.arms.integration.ConfigModule;
import com.fjx.arms.integration.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author guanzhirui
 * @date 2022/7/29 17:12
 */
public class AppDelegate implements App, AppLifecycles {

    @Inject
    @Named("ActivityLifecycle")
    protected Application.ActivityLifecycleCallbacks mActivityLifecycle;

    @Inject
    @Named("ActivityLifecycleForRxLifecycle")
    protected Application.ActivityLifecycleCallbacks mActivityLifecycleForRxLifecycle;

    private Application mApplication;
    private AppComponent mAppComponent;
    private List<ConfigModule> mModules;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    public AppDelegate(@NonNull Context context) {
        this.mModules = new ManifestParser(context).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        this.mApplication = application;

        mAppComponent = DaggerAppComponent
                .builder()
                .application(mApplication)
                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))
                .build();
        mAppComponent.inject(this);

        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);

        this.mModules = null;

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycleForRxLifecycle = null;
        this.mActivityLifecycles = null;
        this.mAppLifecycles = null;
        this.mApplication = null;
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }

}
