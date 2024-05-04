package com.nayan.taskschedulerreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notification extends BroadcastReceiver {
    public static int notificationId;
    public static final String channelId = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = (int) System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_check_circle_outline_24)
                .setContentTitle(intent.getStringExtra("titleExtra"))
                .setContentText(intent.getStringExtra("messageExtra"))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

//        send data
        Intent intent1 = new Intent(context, NotificationData.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("NOTIFICATION_TITLE",intent.getStringExtra("titleExtra"));
        intent1.putExtra("NOTIFICATION_MESSAGE",intent.getStringExtra("messageExtra"));

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                notificationId, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());

    }
}


