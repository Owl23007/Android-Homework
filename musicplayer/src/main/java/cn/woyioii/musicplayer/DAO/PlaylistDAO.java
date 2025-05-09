package cn.woyioii.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import cn.woyioii.musicplayer.entity.Playlist;

@Dao
public interface PlaylistDAO {
    @Transaction
    @Insert
    long insertPlaylist(Playlist playlist);  // 修改返回类型为 long

    @Delete
    void deletePlaylist(Playlist playlist);

    @Query("SELECT * FROM playlist WHERE playlistId = :id")
    Playlist getPlaylistById(String id);

    @Query("SELECT * FROM playlist")
    List<Playlist> getAllPlaylist();
}
