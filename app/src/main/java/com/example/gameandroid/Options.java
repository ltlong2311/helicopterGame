package com.example.gameandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Options extends AppCompatActivity {
    int gameMode;
    Boolean music, sound;
    ImageView btnSave, btnMode, btnSound;
    CheckBox changeSound, changeMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);

        getViews();
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("gameSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        gameMode = preferences.getInt("mode", 1);
        music = preferences.getBoolean("music", true);
        sound = preferences.getBoolean("sound", true);


        setGameMode();
        setChangeSound();
        setChangeMusic();

        btnMode.setOnClickListener(v -> {
            if (gameMode < 3) {
                gameMode++;
            } else {
                gameMode = 1;
            }
            setGameMode();
        });

        changeSound.setOnClickListener(v -> {
            sound = !sound;
            System.out.println("music: " + music);
            setChangeSound();
        });

        changeMusic.setOnClickListener(v -> {
            music = !music;
            System.out.println("music: " + music);
            setChangeMusic();
        });

        btnSave.setOnClickListener(v -> {
            editor.putInt("mode", gameMode);
            editor.putBoolean("music", music);
            editor.putBoolean("sound", sound);
            editor.apply();
            back();
        });

    }

    private void back() {
        Intent myIntent = new Intent(Options.this, Home.class);
        this.startActivity(myIntent);
    }

    private void getViews() {
        btnSave = findViewById(R.id.btnSave);
        btnMode = findViewById(R.id.btnGameMode);
        changeMusic = findViewById(R.id.changeMusic);
        changeSound = findViewById(R.id.changeSound);
    }

    private void setGameMode() {
        switch (gameMode) {
            case 1:
                System.out.println("easy level");
                btnMode.setImageResource(R.drawable.mode_easy);
                break;
            case 2:
                System.out.println("normal level");
                btnMode.setImageResource(R.drawable.mode_normal);
                break;
            case 3:
                System.out.println("hard level");
                btnMode.setImageResource(R.drawable.mode_hard);
                break;
            default:
                btnMode.setImageResource(R.drawable.mode_easy);
                break;
        }
    }

    private void setChangeSound() {
        if (sound) {
            changeSound.setButtonDrawable(R.drawable.sound_on);
        } else {
            changeSound.setButtonDrawable(R.drawable.sound_off);
        }

    }

    private void setChangeMusic() {
        if (music) {
            changeMusic.setButtonDrawable(R.drawable.music_on);
        } else {
            changeMusic.setButtonDrawable(R.drawable.music_off);
        }
    }


}