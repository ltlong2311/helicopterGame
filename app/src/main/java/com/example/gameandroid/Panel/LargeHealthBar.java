package com.example.gameandroid.Panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;

import com.example.gameandroid.GameObject.Player;
import com.example.gameandroid.Graphics.HealthStatus;

import static android.graphics.PorterDuff.Mode.SRC_OUT;

public class LargeHealthBar {
    private HealthStatus healthBarFrame;
    private Player player;
    private int width, height, margin;
    private Paint healthPaint;

    public LargeHealthBar(Player p, HealthStatus bar, int m) {
        player = p;
        healthBarFrame = bar;
        width = 225;
        height = 21;
        margin = m;

        healthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        healthPaint.setColor(Color.parseColor("#c4747c"));
    }

    public void draw(Canvas canvas) {
        float x = (float) healthBarFrame.getX();
        float y =  (float) healthBarFrame.getY();
        float healthPointPercentage = (float) player.getHealthPoint()/player.MAX_HEALTH_POINTS;

        // health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, heathHeight;
        healthWidth = width;
        heathHeight = height;
        healthLeft = x + 45;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = y + 26;
        healthTop = healthBottom - heathHeight;
        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, healthPaint);

        Paint style = new Paint(Paint.ANTI_ALIAS_FLAG);
        style.setTextSize(10);
        style.setColor(Color.parseColor("#ebebeb"));
//        style.setTypeface(Typeface.createFromAsset(,"SourceSansPro-Bold.otf"));
        canvas.drawText(player.getHealthPoint()*100+ "/" + player.MAX_HEALTH_POINTS*100, x + 60,
                (healthBottom - healthTop)/2+ 18, style);
    }
}
