package com.example.android.background.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 11;
    private static final int TRIGGER_WATER_REMINDER_NOTIFICATION_ID = 54;

    public static void remindUserBecauseCharging(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TRIGGER_WATER_REMINDER_NOTIFICATION_ID, builder.build());
    }

    public static PendingIntent contentIntent(Context context) {
        Intent launchIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context, WATER_REMINDER_PENDING_INTENT_ID, launchIntent, FLAG_UPDATE_CURRENT);
    }

    public static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        return BitmapFactory.decodeResource(resources, R.drawable.ic_local_drink_black_24px);
    }
}
