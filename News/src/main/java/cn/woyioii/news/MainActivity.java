package cn.woyioii.news;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.woyioii.news.Fragment.NewsFragment;
import cn.woyioii.news.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity {
    private boolean isTabletMode;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 检测是否为平板模式
        isTabletMode = getResources().getBoolean(R.bool.isTablet);

        initWindowInsets();
        setupBottomNavigation();

        // 初始化默认Fragment
        if (savedInstanceState == null) {
            loadInitialFragment();
        }
    }

    private void initWindowInsets() {
        View mainView = findViewById(R.id.main_content);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Log.e("MainActivity", "Main content view not found!");
        }
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(new NewsFragment());
                    return true;
                case R.id.navigation_user:
                    switchFragment(new UserFragment());
                    return true;
            }
            return false;
        });
    }

    private void loadInitialFragment() {
        int containerId = isTabletMode ? R.id.news_list_container : R.id.fragment_container;
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, new NewsFragment())
                .commit();
    }

    private void switchFragment(Fragment fragment) {
        if (fragment == null || fragment.equals(currentFragment)) {
            return;
        }

        int containerId = isTabletMode ? R.id.news_list_container : R.id.fragment_container;
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();

        currentFragment = fragment;
    }
}
