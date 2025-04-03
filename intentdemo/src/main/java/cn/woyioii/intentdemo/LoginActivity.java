package cn.woyioii.intentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btn;
    private EditText et_phone;
    private EditText et_password;

    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_phone = findViewById(R.id.phone);
        et_password = findViewById(R.id.password);
        btn = findViewById(R.id.login_btn);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 提交登录数据
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("phone", et_phone.getText().toString());
                    bundle.putString("password", et_password.getText().toString());

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        } else {
            Log.e("LoginActivity", "Button with id login_btn not found");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置为全屏模式（隐藏状态条）
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login); // 确保调用 setContentView
        initView();
        // 监听导航按钮
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            // Log 或者其他处理，表示 Toolbar 未找到
            Log.e("LoginActivity", "Toolbar not found");
        }
    }
}
