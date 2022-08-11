# mp3Stream
![Banner](https://github.com/NicholasJohansan/mp3Stream/blob/413b9e19f05e13225c8768e7a99fbc17b22ababc/assets/banner.png?raw=true)

## Features
**mp3Stream** is an android music streaming app which allows you to:
- Search Music across SoundCloud
- Create your own Music Library
- Use and customise an equalizer
- Stream Music from SoundCloud

As of now, the app is incomplete.  
However, I may continue working on it in the future.

## Background
This app was started as an assignment for a polytechnic diploma subject called Computational Thinking (COMT). Its deliverable was a Music-related app that has to be built in Android Studio with Java. This is my first time creating an Android App with Android Studio and Java. The code that was submitted for my assignment is in the [7/8/22-COMT-Submission branch](https://github.com/NicholasJohansan/mp3Stream/tree/7/8/22-COMT-Submission).

For the SoundCloud API, since it has been private for many years, I had to analyse the API requests of the [SoundCloud Website](https://soundcloud.com) using Browser DevTools, and look at how the [API Endpoint](https://api-v2.soundcloud.com) is being used. From there, I had to make my own API wrapper for the SoundCloud API in order to use SoundCloud's API for mp3Stream.

Font used is [Interstate](https://fonts.adobe.com/fonts/interstate)  
Icons used are all from [Material Icons](https://fonts.google.com/icons?selected=Material+Icons)  
App Logo was made myself using Inkscape

## Dependencies
- All the standard android libraries
- [`Glide`](https://github.com/bumptech/glide) - Image Loading
- [`OkHttp3`](https://square.github.io/okhttp/) - Http Requests
- [`ExoPlayer`](https://exoplayer.dev/) - Player Library
- [`SwipeTouchListener`](https://github.com/colorgreen/swipe-touch-listener) - Swipeable Mini Player
- [`Material UI`](https://material.io/develop/android) - UI Components
- [`Hawk`](https://github.com/orhanobut/hawk) - Data Storage
- [`Android Fragment`](https://developer.android.com/guide/fragments) - Latest Fragment Implementation
- [`VerticalSeekBar`](https://github.com/h6ah4i/android-verticalseekbar) - Equalizer Band
- [`SwipeRefreshLayout`](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) - Circular Loading Drawable

## License [![Unlicense](https://img.shields.io/github/license/NicholasJohansan/EP5)](LICENSE)
mp3Stream is [Unlicense](https://unlicense.org/) Licensed (file [license](license)).
