package com.example.administrator.videodemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private VideoView video;
    private String videoUrl1;
    private String videoUrl2;
    int count = 0;
    int Count=0;
    int playcount = 0;
    private String uri;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        video = (VideoView) findViewById(R.id.video);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Button down = (Button) findViewById(R.id.down);
        Button up = (Button) findViewById(R.id.up);
        Button pause = (Button) findViewById(R.id.pause);
        Button resume = (Button) findViewById(R.id.resume);
        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);
//        String str="今天<font color='#FF0000'><big>天气不错</big></font>";
//        CharSequence charSequence;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            charSequence = Html.fromHtml( "今天<br/>天气不错",Html.FROM_HTML_MODE_COMPACT);
//        } else {
//            charSequence = Html.fromHtml( "今天<br/>天气不错");
//        }
//        down.setText(charSequence);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,5,0);
        MediaController controller=new MediaController(this);
        video.setMediaController(controller);
        //第一个视频的播放路径
        videoUrl1 = Environment.getExternalStorageDirectory().getPath() + "/test15.mp4";
        //第二个视频的播放路径
        videoUrl2 = Environment.getExternalStorageDirectory().getPath() + "/b.mp4";
        uri = "android.resource://" + getPackageName() + "/" + R.raw.pacify;
//        video.setVideoPath(videoUrl1);
        video.setVideoURI(Uri.parse(uri));
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                video.start();
            }
        });

        //循环播放
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.suspend();
                video.setVideoURI(Uri.parse(uri));
//                mp.setVolume(0,0);
                count++;
            }
        });
        //声音调节
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER,  0);
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE,  0);
            }
        });
        //暂停
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.pause();
            }
        });
        //继续播放视频
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.resume();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video!=null){
            video.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (video!=null){
            video.resume();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Config","Config");
    }

    public static Boolean writeStrToFile(String str, String name, String path, Boolean supplements) {
        String old_str;
        File file_path = new File(path);
        if (!file_path.exists()) {
            file_path.mkdirs();
        }
        File file = new File(path+name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            FileWriter fileWritter = new FileWriter(file);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            if(supplements){
                String old_data;
                try {
                    old_data = getStrFromFile(name,path);
                    Log.i("str",old_data);
                    if(!TextUtils.isEmpty(old_data)){
                        old_str  = old_data;
                        Log.i("str",old_str);
                    }else{
                        old_str = "";
                        Log.i("str",old_str);
                    }
                    str = old_str + str;

                } catch (JSONException e) {
                    bufferWritter.close();
                    return false;
                }
            }
            bufferWritter.write(str);
            bufferWritter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static String getStrFromFile(String name, String path) throws JSONException{
        String result = "";
        File file = new File(path+name);
        if (!file.exists()) {
            result = "0";
        }else{
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String str = "";
                String mimeTypeLine = null ;
                while ((mimeTypeLine = br.readLine()) != null) {
                    str = str+mimeTypeLine;
                }
                br.close();
                result = str;
            } catch (Exception e) {
                result = "0";
            }
        }
        return result;
    }
}
