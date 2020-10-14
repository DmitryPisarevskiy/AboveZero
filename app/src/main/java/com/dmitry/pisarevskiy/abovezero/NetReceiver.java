package com.dmitry.pisarevskiy.abovezero;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.dmitry.pisarevskiy.abovezero.MainActivity.CONNECTIVITY_ACTION_LOLLIPOP;

public class NetReceiver extends BroadcastReceiver {
    private int messageId = 1000;
    private final String NETWORK_FAILED="Network connection failed";
    private final String TITLE="Broadcast Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && TextUtils.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION) ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && TextUtils.equals(intent.getAction(), CONNECTIVITY_ACTION_LOLLIPOP)) {
            if (intent.getExtras() != null) {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(TITLE)
                            .setContentText(NETWORK_FAILED);
                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(messageId++, builder.build());
                }
            }
        }
    }
}
