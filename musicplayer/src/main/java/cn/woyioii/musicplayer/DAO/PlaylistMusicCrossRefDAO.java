package cn.woyioii.musicplayer.DAO;

import androidx.room.*;

import java.util.List;

import cn.woyioii.musicplayer.entity.Playlist;
import cn.woyioii.musicplayer.entity.PlaylistMusicCrossRef;
import cn.woyioii.musicplayer.entity.PlaylistWithMusics;

@Dao
public interface PlaylistMusicCrossRefDAO {

    @Insert
    void insertPlaylistMusicCrossRef(PlaylistMusicCrossRef playlistMusicCrossRef);

    @Delete
    void deletePlaylistMusicCrossRef(PlaylistMusicCrossRef playlistMusicCrossRef);

    // 根据音乐ID查询播放列表和音乐列表的交叉引用
    @Query("SELECT * FROM playlist_music_cross_ref WHERE musicId = :musicId")
    List<PlaylistMusicCrossRef> getPlaylistMusicCrossRefByMusicId(int musicId);

    // 查询所有播放列表和其对应的音乐列表
    @Transaction
    @Query("SELECT * FROM playlist")
    List<PlaylistWithMusics> getAllPlaylistsWithMusics();
}
