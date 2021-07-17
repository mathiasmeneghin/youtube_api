package com.mathias.youtube.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mathias.youtube.R;
import com.mathias.youtube.helper.YoutubeConfig;

import java.util.List;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayerView ;
    private String idVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        YouTubePlayerView youTubePlayerView =
                new YouTubePlayerView(this);

        //Configura componentes
        youTubePlayerView.initialize(YoutubeConfig.CHAVE_YOUTUBE_API,this);
        youTubePlayerView.findViewById(R.id.playerVideo);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            idVideo = bundle.getString("idVideo");
            youTubePlayerView.initialize(YoutubeConfig.CHAVE_YOUTUBE_API,this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.loadVideo(idVideo);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
