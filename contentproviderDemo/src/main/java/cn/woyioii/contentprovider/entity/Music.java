package cn.woyioii.contentprovider.entity;

public class Music {
    int id;
    String title;
    String artist;
    String data;
    int duration;

    public Music(int id, String title, String artist, String data, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.data = data;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}