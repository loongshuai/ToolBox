package com.lky.mytoolbox;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private TextView dateTextView, reminderTextView;
    private Button saveButton;
    private Button deleteButton;
    private Button addReminderButton;

    private Button addPhotoButton;
    private ImageView photoImageView;

    private Note note;
    private NoteDatabaseHelper dbHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_ALARM_PERMISSION = 4;

    private TextWatcher textWatcher;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        titleEditText = findViewById(R.id.note_title_edit);
        contentEditText = findViewById(R.id.note_content_edit);
        dateTextView = findViewById(R.id.note_date_edit);
        saveButton = findViewById(R.id.save_note_button);
        deleteButton = findViewById(R.id.delete_note_button);
        addReminderButton = findViewById(R.id.add_reminder_button);
        addPhotoButton = findViewById(R.id.add_photo_button);
        photoImageView = findViewById(R.id.photo_image_view);
        reminderTextView = findViewById(R.id.reminder_text_view);
        dbHelper = new NoteDatabaseHelper(this);

        long noteId = getIntent().getLongExtra("note_id", -1);
        if (noteId != -1) {
            note = dbHelper.getNote(noteId);
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
            dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(note.getDate()));
            if (note.getPhotoData() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(note.getPhotoData(), 0, note.getPhotoData().length);
                photoImageView.setImageBitmap(bitmap);
            }
            if (note.getDrawingData() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(note.getDrawingData(), 0, note.getDrawingData().length);
                photoImageView.setImageBitmap(bitmap);
            }
        } else {
            note = new Note(-1, "无标题", "", new Date(), false, "", "", null, null, 0);
            titleEditText.setText("无标题"); // 设置默认标题
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkExactAlarmPermission()) {
                    showDatePicker(); // 直接显示日期选择对话框
                } else {
                    // 跳转到系统设置申请权限
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_ALARM_PERMISSION);
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        if (note.getReminderDate() != null && !note.getReminderDate().isEmpty()) {
            reminderTextView.setText("提醒日期: " + note.getReminderDate());
        }
        if (note.getAlarmTime() != null && !note.getAlarmTime().isEmpty()) {
            reminderTextView.append("\n闹钟时间: " + note.getAlarmTime());
        }

        // 初始化 TextWatcher
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要处理
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 不需要处理
            }

            @Override
            public void afterTextChanged(Editable s) {
                highlightText(s);
            }
        };

        // 添加 TextWatcher 监听 contentEditText 的文本变化
        contentEditText.addTextChangedListener(textWatcher);

        // 初始化 Calendar
        calendar = Calendar.getInstance();
    }

    private boolean checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            return alarmManager.canScheduleExactAlarms();
        }
        // Android 12 以下默认有权限
        return true;
    }

    private void saveNote() {
        note.setTitle(titleEditText.getText().toString());
        note.setContent(contentEditText.getText().toString());
        note.setDate(new Date());

        if (note.getId() == -1) {
            dbHelper.addNote(note);
        } else {
            dbHelper.updateNote(note);
        }
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteNote() {
        // 删除闹钟
        cancelAlarm(note.getRequestCode());

        // 清除提醒信息
        note.setReminderDate("");
        note.setAlarmTime("");
        reminderTextView.setText("提醒时间");

        // 删除笔记
        dbHelper.deleteNote(note);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        showTimePickerForDate();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerForDate() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        // 处理日期时间提醒逻辑
                        long reminderTime = calendar.getTimeInMillis();
                        note.setReminderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(reminderTime));
                        reminderTextView.setText("提醒时间: " + note.getReminderDate());
                        setAlarm(reminderTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ALARM_PERMISSION) {
            if (checkExactAlarmPermission()) {
                showDatePicker(); // 用户已授权，显示日期选择对话框
            } else {
                Toast.makeText(this, "需要精确闹钟权限以设置提醒", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            byte[] drawingData = data.getByteArrayExtra("drawing_data");
            note.setDrawingData(drawingData);
            Bitmap bitmap = BitmapFactory.decodeByteArray(drawingData, 0, drawingData.length);
            photoImageView.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    note.setPhotoData(bitmapToByteArray(bitmap));
                    photoImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        android.graphics.Bitmap.CompressFormat compressFormat = android.graphics.Bitmap.CompressFormat.PNG;
        int quality = 100;
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void setAlarm(long reminderTime) {
        if (reminderTime < System.currentTimeMillis()) {
            Toast.makeText(this, "不能设置过去时间的提醒", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取笔记标题和内容
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("note_id", note.getId());
        intent.putExtra("reminder_title", title);
        intent.putExtra("reminder_content", content);

        // 使用唯一 requestCode 避免覆盖
        int requestCode = note.getId() != -1 ? (int) note.getId() : (int) System.currentTimeMillis();
        note.setRequestCode(requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime,
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime,
                    pendingIntent
            );
        }
    }

    private void cancelAlarm(int requestCode) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALARM_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDatePicker(); // 用户已授权，显示日期选择对话框
            } else {
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限被拒绝")
                .setMessage("为了设置闹钟，需要授予“设置闹钟”权限。请前往应用设置页面手动授予权限。")
                .setPositiveButton("前往设置", (dialog, which) -> openAppSettings())
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void highlightText(Editable text) {
        // 移除 TextWatcher
        contentEditText.removeTextChangedListener(textWatcher);

        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile("(\\b\\d{6,}\\b)|((\\d+)(点|时|分|元|块|min|am|pm|s|秒|月|日|))");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.GRAY);
            spannableString.setSpan(colorSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        contentEditText.setText(spannableString);
        contentEditText.setSelection(text.length()); // 保持光标位置不变

        // 重新添加 TextWatcher
        contentEditText.addTextChangedListener(textWatcher);
    }
}
