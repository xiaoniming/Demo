package com.seu.ni.demo.Media.Music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.seu.ni.demo.R;

import java.io.File;

public class MusicMain extends AppCompatActivity implements View.OnClickListener {
    public static final String MSG = "myMessage";
    public static final String ACION_CTRL = "com.example.ni.app_music.ACION_CTRL";
    public static final String ACION_UPDATE = "com.example.ni.app_music.ACION_UPDATE";
    public static final int CONTROL_PLAY_PAUSE = 1;
    public static final int CONTROL_SEEK = 2;
    public static final int CONTROL_STOP = 3;
    public static final int CONTROL_PLAYNOW = 4;
    public static final int CONTROL_MODE = 5;

    public static final int MODE_SINGLEREPET = 0x20;
    public static final int MODE_ALLREPET = 0x21;
    public static final int MODE_RANDOM = 0x22;

    public static final int STATUS_NULL = 0x10;
    public static final int STATUS_PLAYING = 0x11;
    public static final int STATUS_PAUSED = 0x12;
    public static final int STATUS_STOPPED = 0x13;
    public static final int STATUS_ASKFORNEXT = 0x14;
    public static final int STATUS_ASKFORRANDOM = 0x15;
    public static final int STATUS_ASKFORSAME = 0x16;
    //    static File currentMusicFile = null;
//    static int currentMusicPosition = 0;
    public static Handler handler;
    static int currentMode = MODE_ALLREPET;
    File[] musicFiles;
    Boolean FLAG_MusicExits = false;
    Boolean FLAG_IsPlaying = false;
    UpdateReceiver updateReceiver;
    Button bt_playMusic, bt_stopMusic, bt_nextMusic, bt_preMusic, bt_mode;
    TextView tv_info, tv_duration, tv_secnow;
    ListView listview;
    MusicFileControl musicFileControl;
    ListControl listControl;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        //组件实例
        bt_playMusic = (Button) findViewById(R.id.bt_playpausemusic);
        bt_stopMusic = (Button) findViewById(R.id.bt_stopmusic);
        bt_nextMusic = (Button) findViewById(R.id.bt_nextmusic);
        bt_preMusic = (Button) findViewById(R.id.bt_previousmusic);
        bt_mode = (Button) findViewById(R.id.bt_mode);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_secnow = (TextView) findViewById(R.id.tv_secnow);
        listview = (ListView) findViewById(R.id.lv_play);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        bt_playMusic.setOnClickListener(this);
        bt_stopMusic.setOnClickListener(this);
        bt_nextMusic.setOnClickListener(this);
        bt_preMusic.setOnClickListener(this);
        bt_mode.setOnClickListener(this);

        //动态注册 Receiver， 接受 service 传回的信息
        updateReceiver = new UpdateReceiver();
        IntentFilter updateIntentFilter = new IntentFilter(ACION_UPDATE);
        registerReceiver(updateReceiver, updateIntentFilter);


        //获取音乐文件， listView 初始化
        musicFileControl = new MusicFileControl();
        musicFiles = musicFileControl.getMusicFiles();

        if (musicFiles != null) {
            FLAG_MusicExits = true;
            listControl = new ListControl(listview, musicFiles);
            listControl.initListView(this);
        }

        //开启 Music 服务
        Intent music_intent = new Intent(this, MusicService.class);
        startService(music_intent);

