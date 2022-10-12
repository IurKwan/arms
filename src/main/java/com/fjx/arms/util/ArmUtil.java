package com.fjx.arms.util;

import android.app.ActivityManager;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.fjx.arms.base.App;
import com.fjx.arms.di.component.AppComponent;

import java.util.List;

/**
 * @author guanzhirui
 * @date 2022/8/1 10:21
 */
public class ArmUtil {

    public static AppComponent obtainAppComponent(Context context) {
        return ((App) context.getApplicationContext()).getAppComponent();
    }

    public static boolean isTopActivity(Class<?> tClass) {
        ActivityManager manager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        if (runningTasks.size() > 0) {
            String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
            return name.equals(tClass.getName());
        } else {
            return false;
        }
    }

    public static String getTargetName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

}
