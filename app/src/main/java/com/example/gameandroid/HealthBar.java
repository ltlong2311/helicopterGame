package com.example.gameandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HealthBar {
    private GameObject obj;
    private int width, height, margin, maxHealthPoints;
    private Paint borderPaint, healthPaint;


    public HealthBar(GameObject p, int w, int h, int m, int maxHealth) {
        obj = p;
        width = w;
        height = h;
        margin = m;
        maxHealthPoints = maxHealth;
        obj.setHealthPoint(maxHealth);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.parseColor("#bdf0ff"));

        healthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        healthPaint.setColor(Color.parseColor("#41c200"));
    }

    public void draw(Canvas canvas) {
        float x = (float) obj.getX();
        float y = (float) obj.getY();
        float distanceToPlayer = 30;
        float healthPointPercentage = (float) obj.getHealthPoint()/maxHealthPoints;

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
