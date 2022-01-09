package com.example.gameandroid.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.gameandroid.Panel.GamePanel;

import java.util.Random;

public class EnemyBullet extends GameObject{
    private Paint bulletStyle;
    Bitmap image;
    private Player player;
    private static final int MAX_SPEED = 20;
    private Random rand = new Random();
    private int randY;

    public EnemyBullet(Bitmap res, int w, int h, Player player, Enemy enemy) {
        x = enemy.getX() - 10;
        y = enemy.getY() + enemy.getHeight()/2;
        image = res;
        this.player = player;
        bulletStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
        randY= rand.nextInt(8 + 9) - 9;


    }

    public void update() {
//        int distanceToPlayerX = player.x - x;
//        int distanceToPlayerY = player.y - y;
//
//        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);
//
//        int directionX = (int) (distanceToPlayerX/distanceToPlayer);
//        int directionY = (int) (distanceToPlayerY/distanceToPlayer);
//
//        x += directionX;
//        y += directionY*10;
        x -= 15;
        y += randY;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, bulletStyle);

    }
}
