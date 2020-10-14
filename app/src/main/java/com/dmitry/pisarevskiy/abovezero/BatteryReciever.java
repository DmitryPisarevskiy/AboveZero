package com.dmitry.pisarevskiy.abovezero;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class BatteryReciever extends BroadcastReceiver {
    private int messageId = 2000;
    private final String BATTERY_LOW ="Battery low";
    private final String TITLE="Broadcast Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(TITLE)
                .setContentText(BATTERY_LOW);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}
