package com.fjx.arms.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.util.ArmUtil;

/**
 * @author guanzhirui
 * @date 2022/8/1 10:20
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        iActivity.setupActivityComponent(ArmUtil.obtainAppComponent(mActivity));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroy() {
        this.iActivity = null;
        this.mActivity = null;
    }
}