        //listview 模式设置，监听开启
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                musicFileControl.setCurrentPosition(position);
                playNow(musicFileControl.getCurrentFile(), CONTROL_PLAYNOW);
            }
        });

        //handler，接收数据，更新 SeekBar
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                seekBar.setMax(msg.arg1);
                seekBar.setProgress(msg.arg2);
                tv_duration.setText(transTime(msg.arg1));
                tv_secnow.setText(transTime(msg.arg2));
                super.handleMessage(msg);
            }
        };
        //SeekBar 监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent controlIntent = new Intent(ACION_CTRL);
                controlIntent.putExtra("control", CONTROL_SEEK);
                controlIntent.putExtra("musicPath", musicFileControl.getCurrentFile().getAbsolutePath());
                controlIntent.putExtra("offset", seekBar.getProgress());
                sendBroadcast(controlIntent);
            }
        });


    }


    /**
     * 绑定 Button 监听事件
     */
    @Override
    public void onClick(View v) {
        if (!FLAG_MusicExits) return;
        switch (v.getId()) {
            case R.id.bt_playpausemusic:
                playNow(musicFileControl.getCurrentFile(), CONTROL_PLAY_PAUSE);
                break;
            case R.id.bt_stopmusic:
                playNow(musicFileControl.getCurrentFile(), CONTROL_STOP);
                break;
            case R.id.bt_nextmusic:
                if (currentMode == MODE_RANDOM) {
                    playNow(musicFileControl.getRandomFile(), CONTROL_PLAYNOW);
                    break;
                } else
                    playNow(musicFileControl.getNextFile(), CONTROL_PLAYNOW);
                break;
            case R.id.bt_previousmusic:
                //TODO Random mode
                playNow(musicFileControl.getPreviousFile(), CONTROL_PLAYNOW);
                break;
            case R.id.bt_mode:
                Log.i(MSG, "click,cmode:" + currentMode);
                if (currentMode == MODE_ALLREPET) {
                    currentMode = MODE_SINGLEREPET;
                    bt_mode.setBackground(getResources().getDrawable(R.drawable.music_one));
                } else if (currentMode == MODE_SINGLEREPET) {
                    currentMode = MODE_RANDOM;
                    Log.i(MSG, "2cmode:" + currentMode);
                    bt_mode.setBackground(getResources().getDrawable(R.drawable.music_radom));
                } else if (currentMode == MODE_RANDOM) {
                    currentMode = MODE_ALLREPET;
                    Log.i(MSG, "3cmode:" + currentMode);
                    bt_mode.setBackground(getResources().getDrawable(R.drawable.music_all));
                }
                Intent controlIntent = new Intent(ACION_CTRL);
                controlIntent.putExtra("control", CONTROL_MODE);
                controlIntent.putExtra("mode", currentMode);
                sendBroadcast(controlIntent);
                break;
        }
    }

    /**
     * 播放控制
     */
    public void playNow(File file, int flag) {

        listControl.setSelectItem(musicFileControl.getCurrentPosition());
        Intent controlIntent = new Intent(ACION_CTRL);
        controlIntent.putExtra("control", flag);
        controlIntent.putExtra("musicPath", file.getAbsolutePath());
        sendBroadcast(controlIntent);
    }

    /**
     * 时间格式转换
     *
     * @param millis milliseconds
     * @return time we usually use, eg. 04 : 50
     */
    public String transTime(int millis) {
        int min = millis / 60000;
        int sec = (millis % 60000) / 1000;
        return ((min < 10 ? "0" + min : min) + " : " + (sec < 10 ? "0" + sec : sec));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * BroadcastReceiver 处理 Service 传回的消息，改变界面状态
     */
    public class UpdateReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //获取歌曲名字
            String songname = musicFileControl.getSongName();

            switch (intent.getIntExtra("update", 0)) {
                case STATUS_PLAYING:
                    tv_info.setText("正在播放：" + songname);

                    bt_playMusic.setBackground(getResources().getDrawable(R.drawable.music_pause));

                    break;
                case STATUS_PAUSED:

                    tv_info.setText("暂停播放：" + songname);
                    bt_playMusic.setBackground(getResources().getDrawable(R.drawable.music_play));
                    break;
                case STATUS_STOPPED:
                    tv_info.setText("停止播放");

                    bt_playMusic.setBackground(getResources().getDrawable(R.drawable.music_play));
                    break;
                case STATUS_ASKFORNEXT:
                    Log.i(MSG, "STATUS_ASK FOR NEXT");
                    playNow(musicFileControl.getNextFile(), CONTROL_PLAYNOW);
                    break;
                case STATUS_ASKFORRANDOM:
                    Log.i(MSG, "STATUS_ASK FOR NEXT");
                    playNow(musicFileControl.getRandomFile(), CONTROL_PLAYNOW);
                    break;
                case STATUS_ASKFORSAME:
                    Log.i(MSG, "STATUS_ASK FOR NEXT");
                    playNow(musicFileControl.getCurrentFile(), CONTROL_PLAYNOW);
                    break;

            }
        }
    }
}
