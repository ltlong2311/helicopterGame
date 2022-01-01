package com.example.gameandroid.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.gameandroid.Panel.GamePanel;
import com.example.gameandroid.R;
import com.example.gameandroid.Sound.SoundPlayer;

public class Game extends AppCompatActivity {
    Boolean music;
    MediaPlayer gameMusic;
    SoundPlayer soundPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));
        soundPlayer = new SoundPlayer(this);
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("gameSettings", Context.MODE_PRIVATE);
        music = preferences.getBoolean("music", true);
        gameMusic = MediaPlayer.create(this, R.raw.main_war);
        if (!gameMusic.isPlaying() && music)
        {
            gameMusic.start();
            gameMusic.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        gameMusic.pause();
        soundPlayer.setSoundPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        gameMusic.stop();
        soundPlayer.setSoundStop();
        super.onStop();
    }
}