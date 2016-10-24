package cn.ran.flicenter;

import android.app.Application;

import cn.ran.flicenter.bean.UserAvatarBean;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;
    public static String userName;
    private static FuLiCenterApplication instance;
    private static UserAvatarBean user;

    public FuLiCenterApplication() {
        instance = this;
    }

    public static UserAvatarBean getUser() {
        return user;
    }

    public static void setUser(UserAvatarBean user) {
        FuLiCenterApplication.user = user;
    }

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }
}
