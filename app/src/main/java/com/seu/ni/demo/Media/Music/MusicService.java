package com.seu.ni.demo.Media.Music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    public static final String MSG = "myMessage";
    public static final int STATUS_NULL = 0x10;
    public static final int STATUS_PLAYING = 0x11;
    public static final int STATUS_PAUSED = 0x12;
    public static final int STATUS_STOPPED = 0x13;public static final int STATUS_ASKFORNEXT = 0x14;

    public static final int STATUS_ASKFORRANDOM = 0x15;
    public static final int STATUS_ASKFORSAME = 0x16;

    public static final int MODE_SINGLEREPET = 0x20;
    public static final int MODE_ALLREPET = 0x21;
    public static final int MODE_RANDOM = 0x22;
    static int currentStatus = STATUS_NULL;
    static int currentMode = MODE_ALLREPET;
    ControlReceiver controlReceiver;
    MediaPlayer mediaPlayer;
    Timer timer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //动态注册 Receiver， 接受 控制界面 传来的信息
        controlReceiver = new ControlReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicMain.ACION_CTRL);
        registerReceiver(controlReceiver, filter);


        //mediaPlayer 实例化
        mediaPlayer = new MediaPlayer();

        /**
         * 一首完全播放完，监听
         */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(MSG, "On Complete");
                if (timer != null) timer.cancel();
                Intent intent = new Intent(MusicMain.ACION_UPDATE);
                switch (currentMode) {
                    case MODE_ALLREPET:
                        intent.putExtra("update", MusicMain.STATUS_ASKFORNEXT);
                        break;
                    case MODE_SINGLEREPET:
                        intent.putExtra("update", MusicMain.STATUS_ASKFORSAME);
                        break;
                    case MODE_RANDOM:
                        intent.putExtra("update", MusicMain.STATUS_ASKFORRANDOM);
                        break;
                }
                sendBroadcast(intent);

            }
        });
//        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.i(MSG, "ERROR!");
//                Intent intent = new Intent(MusicMain.ACION_UPDATE);
//                intent.putExtra("update", MusicMain.STATUS_ASKFORNEXT);
//                sendBroadcast(intent);
//                return false;
//            }
//        });
        /**
         * 准备工作完成，开始播放，开启 TimerTask
         */
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(MSG, "prepared");
            }
        });
    }

    /**
     * 准备播放工作
     */
    private void prepareAndPlay(String musicPath) {
        Log.i(MSG, musicPath);

//        mediaPlayer = MediaPlayer.create(this, R.raw.reserved);
        try {
            if (timer != null) timer.cancel();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();

            mediaPlayer.start();
            //Timer 实例
            timer = new Timer();
            timer.schedule(new MusicTimerTask(), 500, 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Service Destroy
     */

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    /**
     * MusicTimerTask，给主界面发送 Message，通知更新 UI
     */
    public class MusicTimerTask extends TimerTask {

        @Override
        public void run() {
            Message message = new Message();
            //歌曲长度
            message.arg1 = mediaPlayer.getDuration();
            //播放进度
            message.arg2 = mediaPlayer.getCurrentPosition();
            //发送 Message 给 Main 更新界面
            MusicMain.handler.sendMessage(message);
        }
    }

    /**
     * 监听处理 主界面传来的广播
     */
    public class ControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Intent updateIntent = new Intent(MusicMain.ACION_UPDATE);
            String musicPath;
            Log.i(MSG, "previous status:" + currentStatus);

            switch (intent.getIntExtra("control", 0)) {


                // 无条件播放
                case MusicMain.CONTROL_PLAYNOW:
                    currentStatus = STATUS_PLAYING;
                    musicPath = intent.getStringExtra("musicPath");
                    prepareAndPlay(musicPath);
                    break;

                //播放 or 暂停按键
                case MusicMain.CONTROL_PLAY_PAUSE:

                    //当前在 停止 状态，初始化准备播放
                    if (currentStatus == STATUS_STOPPED || currentStatus == STATUS_NULL) {
                        musicPath = intent.getStringExtra("musicPath");
                        prepareAndPlay(musicPath);
                        currentStatus = STATUS_PLAYING;
                    }

                    // 当前在 播放 状态，暂停播放
                    else if (currentStatus == STATUS_PLAYING) {
                        mediaPlayer.pause();
                        currentStatus = STATUS_PAUSED;
                    }

                    // 当前在 暂停 状态，继续播放
                    else if (currentStatus == STATUS_PAUSED) {
                        Log.i(MSG, "s");
                        mediaPlayer.start();
                        currentStatus = STATUS_PLAYING;
                    }
                    break;
                //停止按键
                case MusicMain.CONTROL_STOP:
                    mediaPlayer.stop();
                    currentStatus = STATUS_STOPPED;
                    break;

                //进度条拖动
                case MusicMain.CONTROL_SEEK:

                    //如果尚未开始准备
                    if (currentStatus == STATUS_NULL) {
                        try {
                            musicPath = intent.getStringExtra("musicPath");
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(musicPath);
                            mediaPlayer.prepare();

                            timer = new Timer();
                            timer.schedule(new MusicTimerTask(), 500, 100);
                            mediaPlayer.seekTo(mediaPlayer.getDuration() * intent.getIntExtra("offset", 0) / 100);

                            currentStatus = STATUS_PAUSED;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mediaPlayer.seekTo(intent.getIntExtra("offset", 0));
                    }
                    break;

                case MusicMain.CONTROL_MODE:
                    currentMode = intent.getIntExtra("mode", 0);
                    break;
            }
            Log.i(MSG, "current status:" + currentStatus);
            //发送广播
            updateIntent.putExtra("update", currentStatus);
            sendBroadcast(updateIntent);
        }


    }
}
