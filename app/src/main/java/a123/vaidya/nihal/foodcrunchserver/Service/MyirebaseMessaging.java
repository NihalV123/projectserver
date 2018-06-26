package a123.vaidya.nihal.foodcrunchserver.Service;

<<<<<<< HEAD
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
=======
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
>>>>>>> old1/master
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
<<<<<<< HEAD
import android.os.IBinder;
=======
import android.os.Build;
>>>>>>> old1/master
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

<<<<<<< HEAD
import a123.vaidya.nihal.foodcrunchserver.MainActivity;
=======
import java.util.Random;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Helper.NotificationHelper;
import a123.vaidya.nihal.foodcrunchserver.MainActivity;
import a123.vaidya.nihal.foodcrunchserver.OrderStatus;
>>>>>>> old1/master
import a123.vaidya.nihal.foodcrunchserver.R;

public class MyirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
<<<<<<< HEAD
        sendNotification(remoteMessage);
    }

=======
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            sendNotificationAPIO(remoteMessage);
        else
        sendNotification(remoteMessage);
    }

    private void sendNotificationAPIO(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = notification.getTitle();
        String content = notification.getBody();
        Intent intent = new Intent(this, OrderStatus.class);//go to order status on clicking notification
        intent.putExtra(Common.PHONE_TEXT, Common.currentUser.getPhone());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder = helper.foodcrunchChannelNotification(title,content,pendingIntent,defaultUri);
        //random id for all notificatrions
        helper.getManager().notify(new Random().nextInt(),builder.build());
    }

>>>>>>> old1/master
    private void sendNotification(RemoteMessage remoteMessage) {
    RemoteMessage.Notification notification = remoteMessage.getNotification();
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(notification.getTitle())
        .setContentText(notification.getBody())
        .setAutoCancel(true)
        .setSound(defaultUri)
        .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());

    }

}
