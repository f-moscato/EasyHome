package it.uniba.di.easyhome;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifica extends Application {
    public static final String CHANNEL_1="channel1";
   @Override
    public void onCreate(){
       super.onCreate();
       createNotificationChannels();
   }
   private void createNotificationChannels(){
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           NotificationChannel channel= new NotificationChannel(
                   CHANNEL_1,
                   "channel 1",
                   NotificationManager.IMPORTANCE_HIGH
           );
           channel.setDescription("This is Channel 1");
           NotificationManager manager= getSystemService(NotificationManager.class);
           manager.createNotificationChannel(channel);
       }
   }
}
