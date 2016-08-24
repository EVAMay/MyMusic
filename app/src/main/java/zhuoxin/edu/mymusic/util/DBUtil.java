package zhuoxin.edu.mymusic.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.bean.MusicInfo;

/**
 * Created by lenovo on 2016/7/7.
 */
public class DBUtil {
    private SQLHelper helper;
    private SQLiteDatabase db;
    private  static DBUtil sInstance;
    private DBUtil(Context context){
        helper=new SQLHelper(context);
        db=helper.getWritableDatabase();
    }
    public static DBUtil getsInstance(Context context){
        if (sInstance==null){
            sInstance=new DBUtil(context);
        }

        return sInstance;
    }
    public void saveMusic(MusicInfo info){
        ContentValues values=new ContentValues();
        values.put("title",info.getTitle());
        values.put("_id",info.get_id());
        values.put("artist",info.getArtist());
        values.put("duration",info.getDuration());
        values.put("album",info.getAlbum());
        values.put("album_id",info.getAlbum_id());
        values.put("data",info.getData());
        db.insert(SQLHelper.TABLE_RECENT,null,values);//将数据插入数据库
    }
    public ArrayList<MusicInfo> getRecentList(){
        ArrayList<MusicInfo> list=new ArrayList<>();
        Cursor cursor=db.query(true,SQLHelper.TABLE_RECENT,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String artist=cursor.getString(cursor.getColumnIndex("artist"));
            String album=cursor.getString(cursor.getColumnIndex("album"));
            String data=cursor.getString(cursor.getColumnIndex("data"));

            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            int album_id=cursor.getInt(cursor.getColumnIndex("album_id"));
            int duration=cursor.getInt(cursor.getColumnIndex("duration"));

            list.add(new MusicInfo(title,_id,artist,duration,album,album_id,data));
        }
        return list;
    }
}
