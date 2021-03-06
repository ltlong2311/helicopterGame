package com.example.gameandroid.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.gameandroid.Panel.GamePanel;

public class Background {
    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res){
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update() {
        x+=dx;
//        if (x<0){
//            x=0;
//        }
        if(x<-GamePanel.WIDTH){
            x=0;
        }
    }

    public void draw(Canvas canvas) {
       canvas.drawBitmap(image, x,y,null);
       if (x<0){
           canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
       }
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
