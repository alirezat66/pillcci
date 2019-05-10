package greencode.ir.pillcci.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationManagerCompat;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.MainActivity;

/**
 * Created by alireza on 5/22/18.
 */

public class NotificationManagerService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public NotificationManagerService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("پیلچی");
        builder.setContentText("شما باید داروی خودتان را بخورید. برای دیدن جزیات کلیک کنید.");
        builder.setSmallIcon(R.drawable.ic_add_white_24dp);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
