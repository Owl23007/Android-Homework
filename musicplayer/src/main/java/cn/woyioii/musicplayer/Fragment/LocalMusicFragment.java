package cn.woyioii.musicplayer.Fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.woyioii.musicplayer.Adapter.MusicAdapter;
import cn.woyioii.musicplayer.MainActivity;
import cn.woyioii.musicplayer.R;
import cn.woyioii.musicplayer.model.Music;

// LocalMusicFragment.java
public class LocalMusicFragment extends Fragment {
    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private List<Music> musicList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);

        // 先获取数据
        musicList = getMusicList();

        Log.d("MusicList", musicList.toString());

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.localMusicList);

        // 先设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 再设置适配器
        adapter = new MusicAdapter(requireContext(), musicList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<Music> getMusicList() {
        List<Music> list = new ArrayList<Music>();
        Cursor cursor = requireActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media._ID + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //获取音乐的id、歌曲名、歌手名、文件全路径、时长(毫秒)
                int id_index = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int title_index = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artist_index = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int album_index = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int path_index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int duration_index = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                String id = cursor.getString(id_index);
                String title = cursor.getString(title_index);
                String artist = cursor.getString(artist_index);
                String album = cursor.getString(album_index);
                String path = cursor.getString(path_index);
                String duration = cursor.getString(duration_index);

                Music music = new Music(id, title, artist, album, duration, path);
                list.add(music);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return list;
    }
}
