package zhuoxin.edu.mymusic.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/6/23.
 */
public class MusicInfo implements Serializable {
    private String title;//音乐名字
    private int _id;//音乐ID
    private String artist;//歌手
    private int duration;//歌曲总时长
    private String album;//音乐专辑名
    private int album_id;//专辑ID
    private String data; //音乐路径

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MusicInfo(String title, int _id, String artist, int duration, String album, int album_id, String data) {

        this.title = title;
        this._id = _id;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.album_id = album_id;
        this.data = data;
    }
}
