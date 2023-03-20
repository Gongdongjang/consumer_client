package com.example.consumer_client.alarm;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.consumer_client.R;
import com.example.consumer_client.notification.NotificationList;
import com.example.consumer_client.user.IntegratedLoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FCMService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "test";
    private static final CharSequence CHANNEL_NAME = "1";
    String title;
    String content;
    String user_id;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
        Log.d("fcm",token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().isEmpty()) {
            title = remoteMessage.getNotification().getTitle();
            content = remoteMessage.getNotification().getBody();
        } else {
            title = remoteMessage.getData().get("title");
            content = remoteMessage.getData().get("content");
            user_id=remoteMessage.getData().get("userId");
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        //푸시를 클릭했을때 이동//
        Intent intent = new Intent(this, IntegratedLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("user_id", user_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.img_gongdongjang_logo)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

}
