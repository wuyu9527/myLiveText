package com.example.rtmp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dou361.ijkplayer.widget.AndroidMediaController;
import com.dou361.ijkplayer.widget.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

    IjkVideoView ijkVideoView;
    EditText etPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPath = findViewById(R.id.etPath);
        ijkVideoView = findViewById(R.id.ijkVideoView);


        //初始化播放库
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();


        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        // 黑屏
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec_mpeg4", 1);
        // 降低 播放 rtmp 播放的延迟
        ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);
        ijkMediaPlayer.setOption(1, "probesize", 10240L);
        ijkMediaPlayer.setOption(1, "flush_packets", 1L);
        ijkMediaPlayer.setOption(4, "packet-buffering", 0L);
        //丢帧
        ijkMediaPlayer.setOption(4, "framedrop", 1L);


        //硬解码造成黑屏无声
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);


        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1);

        // 清空DNS，因为DNS的问题报10000
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);

        //使用AndroidMediaController类控制播放相关操作
        AndroidMediaController mMediaController = new AndroidMediaController(MainActivity.this, false);
        //mMediaController.setSupportActionBar(actionBar);
//        ijkVideoView = findViewById(R.id.ijkVideoView);
        ijkVideoView.setMediaController(mMediaController);


        // 测试可用地址
        // 香港财经  rtmp://202.69.69.180:443/webcast/bshdlive-pc
        // 湖南卫视   rtmp://58.200.131.2:1935/livetv/hunantv
        // 美国2, rtmp://media3.scctv.net/live/scctv_800
        //      rtmp://zv.3gv.ifeng.com/live/zhongwen800k.m3u8
        oldPath = "rtmp://media3.scctv.net/live/scctv_800";
        etPath.setText(oldPath);
        //设置要播放的直播或者视频的地址：
        ijkVideoView.setVideoPath(etPath.getText().toString());
        //开始播放
        ijkVideoView.start();
    }

    String oldPath = "";

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                //设置要播放的直播或者视频的地址：
                if (!TextUtils.equals(oldPath, etPath.getText().toString())) {
                    oldPath = etPath.getText().toString();
//                    ijkVideoView.pause();
                    if (ijkVideoView.isPlaying()) {
                        ijkVideoView.stopPlayback();
                        ijkVideoView.release(true);
                    }
                    ijkVideoView.setVideoPath(oldPath);
//                    ijkVideoView.release(false);
                }
                //开始播放
                ijkVideoView.start();
                break;
            case R.id.stop:
                //停止播放
                ijkVideoView.pause();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkVideoView.isPlaying()) {
            ijkVideoView.stopPlayback();
            ijkVideoView.release(true);
        }
        IjkMediaPlayer.native_profileEnd();
    }
}