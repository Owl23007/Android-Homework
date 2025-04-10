package cn.woyioii.intentdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btn;
    private EditText et_phone;
    private EditText et_password;
    private ImageView photo;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    result.getData();
                    Log.e("LoginActivity", "Result OK");
                } else {
                    Log.e("LoginActivity", "Result not OK");
                }
            });

    ActivityResultLauncher<String> launcher2 = registerForActivityResult(
            new ActivityResultContracts.GetContent(), result -> {
                if (result != null) {
                    photo.setImageURI(result);
                    Log.e("LoginActivity", "Image selected");
                } else {
                    Log.e("LoginActivity", "No image selected");
                }
            });

    ActivityResultLauncher<Void> launcher3 = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            new ActivityResultCallback<Bitmap>(){
                @Override
                public void onActivityResult(Bitmap result) {
                    //result为拍摄照片Bitmap格式
                    photo.setImageBitmap(result);
                }
            });

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

                    launcher.launch(intent);
                }
            });
        } else {
            Log.e("LoginActivity", "Button with id login_btn not found");
        }
        photo = (ImageView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher3.launch(null);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==200){
            if (resultCode == RESULT_OK){
                photo.setImageURI(data.getData());
            }
        }
    }
}
