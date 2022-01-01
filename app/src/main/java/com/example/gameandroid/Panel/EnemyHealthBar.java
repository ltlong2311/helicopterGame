package com.example.gameandroid.Panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.gameandroid.GameObject.Enemy;
import com.example.gameandroid.GameObject.Player;

public class EnemyHealthBar {
    private Enemy enemy;
    private int width, height, margin;
    private Paint borderPaint, healthPaint;
    private int enemyHealth;

    public EnemyHealthBar(Enemy e, int h, int m) {
        enemy = e;
        width = 15 + 10*enemy.getMaxHealthPoints();
        height = h;
        margin = m;
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.parseColor("#bfe7f2"));

        healthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        healthPaint.setColor(Color.parseColor("#c3150c"));
    }

    public void draw(Canvas canvas) {
        float x = (float) enemy.getX();
        float y = (float) enemy.getY();
        int enemyWidth = enemy.getWidth();
        float distanceToPlayer = 10;
        float healthPointPercentage = (float) enemy.getHealthPoint()/enemy.getMaxHealthPoints();

        // border
        float borderLeft, borderTop, borderRight, borderBottom;
//        borderLeft = x;
//        borderRight = x + width;
        if(width > enemyWidth){
            borderLeft = x - (width-enemyWidth)/2;
            borderRight = x + enemyWidth + (width-enemyWidth)/2;
        } else {
            borderLeft = x + (enemyWidth-width)/2;
            borderRight = x + enemyWidth - (enemyWidth-width)/2;
        }
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);

        // health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, heathHeight;
        healthWidth = width - 2*margin;
        heathHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = borderBottom;
        healthTop = healthBottom - heathHeight;
        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, healthPaint);
    }

    public int getEnemyHealth() {
        return enemyHealth = enemy.getHealthPoint();
    }

}
