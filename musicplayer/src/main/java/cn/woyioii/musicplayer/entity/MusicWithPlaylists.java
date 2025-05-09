package cn.woyioii.musicplayer.entity;

import androidx.room.*;

import java.util.List;

public class MusicWithPlaylists {
    @Embedded
    public Music music;

    @Relation(parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(PlaylistMusicCrossRef.class))
    public List<Playlist> playlists;
}
