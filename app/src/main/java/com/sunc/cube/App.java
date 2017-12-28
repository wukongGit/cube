package com.sunc.cube;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.sunc.cube.bean.Achievement;
import com.sunc.cube.utils.AndroidUtils;
import com.sunc.cube.utils.DBKeys;
import com.sunc.cube.utils.DBUtils;

import cn.bmob.v3.Bmob;

public class App extends MultiDexApplication {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Achievement mAchievement;
    private static App mInstance;
    public static App Instance() {
        return mInstance;
    }

    public static synchronized void setAchievement(Achievement jsonObject) {
        mInstance.mAchievement = jsonObject;
        DBUtils.write(DBKeys.ACHIEVEMENT, jsonObject);
    }

    public static Achievement getAchievement() {
        if (mInstance.mAchievement == null) {
            mInstance.mAchievement = DBUtils.read(DBKeys.ACHIEVEMENT);
        }
        return mInstance.mAchievement;
    }
    /**
     * 获取当前应用全局的Context，注意使用该方法必须要在AndroidManifest.xml中配置MyApplication
     *
     * @return Context
     * @Title: getAppContext
     */
    public static Context getAppContext() {
        if (null == mInstance) {
            throw new RuntimeException("请在AndroidManifest.xml中配置MyApplication");
        }
        return mInstance;
    }

    /**
     * 获取当前应用全局的Application，注意使用该方法必须要在AndroidManifest.xml中配置MyApplication
     *
     * @return Application
     * @Title: getAppContext
     */
    public static App getApp() {
        if (null == mInstance) {
            throw new RuntimeException("请在AndroidManifest.xml中配置MyApplication");
        }
        return mInstance;
    }

    /**
     * Called when the application is starting, before any other application
     * objects have been created. Implementations should be as quick as possible
     * (for example using lazy initialization of state) since the time spent in
     * this function directly impacts the performance of starting the first
     * activity, service, or receiver in a process. If you override this method,
     * be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (getApplicationInfo().packageName.equals(AndroidUtils.getCurrentProcessName())) {
            Bmob.initialize(this, "55bcfa34ccddd498cc951666a02a5a48");
        }
    }

    /**
     * Called when the application is stopping. There are no more application
     * objects running and the process will exit. <em>Note: never depend on
     * this method being called; in many cases an unneeded application process
     * will simply be killed by the kernel without executing any application
     * code.</em> If you override this method, be sure to call
     * super.onTerminate().
     */
    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onLowMemory() {
        super.onLowMemory();
    }
}
