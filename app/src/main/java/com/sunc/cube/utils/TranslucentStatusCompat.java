package com.sunc.cube.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TranslucentStatusCompat {
    static final TranslucentStatusCompatImpl IMPL;

    static {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            IMPL = new LollipopTranslucentStatusCompatImpl();
        } else if (version >= Build.VERSION_CODES.KITKAT) {
            IMPL = new KitKatTranslucentStatusCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void requestTranslucentStatus(Activity activity) {
        IMPL.requestTranslucentStatus(activity);
    }

    private interface TranslucentStatusCompatImpl {
        void requestTranslucentStatus(Activity activity);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static class LollipopTranslucentStatusCompatImpl
            implements TranslucentStatusCompatImpl {
        @Override
        public void requestTranslucentStatus(Activity activity) {
            Window window = activity.getWindow();
            window.getDecorView()
                    .setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static class KitKatTranslucentStatusCompatImpl
            implements TranslucentStatusCompatImpl {
        @Override
        public void requestTranslucentStatus(Activity activity) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private static class BaseViewCompatImpl implements TranslucentStatusCompatImpl {
        @Override
        public void requestTranslucentStatus(Activity activity) {

        }
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置沉浸式, 这玩意还有个特殊作用，就是可以限制输入法把相对布局alignParentBottom的控件往上顶，嘿嘿，爽啊
     *
     * @param on
     */
    public static boolean setTranslucentStatus(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            height = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
