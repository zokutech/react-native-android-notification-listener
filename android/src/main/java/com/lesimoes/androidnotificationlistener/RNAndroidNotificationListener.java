 
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
        System.out.println(nots);
        System.out.println("blleeep");
        if (notification == null || notification.extras == null) return;
        
        String app = sbn.getPackageName();

        if (app == null) app = "Unknown";

        CharSequence titleChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);

        if (titleChars == null || textChars == null) return;
        
        String title = titleChars.toString();
        String text = textChars.toString();

        if (text == null || text == "" || title == null || title == "") return;

//        Log.d(TAG, "Notification received: " + app + " | " + title + " | " + text);

        WritableMap params = Arguments.createMap();
        params.putString("app", app);
        params.putString("title", title);
        params.putString("text", text);

//        =============================================================================
        WritableArray not2 = new WritableNativeArray();
        for (int i = nots.length - 1; i >= 0; i--) {
            StatusBarNotification noti = nots[i];
                    Notification notificationL = noti.getNotification();
//        StatusBarNotification[] nots = getActiveNotifications();
        System.out.println(noti);
        System.out.println("blleeep2");
        if (notificationL == null || notificationL.extras == null) break;

        String appL = noti.getPackageName();

        if (appL == null) appL = "Unknown";

        CharSequence titleCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textCharsL = notificationL.extras.getCharSequence(Notification.EXTRA_TEXT);

        if (titleCharsL == null || textCharsL == null) break;

        String titleL = titleCharsL.toString();
        String textL = textCharsL.toString();

        if (textL == null || textL == "" || titleL == null || titleL == "") break;

        Log.d(TAG, "Notification received5: " + appL + " | " + titleL + " | " + textL);
        System.out.println(nots.length);
//        System.out.println("blleeep3");
        WritableMap paramsL = Arguments.createMap();
        paramsL.putString("app", appL);
        paramsL.putString("title", titleL);
        paramsL.putString("text", textL);
        System.out.println(paramsL.toString());
        not2.pushMap(paramsL);
        }
        System.out.println("NOTIFICATIONS IN THE TRAY");
        System.out.println(not2.toString());
//        =============================================================================
        RNAndroidNotificationListenerModule.sendEvent("notificationReceived", params);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}