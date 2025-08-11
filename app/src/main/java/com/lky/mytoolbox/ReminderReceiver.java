package com.lky.mytoolbox;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        long noteId = intent.getLongExtra("note_id", -1);
        int notificationId = (int) noteId; // 将Long类型转换为Integer类型
        if (notificationId == -1) {
            notificationId = (int) System.currentTimeMillis(); // 如果noteId为-1，则使用当前时间戳作为通知ID
        }
        String reminderTitle = intent.getStringExtra("reminder_title");
        String reminderContent = intent.getStringExtra("reminder_content");


        // 创建通知渠道
        createNotificationChannel(context);

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(reminderTitle)
                .setContentText(reminderContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // 显示通知
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build()); // 使用唯一ID
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 设置渠道重要性为 HIGH 并启用振动
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "重要提醒",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("重要事项提醒通道");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 300, 500});

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
