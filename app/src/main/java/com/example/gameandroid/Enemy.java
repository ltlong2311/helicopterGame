package com.example.gameandroid;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy extends GameObject {
    private Bitmap image;
    private int score;
    private int speed;
    private Random rand = new Random();

    public Enemy(Bitmap res, int x, int y, int w, int h, int s) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 3 + (int) (rand.nextDouble() * score / 50);

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

    @Override
    public int getWidth() {
        // them chut do rong de phat hien va cham thuc te hon
        return width - 10;
    }
}
