package com.example.gameandroid.GameObject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.gameandroid.GameObject.GameObject;

import java.util.Random;

public class Enemy extends GameObject {
    private Bitmap image;
    private int score, speed, healthPoints, maxHealthPoints;
    private Random rand = new Random();

    public Enemy(Bitmap res, int x, int y, int w, int h, int s, int maxHealth) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        maxHealthPoints = maxHealth;
        healthPoints = maxHealth;
        speed = 5 + (int) (rand.nextDouble() * score / 50);

        // speed
        if (speed > 40) speed = 40;
        image = res;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
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
