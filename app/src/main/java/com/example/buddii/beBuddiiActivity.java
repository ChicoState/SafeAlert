package com.example.buddii;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class beBuddiiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_being_abuddy);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        VideoView mVideoView2 = (VideoView)findViewById(R.id.tutorialVideo);

        String uriPath2 = "android.resource://"+getPackageName()+"/"+R.raw.movie;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start(); //Plays the tutorial video

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView2);
        mVideoView2.setMediaController(controller);

        mVideoView2.start();
    }
}
