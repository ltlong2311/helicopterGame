package com.example.gameandroid.GameObject;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet extends GameObject {
    private double dxa;
    Bitmap image;

    public Bullet(Bitmap res, Player player) {
        x = player.getX() + player.getWidth() + 10;
        y = player.getY() + player.getHeight()/2;
        image = res;
    }

    public void update() {
        x += 30;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);

    }
}
