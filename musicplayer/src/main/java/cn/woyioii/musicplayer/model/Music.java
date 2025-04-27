package cn.woyioii.musicplayer.model;

public class Music {
    public String id;           // 歌曲ID
    public String title;        // 歌曲名
    public String artist;       // 歌手名
    public String album;        // 专辑名
    public String duration;     // 时长
    public String path;         // 本地路径或网络URL
    public boolean isLocal;     // 是否为本地音乐

    public Music(String id, String title, String artist,String album, String duration, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.isLocal = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
