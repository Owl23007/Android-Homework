package cn.woyioii.musicplayer.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "music")
public class Music {
    @NonNull
    @PrimaryKey
    public String musicId;           // 歌曲ID
    @ColumnInfo
    public String title;        // 歌曲名
    @ColumnInfo
    public String artist;       // 歌手名
    @ColumnInfo
    public String album;        // 专辑名
    @ColumnInfo
    public String duration;     // 时长
    @ColumnInfo
    public String path;         // 本地路径或网络URL
    @ColumnInfo
    public boolean isLocal;     // 是否为本地音乐

    public Music(String musicId, String title, String artist, String album, String duration, String path) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.isLocal = true;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }}
