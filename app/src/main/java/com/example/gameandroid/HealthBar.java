package com.example.gameandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HealthBar {
    private Player player;
    private int width, height, margin;
    private Paint borderPaint, healthPaint;

    public HealthBar(Player player, int width, int height, int margin) {
        this.player = player;
        this.width = width;
        this.height = height;
        this.margin = margin;
        this.borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#bdf0ff"));

        this.healthPaint = new Paint();
        healthPaint.setColor(Color.parseColor("#41c200"));
    }

    public void draw(Canvas canvas) {
        float x = (float) player.getX();
        float y = (float) player.getY();
        float distanceToPlayer = 30;
        float healthPointPercentage = (float) player.getHealthPoint()/player.MAX_HEALTH_POINTS;

        // border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x;
        borderRight = x + width;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);
        // health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, heathHeight;
        healthWidth = width - 2*margin;
        heathHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - heathHeight;
        canvas.drawRect(healthLeft, healthTop, healthRight, borderBottom, healthPaint);
    }
}
