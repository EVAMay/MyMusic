package zhuoxin.edu.mymusic.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import zhuoxin.edu.mymusic.bean.MusicInfo;

/**
 * Created by lenovo on 2016/6/29.
 */
public class MusicUtil {
    private static ArrayList<MusicInfo> musicList;//音乐主列表
    private static ArrayList<MusicInfo> recentList=new ArrayList<>();//播放记录列表
    private static ArrayList<MusicInfo> likeList;//收藏列表
    private static final String LIST_FILE_PATH="/data/data/zhuoxin.edu.mymusic/list";
    public static ArrayList<MusicInfo> getMusicList(Context context){
        if (musicList==null){
            musicList=new ArrayList<>();
            //内容解析器
            ContentResolver resolver=context.getContentResolver();
            Cursor cursor=resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null
            ,MediaStore.Audio.Media.DATE_MODIFIED);

            while (cursor.moveToNext()){
                String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String data=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                int _id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int album_id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                if (duration>15000){
                    musicList.add(new MusicInfo(title,_id,artist,duration,album,album_id,data));
                }

            }

        }
        return musicList;
    }
public static String time(int curationProgress){
    String time="";
    int minute=curationProgress/60000;
    int second=curationProgress/1000%60;
    if (minute<10){
        time+="0";
    }
    time+=minute+":";
    if (second<10){
        time+="0";
    }
    time+=second;

    return time;
}
    public static ArrayList<MusicInfo> getRecentList(){
            recentList.clear();//清空
            File f=new File(LIST_FILE_PATH+"recentList");
            try {
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));

                while (true){
                    MusicInfo info= (MusicInfo) ois.readObject();
                    if (info!=null){
                        recentList.add(info);
                    }else {
                        break;
                    }

                }
                ois.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        return recentList;
    }
    public static void writeInfo(MusicInfo info){
        for (MusicInfo musicInfo:recentList) {
            if (musicInfo.get_id()==info.get_id()){
                recentList.remove(musicInfo);
                break;
            }
        }
        recentList.add(0,info);
        File f=new File(LIST_FILE_PATH+"recentList");

            try {
                f.delete();
                if (!f.exists()){
                f.createNewFile();
                }
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f));
                for (MusicInfo musicInfo:recentList) {
                    oos.writeObject(musicInfo);
                }
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
