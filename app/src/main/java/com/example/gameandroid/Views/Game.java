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
import com.example.gameandroid.Sound.MusicPlayer;
import com.example.gameandroid.Sound.SoundPlayer;

public class Game extends AppCompatActivity {
    MusicPlayer musicPlayer;
    SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));
        soundPlayer = new SoundPlayer(this);
        musicPlayer = new MusicPlayer(this);
    }

    @Override
    protected void onPause() {
        soundPlayer.setSoundPause();
        musicPlayer.pauseInGameMusic();
        musicPlayer.pauseGameMusic();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        soundPlayer.setSoundStop();
        musicPlayer.stopInGameMusic();
        musicPlayer.stopGameMusic();
        super.onStop();
    }
}