package zhuoxin.edu.mymusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.activity.PlayActivity;
import zhuoxin.edu.mymusic.adapter.MainListApadter;
import zhuoxin.edu.mymusic.bean.MusicInfo;
import zhuoxin.edu.mymusic.service.MusicService;
import zhuoxin.edu.mymusic.util.MusicUtil;

/**
 * Created by lenovo on 2016/7/4.
 */
public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView listView_main;
    private ArrayList<MusicInfo> musicList;
    private MainListApadter apadter;
    private int index=-1;
    private LocalBroadcastManager manager;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mainlist,container,false);
//        getActivity().setContentView(R.layout.fragment_mainlist);

        apadter=new MainListApadter(getActivity());
        listView_main= (ListView) v.findViewById(R.id.listview_mainlist);
        listView_main.setAdapter(apadter);
        new Thread(new Runnable() {
            //开启子线程，读取音乐文件
            @Override
            public void run() {
                musicList= MusicUtil.getMusicList(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    //在UI线程中运行的程序
                    @Override
                    public void run() {
                        //获取音乐列表
                        apadter.setMusicList(musicList);
                        Intent intent=new Intent(getActivity(), MusicService.class);
                        getActivity().startService(intent);
                    }
                });
            }
        }).start();
        listView_main.setOnItemClickListener(this);
        IntentFilter filter=new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        getActivity().registerReceiver(receiver,filter);
        return v;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);

    }

    public void mainClick(View v){
//    Intent intent=new Intent(this,PlayActivity.class);
//
//        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent activity_intent=new Intent(getActivity(),PlayActivity.class);
        if (index==position){
            //如果选取的音乐正在播放，则只跳转到播放界面
            activity_intent.putExtra("index",index);
            startActivity(activity_intent);
            return;
        }
        index=position;

        activity_intent.putExtra("index",index);
        startActivity(activity_intent);
        Intent intent=new Intent(Constants.MUSIC_PLAY);
        intent.putExtra("index",position);
        getActivity().sendBroadcast(intent);

    }


}
