package cn.woyioii.musicplayer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import cn.woyioii.musicplayer.DAO.MusicDAO;
import cn.woyioii.musicplayer.DAO.PlaylistDAO;
import cn.woyioii.musicplayer.DAO.PlaylistMusicCrossRefDAO;
import cn.woyioii.musicplayer.entity.Music;
import cn.woyioii.musicplayer.entity.Playlist;
import cn.woyioii.musicplayer.entity.PlaylistMusicCrossRef;

@Database(entities = { Music.class, Playlist.class, PlaylistMusicCrossRef.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MusicDAO musicDAO();
    public abstract PlaylistDAO playlistDAO();
    public abstract PlaylistMusicCrossRefDAO playlistMusicCrossRefDAO();
}