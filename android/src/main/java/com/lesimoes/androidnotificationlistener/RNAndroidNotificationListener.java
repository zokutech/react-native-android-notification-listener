 
package com.lesimoes.androidnotificationlistener;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class RNAndroidNotificationListener extends NotificationListenerService {
    private static final String TAG = "RNAndroidNotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        StatusBarNotification[] nots = getActiveNotifications();
        WritableMap currentNotification = extractNotificationGoodies(sbn);
        WritableMap exportedNotificationsMap = new WritableNativeMap();
        WritableArray not2 = new WritableNativeArray();
        for (int i = nots.length - 1; i >= 0; i--) {
            StatusBarNotification noti = nots[i];
            WritableMap exportMap = extractNotificationGoodies(noti);
            not2.pushMap(exportMap);
        }
        exportedNotificationsMap.putArray("notifications", not2);
        RNAndroidNotificationListenerModule.sendEvent("allNotifications", exportedNotificationsMap);
        RNAndroidNotificationListenerModule.sendEvent("notificationReceived", currentNotification);

    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

    public WritableMap extractNotificationGoodies(StatusBarNotification noti) {
        Notification notificationL = noti.getNotification();
        if (notificationL == null || notificationL.extras == null) return new WritableNativeMap();

        String appL = noti.getPackageName();

        if (appL == null) appL = "Unknown";

        CharSequence titleCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TEXT);
        int numberL = notificationL.number;
        long dateTimeLongL = notificationL.when;
        int priorityL = notificationL.priority;

        SimpleDateFormat sdfL;
        sdfL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdfL.setTimeZone(TimeZone.getTimeZone("UTC"));
        String isoDateL = sdfL.format(dateTimeLongL);

        if (titleCharsL == null || textCharsL == null) return new WritableNativeMap();

        String titleL = titleCharsL.toString();
        String textL = textCharsL.toString();
        String dateTimeL = dateTimeLongL + "";

        if (textL == null || textL == "" || titleL == null || titleL == "") return new WritableNativeMap();

        Log.d(TAG, "Notification in Tray: " + appL + " | " + titleL + " | " + textL);
        WritableMap paramsL = Arguments.createMap();
        paramsL.putString("app", appL);
        paramsL.putString("title", titleL);
        paramsL.putString("text", textL);
        paramsL.putInt("number", numberL);
        paramsL.putString("dateTimeISO", isoDateL);
        paramsL.putString("dateTimeMS", dateTimeL);
        paramsL.putInt("priority", priorityL);
        return paramsL;
    }
}