package com.example.gameandroid.GameObject;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet extends GameObject {
    private Paint bulletStyle;
    Bitmap image;

    public Bullet(Bitmap res, Player player) {
        x = player.getX() + player.getWidth() - 10;
        y = player.getY() + player.getHeight()/2;
        image = res;

        bulletStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void update() {
        x += 30;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, bulletStyle);

    }
}
