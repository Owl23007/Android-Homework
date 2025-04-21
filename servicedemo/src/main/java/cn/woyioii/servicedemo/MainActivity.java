package cn.woyioii.servicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import cn.woyioii.servicedemo.service.MusicService;

public class MainActivity extends AppCompatActivity {
    public Button btn_start, btn_stop;
    Intent intent;

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

        initView();
    }

    private final ActivityResultLauncher permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Toast.makeText(MainActivity.this, "获取了读取音频权限", Toast.LENGTH_SHORT).show();
                        startService(intent);     //启动服务
                    } else {
                        Toast.makeText(MainActivity.this, "没有获取读取音频权限", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //启动权限请求
    private void requestPermission() {
        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(MainActivity.this, MusicService.class);
            intent.putExtra("msg", "hello");
           if(v.getId() == R.id.btn_start) {
               requestPermission();
           }else if(v.getId() == R.id.btn_stop) {
               stopService(intent);
           }
        }
    };

    public void initView() {
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(listener);
        btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("flag", "onDestroy");
        stopService(intent);       // 结束服务
    }
}