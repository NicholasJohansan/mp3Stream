package co.carrd.njportfolio.mp3stream;

import android.app.Application;

import com.google.android.exoplayer2.ExoPlayer;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;

public class MainApplication extends Application {
    private static MainApplication application;
    private ApiWrapper soundcloudApi;
    private ExoPlayer player;

    public static MainApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        soundcloudApi = new ApiWrapper();
        player = new ExoPlayer.Builder(this)
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        player.release();
        player = null;
    }

    public ApiWrapper getSoundcloudApi() {
        return soundcloudApi;
    }
    public ExoPlayer getPlayer() {
        return player;
    }
}
