package co.carrd.njportfolio.mp3stream;

import android.app.Application;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;

public class MainApplication extends Application {
    private static MainApplication application;
    private ApiWrapper soundcloudApi;

    public static MainApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        soundcloudApi = new ApiWrapper();
    }

    public ApiWrapper getSoundcloudApi() {
        return soundcloudApi;
    }
}
