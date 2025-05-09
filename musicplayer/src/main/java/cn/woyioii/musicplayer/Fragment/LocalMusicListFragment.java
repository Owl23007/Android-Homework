package cn.woyioii.musicplayer.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import cn.woyioii.musicplayer.service.MusicService;

public class LocalMusicListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private List<Music> musicList = new ArrayList<>();
    private Button btnImportMusic;
    private AppDatabase db;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);

        db = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "music-db").build();

        initViews(view);
        setupRecyclerView();
        setupPermissionLauncher();
        loadLocalMusic();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.localMusicList);
        btnImportMusic = view.findViewById(R.id.btnImportMusic);
        btnImportMusic.setOnClickListener(v -> checkAndRequestPermission());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MusicAdapter(requireContext(), musicList);
        adapter.setOnItemClickListener((music, position) -> {
            // 播放音乐
            Intent intent = new Intent(getActivity(), MusicService.class);
            intent.setAction("PLAY");
            intent.putExtra("url", music.getPath());
            requireActivity().startService(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        importLocalMusic();
                    } else {
                        Toast.makeText(requireContext(),
                                "需要权限来访问本地音乐",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            importLocalMusic();
        }
    }

    private void importLocalMusic() {
        new Thread(() -> {
            List<Music> localMusic = scanLocalMusic();
            if (!localMusic.isEmpty()) {
                try {
                    for (Music music : localMusic) {
                        db.musicDAO().insertMusic(music);
                    }
                    requireActivity().runOnUiThread(() -> {
                        loadLocalMusic();
                        Toast.makeText(requireContext(),
                                "成功导入 " + localMusic.size() + " 首歌曲",
                                Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(),
                                    "导入失败: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }
            }
        }).start();
    }

    private List<Music> scanLocalMusic() {
        List<Music> list = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = requireActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null);

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

    private void loadLocalMusic() {
        new Thread(() -> {
            try {
                List<Music> loadedMusic = db.musicDAO().getAllMusic();
                requireActivity().runOnUiThread(() -> {
                    musicList.clear();
                    musicList.addAll(loadedMusic);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
