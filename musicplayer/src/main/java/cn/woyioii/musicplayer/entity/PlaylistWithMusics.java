package cn.woyioii.musicplayer.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaylistWithMusics {
    @Embedded
    public Playlist playlist;

    @Relation(parentColumn = "playlistId",
            entityColumn = "musicId",
            associateBy = @Junction(PlaylistMusicCrossRef.class))
    public List<Music> musics;
}