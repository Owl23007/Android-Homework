package cn.woyioii.musicplayer.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.woyioii.musicplayer.Adapter.MusicAdapter;
import cn.woyioii.musicplayer.R;
import cn.woyioii.musicplayer.db.AppDatabase;
import cn.woyioii.musicplayer.entity.Music;
import cn.woyioii.musicplayer.entity.Playlist;
import cn.woyioii.musicplayer.entity.PlaylistMusicCrossRef;
import cn.woyioii.musicplayer.entity.PlaylistWithMusics;
import cn.woyioii.musicplayer.service.MusicService;

public class PlaylistFragment extends Fragment {
    private RecyclerView playlistView;
    private MusicAdapter adapter;
    private List<Music> musicList = new ArrayList<>();
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView totalTime;
    private TextView currentMusicTitle;
    private TextView currentMusicArtist;
    private ImageButton playPauseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private Button importButton;

    private AppDatabase db;
    private boolean isPlaying = false;
    private int currentMusicPosition = -1;
    private Handler handler;
    private Runnable updateSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // 初始化数据库
        db = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "music-db").build();

        initViews(view);
        setupRecyclerView();
        setupPlaybackControls();
        loadMusicFromDatabase();
        return view;
    }

    private void initViews(View view) {
        playlistView = view.findViewById(R.id.playlistView);
        seekBar = view.findViewById(R.id.seekBar);
        currentTime = view.findViewById(R.id.currentTime);
        totalTime = view.findViewById(R.id.totalTime);
        currentMusicTitle = view.findViewById(R.id.currentMusicTitle);
        currentMusicArtist = view.findViewById(R.id.currentMusicArtist);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);

        importButton = view.findViewById(R.id.importButton);
        importButton.setOnClickListener(v -> importLocalMusic());

        handler = new Handler(Looper.getMainLooper());
    }

    private void setupRecyclerView() {
        playlistView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MusicAdapter(requireContext(), musicList);
        playlistView.setAdapter(adapter);

        adapter.setOnItemClickListener((music, position) -> {
            playMusic(position);
        });
    }

    private void setupPlaybackControls() {
        playPauseButton.setOnClickListener(v -> togglePlayPause());
        prevButton.setOnClickListener(v -> playPrevious());
        nextButton.setOnClickListener(v -> playNext());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // 用户拖动进度条
                    updateCurrentTime(progress * 1000L);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 停止自动更新进度
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 发送seek命令到服务
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.setAction("SEEK");
                intent.putExtra("position", seekBar.getProgress() * 1000);
                requireActivity().startService(intent);

                // 恢复自动更新进度
                handler.postDelayed(updateSeekBar, 1000);
            }
        });
    }

    private void importLocalMusic() {
        new Thread(() -> {
            // 查询本地音乐
            List<Music> localMusic = getLocalMusic();
            
            // 创建默认播放列表（如果不存在）
            Playlist defaultPlaylist = new Playlist();
            defaultPlaylist.setName("本地音乐");
            defaultPlaylist.setDescription("导入的本地音乐");
            
            try {
                // 插入播放列表并获取生成的ID
                long playlistId = db.playlistDAO().insertPlaylist(defaultPlaylist);
                
                // 插入音乐
                for (Music music : localMusic) {
                    try {
                        db.musicDAO().insertMusic(music);
                        
                        // 创建关联
                        db.playlistMusicCrossRefDAO().insertPlaylistMusicCrossRef(
                            new PlaylistMusicCrossRef(music.getMusicId(), (int)playlistId)
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // 更新UI
                requireActivity().runOnUiThread(() -> {
                    loadMusicFromDatabase();
                    Toast.makeText(requireContext(), 
                        "成功导入 " + localMusic.size() + " 首歌曲", 
                        Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> 
                    Toast.makeText(requireContext(), 
                        "导入失败: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private List<Music> getLocalMusic() {
        List<Music> list = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = requireActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = UUID.randomUUID().toString();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                Music music = new Music(id, title, artist, album, duration, path);
                music.setLocal(true);
                list.add(music);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    private void loadMusicFromDatabase() {
        new Thread(() -> {
            try {
                // 获取所有播放列表及其音乐
                List<PlaylistWithMusics> playlists =
                        db.playlistMusicCrossRefDAO().getAllPlaylistsWithMusics();

                if (!playlists.isEmpty()) {
                    // 暂时只显示第一个播放列表的音乐
                    List<Music> musics = playlists.get(0).musics;
                    requireActivity().runOnUiThread(() -> {
                        musicList.clear();
                        musicList.addAll(musics);
                        adapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void playMusic(int position) {
        if (position >= 0 && position < musicList.size()) {
            currentMusicPosition = position;
            Music music = musicList.get(position);
            
            // 更新UI
            currentMusicTitle.setText(music.getTitle());
            currentMusicArtist.setText(music.getArtist());
            
            try {
                long duration = Long.parseLong(music.getDuration());
                seekBar.setMax((int)(duration / 1000)); // 转换为秒
                updateTotalTime(duration);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                seekBar.setMax(0);
                totalTime.setText("00:00");
            }
            
            // 发送播放命令到服务
            Intent intent = new Intent(getActivity(), MusicService.class);
            intent.setAction("PLAY");
            intent.putExtra("url", music.getPath());
            requireActivity().startService(intent);
            
            isPlaying = true;
            updatePlayPauseButton();
            
            // 开始更新进度条
            startProgressUpdate();
        }
    }

    private void togglePlayPause() {
        if (currentMusicPosition == -1) {
            // 如果没有选中的歌曲，播放第一首
            if (!musicList.isEmpty()) {
                playMusic(0);
            }
            return;
        }

        Intent intent = new Intent(getActivity(), MusicService.class);
        if (isPlaying) {
            intent.setAction("PAUSE");
        } else {
            intent.setAction("RESUME");
        }
        requireActivity().startService(intent);

        isPlaying = !isPlaying;
        updatePlayPauseButton();
    }

    private void playPrevious() {
        if (currentMusicPosition > 0) {
            playMusic(currentMusicPosition - 1);
        }
    }

    private void playNext() {
        if (currentMusicPosition < musicList.size() - 1) {
            playMusic(currentMusicPosition + 1);
        }
    }

    private void updatePlayPauseButton() {
        playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void startProgressUpdate() {
        if (handler != null) {
            handler.removeCallbacks(updateSeekBar);
        }

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    // 从服务获取当前播放进度（毫秒）
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.setAction("GET_POSITION");
                    try {
                        int currentPosition = MusicService.getCurrentPosition() / 1000; // 转换为秒
                        seekBar.setProgress(currentPosition);
                        updateCurrentTime(currentPosition * 1000L); // 转换回毫秒以用于时间显示
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(this, 1000); // 每秒更新一次
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void updateCurrentTime(long milliseconds) {
        currentTime.setText(formatTime(milliseconds));
    }

    private void updateTotalTime(long milliseconds) {
        totalTime.setText(formatTime(milliseconds));
    }

    private String formatTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) (milliseconds / (1000 * 60));
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }
}
