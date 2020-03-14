package com.example.mylist.Service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mylist.Activities.MainActivity;
import com.example.mylist.R;
import com.example.mylist.ui.login.LoginActivity;

import java.util.Calendar;

//import java.util.logging.Handler;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends IntentService {
    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "this is default channel!";
    private NotificationManager mManager;
    private String data="This is the data";
    public static final String EXTRA_MESSAGE = "message";
    public static  int NOTIFICATION_ID = 5453;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.mylist.action.FOO";
    private static final String ACTION_BAZ = "com.example.mylist.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.mylist.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.mylist.extra.PARAM2";
//    private Handler handler;

    public MyService() {
        super("MyService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String text = intent.getStringExtra(EXTRA_MESSAGE);

        createNotificationChannel();

//        synchronized (this){
//            try {
//
//                    wait(5000);
//                    System.out.println("延时了");
////                Toast.makeText(getApplicationContext(), "截止日期不能为空", Toast.LENGTH_SHORT).show();
//                showText(text);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }

        while(true) {
            boolean flag = false;
            try {
                synchronized (this) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             Calendar calendar = Calendar.getInstance();
//            Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item WHERE UserName = '" + intent.getStringExtra("userName") + "'");
//            showText(text);
            Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM Item WHERE UserName = '" + data + "'");


            while (cursor.moveToNext()) {

                String RemindTime = cursor.getString(4);
                String ItemName = cursor.getString(1);
                String Context = cursor.getString(2);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                String amPm;

                ++month;

                if(currentHour >=12){
                    amPm = "PM";
                }
                else {
                    amPm = "AM";
                }
                String hourtime = String.format("%02d:%02d", currentHour, currentMinute) + amPm;
                String daytime = "" + year + "-" + month + "-" + day;
                String finaltime = daytime +"   "+ hourtime;

                if(finaltime.equals(RemindTime)){
                    flag = true;
                    sendNotification(ItemName, Context);
                }


            }
            if(flag){
                try {
                    synchronized (this) {
                        flag = false;
                        wait(60000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
               data=intent.getStringExtra("username");
               return super.onStartCommand(intent, flags, startId);
           }
    private void showText(final String text){

        sendNotification("Mylist", "notification_test");
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//                    Toast.makeText(getApplicationContext(), "service测试", Toast.LENGTH_SHORT).show();
//
//            }
//        });




//        Intent intent23 = new Intent(this, LoginActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(intent23);
//        PendingIntent pendingIntent =
//                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getString(R.string.app_name))
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setContentIntent(pendingIntent)
//                .setContentText(text)
//                .build();
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(NOTIFICATION_ID,notification);





    }




    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }
//    @Override
//    public int onStartCommand(Intent intent,int flags,int startId){
//        handler = new Handler();
//        return super.onStartCommand(intent,flags,startId);
//
//    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        //是否绕过请勿打扰模式
        channel.canBypassDnd();
        //闪光灯
        channel.enableLights(true);
        //锁屏显示通知
        channel.setLockscreenVisibility(1);
        //闪关灯的灯光颜色
        channel.setLightColor(Color.RED);
        //桌面launcher的消息角标
        channel.canShowBadge();
        //是否允许震动
        channel.enableVibration(true);
        //获取系统通知响铃声音的配置
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        channel.getAudioAttributes();
        channel.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT);

        //获取通知取到组
        channel.getGroup();
        //设置可绕过  请勿打扰模式
        channel.setBypassDnd(true);
        //设置震动模式
        channel.setVibrationPattern(new long[]{100, 100, 200});
        //是否会有灯光
        channel.shouldShowLights();
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public void sendNotification(String title, String content) {
        NotificationCompat.Builder builder = getNotification(title, content);
        Intent perIntent = new Intent(this, MainActivity.class);
        perIntent.putExtra("userName",data);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, perIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        getManager().notify(NOTIFICATION_ID++, builder.build());
    }
    private NotificationCompat.Builder getNotification(String title, String content) {
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
//            builder.setPriority(PRIORITY_DEFAULT);
        }
        //标题
        builder.setContentTitle(title);
        //文本内容
        builder.setContentText(content);
        //小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置点击信息后自动清除通知
        builder.setAutoCancel(true);
        return builder;
    }
    public void sendNotification(int notifyId, String title, String content) {
        NotificationCompat.Builder builder = getNotification(title, content);
        getManager().notify(notifyId, builder.build());
    }
}
