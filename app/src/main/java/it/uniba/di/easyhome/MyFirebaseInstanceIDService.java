package it.uniba.di.easyhome;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

        private static final String TAG = "mFirebaseIIDService";
        private static final String SUBSCRIBE_TO = "userABC";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().isEmpty())
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        else
            showNotification(remoteMessage.getData());
    }

    private void showNotification(Map<String, String> data) {
        String title=data.get("title");
        String body=data.get("body");


        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFACTION_CHANNEL_ID="it.uniba.di.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFACTION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("EDMT Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFACTION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.easyhome)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");
        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());


    }

    private void showNotification(String title, String body){
    NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    String NOTIFACTION_CHANNEL_ID="it.uniba.di.test";
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        NotificationChannel notificationChannel=new NotificationChannel(NOTIFACTION_CHANNEL_ID,"Notification",
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("EDMT Channel");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
        notificationChannel.enableLights(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFACTION_CHANNEL_ID);
    notificationBuilder.setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(Color.BLUE)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(alarmSound)
            .setContentInfo("Info");
    notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

}


    @Override
        public void onNewToken(String s) {
         super.onNewToken(s);
            Log.d(TAG, s);
        }
    }