package com.example.buddii;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;


//class runs the beingABuddyActivity activity which shows the tutorial video
public class beingABuddyActivity extends AppCompatActivity {

    @Override
    //creates the layout of this activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_being_abuddy);


        getWindow().setFormat(PixelFormat.UNKNOWN);

        VideoView mVideoView2 = (VideoView)findViewById(R.id.tutorialVideo);

        //this gets the video from the raw file and starts the video when user
        //has begun this activity
        String uriPath2 = "android.resource://"+getPackageName()+"/"+R.raw.movie;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();

        //this makes a pause and play button as well as video scroll
        //jjst in case user needs to pause the video or go forwards
        //or backwards through the video
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView2);
        mVideoView2.setMediaController(controller);

        mVideoView2.start();

    }
}
