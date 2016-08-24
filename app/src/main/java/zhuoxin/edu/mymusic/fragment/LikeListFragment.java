package zhuoxin.edu.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.adapter.MainListApadter;
import zhuoxin.edu.mymusic.bean.MusicInfo;

/**
 * Created by lenovo on 2016/7/5.
 */
public class LikeListFragment extends Fragment {
    private ListView listView_mainlist;
    private ArrayList<MusicInfo> recentList=new ArrayList<>();
    private MainListApadter apadter;
    private ImageView imageView_main;
    private int index=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_mainlist,container,false);
        listView_mainlist= (ListView) v.findViewById(R.id.listview_mainlist);
        imageView_main= (ImageView) v.findViewById(R.id.music_main);
        apadter=new MainListApadter(getActivity());
        listView_mainlist.setAdapter(apadter);

        return v;
    }
}
