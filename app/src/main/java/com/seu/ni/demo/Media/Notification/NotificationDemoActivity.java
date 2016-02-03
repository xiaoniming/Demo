package com.seu.ni.demo.Media.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.seu.ni.demo.R;

public class NotificationDemoActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = "---Notification--->";
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    Button mSimpleNotiButton;
    Button mBiggerNotiButton;
    Button mProgressNotiButton;
    Button mCustomNotiButton;
    Button mCancelNotiButton;


    private <T> void L(T para) {
        Log.i(TAG, "" + para);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initView();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_simple_notification:
                buildBasicNoti();
                mNotificationManager.notify(1, mBuilder.build());
                break;

            case R.id.bt_bigger_notification:
                buildBiggerNoti();
                mNotificationManager.notify(2, mBuilder.build());
                break;

            case R.id.bt_notification_progress:
                buildProgressNoti();
                break;

            case R.id.bt_notification_custom:
                buildCustomNoti();
                mNotificationManager.notify(5, mBuilder.build());
                break;

            case R.id.bt_notification_cancel:
                mNotificationManager.cancelAll();
                break;

            default:
                return;
        }

    }


    /**
     * Normal view layouts are limited to 64 dp
     */
    private void buildBasicNoti() {
        mBuilder = null;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_cute)
                .setContentText(getResources().getString(R.string.string_notification_text))
                .setContentTitle(getResources().getString(R.string.string_notification_title));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        // How to inform our users, here we need to claim uses-permission in Mainifests
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setContentIntent(getPengdingIntent());
    }

    /**
     * 大图标通知
     * expanded view layouts are limited to 256 dp.
     */
    private void buildBiggerNoti() {

        buildBasicNoti();
        //大图标设置
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_important));

        //1、InboxStyle
        mBuilder.setStyle(new NotificationCompat.InboxStyle().addLine("newLineMessage").addLine
                ("anotherNewLineMessage").addLine("newMessageToo").setBigContentTitle("big title").setSummaryText
                ("Summary Text"));

//        //2、BigTextStyle
//        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
//                .bigText("1、起初,神创造天地。\n" +
//                        "2  地是空虚混沌。渊面黑暗。神的灵运行在水面上。\n" +
//                        "3  神说，要有光，就有了光。\n" +
//                        "4  神看光是好的，就把光暗分开了。\n" +
//                        "5  神称光为昼，称暗为夜。有晚上，有早晨，这是头一日。\n" +
//                        "6  神说，诸水之间要有空气，将水分为上下。\n" +
//                        "7  神就造出空气，将空气以下的水，空气以上的水分开了。事就这样成了。"));
//        // 3、BigPictureStyle
//        mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
//                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.pic_bible)));

    }

    /**
     * 进度条通知
     */
    private void buildProgressNoti() {

        mBuilder = null;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_cute)
                .setContentText(getResources().getString(R.string.string_down_text))
                .setContentTitle(getResources().getString(R.string.string_down_title));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(getPengdingIntent());

        // 1、推进型进度条
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i += 5) {
                    //Set Progress
                    mBuilder.setProgress(100, i, false);
                    mNotificationManager.notify(4, mBuilder.build());
                    SystemClock.sleep(300);
                }
                mBuilder.setProgress(0, 0, false);
                mBuilder.setContentText(getResources().getString(R.string.string_down_succ));
                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                mNotificationManager.notify(4, mBuilder.build());
            }
        }).start();

//        // 2、滚动型进度条
//        mBuilder.setProgress(0, 0, true);
//        mNotificationManager.notify(4, mBuilder.build());
//        SystemClock.sleep(4000);
//        mBuilder.setProgress(100, 100, false);
//        mBuilder.setContentText(getResources().getString(R.string.string_down_succ));
//        mBuilder.setDefaults(Notification.DEFAULT_ALL);
//        mNotificationManager.notify(4, mBuilder.build());

    }

    /**
     * The height available for a custom notification layout depends on the notification view.
     * Type normal is 64 dp,  Type expanded is 256 dp
     * instead of calling methods such as setContentTitle(), call setContent().
     */
    private void buildCustomNoti() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tv_noti_title, "TO BE CONTINUED");
        remoteViews.setTextColor(R.id.tv_noti_title, Color.BLUE);
        remoteViews.setImageViewResource(R.id.img_noti, R.drawable.pic_qiaoba);

        mBuilder = null;
        mBuilder = new NotificationCompat.Builder(this)
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.icon_cute);
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setContentIntent(getPengdingIntent());


    }

    /**
     * PendingIntent 在需要的时候执行跳转的 Intent
     *
     * @return PendingIntent
     */
    private PendingIntent getPengdingIntent() {

        // 创建PendingIntent 方法一：
//        Intent resultIntent[] = new Intent[1];
//        resultIntent[0] = new Intent(this, NotificationResultActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, resultIntent, PendingIntent
//                .FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingIntent);

        // 创建PendingIntent 方法二：
        Intent resultIntent = new Intent(this, NotificationResultActivity.class);
        //创建一个 TaskStackBuilder 对象
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // 添加后台堆栈 mainifest 文件里要加上父activity，如果这两步不做，直接退回原始屏幕。
        stackBuilder.addParentStack(NotificationResultActivity.class);
        // 添加Intent到栈顶
        stackBuilder.addNextIntent(resultIntent);
        // 获得一个PendingIntent包含整个后台堆栈
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        return resultPendingIntent;

    }

    void initView() {
        mSimpleNotiButton = (Button) findViewById(R.id.bt_simple_notification);
        mBiggerNotiButton = (Button) findViewById(R.id.bt_bigger_notification);
        mProgressNotiButton = (Button) findViewById(R.id.bt_notification_progress);
        mCustomNotiButton = (Button) findViewById(R.id.bt_notification_custom);
        mCancelNotiButton = (Button) findViewById(R.id.bt_notification_cancel);
        mBiggerNotiButton.setOnClickListener(this);
        mSimpleNotiButton.setOnClickListener(this);
        mProgressNotiButton.setOnClickListener(this);
        mCustomNotiButton.setOnClickListener(this);
        mCancelNotiButton.setOnClickListener(this);

    }

}
