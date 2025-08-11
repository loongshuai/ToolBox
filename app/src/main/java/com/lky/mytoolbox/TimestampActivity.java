package com.lky.mytoolbox;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimestampActivity extends AppCompatActivity {
    private Spinner spinnerTimeZones;
    private EditText etDateTime;
    private TextView tvResultDate, tvResultISO, tvResult10, tvResult13, tvExplanation;
    private Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timestamp);

        // 初始化组件
        spinnerTimeZones = findViewById(R.id.spinner_time_zones);
        etDateTime = findViewById(R.id.et_date_time);
        btnGenerate = findViewById(R.id.btn_generate);
        tvResultDate = findViewById(R.id.tv_result_date); // 新增
        tvResultISO = findViewById(R.id.tv_result_iso);
        tvResult10 = findViewById(R.id.tv_result_10);
        tvResult13 = findViewById(R.id.tv_result_13);
        tvExplanation = findViewById(R.id.tv_explanation);

        // 设置默认当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        etDateTime.setText(dateFormat.format(new Date()));

        // 填充时区下拉列表并设置默认时区（东八区）
        List<String> timeZones = new ArrayList<>();
        String defaultTimeZone = "Asia/Shanghai"; // 东八区
        int defaultIndex = 0;
        for (String id : TimeZone.getAvailableIDs()) {
            timeZones.add(id);
            if (id.equals(defaultTimeZone)) {
                defaultIndex = timeZones.size() - 1;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timeZones);
        spinnerTimeZones.setAdapter(adapter);
        spinnerTimeZones.setSelection(defaultIndex); // 设置默认选中

        // 绑定生成按钮点击事件
        btnGenerate.setOnClickListener(v -> generateTimestamp());
    }

    private void generateTimestamp() {
        try {
            String input = etDateTime.getText().toString();
            String timeZoneId = spinnerTimeZones.getSelectedItem().toString();
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);

            // 解析输入日期时间
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputFormat.setTimeZone(timeZone);
            Date date = inputFormat.parse(input);

            // 生成多种格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(timeZone);
            String formattedDate = dateFormat.format(date);

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            isoFormat.setTimeZone(timeZone);
            String isoString = isoFormat.format(date).replace("GMT", "+");

            long timestamp = date.getTime();
            String ts10 = String.valueOf(timestamp / 1000);
            String ts13 = String.valueOf(timestamp);

            // 显示结果
            tvResultDate.setText("日期时间: " + formattedDate);
            tvResult10.setText("10位时间戳: " + ts10);
            tvResult13.setText("13位时间戳: " + ts13);
            tvResultISO.setText("ISO 8601: " + isoString);
            tvExplanation.setText("说明：+表示东时区（如UTC+8），-表示西时区（如UTC-5）");

        } catch (ParseException e) {
            tvResultDate.setText("日期格式错误，请输入：yyyy-MM-dd HH:mm:ss");
        }
    }
}
