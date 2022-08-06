package co.carrd.njportfolio.mp3stream;

import android.app.Application;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.ResolvingDataSource;

import java.io.IOException;
import java.util.Map;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;

//https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/

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
