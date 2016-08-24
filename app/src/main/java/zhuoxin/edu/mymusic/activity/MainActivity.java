package zhuoxin.edu.mymusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.adapter.MainViewPagerAdapter;
import zhuoxin.edu.mymusic.bean.MusicInfo;
import zhuoxin.edu.mymusic.fragment.LikeListFragment;
import zhuoxin.edu.mymusic.fragment.MainListFragment;
import zhuoxin.edu.mymusic.fragment.RecentListFragment;
import zhuoxin.edu.mymusic.util.MusicUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
//    private   ListView listView_main;
    private ArrayList<MusicInfo> musicList;
//    private MainListApadter apadter;
    private int index=-1;
    private TextView music_name_main,artist_main;
    private ViewPager viewPager_main;
    private ImageView imageView_music_main;
    private ImageButton back_mian,start_main,next_main;
    private int musicState= Constants.STATE_STOP;//播放状态默认停止
    private MyReciever reciever;

    private Fragment[]frags=new Fragment[3];
    private Button []buttons=new Button[3];
    private int []buyID={
            R.id.button1_main,R.id.button2_main,R.id.button3_main,
    };
    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicList= MusicUtil.getMusicList(this);

        back_mian= (ImageButton) findViewById(R.id.back_main);//上一曲按钮
        start_main= (ImageButton) findViewById(R.id.start_main);//播放暂停按钮
        next_main= (ImageButton) findViewById(R.id.next_main);//下一曲按钮


        music_name_main= (TextView) findViewById(R.id.music_name_main);//歌名
        artist_main= (TextView) findViewById(R.id.artist_main);//歌手名

        viewPager_main= (ViewPager) findViewById(R.id.viewPager_main);
        imageView_music_main= (ImageView) findViewById(R.id.music_main);

        reciever=new MyReciever();
        IntentFilter filter=new IntentFilter(Constants.MUSIC_PLAY);
        registerReceiver(reciever,filter);


        for (int i=0;i<buttons.length;i++){
            buttons[i]= (Button) findViewById(buyID[i]);
            buttons[i].setOnClickListener(this);
        }
        buttons[0].setEnabled(false);//默认第一个不可用

        frags[0]=new MainListFragment();
        frags[1]=new RecentListFragment();
        frags[2]=new LikeListFragment();
        adapter=new MainViewPagerAdapter(getSupportFragmentManager(),frags);
        viewPager_main.setAdapter(adapter);
        viewPager_main.addOnPageChangeListener(this);
        imageView_music_main.setOnClickListener(this);
//        Intent intent=new Intent(this, MusicService.class);
//        startService(intent);
//        apadter=new MainListApadter(this);
//        listView_main= (ListView) findViewById(R.id.listview_main);
//        listView_main.setAdapter(apadter);
//        new Thread(new Runnable() {
//            //开启子线程，读取音乐文件
//            @Override
//            public void run() {
//                musicList= MusicUtil.getMusicList(MainActivity.this);
//                runOnUiThread(new Runnable() {
//                    //在UI线程中运行的程序
//                    @Override
//                    public void run() {
//                        //获取音乐列表
//                        apadter.setMusicList(musicList);
//                    }
//                });
//            }
//        }).start();
//        listView_main.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1_main:
                setbtnEnable(0);
                break;
            case R.id.button2_main:
                setbtnEnable(1);
                break;
            case R.id.button3_main:
                setbtnEnable(2);
                break;
            case R.id.music_main:
                Intent activity_intent=new Intent(MainActivity.this,PlayActivity.class);
                index=0;
                activity_intent.putExtra("index",index);
                 startActivity(activity_intent);
                break;
        }
    }

    private void setbtnEnable(int number){
        viewPager_main.setCurrentItem(number);//设置跳转页面
        for (int i=0;i<buttons.length;i++){
            if (i==number){
                buttons[i].setEnabled(false);
            }else {
                buttons[i].setEnabled(true);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    setbtnEnable(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent activity_intent=new Intent(MainActivity.this,PlayActivity.class);
//        if (index==position){
//            //如果选取的音乐正在播放，则只跳转到播放界面
//            activity_intent.putExtra("index",index);
//            startActivity(activity_intent);
//            return;
//        }
//        index=position;
//
//        activity_intent.putExtra("index",index);
//        startActivity(activity_intent);
//        Intent intent=new Intent(Constants.MUSIC_PLAY);
//        intent.putExtra("index",position);
//        sendBroadcast(intent);
//
//    }
    class MyReciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        musicState=intent.getIntExtra("musicState",musicState);
        if (musicState==Constants.STATE_PLAY){
            musicState=Constants.STATE_PLAY;

            start_main.setImageResource(R.mipmap.pause);
        }else {
            musicState=Constants.STATE_PAUSE;
            start_main.setImageResource(R.mipmap.start);
        }
        index=getIntent().getIntExtra("index",index);//接收角标
        int tempIndex=intent.getIntExtra("index",index);
        if (tempIndex!=index) {
            index = tempIndex;
            MusicInfo musicInfo = musicList.get(index);
            music_name_main.setText(musicInfo.getTitle());//设置音乐名
            artist_main.setText(musicInfo.getArtist());//设置音乐家

        }

    }

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciever);
    }
    public void onImageButton(View v) {
        final Intent intent = new Intent(Constants.MUSIC_PLAY);
        musicState=intent.getIntExtra("musicStat",musicState);
        index=getIntent().getIntExtra("index",index);//接收角标
        switch (v.getId()) {
            //播放与暂停
            case R.id.start_main:
                //如果是播放状态，点击变为暂停状态，更改图标
                if (musicState == Constants.STATE_PLAY) {
                    musicState = Constants.STATE_PAUSE;
                    start_main.setImageResource(R.mipmap.start);
                } else {
                    musicState = Constants.STATE_PLAY;
                    start_main.setImageResource(R.mipmap.pause);
                }
                break;
            //上一曲
            case R.id.back_main:
                musicState = Constants.STATE_BACK;
                break;
            //下一曲
            case R.id.next_main:
                musicState = Constants.STATE_NEXT;
                break;
        }
    }
}
