package com.mosl.tabapp.service;

/**
 * Created by pramod on 04/05/17.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.ui.MOSLCommonActivity;
import java.util.Map;
import java.util.Random;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class MessageReceiverIntentService extends FirebaseMessagingService {
  private static final String CHANNEL_ID = "saathi";
  String TAG = MessageReceiverIntentService.class.getCanonicalName();

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG, "onMessageReceived");
    super.onMessageReceived(remoteMessage);
    sendNotification(remoteMessage.getData());
  }

  @Override public void onNewToken(String refreshedToken) {
    try {
      SharedPreferences.Editor editor = getPrefs(this).edit();
      editor.putString("TOKEN_ID", refreshedToken);
      editor.apply();

      Looper.prepare();
      MOSLCommonActivity.sendRegistrationToServer(this, this.getContentResolver(), false);
      Log.d(TAG, "New firebase token: " + refreshedToken);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void sendNotification(Map data) {
    if (data != null) {
      String category = "";
      Random random = new Random();
      int m = random.nextInt(9999 - 1000) + 1000;
      String color = data.get("color").toString();
      if (data.get("category") == null) {
      } else {
        category = data.get("category").toString();
      }
      String newsId = data.get("newsId").toString();
      String title = data.get("title").toString();
      String message = data.get("message").toString();
      Intent notificationIntent = new Intent(this, MOSLCommonActivity.class);
      notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      notificationIntent.setAction("Mosl_" + m);
      notificationIntent.putExtra(MOSLConstants.SOURCE, MOSLConstants.FCM_NOTIFICATION_SOURCE);
      notificationIntent.putExtra(MOSLConstants.FCM_NOTIFICATION_CATEGORY, category);
      PendingIntent pendingIntent =
          PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
              PendingIntent.FLAG_ONE_SHOT);
      Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      NotificationCompat.Builder notificationBuilder =
          new NotificationCompat.Builder(this, CHANNEL_ID)
              .setSmallIcon(R.drawable.ic_icon)
              .setTicker(title)
              .setContentTitle(title)
              .setContentText(message)
              .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
              .setAutoCancel(true)
              .setSound(defaultSoundUri)
              .setContentIntent(pendingIntent);

      notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
      NotificationManager notificationManager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      //region OREO
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        String name = "MOSL";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build();

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            audioAttributes
        );

        long[] longArray = { 100, 200, 300, 400, 500, 400, 500, 200, 500 };
        channel.setVibrationPattern(longArray);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setDescription(description);

        notificationManager.createNotificationChannel(channel);
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
      }
      //endregion
      notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }
  }
}
