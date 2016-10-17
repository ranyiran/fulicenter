package cn.ran.flicenter;

import android.app.Application;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    public static FuLiCenterApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }

    public FuLiCenterApplication() {
        instance = this;
    }

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;

    }
}
