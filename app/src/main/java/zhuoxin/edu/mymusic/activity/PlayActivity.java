package zhuoxin.edu.mymusic.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.bean.LrcInfo;
import zhuoxin.edu.mymusic.bean.MusicInfo;
import zhuoxin.edu.mymusic.util.LrcUtil;
import zhuoxin.edu.mymusic.util.MusicUtil;

/**
 * Created by lenovo on 2016/6/24.
 */
public class PlayActivity extends Activity  {
    private String title;
    private SeekBar seekBar_play;
    private ImageView previous_play,start_play,next_play,loop_paly,back_actionbar;
    private TextView current_play,duration_play,title_play,artist_play,lrc_play;
    private int curation_progress;//当前进度
    private int duration;//总进度
    private int index=-1;
    private ArrayList<MusicInfo> musicList;
    private int musicState= Constants.STATE_STOP;//默认状态为stop
    private int loopMode=0;//播放模式默认循环列表

    private ArrayList<LrcInfo> lrcList;
    private int flag=0;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            curation_progress=intent.getIntExtra("progress",curation_progress);
            seekBar_play.setProgress(curation_progress);
            current_play.setText(MusicUtil.time(curation_progress));//设置当前时间

//            if (lrcList.size()<=1){
//                lrc_play.setText("未发现歌词");
//            }
//            if (lrcList.get(flag+1).getTime()<curation_progress){
//                flag++;
//                if (lrcList.get(flag).getTime()>lrcList.size()){
//                    flag=0;
//                }
//                lrc_play.setText(lrcList.get(flag).getLrc());//设置歌词
//            }


            loopMode=intent.getIntExtra("loopMode",loopMode);
            switch (loopMode){
                case Constants.LOOP_LIST://列表循环
                    loop_paly.setImageResource(R.mipmap.loop);
                    break;
                case Constants.LOOP_RANDOM://随机播放
                    loop_paly.setImageResource(R.mipmap.random);
                    break;
                case Constants.LOOP_SINGLE://单曲循环
                    loop_paly.setImageResource(R.mipmap.singler);

                    break;
            }
            musicState=intent.getIntExtra("musicState",musicState);
            if (musicState==Constants.STATE_PLAY){
                musicState=Constants.STATE_PLAY;

                start_play.setImageResource(R.mipmap.pause);
            }else {
                musicState=Constants.STATE_PAUSE;
                start_play.setImageResource(R.mipmap.start);
            }
            int tempIndex=intent.getIntExtra("index",index);


            if (tempIndex!=index){
                index=tempIndex;
                MusicInfo musicInfo=musicList.get(index);
                title_play.setText(musicInfo.getTitle());//设置音乐名
                artist_play.setText(musicInfo.getArtist());//设置音乐家
//                duration_play.setText(musicInfo.getDuration()/1000/60+":"+musicInfo.getDuration()/1000%60);//设置总时长
                  duration_play.setText(MusicUtil.time(musicInfo.getDuration()));//设置总时长
                seekBar_play.setMax(musicInfo.getDuration());//设置最大值

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        musicList= MusicUtil.getMusicList(this);
        index=getIntent().getIntExtra("index",index);//接收角标
        initViews();
        initData();
        lrcList= LrcUtil.getLrc("大海");

//        reciever=new MyReciever();//初始化广播接收
//        IntentFilter filter=new IntentFilter(MusicServiceFix.MUSIC_ACTION);//初始化意图过滤器
//        registerReceiver(reciever,filter);//动态注册广播

    }

    private void initData() {
        if (index>=0){

            MusicInfo musicInfo=musicList.get(index);
            title_play.setText(musicInfo.getTitle());//设置音乐名
            artist_play.setText(musicInfo.getArtist());//设置音乐家
//           duration_play.setText(musicInfo.getDuration()/1000/60+":"+musicInfo.getDuration()/1000%60);//设置总时长
            duration_play.setText(MusicUtil.time(musicInfo.getDuration()));//设置总时长
            seekBar_play.setMax(musicInfo.getDuration());


        }
        IntentFilter filter=new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        registerReceiver(receiver,filter);

    }

