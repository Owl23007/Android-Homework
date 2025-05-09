package cn.woyioii.musicplayer.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import cn.woyioii.musicplayer.Adapter.PlaylistAdapter;
import cn.woyioii.musicplayer.R;
import cn.woyioii.musicplayer.db.AppDatabase;
import cn.woyioii.musicplayer.entity.Playlist;
import cn.woyioii.musicplayer.entity.PlaylistWithMusics;

public class PlaylistsFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private List<PlaylistWithMusics> playlists = new ArrayList<>();
    private Button btnCreatePlaylist;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        db = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "music-db").build();

        initViews(view);
        setupRecyclerView();
        loadPlaylists();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
        btnCreatePlaylist.setOnClickListener(v -> showCreatePlaylistDialog());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlaylistAdapter(requireContext(), playlists);
        adapter.setOnItemClickListener((playlist, position) -> {
            // 打开播放列表详情
            // TODO: 实现播放列表详情页面
        });
        recyclerView.setAdapter(adapter);
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("新建歌单");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_playlist, null);
        EditText etPlaylistName = dialogView.findViewById(R.id.etPlaylistName);
        EditText etPlaylistDescription = dialogView.findViewById(R.id.etPlaylistDescription);

        builder.setView(dialogView)
                .setPositiveButton("创建", (dialog, which) -> {
                    String name = etPlaylistName.getText().toString();
                    String description = etPlaylistDescription.getText().toString();
                    createPlaylist(name, description);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void createPlaylist(String name, String description) {
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "歌单名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                Playlist playlist = new Playlist();
                playlist.setName(name);
                playlist.setDescription(description);
                db.playlistDAO().insertPlaylist(playlist);

                requireActivity().runOnUiThread(() -> {
                    loadPlaylists();
                    Toast.makeText(requireContext(),
                            "歌单创建成功",
                            Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "创建失败: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void loadPlaylists() {
        new Thread(() -> {
            try {
                List<PlaylistWithMusics> loadedPlaylists = 
                    db.playlistMusicCrossRefDAO().getAllPlaylistsWithMusics();
                requireActivity().runOnUiThread(() -> {
                    playlists.clear();
                    playlists.addAll(loadedPlaylists);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
