package com.example.gameandroid.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.gameandroid.R;
import com.example.gameandroid.Sound.MusicPlayer;
import com.example.gameandroid.Sound.SoundPlayer;

public class Home extends AppCompatActivity {

    ImageView btnStart, btnMode, btnExit;
    MusicPlayer musicPlayer;
    SoundPlayer soundPlayer;
    private boolean flag = true;
    public static final String GAME_SETTINGS = "gameSettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);
        getViews();
        System.out.println("flag: "+ flag);
        if (flag) {
            musicPlayer = new MusicPlayer(this);
            musicPlayer.playIntroMusic();
            flag = false;
        }
        soundPlayer = new SoundPlayer(this);

        btnStart.setOnClickListener(v -> {
            Intent myIntent = new Intent(Home.this, Game.class);
            this.startActivity(myIntent);
        });
        btnMode.setOnClickListener(v -> {
            Intent myIntent = new Intent(Home.this, Options.class);
            this.startActivity(myIntent);
        });
        btnExit.setOnClickListener(v -> {
            System.exit(0);
            Process.killProcess(Process.myPid());
            finish();
        });
    }

    private void getViews() {
        btnStart= findViewById(R.id.btnStart);
        btnMode= findViewById(R.id.btnOptions);
        btnExit= findViewById(R.id.btnExit);
    }

    @Override
    protected void onPause() {
        flag = false;
        musicPlayer.pauseIntroMusic();
        super.onPause();
    }

    @Override
    protected void onResume() {
        flag = false;
        if(musicPlayer == null) {
            musicPlayer = new MusicPlayer(this);
        }
        musicPlayer.playIntroMusic();
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (!flag){
            musicPlayer.stopIntroMusic();
            flag = true;
        }
        super.onStop();

    }

    private static void SetWindowFlag(Home home, final int Bits, Boolean on) {
        Window win =  home.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags  |=Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);
    }
}