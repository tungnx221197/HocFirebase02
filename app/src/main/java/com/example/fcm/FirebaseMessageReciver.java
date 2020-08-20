package com.example.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.hocfirebase02.R;
import com.example.hocfirebase02.SplashSecreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReciver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private RemoteViews getCustomDesign(String title, String messgae) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.item);
        remoteViews.setTextViewText(R.id.txtTitle, title);
        remoteViews.setTextViewText(R.id.txtMessage, messgae);
        remoteViews.setImageViewResource(R.id.imgIcon, android.R.drawable.ic_dialog_email);
        return remoteViews;
    }

    public void showNotification(String s, String message) {
        Intent intent = new Intent(this, SplashSecreen.class);
        String channel_id = "wep_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(android.R.drawable.ic_dialog_email).setSound(uri).setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(true).setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(s, message));
        } else {
            builder = builder.setContentTitle(s).setContentText(message).setSmallIcon(android.R.drawable.ic_dialog_email);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "App", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
        
    }
}
