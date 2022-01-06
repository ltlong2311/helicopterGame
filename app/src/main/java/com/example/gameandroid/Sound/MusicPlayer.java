package com.example.gameandroid.Sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.example.gameandroid.R;

public class MusicPlayer {
    Boolean music;
    MediaPlayer introMusic, optionsBgMusic, gameMusic, inGameMusic;
    SharedPreferences preferences;
    public MusicPlayer(Context context) {
        preferences = context.getSharedPreferences("gameSettings", Context.MODE_PRIVATE);
        music = preferences.getBoolean("music", true);
        System.out.println("music: "+ music);
        introMusic = MediaPlayer.create(context, R.raw.intro);
        optionsBgMusic = MediaPlayer.create(context, R.raw.intro2);
        gameMusic = MediaPlayer.create(context, R.raw.main_war);
        inGameMusic = MediaPlayer.create(context, R.raw.helicopter_sound_effect_flying);
    }

    public void playIntroMusic() {
        if (music) {
            introMusic.start();
            introMusic.setLooping(true);
        }
    }

    public void playOptionMusic() {
        if (music) {
            optionsBgMusic.start();
            optionsBgMusic.setLooping(true);
        }
    }

    public void playGameMusic() {
        if (music) {
            gameMusic.start();
            gameMusic.setLooping(true);
        }
    }

    public void playInGameMusic() {
        if (music) {
            inGameMusic.start();
            inGameMusic.setLooping(true);
        }
    }

    public void pauseIntroMusic() {
        if (introMusic.isPlaying()) {
            introMusic.pause();
        }
    }

    public void pauseOptionMusic() {
        if (optionsBgMusic.isPlaying()) {
            optionsBgMusic.pause();
        }
    }

    public void pauseGameMusic() {
        if (gameMusic.isPlaying()) {
            gameMusic.pause();
        }
    }

    public void pauseInGameMusic() {
        if (inGameMusic.isPlaying()) {
            inGameMusic.pause();
        }
    }

    public void stopIntroMusic() {
        if(introMusic.isPlaying()) {
            introMusic.stop();
            introMusic.release();
        }
    }

    public void stopOptionMusic() {
        if (optionsBgMusic.isPlaying()) {
            optionsBgMusic.stop();
            optionsBgMusic.release();
        }
    }

    public void stopGameMusic() {
        if (gameMusic.isPlaying()) {
            gameMusic.stop();
            gameMusic.release();
        }
    }

    public void stopInGameMusic() {
        if (inGameMusic.isPlaying()) {
            inGameMusic.stop();
            inGameMusic.release();
        }
    }
}
