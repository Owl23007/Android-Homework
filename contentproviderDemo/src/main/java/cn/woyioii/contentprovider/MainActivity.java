package cn.woyioii.contentprovider;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.woyioii.contentprovider.entity.Music;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestPermission3();
    }

    private final ActivityResultLauncher permissionLauncher3 = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    //true表示权限申请成功
                    if ( result ) {
                       musicList = getMusicList();     //获取音乐列表
                       display(musicList);
                        listView= findViewById(R.id.list);
                        MusicAdapter adapter = new MusicAdapter( MainActivity.this, R.layout.list_item, musicList);
                        listView.setAdapter(adapter);
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);      //单选
                        listView.setOnItemClickListener(listener);
                    }
                }
            }
    );

    ListView listView = null;
    MediaPlayer mediaPlayer= new MediaPlayer();     // 播放器
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {       // ListView监听器
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView.setSelector( R.color.teal_200 );           // 设置高亮背景色
            String data = musicList.get(position).getData();      //获得选中项音乐的路径
            mediaPlayer.reset();         //重置
            try {
                mediaPlayer.setDataSource(data);     // 设置音乐源
                mediaPlayer.prepare();        //准备
                mediaPlayer.start();             //播放
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    //在需要的时候启动权限请求
    private void requestPermission3() {
        permissionLauncher3.launch(Manifest.permission.READ_MEDIA_AUDIO);
    }

    private List<Music> musicList = new ArrayList<>();
    //获取音乐列表
    public List<Music> getMusicList() {
        List<Music> list = new ArrayList<Music>();
        Cursor cursor = getContentResolver().query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,  null,  null,  MediaStore.Audio.Media._ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //获取音乐的id、歌曲名、歌手名、文件全路径、时长(毫秒)
                int id_index = cursor.getColumnIndex( MediaStore.Audio.Media._ID );
                int title_index = cursor.getColumnIndex( MediaStore.Audio.Media.TITLE );
                int artist_index = cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST );
                int path_index = cursor.getColumnIndex( MediaStore.Audio.Media.DATA );
                int duration_index = cursor.getColumnIndex( MediaStore.Audio.Media.DURATION );
                int id = cursor.getInt(id_index);
                String title = cursor.getString(title_index);
                String artist = cursor.getString(artist_index);
                String path = cursor.getString(path_index);
                int duration = cursor.getInt(duration_index);
                //创建音乐对象，并添加到list中
                Music music = new Music(id, title, artist, path, duration);
                list.add(music);
            } while ( cursor.moveToNext() );
        }
        cursor.close();
        return list;
    }
    // 记得关闭一

    public void display( List<Music> list ) {
        if (list == null || list.size() < 1) {
            Log.d("flag", "没有音乐信息");
        } else {
            Iterator<Music> iterator = list.iterator();
            while( iterator.hasNext() ) {
                Music music = iterator.next();
                Log.d("flag", "id=" + music.getId());
                Log.d("flag", "歌曲名=" + music.getTitle());
                Log.d("flag", "歌手名=" + music.getArtist());
                Log.d("flag", "文件路径=" + music.getData());
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");     //设置时间格式 -- "分:秒"格式
                String time = sdf.format( music.getDuration() );    //将时长(毫秒数)转换为分秒格式
                Log.d("flag", "时长=" + time);
            }
        }
    }

}