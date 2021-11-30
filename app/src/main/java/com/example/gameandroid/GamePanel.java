package com.example.gameandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

@SuppressLint({"MissingSuperCall", "ClickableViewAccessibility"})

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private Random rand = new Random();
    private boolean newGameCreated;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best;


    public GamePanel(Context context) {
        super(context);

        // add callback vào surfaceHolder để chặn các event
        getHolder().addCallback(this);

//        thread = new MainThread(getHolder(), this);

        // make gamePanel focusable để nó có thể xử lý các event
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg4));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        bg.setVector(-5);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
                player.setUp(true);
            }
            if (player.getPlaying()) {
                if (!started) started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            player.update();

            //thêm tên lửa vào timer
            long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
            if (missileElapsed > (2000 - player.getScore() / 4)) {

                System.out.println("making missile");
                //tên lửa đầu tiên luôn đi ở giữa
                if (missiles.size() == 0) {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            missile), WIDTH + 10, HEIGHT / 2, 45, 15, player.getScore(), 13));
                } else {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT)), 45, 15, player.getScore(), 13));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }
            //kiểm tra tất cả tên lửa, check va chạm và remove
            for (int i = 0; i < missiles.size(); i++) {
                //update missile
                missiles.get(i).update();

                if (collision(missiles.get(i), player)) {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //loại bỏ tên lửa nếu nó nằm ngoài màn hình
                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i);
                    break;
                }
            }


            //thêm khói phun vào timer
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));   // khoi sau toa do player (may bay)
                smokeStartTime = System.nanoTime();
            }
            for (int i = 0; i < smoke.size(); i++) {
                smoke.get(i).update();
                if (smoke.get(i).getX() < -10) {
                    smoke.remove(i);
                }
            }

        }
        else {
//            player.resetDY();
            player.resetDYA();
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }
        }
    }

    private void newGame() {
        disappear = false;
        missiles.clear();
        smoke.clear();

//            player.resetDY();
        player.resetDYA();
        player.resetScore();
        player.setY(HEIGHT/2);

        if(player.getScore()>best)
        {
            best = player.getScore();

        }

        newGameCreated = true;
    }

    public boolean collision(GameObject a, GameObject b) {
//        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
//            return true;
//        }
//        return false;

        // sử dụng React.intersects tìm giao điểm của hình chữ nhật hiện tại và hình chữ nhật được// chỉ định và lưu trữ kết quả dưới dạng hình chữ nhật hiện tại.
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = (float) getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = (float) getHeight() / (HEIGHT * 1.f);
//        System.out.println("scaleFactorX" + scaleFactorX);
//        System.out.println("scaleFactorY" + scaleFactorY);
        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            if(!disappear) {
                player.draw(canvas);
            }
            for (Smokepuff sp : smoke) {
                sp.draw(canvas);   // draw các smoke puff trong vòng lặp tiếp theo
            }

            for (Missile m : missiles) {
                m.draw(canvas);
            }

            if(started){
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + (player.getScore()*3), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }
}
