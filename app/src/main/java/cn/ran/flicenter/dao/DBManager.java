package cn.ran.flicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ran.flicenter.bean.UserAvatarBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private DBOpenHelper mHelper;

    public static synchronized DBManager getInstance() {
        return dbMgr;
    }

    void onInit(Context mContext) {
        mHelper = new DBOpenHelper(mContext);
    }

    public synchronized void closeDB() {
        if (mHelper != null) {
            mHelper.closeDB();
        }
    }

    public synchronized boolean saveUser(UserAvatarBean user) {

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

    public synchronized UserAvatarBean getUser(String userName) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                UserDao.USER_COLUMN_NAME + " =?";
        UserAvatarBean user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        if (cursor.moveToNext()) {
            user = new UserAvatarBean();
            user.setMuserName(userName);
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            return user;
        }
        return user;
    }

    public synchronized boolean updateUser(UserAvatarBean user) {
        int resule = -1;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME + "=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK, user.getMuserNick());
        if (db.isOpen()) {
            resule = db.update(UserDao.USER_TABLE_NAME, values, sql, new String[]{user.getMuserName()});
        }
        return resule > 0;
    }


}
