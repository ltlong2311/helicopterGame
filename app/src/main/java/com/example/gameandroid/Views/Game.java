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

public class Game extends AppCompatActivity {
    Boolean music, sound;
    MediaPlayer gameMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("gameSettings", Context.MODE_PRIVATE);
        music = preferences.getBoolean("music", true);
        gameMusic = MediaPlayer.create(this, R.raw.far_away);
        if (!gameMusic.isPlaying() && music)
        {
            gameMusic.start();
            gameMusic.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        gameMusic.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        gameMusic.stop();
        super.onStop();
    }
}