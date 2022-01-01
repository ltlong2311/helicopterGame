package com.example.gameandroid.GameObject;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.gameandroid.GameObject.GameObject;

import java.util.Random;

public class Enemy extends GameObject {
    private Bitmap image;
    private int score, speed, healthPoints, maxHealthPoints;
    private Random rand = new Random();
    private Paint enemyStyle;

    public Enemy(Bitmap res, int x, int y, int w, int h, int s, int maxHealth, int baseSpeed) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        maxHealthPoints = maxHealth;
        healthPoints = maxHealth;
        speed = baseSpeed + (int) (rand.nextDouble() * score /5);

        // speed
        if (speed > 35) speed = 35;
        image = res;

        enemyStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void update() {
        x -= speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, enemyStyle);
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }


    @Override
    public int getWidth() {
        // them chut do rong de phat hien va cham thuc te hon
        return width - 10;
    }
}
