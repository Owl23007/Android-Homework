package cn.woyioii.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.woyioii.musicplayer.R;
import cn.woyioii.musicplayer.model.Music;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<Music> musicList;
    private Context context;

    public MusicAdapter(Context context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Music music = musicList.get(position);

        // 设置标题
        holder.tvTitle.setText(music.getTitle());

        // 设置副标题（歌手 - 专辑）
        String subInfo = music.getArtist() + " - " + music.getAlbum();
        holder.tvSubInfo.setText(subInfo);

        // 设置时长
        String duration = formatDuration(Long.parseLong(music.getDuration()));
        holder.tvDuration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(long duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvTitle;
        TextView tvSubInfo;
        TextView tvDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.ivType);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubInfo = itemView.findViewById(R.id.tvSubInfo);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
    }
}