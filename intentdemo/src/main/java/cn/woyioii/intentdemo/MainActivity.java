package cn.woyioii.intentdemo;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动登录窗口
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction( Intent.ACTION_SEND );
//                //下面2行代码功能：将项目raw文件夹中图片复制到手机相册，便于得到图片uri值
//                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.raw.gui );
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"hi",""));
//                intent.setType("image/*"); //设置数据MIME类型
//
//                intent.putExtra( Intent.EXTRA_STREAM, uri );
//                startActivity(intent);
//                // Intent.EXTRA_STREAM是附件
                requestPermission();    //申请权限
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               testAlarm();
            }
        });
    }

    public void testAlarm() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = new TimePickerDialog(
                MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        // ①创建Intent
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        // ②创建PendingIntent(封装intent)
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 100, intent, PendingIntent.FLAG_IMMUTABLE);

                        Calendar tmp = Calendar.getInstance();
                        tmp.set(Calendar.HOUR_OF_DAY, hourOfDay);  //设置时
                        tmp.set(Calendar.MINUTE, minute);          //设置分
                        tmp.set(Calendar.SECOND, 0);               //设置秒
                        // ③创建闹钟（一次性任务）
                        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        manager.set(AlarmManager.RTC_WAKEUP, tmp.getTimeInMillis(), pendingIntent);
                        Toast.makeText(MainActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        tpd.show();
    }

    private static final int NOTIFY_ID = 1;
    //通知的id (自拟)
    private static final String CHANNEL_ID = "123";     //通知渠道的id (自拟)
    private static final String CHANNEL_Name = "mychannel";    //通知渠道的名称(自拟)
    NotificationManager manager;
    Notification notification;
    NotificationChannel channel;
    PendingIntent pendingIntent;
    Bitmap bitmap;
    //通知管理类
    //通知类
    //通知渠道
    //延迟Intent
    //通知的LargeIcon
    // (1) 创建权限申请的启动器
    private final ActivityResultLauncher permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {    // true表示申请成功
                        sendNotify();     //发送通知
                    }
                }
            }

    );

    // (2) 在需要的时候启动权限请求
    private void requestPermission() {

        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
    }

    public void sendNotify() {
        // ② 创建通知管理器
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // ③ 创建通知渠道
        channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name,
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        // ④ 创建PendingIntent

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                intent, PendingIntent.FLAG_IMMUTABLE);
        // ⑤ 创建大图标的Bitmap
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        notification = new Notification.Builder(MainActivity.this, CHANNEL_ID)
                .setContentTitle("提醒")                //通知标题
                .setContentText("通知内容…")     //通知内容
                .setWhen(System.currentTimeMillis())   //通知产生的时间
                .setShowWhen(true)     //显示时间
                .setSmallIcon(R.drawable.icon)    //小图标
                .setLargeIcon(bitmap)     //大图标Bitmap
                .setAutoCancel(true)       //通知点击后自动删除
                .setContentIntent(pendingIntent)    //设置通知点击的Intent
                .build();     //构建通知
        // ⑦发送通知
        manager.notify(NOTIFY_ID, notification);
    }
    }
