package gtikhono.msse.asu.edu.moviecollection;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/*
* Copyright © 2016 Gabriela Tikhonova
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Purpose: Display Video from the Server
*
* Orginal Code made by Copyright ©  2016 Tim Lindquist, and modified by
* Gabriela Tikhonova to use on Assignemnt 9
*
* Ser423 Mobile Applications
* @author   Gabriela Tikhonova  mailto:gtikhono@asu.edu.
* @version April 28, 2016
*/


public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private VideoView mVideoView;
    private MediaController mController;
    private MediaMetadataRetriever mMetadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle fileBundle = getIntent().getExtras();

        try {
            String file = fileBundle.getString("file");
            System.out.println("File Name of Video "+ file);

            mVideoView = (VideoView) findViewById(R.id.avideoview);
            mVideoView.setVideoPath(getString(R.string.videourl) + file);
            //mVideoView.setVideoPath(getString(R.string.urlprefix) + "Crazy.mp3");
            MediaController mediaController = new MediaController((Context)this);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            mVideoView.setOnPreparedListener(this);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception processing newTitle: " +
                    ex.getMessage());
        }

//        mVideoView = (VideoView) findViewById(R.id.avideoview);
//        mVideoView.setVideoPath(getString(R.string.defaulturl) + "MinionsBananaSong.mp4");
//        //mVideoView.setVideoPath(getString(R.string.urlprefix) + "Crazy.mp3");
//        MediaController mediaController = new MediaController((Context)this);
//        mediaController.setAnchorView(mVideoView);
//        mVideoView.setMediaController(mediaController);
//        mVideoView.setOnPreparedListener(this);
    }

    public void onPrepared(MediaPlayer mp){
        android.util.Log.d(this.getClass().getSimpleName(), "onPrepared called. Video Duration: "
                + mVideoView.getDuration());
        mVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        android.util.Log.d(this.getClass().getSimpleName(), "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
