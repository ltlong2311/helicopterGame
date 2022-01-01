package com.example.gameandroid.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.gameandroid.Panel.GamePanel;

public class HealthStatus {
    private Bitmap image;
    private int x, y;
    private Paint healthStyle;

    public HealthStatus(Bitmap res, int x, int y){
        image = res;
        this.x = x;
        this.y =y ;
        healthStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x,y,healthStyle);
    }

    public int getX() {return x;}

    public int getY() {return y;}
}

