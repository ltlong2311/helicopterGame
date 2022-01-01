package com.example.gameandroid.GameObject;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.gameandroid.Graphics.Animation;
import com.example.gameandroid.Panel.GamePanel;


public class Player extends GameObject {
    public static final int MAX_HEALTH_POINTS = 9;
    private Bitmap spritesheet;
    private int score;
    private boolean up, down, left, right;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private int healthPoints = MAX_HEALTH_POINTS;
    private Paint playerStyle;

    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 90;
        y = GamePanel.HEIGHT / 2;
        dx = 0;
        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

        playerStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 2000) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if (up) {
            if (y > 0) {
                dy -= 3;
            } else {
                dy = 0;
            }
        } else if (right) {
            if (x < GamePanel.WIDTH / 2 - width) {
                dx += 3;
            } else {
                dx = 0;
            }
        }
        else if (left) {
            if (x > 0) {
                dx -= 3;
            } else {
                dx = 0;
            }
        }
        else {
            // dy = (int)(dya += 0.8);
            dy += 1;
        }
        if (down) { dy += 3; }

        y += dy * 2;
        x += dx * 2;

        // gioi han dx, dy khi an giu nut dieu khien
        if (dy > 20) dy = 20;
        if (dy < -15) dy = -15;
        if (dx > 15) dx = 15;
        if (dx < -15) dx = -15;

        dy = 0;
        dx = 0;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y,  playerStyle);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDY() {
        dy = 0;
    }

    public void resetDX() {
        dx = 0;
    }


    public void resetScore() {
        score = 0;
    }
}
