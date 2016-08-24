package zhuoxin.edu.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.bean.MusicInfo;

/**
 * Created by lenovo on 2016/6/28.
 */
public class MusicServiceFix extends Service {
    private MediaPlayer mediaPlayer;
    private ArrayList<MusicInfo> musiclist;
    private int id;
    private int current_progress;//当前进度
    private int duration;//总进度
    public static final String MUSIC_ACTION="music_play";//action常量，用于发送广播
    private int music_state;//保存音乐状态的变量
    private MediaPlayer.OnPreparedListener preparedlistener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mp.seekTo(current_progress);//根据暂停时的进度继续播放
            handler.sendEmptyMessage(0x10);//启动handler
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (mediaPlayer!=null&&music_state==Constants.STATE_PLAY||music_state==Constants.STATE_RESUME){
//                current_progress=mediaPlayer.getCurrentPosition(); //获取当前进度
//                duration=mediaPlayer.getDuration();//获取总进度
//                Intent intent=new Intent(MUSIC_ACTION);//启动广播的意图
//                intent.putExtra("progress",current_progress);
//                intent.putExtra("duration",duration);
//                intent.putExtra("title",musiclist.get(id).getName());
//                intent.putExtra("id",id);
//                sendBroadcast(intent);
//                //音乐播放时发送广播，如果为空或者没有播放就不再发送广播
//
//                handler.sendEmptyMessageDelayed(0x10,300);
//            }
            if (music_state==Constants.STATE_BACK||music_state==Constants.STATE_NEXT){
                music_state=Constants.STATE_PLAY;
                Intent intent=new Intent(MUSIC_ACTION);
                intent.putExtra("state",music_state);
                sendBroadcast(intent);
            }
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        musiclist= MusicUtil.getMusic(this);//获取音乐列表
        mediaPlayer=new MediaPlayer();//初始化音乐
        mediaPlayer.setOnPreparedListener(preparedlistener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id=intent.getIntExtra("id",id);//如果intent中不含id，则id保持原状
        music_state=intent.getIntExtra("state", Constants.STATE_STOP);
        if (mediaPlayer.isPlaying()){
            musicStop();
        }
        //获取当前播放进度
        current_progress=intent.getIntExtra("progress",current_progress);

        switch (music_state){
            case Constants.STATE_STOP:
                musicStop();
                break;
            case Constants.STATE_PLAY:

                musicPlay();
                break;
            case Constants.STATE_PAUSE:
                musicPause();
                break;
//            case Constants.STATE_RESUME:
////                current_progress=intent.getIntExtra("progress",current_progress);
//                musicResume();
//                break;
            case Constants.STATE_BACK:
                musicBack();
                break;
            case Constants.STATE_NEXT:
                musicNext();
                break;
        }

        return super.onStartCommand(intent, flags, startId);

    }
/**
 * 下一曲的方法
 * */
    private void musicNext() {
        id++;
        //id大于总长，则跳转到第一首
        if (id>musiclist.size()-1){
        id=0;
        }
        musicStop();
        musicPlay();
        handler.sendEmptyMessage(0x10);
    }
    /**
     * 上一曲的方法
     * */
    private void musicBack() {
        id--;
        if (id<0){
            id=musiclist.size()-1;//如果id小于0，回到总共的最后一首
        }
        musicStop();
        musicPlay();
        handler.sendEmptyMessage(0x10);
    }
    /**
     * 继续播放的方法
     * */
    private void musicResume() {
        if (!mediaPlayer.isPlaying()&&mediaPlayer!=null){
            mediaPlayer.start();
            mediaPlayer.seekTo(current_progress);//从获取的进度继续播放
            handler.sendEmptyMessage(0x10);
        }
    }
    /**
     * 暂停播放的方法
     * */
    private void musicPause() {
        if (mediaPlayer.isPlaying()&&mediaPlayer!=null){
            mediaPlayer.pause();
            current_progress=mediaPlayer.getCurrentPosition();//获取暂停时的进度


        }
    }
    /**
     * 播放的方法
     * */
    private void musicPlay() {

//        try {
//            if (mediaPlayer.isPlaying()){
//                musicStop();
//                mediaPlayer.setDataSource(musiclist.get(id).getPath());
//            }else {
//                musicStop();
//            mediaPlayer.setDataSource(musiclist.get(id).getPath());
//            }
//            mediaPlayer.prepareAsync();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    /**
     * 停止的方法
     * */
    private void musicStop() {
        if (mediaPlayer!=null){
            current_progress=0;
            mediaPlayer.stop();
            mediaPlayer.reset();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicStop();
    }
}
