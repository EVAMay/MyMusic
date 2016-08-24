package zhuoxin.edu.mymusic;

/**
 * Created by lenovo on 2016/6/27.
 */
public interface Constants {

    int STATE_STOP=0;//音乐状态停止
    int STATE_PLAY=1;//音乐状态播放
    int STATE_PAUSE=2;//音乐状态暂停
    int STATE_BACK=3;//上一曲
    int STATE_NEXT=4;//下一曲

    int LOOP_LIST=0;//列表循环
    int LOOP_RANDOM=1;//随机
    int LOOP_SINGLE=2;//单曲循环

    String LOOP_CHANGE="loop_change";//播放界面改变循环模式的广播频道
    String BUTTON_PRESS="press_button";//播放界面按下播放按钮的广播频道
    String PROGRESS_CHANGE="progress_change";//播放界面进度条发生变化的广播频道
    String MUSIC_PLAY="music_play";//点击item时播放音乐的广播频道

    String HANDLER_MUSIC_STATE="music_state";//服务发送音乐状态的广播

    String RECENT_FULSH="recentList_flush";//播放记录

    int FLAG_NEXT=0;
    int FLAG_BACK=1;
    int FLAG_SINGLE=2;
}
