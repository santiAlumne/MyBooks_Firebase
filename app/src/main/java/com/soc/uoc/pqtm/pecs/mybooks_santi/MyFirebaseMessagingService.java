package com.soc.uoc.pqtm.pecs.mybooks_santi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_VIEW;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager notificationManager;
    private String NOTIFICATION_CHANNEL_ID;
    private String BOOK;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Map<String, String> data = null;

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);


        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            Log.d(TAG, "Message Notification Body: " + body);
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            //send notification
            sendNotification(title, body, data);
        }


    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NOTIFICATION_CHANNEL_ID = "com.soc.uoc.pqtm.pecs.Mybooks_santi";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Mybooks Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null, null);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ha)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }


    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    /* private void scheduleJob() {
     *//*  // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);*//*
        // [END dispatch_job]
    }

    *//**
     * Handle time allotted to BroadcastReceivers.
     *//*
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }*/

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
   /* private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
*/
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle, String messageBody, Map<String, String> messageData) {

        //pren la posició del llibre

        String book_position = messageData.get("book_position");
        if (book_position == null) {
            Log.d(TAG, "Notification book not found");
            return;
        }
        Log.d(TAG, " Notification book: " + book_position);

        //les dues opcions:Eliminar de la llista local o veure detall
        Intent intentDel = new Intent(this, BookListActivity.class);
        Intent intentView = new Intent(this, BookListActivity.class);

        intentDel.setAction(ACTION_DELETE);
        intentDel.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentDel.putExtra(BOOK, book_position);
        PendingIntent delPendIntent = PendingIntent.getActivity(this, 0,
                intentDel, PendingIntent.FLAG_ONE_SHOT);

        intentView.setAction(ACTION_VIEW);
        intentView.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentView.putExtra(BOOK, book_position);
        PendingIntent viewPendIntent = PendingIntent.getActivity(this, 0,
                intentView, PendingIntent.FLAG_ONE_SHOT);

        //defineix el channel
        NOTIFICATION_CHANNEL_ID = "com.soc.uoc.pqtm.pecs.Mybooks_santi";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ha)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setLights(Color.BLUE, 100, 1900)
                        .setContentTitle(messageTitle)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .addAction(new NotificationCompat.Action(
                                R.drawable.ic_stat_ic_notification,
                                "book  delete", delPendIntent))
                        .addAction(new NotificationCompat.Action(
                                R.drawable.ic_stat_ic_notification,
                                "book View", viewPendIntent));
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Oreo version .
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        //show notification
        notificationManager.notify(0, notificationBuilder.build());
    }


}
