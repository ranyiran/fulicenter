package cn.ran.flicenter.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SharePreferencesUtils {
    private static final String SHARE_NAME = "saveuserInfo";
    private static final String SHAR_KEY_NAME = "share_key_user_name";
    private static SharePreferencesUtils instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public SharePreferencesUtils(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharePreferencesUtils getInstance(Context mContext) {
        if (instance == null) {
            instance = new SharePreferencesUtils(mContext);
        }
        return instance;
    }

    public void saveUser(String userName) {
        mEditor.putString(SHAR_KEY_NAME, userName);
        mEditor.commit();
    }

    public String getUser() {
        return mSharedPreferences.getString(SHAR_KEY_NAME, null);
    }

    public void removeUser() {
        mEditor.remove(SHAR_KEY_NAME);
    }
}
