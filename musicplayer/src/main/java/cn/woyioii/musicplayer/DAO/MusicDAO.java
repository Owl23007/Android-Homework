package cn.woyioii.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import cn.woyioii.musicplayer.entity.Music;

@Dao
public interface MusicDAO {
    @Transaction
    @Insert
    void insertMusic(Music music);

    @Delete
    void deleteMusic(Music music);

    @Update
    void updateMusic(Music music);

    @Query("SELECT * FROM music WHERE musicId = :id")
    Music getMusicById(String id);

    @Query("SELECT * FROM music")
    List<Music> getAllMusic();

    @Query("SELECT * FROM music WHERE title = :title")
    List<Music> getMusicListByTitle(String title);
}
