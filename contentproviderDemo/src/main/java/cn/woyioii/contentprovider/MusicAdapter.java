package cn.woyioii.contentprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.woyioii.contentprovider.entity.Music;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourceId;

    // 存放ListView布局资源的id
    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        resourceId = resource;     // 在构造函数中传入ListView布局资源id（对应的是 list_item.xml）
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 填充布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        // 完成数据和界面元素的绑定（将对应的音乐信息填充到position位置）
        Music music = getItem(position);
        // 获得position位置的音乐信息，getItem是ArrayAdapter的方法
        ImageView musicImage = (ImageView) view.findViewById(R.id.music_image);
        TextView musicTitle = (TextView) view.findViewById(R.id.music_title);
        TextView musicDuration = (TextView) view.findViewById(R.id.music_duration);
        TextView musicArtist = (TextView) view.findViewById(R.id.music_artist);
        TextView musicId = (TextView) view.findViewById(R.id.music_id);
        TextView musicData = (TextView) view.findViewById(R.id.music_data);
        // 绑定数据
        musicTitle.setText(music.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(music.getDuration());
        musicDuration.setText(time);
        musicArtist.setText(music.getArtist());
        musicId.setText(String.valueOf(music.getId()));
        musicData.setText(music.getData());
        return view;
    }
}