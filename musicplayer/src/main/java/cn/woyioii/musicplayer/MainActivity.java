package cn.woyioii.musicplayer;

import android.os.Bundle;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.woyioii.musicplayer.Fragment.LocalMusicFragment;
import cn.woyioii.musicplayer.Fragment.PlaylistFragment;
import cn.woyioii.musicplayer.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private SearchFragment searchFragment;
    private PlaylistFragment playlistFragment;
    private LocalMusicFragment localMusicFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Fragment
        initFragments();

        // 初始化底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    switchFragment(searchFragment);
                    return true;
                case R.id.navigation_playlist:
                    switchFragment(playlistFragment);
                    return true;
                case R.id.navigation_local:
                    switchFragment(localMusicFragment);
                    return true;
            }
            return false;
        });

        // 默认显示搜索页面
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
    }

    private void initFragments() {
        searchFragment = new SearchFragment();
        playlistFragment = new PlaylistFragment();
        localMusicFragment = new LocalMusicFragment();
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            // 如果目标Fragment未被添加，则添加它
            transaction.add(R.id.fragment_container, targetFragment);
        }
        // 隐藏当前Fragment，显示目标Fragment
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.show(targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
    }


}