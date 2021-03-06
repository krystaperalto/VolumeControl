package com.volutime;


import android.app.Notification;
import android.content.Context;

public class Session extends Thread {

    private Context context;
    private NotificationUtils mNotificationUtils;

    public Session(Context context) {
        this.context = context;
        System.out.println("Session Started"); // Debug only

    }

    public void run() {
        while (true) {
            try {
                Synch.session.acquire();
                System.out.println("Acquired"); // Debug only
            } catch (Exception ignored) {}

            // We've acquired the session semaphore which means the session has started
            sendNotification("Session Started", "Pause your music to pause the current session");

            try {
                Synch.mutex_session_active.acquire();
                Synch.session_active = true;
                Synch.mutex_session_active.release();
            } catch (Exception ignored) {}

            Synch.session_update.release();
        }
    }

    public void sendNotification(String title, String body) {
        mNotificationUtils = new NotificationUtils(context);
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification(title, body);

        mNotificationUtils.getManager().notify(101, nb.build());
        System.out.println("Notification Sent"); // Debug only
    }

}
