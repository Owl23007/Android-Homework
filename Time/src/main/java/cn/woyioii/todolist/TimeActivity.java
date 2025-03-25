package cn.woyioii.todolist;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimeActivity extends AppCompatActivity {
    // 时间
    private TextView timeTextView;
    // 日期
    private TextView dateTextView;
    // 星期
    private TextView weekdayTextView;
    // 背景布局
    private RelativeLayout backgroundLayout;

    private Timer timer;
    private int currentHour = -1;

    // 配置每个时间段对应的背景图片
    private final int[] hourBackgrounds = {
            R.drawable.img10_14, R.drawable.img6_10, R.drawable.img10_14, R.drawable.img14_18,
            R.drawable.img18_22, R.drawable.img22_2,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        initViews();
        startClock();
    }

    public void initViews() {
        // 全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        timeTextView = findViewById(R.id.timeTextView);
        dateTextView = findViewById(R.id.dateTextView);
        weekdayTextView = findViewById(R.id.weekdayTextView);
        backgroundLayout = findViewById(R.id.clock_background);

        // 设置数字字体到所有文本视图
        Typeface digitalFont;

        try {
            digitalFont = androidx.core.content.res.ResourcesCompat.getFont(this, R.font.digital);
        } catch (Exception e) {
            digitalFont = Typeface.MONOSPACE;
        }
        timeTextView.setTypeface(digitalFont);
    }

    private void startClock() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateClock());
            }
        }, 0, 1000); // 每秒更新一次
    }

    private void updateClock() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        // (HH:mm:ss)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeTextView.setText(timeFormat.format(now));

        //  (yyyy年MM月dd日)
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        dateTextView.setText(dateFormat.format(now));

        // (星期x)
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("E", Locale.CHINESE);
        weekdayTextView.setText(weekdayFormat.format(now));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour != currentHour) {
            currentHour = hour;
            updateBackground(hour);
        }
    }

    private void updateBackground(int hour) {
        // 根据当前小时选择合适的背景
        int backgroundIndex;

        if (hour >= 2 && hour < 6) {
            backgroundIndex = 0;
        } else if (hour >= 6 && hour < 10) {
            backgroundIndex = 1;
        } else if (hour >= 10 && hour < 14) {
            backgroundIndex = 2;
        } else if (hour >= 14 && hour < 18) {
            backgroundIndex = 3;
        } else if (hour >= 18 && hour < 22) {
            backgroundIndex = 4;
        } else { // 22-2点
            backgroundIndex = 5;
        }

        backgroundLayout.setBackgroundResource(hourBackgrounds[backgroundIndex]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}