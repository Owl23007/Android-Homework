package cn.woyioii.musicplayer.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String id;
    private String name;
    private List<Music> musicList;

    public Playlist() {
        this.musicList = new ArrayList<>();
    }

    public List<Music> getMusicList() {
        return musicList;
    }
}