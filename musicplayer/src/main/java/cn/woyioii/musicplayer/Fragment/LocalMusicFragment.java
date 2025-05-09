package cn.woyioii.musicplayer.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import cn.woyioii.musicplayer.Adapter.MusicAdapter;
import cn.woyioii.musicplayer.R;
import cn.woyioii.musicplayer.db.AppDatabase;
import cn.woyioii.musicplayer.entity.Music;
import cn.woyioii.musicplayer.service.MusicService;

public class LocalMusicFragment extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        
        // 初始化数据库
        db = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "music-db").build();
        
        initViews(view);
        setupViewPager();
        return view;
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(position == 0 ? "本地音乐" : "我的歌单");
                }
        ).attach();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new LocalMusicListFragment();
            } else {
                return new PlaylistsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
