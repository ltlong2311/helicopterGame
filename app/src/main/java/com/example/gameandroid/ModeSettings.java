package com.example.gameandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ModeSettings extends AppCompatActivity {

    TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mode_settings);
        getViews();

        btnBack.setOnClickListener(v -> {
            Intent myIntent = new Intent(this, Home.class);
            this.startActivity(myIntent);
//            onBackPressed();
        });


    }

    private void getViews() {
        btnBack = findViewById(R.id.btnBack);
    }
}