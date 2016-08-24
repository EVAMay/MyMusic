package zhuoxin.edu.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zhuoxin.edu.mymusic.R;
import zhuoxin.edu.mymusic.bean.MusicInfo;

/**
 * Created by lenovo on 2016/6/29.
 */
public class MainListApadter extends BaseAdapter{
    private Context context;
    private ArrayList<MusicInfo> musicList;

    public MainListApadter(Context context) {
        this.context = context;
        musicList=new ArrayList<>();

    }
    public void setMusicList(ArrayList<MusicInfo> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();//提示数据发生变动请求刷新界面
    }
    @Override
    public int getCount() {
        return musicList.size();
    }





    @Override
    public Object getItem(int position) {
        return musicList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);

           //获取歌名和歌手
            viewHolder.title= (TextView) convertView.findViewById(R.id.name_item);
            viewHolder.artist= (TextView) convertView.findViewById(R.id.artist_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //设置填充歌名和歌手
        viewHolder.title.setText(musicList.get(position).getTitle());
        viewHolder.artist.setText(musicList.get(position).getArtist());
        return convertView;
    }
    private class ViewHolder{
        TextView title,artist;
    }

}
