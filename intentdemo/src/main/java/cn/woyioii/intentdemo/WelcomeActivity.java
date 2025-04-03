package cn.woyioii.intentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private TextView tv_phone;
    private TextView tv_password;
    
    public void initView() {
        tv_phone = findViewById(R.id.tv_phone);
        tv_password = findViewById(R.id.tv_password);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);  // 添加这行
        initView();

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String s_phone = bundle.getString("phone", "");
            String s_password = bundle.getString("password", "");

            if (tv_phone != null) {
                tv_phone.setText("手机号=" + s_phone);
            }
            if (tv_password != null) {
                tv_password.setText("密码=" + s_password);
            }
        } else {
            Log.e("WelcomeActivity", "No extras found in intent");
        }
    }
}