    private void initViews() {

        seekBar_play= (SeekBar) findViewById(R.id.seekbar_play);//进度条
        current_play= (TextView) findViewById(R.id.current_play);//当前时常
        duration_play= (TextView) findViewById(R.id.duration_play);//总时长
        title_play= (TextView) findViewById(R.id.name_play);//音乐名
        artist_play= (TextView) findViewById(R.id.artist_play);//艺术家
        previous_play= (ImageView) findViewById(R.id.previous_play);//上一曲图标
        start_play= (ImageView) findViewById(R.id.start_play);//暂停，播放图标
        next_play= (ImageView) findViewById(R.id.next_play);//下一曲图标
        loop_paly= (ImageView) findViewById(R.id.loop_play);//播放模式
        lrc_play= (TextView) findViewById(R.id.lrc_play);//歌词
        back_actionbar= (ImageView) findViewById(R.id.back_actionbar);
        back_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlayActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        seekBar_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Intent intent=new Intent(Constants.PROGRESS_CHANGE);
                intent.putExtra("musicState",Constants.STATE_PAUSE);
                sendBroadcast(intent);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent=new Intent(Constants.PROGRESS_CHANGE);
                intent.putExtra("musicState",Constants.STATE_PLAY);
                intent.putExtra("progress",seekBar.getProgress());
                sendBroadcast(intent);
            }
        });
//        start_play.setOnClickListener(this);
//        previous_play.setOnClickListener(this);
//        next_play.setOnClickListener(this);
//        seekBar_play.setOnSeekBarChangeListener(this);
//       seekBar_play.setMax(duration);
//        seekBar_play.setProgress(current_progress);
//        title_play.setText(title);
    }


        public void playClick(View v){
            final Intent intent=new Intent(Constants.BUTTON_PRESS);
            switch (v.getId()){
                //播放与暂停
                case R.id.start_play:
                    //如果是播放状态，点击变为暂停状态，更改图标
                    if (musicState==Constants.STATE_PLAY){
                        musicState=Constants.STATE_PAUSE;
                        start_play.setImageResource(R.mipmap.start);
                    }else {
                        musicState=Constants.STATE_PLAY;
                        start_play.setImageResource(R.mipmap.pause);
                    }
                    break;
                //上一曲
                case R.id.previous_play:
                musicState=Constants.STATE_BACK;
                    break;
                //下一曲
                case R.id.next_play:
                musicState=Constants.STATE_NEXT;
                    break;
            }

            intent.putExtra("musicState",musicState);//发送音乐状态
            intent.putExtra("index",index);//发送ID
            sendBroadcast(intent);//发送广播
            loop_paly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (loopMode){
                        case Constants.LOOP_LIST:
                            loopMode=Constants.LOOP_RANDOM;
                            //按下按钮，模式变为随机
                            loop_paly.setImageResource(R.mipmap.random);
                            Toast.makeText(PlayActivity.this,"随机播放",Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.LOOP_RANDOM:
                            loopMode=Constants.LOOP_SINGLE;
                            //按下按钮，模式变为单曲循环
                            loop_paly.setImageResource(R.mipmap.singler);
                            Toast.makeText(PlayActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.LOOP_SINGLE:
                            loopMode=Constants.LOOP_LIST;
                            //按下按钮，模式变为列表循环
                            loop_paly.setImageResource(R.mipmap.loop);
                            Toast.makeText(PlayActivity.this,"列表循环",Toast.LENGTH_SHORT).show();
                            break;

                    }
                    Intent intent=new Intent(Constants.LOOP_CHANGE);
                    intent.putExtra("loopMode",loopMode);
                    sendBroadcast(intent);
                }
            });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);//如果使用动态注册，一定要在关闭页面前取消动态注册
    }


}
