package zhuoxin.edu.mymusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.activity.MainActivity;
import zhuoxin.edu.mymusic.bean.MusicInfo;
import zhuoxin.edu.mymusic.util.DBUtil;
import zhuoxin.edu.mymusic.util.MusicUtil;

/**
 * Created by lenovo on 2016/6/24.
 */
public class MusicService extends Service {
    private ArrayList<MusicInfo> musicList;
    private MediaPlayer mediaPlayer;
    private int index=-1;//音乐角标
    private MusicInfo musicNow;//当前正在播放的音乐
    private int curation_progress;//当前播放进度
    private int musicState=Constants.STATE_STOP;
    private Notification notification;
    private NotificationManager manager;
    private int loopMode=Constants.LOOP_LIST;//播放模式默认列表循环
    private int[] ranBox;//随机角标数组
    private Random random=new Random();//随机对象
    private int ranIndex=-1;

    private DBUtil dbUtil;
    //mediaplayer准备完成的监听
    private MediaPlayer.OnPreparedListener preparedListener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
        mp.start();
            musicState=Constants.STATE_PLAY;
//            MusicUtil.writeInfo(musicNow);
            dbUtil.saveMusic(musicNow);
            Intent intent=new Intent(Constants.RECENT_FULSH);
//            intent.setAction(Constants.RECENT_FULSH);
            sendBroadcast(intent);
            handler.sendEmptyMessage(0);
        }
    };
    //音乐播放完毕的监听
    private MediaPlayer.OnCompletionListener completionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            changeMusic(Constants.FLAG_NEXT);

        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaPlayer!=null){
                curation_progress=mediaPlayer.getCurrentPosition();
            }

            Intent intent=new Intent(Constants.HANDLER_MUSIC_STATE);

            intent.putExtra("index",index);
            intent.putExtra("progress",curation_progress);
            intent.putExtra("musicState",musicState);
            if (musicState==Constants.STATE_PLAY){
                handler.sendEmptyMessageDelayed(0,300);
            }

            initNotify();
            sendBroadcast(intent);//发送广播
        }
    };
    //接收广播
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
        switch (action){
            case Constants.LOOP_CHANGE:
                loopMode=intent.getIntExtra("loopMode",loopMode);
                break;
            case Constants.BUTTON_PRESS:
                pressButton(intent);
                break;
            case Constants.PROGRESS_CHANGE:
                curation_progress=intent.getIntExtra("progress",curation_progress);

                resumeMusic();
                break;
            case Constants.MUSIC_PLAY:
                index=intent.getIntExtra("index",index);
                playMusic();
                break;
        }
        }
    };

    private void pressButton(Intent intent) {
        musicState=intent.getIntExtra("musicState",musicState);//通过intent获取音乐状态
        switch (musicState){
            case Constants.STATE_PLAY:
                resumeMusic();
                break;
            case Constants.STATE_STOP:
                stopMusic();
                break;
            case Constants.STATE_PAUSE:
                pauseMusic();
                break;
            case Constants.STATE_BACK:
                changeMusic(Constants.FLAG_BACK);
                break;
            case Constants.STATE_NEXT:
                changeMusic(Constants.FLAG_NEXT);
                break;
        }
    }
    private void changeMusic(int flag){
        stopMusic();//切换歌曲前先停止音乐

        //根据操作改变角标
        switch (flag){
            case Constants.FLAG_BACK:
                index--;
                if (index<0){
                    index=musicList.size()-1;
                }
                break;
            case Constants.FLAG_NEXT:
               nextMusic();
                break;
            case Constants.FLAG_SINGLE:

                break;
        }

                playMusic();//播放音乐
    }

    private void nextMusic() {
        switch (loopMode){
            case Constants.LOOP_LIST:
                index++;
                if (index>musicList.size()-1){
                    index=0;
                }
                break;
            case Constants.LOOP_RANDOM:
                //找到当前这首歌在随机列表中的位置，index++播放下一曲
                if (ranIndex<0){
                    for (int i=0;i<ranBox.length;i++){
                        if (index==ranBox[i]){
                            ranIndex=i;
                            break;
                        }
                    }
                }
                ranIndex++;
                index=ranBox[ranIndex];
                break;
            case Constants.LOOP_SINGLE:

                break;
        }
    }

    private void stopMusic() {
        if (mediaPlayer!=null){
        mediaPlayer.stop();
        mediaPlayer.reset();
        }
    }

    private void pauseMusic() {
    if (mediaPlayer!=null){
        mediaPlayer.pause();
       curation_progress= mediaPlayer.getCurrentPosition();
    }

    }

    private void resumeMusic() {
        musicState=Constants.STATE_PLAY;
        if (curation_progress>0){
            mediaPlayer.start();
            mediaPlayer.seekTo(curation_progress);
            handler.sendEmptyMessage(0);
        }else {
            playMusic();
        }
    }

    private void playMusic() {
        if (mediaPlayer!=null){
            mediaPlayer.pause();
            mediaPlayer.reset();
        }
        musicNow=musicList.get(index);
        try {
            mediaPlayer.setDataSource(musicNow.getData());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbUtil=DBUtil.getsInstance(this);
        musicList= MusicUtil.getMusicList(this);
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.LOOP_CHANGE);//播放模式发生变化的频道
        filter.addAction(Constants.BUTTON_PRESS);//button发生变化的频道
        filter.addAction(Constants.PROGRESS_CHANGE);//播放进度发生变化的频道
        filter.addAction(Constants.MUSIC_PLAY);//点击item时播放音乐的广播频道
        registerReceiver(receiver,filter);//注册广播
        manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    private void initNotify() {
        notification=new Notification();
        notification.flags=Notification.FLAG_NO_CLEAR;//设置通知栏不可清除
        notification.icon= R.drawable.music_item;//设置通知栏图标
        notification.tickerText="正在播放"+musicNow.getTitle();//设置通知内容

        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_notify);
        notification.contentView=remoteViews;
        remoteViews.setTextViewText(R.id.title_notify,musicNow.getTitle());//设置歌名
        remoteViews.setTextViewText(R.id.artist_notify,musicNow.getArtist());//设置歌曲名
//        remoteViews.setImageViewResource();//通知栏图片设置资源ID
        //设置进度条
        remoteViews.setProgressBar(R.id.progressBarr_notify,musicNow.getDuration(),curation_progress,false);
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        remoteViews.setOnClickPendingIntent(R.id.notify,pendingIntent);
        //给通知栏图片设置点击事件，跳转到MainActivity

        Intent musicState =new Intent(Constants.MUSIC_PLAY);
        PendingIntent pending=pendingIntent.getBroadcast(this,0,musicState,PendingIntent.FLAG_ONE_SHOT);
        manager.notify(0x110,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer=new MediaPlayer();
        //设置监听
        mediaPlayer.setOnPreparedListener(preparedListener);
        mediaPlayer.setOnCompletionListener(completionListener);
        
        initRanBox();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initRanBox() {
        ranBox=new int[musicList.size()];
        for (int i=0;i<musicList.size();i++){
            ranBox[i]=random.nextInt(musicList.size());
            for (int j=0;j<i;j++){
                if (ranBox[i]==ranBox[j]){
                    i--;
                    break;
                }

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);//解除注册
    }
}
