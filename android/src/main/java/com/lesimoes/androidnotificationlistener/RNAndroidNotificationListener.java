 
package com.lesimoes.androidnotificationlistener;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.util.Log;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RNAndroidNotificationListener extends NotificationListenerService {
    private static final String TAG = "RNAndroidNotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        StatusBarNotification[] nots = getActiveNotifications();
        if (notification == null || notification.extras == null) return;
        
        String app = sbn.getPackageName();

        if (app == null) app = "Unknown";

        CharSequence titleChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);

        if (titleChars == null || textChars == null) return;
        
        String title = titleChars.toString();
        String text = textChars.toString();

        if (text == null || text == "" || title == null || title == "") return;

        Log.d(TAG, "Notification received: " + app + " | " + title + " | " + text);

        WritableMap params = Arguments.createMap();
        params.putString("app", app);
        params.putString("title", title);
        params.putString("text", text);

//        =============================================================================
        WritableMap exportedNotificationsMap = new WritableNativeMap();
        WritableArray not2 = new WritableNativeArray();
        for (int i = nots.length - 1; i >= 0; i--) {
            StatusBarNotification noti = nots[i];
                    Notification notificationL = noti.getNotification();
        if (notificationL == null || notificationL.extras == null) break;

        String appL = noti.getPackageName();

        if (appL == null) appL = "Unknown";

        CharSequence titleCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TEXT);
        int numberL = notificationL.number;
        long dateTimeLongL = notificationL.when;
        int priorityL = notificationL.priority;

        if (titleCharsL == null || textCharsL == null) break;

        String titleL = titleCharsL.toString();
        String textL = textCharsL.toString();
        String dateTimeL = dateTimeLongL + "";

        if (textL == null || textL == "" || titleL == null || titleL == "") break;

        Log.d(TAG, "Notification in Tray: " + appL + " | " + titleL + " | " + textL);
        WritableMap paramsL = Arguments.createMap();
        paramsL.putString("app", appL);
        paramsL.putString("title", titleL);
        paramsL.putString("text", textL);
        paramsL.putInt("number", numberL);
        paramsL.putString("dateTime", dateTimeL);
        paramsL.putInt("priority", priorityL);
        not2.pushMap(paramsL);
        }
//        =============================================================================
        exportedNotificationsMap.putArray("notifications", not2);
        RNAndroidNotificationListenerModule.sendEvent("allNotifications", exportedNotificationsMap);
        RNAndroidNotificationListenerModule.sendEvent("notificationReceived", params);

    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}