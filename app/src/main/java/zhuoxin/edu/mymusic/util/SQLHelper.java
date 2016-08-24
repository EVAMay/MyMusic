package zhuoxin.edu.mymusic.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/7/7.
 */
public class SQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="music_db";//数据库名
    public static final String TABLE_RECENT="recent_list";//播放记录列表

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, 1);//参数一上下文内容，参数二数据库名字，参数三游标工厂2，参数四数据库版本
    }


//    当这个类第一次被调用时会执行的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_RECENT+"(title varchar(255)," +
                "_id int," +
                "artist varchar(255)," +
                "duration int," +
                "album varchar(255)," +
                "album_id int," +
                "data varchar(255))");
    }


    //    当数据库更新时被调用时会执行的方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
