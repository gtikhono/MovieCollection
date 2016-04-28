package gtikhono.msse.asu.edu.moviecollection;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

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
