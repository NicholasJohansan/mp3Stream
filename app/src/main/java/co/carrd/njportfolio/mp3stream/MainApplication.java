package co.carrd.njportfolio.mp3stream;

import android.app.Application;
import android.media.audiofx.Equalizer;
import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.ResolvingDataSource;
import com.orhanobut.hawk.Hawk;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;

public class MainApplication extends Application {
    private static MainApplication application;
    private ApiWrapper soundcloudApi;
    private ExoPlayer player;
    private Equalizer equalizer;

    public static MainApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        soundcloudApi = new ApiWrapper();

        // Configure data source to use just-in-time stream url resolution
        DataSource.Factory dataSourceFactory = new ResolvingDataSource.Factory(
                new DefaultDataSource.Factory(getApplicationContext()),
                dataSpec -> dataSpec.withUri(Uri.parse(soundcloudApi.getSongStreamUrl(dataSpec.uri.toString())))
        );

        // Build player with above data source factory and with hls media source
        player = new ExoPlayer.Builder(getApplicationContext())
                .setMediaSourceFactory(new HlsMediaSource.Factory(dataSourceFactory))
                .build();

        equalizer = new Equalizer(0, MainApplication.getInstance().getPlayer().getAudioSessionId());

        // Initialise Hawk Data Storage
        Hawk.init(getApplicationContext()).build();
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
    public Equalizer getEqualizer() {
        return equalizer;
    }
}
