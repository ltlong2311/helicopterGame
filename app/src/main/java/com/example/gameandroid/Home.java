package com.example.gameandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    ImageView btnStart, btnMode, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getViews();

        btnStart.setOnClickListener(v -> {
            Intent myIntent = new Intent(Home.this, Game.class);
            myIntent.putExtra("key", "url");
            this.startActivity(myIntent);
        });
        btnMode.setOnClickListener(v -> {
            Intent myIntent = new Intent(Home.this, Options.class);
            this.startActivity(myIntent);
        });
        btnExit.setOnClickListener(v -> {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        });
    }

    private void getViews() {
        btnStart= findViewById(R.id.btnStart);
        btnMode= findViewById(R.id.btnOptions);
        btnExit= findViewById(R.id.btnExit);
    }


}