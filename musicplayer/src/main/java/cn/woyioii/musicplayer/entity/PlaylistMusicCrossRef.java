package cn.woyioii.musicplayer.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        primaryKeys = {"musicId", "playlistId"},
        foreignKeys = {
                @ForeignKey(entity = Music.class,
                        parentColumns = "musicId",
                        childColumns = "musicId",
                        onDelete = CASCADE),
                @ForeignKey(entity = Playlist.class,
                        parentColumns = "playlistId",
                        childColumns = "playlistId",
                        onDelete = CASCADE)
        },
        indices = {@Index("playlistId")},
        tableName = "playlist_music_cross_ref"
)
public class PlaylistMusicCrossRef {
    @NonNull
    public String musicId;
    public int playlistId;

    public PlaylistMusicCrossRef(@NonNull String musicId, int playlistId) {
        this.musicId = musicId;
        this.playlistId = playlistId;
    }
}