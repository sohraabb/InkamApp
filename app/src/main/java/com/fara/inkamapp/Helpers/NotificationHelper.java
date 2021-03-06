package com.fara.inkamapp.Helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fara.inkamapp.R;


public class NotificationHelper {
    private final Context mContext;
    private final int NOTIFICATION_ID = 1;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Put the notification into the status bar
     */
    public NotificationManager createNotification() {
        // get the notification manager
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // create the notification
        int icon = android.R.drawable.stat_sys_download;
        CharSequence tickerText = mContext.getString(R.string.downloading_update); // Initial
        // text
        // that
        // appears
        // in
        // the
        // status
        // bar
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);

        // create the content which is shown in the notification pulldown
        mContentTitle = mContext.getString(R.string.downloading); // Full title
        // of the
        // notification
        // in the
        // pull down
        CharSequence contentText = "0% complete"; // Text of the notification in
        // the pull down

        // you have to set a PendingIntent on a notification to tell the system
        // what you want it to do when the notification is selected
        // I don't want to use this here so I'm just creating a blank one
        Intent notificationIntent = new Intent();
        mContentIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, 0);

        // add the additional content and intent to the notification
//        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText,
//                mContentIntent);
        Notification.Builder builder = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.logo_income)
                .setContentTitle(mContentTitle)
                .setContentText(contentText)
                .setContentIntent(mContentIntent);
        Notification mNotification = builder.build();
        // make this notification appear in the 'Ongoing events' section
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        // show the notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        return mNotificationManager;
    }

    /**
     * Receives progress updates from the background task and updates the status
     * bar notification appropriately
     *
     * @param percentageComplete
     */
    public void progressUpdate(int percentageComplete) {
        // build up the new status message
        CharSequence contentText = percentageComplete + "% complete";
        // publish it to the status bar
//        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText,
//                mContentIntent);
        Notification.Builder builder = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.logo_income)
                .setContentTitle(mContentTitle)
                .setContentText(contentText)
                .setContentIntent(mContentIntent);
        Notification mNotification = builder.build();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * called when the background task is complete, this removes the
     * notification from the status bar. We could also use this to add a new
     * ‘task complete’ notification
     */
    public void completed() {
        // remove the notification from the status bar
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}

