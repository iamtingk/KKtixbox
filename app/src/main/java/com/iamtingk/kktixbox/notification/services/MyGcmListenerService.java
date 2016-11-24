package com.iamtingk.kktixbox.notification.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.iamtingk.kktixbox.MainActivity;
import com.iamtingk.kktixbox.NotificationActivity;
import com.iamtingk.kktixbox.R;

/**
 * Created by tingk on 2016/7/10.
 */
public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("messageon",message);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT   );
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notifi_icon)//狀態上列圖案
                .setLargeIcon(BitmapFactory.decodeResource(MyGcmListenerService.this.getResources(),R.mipmap.notifi_icon))//狀態清單圖案
                .setContentTitle("KKtix Box")//狀態標題
                .setContentText(message)//狀態內文
                .setAutoCancel(true)//點擊後會自動移除狀態列
                .setSound(defaultSoundUri)//默認提示音效
                .setContentIntent(pendingIntent);//點擊後連到哪
        notificationManager.notify(0,notificationBuilder.build());

    }
}
