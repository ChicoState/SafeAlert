package com.example.buddii;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class beingABuddyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_being_abuddy);

        //Button playTutorialVideo = (Button)findViewById(R.id.playButton);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        VideoView mVideoView2 = (VideoView)findViewById(R.id.tutorialVideo);

        String uriPath2 = "android.resource://"+getPackageName()+"/"+R.raw.movie;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();

        /*
        playTutorialVideo.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                VideoView mVideoView2 = (VideoView) findViewById(R.id.tutorialVideo);
                String uriPath = "android.resources://"+getPackageName()+"/" + R.raw.movie;
                Uri uri2 = Uri.parse(uriPath);
                mVideoView2.setVideoURI(uri2);
                mVideoView2.requestFocus();
                mVideoView2.start();
            }
        });

        */
    }
}
