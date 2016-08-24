package zhuoxin.edu.mymusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.Constants;
import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.adapter.MainListApadter;
import zhuoxin.edu.mymusic.bean.MusicInfo;
import zhuoxin.edu.mymusic.util.DBUtil;

/**
 * Created by lenovo on 2016/7/5.
 */
public class RecentListFragment extends Fragment  {
    private ListView listView_mainlist;
    private ArrayList<MusicInfo> recentList=new ArrayList<>();
    private MainListApadter apadter;
    private ImageView imageView_main;
    private int index=-1;
    private DBUtil dbUtil;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//        recentList= MusicUtil.getRecentList();//文件流保存数据
            recentList=dbUtil.getRecentList();//数据库保存数据
            apadter.setMusicList(recentList);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_mainlist,container,false);
        dbUtil=DBUtil.getsInstance(getActivity());
       listView_mainlist= (ListView) v.findViewById(R.id.listview_mainlist);
        imageView_main= (ImageView) v.findViewById(R.id.music_main);
        apadter=new MainListApadter(getActivity());
        listView_mainlist.setAdapter(apadter);
        IntentFilter filter = new IntentFilter(Constants.RECENT_FULSH);
        getActivity().registerReceiver(receiver,filter);
//        recentList= MusicUtil.getRecentList();//文件流保存数据
        recentList=dbUtil.getRecentList();//数据库保存数据
        apadter.setMusicList(recentList);
        return v;
    }


}
