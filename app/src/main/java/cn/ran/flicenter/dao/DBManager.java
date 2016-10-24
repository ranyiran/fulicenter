package cn.ran.flicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.ran.flicenter.bean.UserAvatarBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private static DBOpenHelper mHelper;

    public DBManager() {

    }

    public static synchronized DBManager getInstance() {
        return dbMgr;
    }

    void onInit(Context mContext) {
        mHelper = new DBOpenHelper(mContext);
    }

    public synchronized void coloseDB() {
        if (mHelper != null) {
            mHelper.closeDB();

        }
    }

    public boolean saveUser(UserAvatarBean user) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME, user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK, user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID, user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE, user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH, user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX, user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME, user.getMavatarLastUpdateTime());
        if (db.isOpen()) {
            return db.replace(UserDao.USER_TABLE_NAME, null, values) != -1;
        }
        return false;
    }

    public UserAvatarBean getUser(String userName) {
        return null;
    }

    public boolean updateUser(UserAvatarBean user) {
        return false;
    }


}
