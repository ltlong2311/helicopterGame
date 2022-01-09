package com.example.gameandroid.Panel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.gameandroid.GameObject.EnemyBullet;
import com.example.gameandroid.Graphics.Background;
import com.example.gameandroid.Graphics.Explosion;
import com.example.gameandroid.GameObject.Bullet;
import com.example.gameandroid.GameObject.Enemy;
import com.example.gameandroid.GameObject.GameObject;
import com.example.gameandroid.GameObject.Missile;
import com.example.gameandroid.GameObject.Smokepuff;
import com.example.gameandroid.Graphics.HealthStatus;
import com.example.gameandroid.MainThread;
import com.example.gameandroid.GameObject.Player;
import com.example.gameandroid.R;
import com.example.gameandroid.Sound.MusicPlayer;
import com.example.gameandroid.Sound.SoundPlayer;
import com.example.gameandroid.Views.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint({"MissingSuperCall", "ClickableViewAccessibility"})

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private long enemyStartTime, enemyLv2StartTime, enemyLv3StartTime;
    private long enemyBulletStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private HealthStatus healthStatus;
    private HealthBar healthBar;
    private LargeHealthBar largeHealthBar;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private List<Bullet> bulletList;
    private List<EnemyBullet> enemyBulletList;
    private List<Enemy> enemyList;
    private List<EnemyHealthBar> healthBarEnemyList;
    private Random rand = new Random();
    private boolean newGameCreated;
    private boolean isFocusUpButton;
    private boolean isFocusDownButton;
    private boolean isFocusLeftButton;
    private boolean isFocusRightButton;
    private boolean isFocusAimButton;
    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best;
    private int gameMode;
    private int gameDifficulty;
    private SoundPlayer sound;
    MusicPlayer musicPlayer;
    Bitmap enemyType, enemyLv2Type, enemyLv3Type;
    SharedPreferences preferences;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        // make gamePanel focusable de co the xu ly cac event
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        preferences = getContext().getApplicationContext().getSharedPreferences(Home.GAME_SETTINGS, Context.MODE_PRIVATE);
        gameMode = preferences.getInt("mode", 1);
        best = preferences.getInt("highestScore", 1);
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg4));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3, gameMode);
        healthStatus = new HealthStatus(BitmapFactory.decodeResource(getResources(),R.drawable.hpbar_emty_9), 15, 10);
        largeHealthBar = new LargeHealthBar(player, healthStatus, 1);
        healthBar = new HealthBar(player, 65, 12, 2);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        bulletList = new ArrayList<Bullet>();
        enemyBulletList = new ArrayList<EnemyBullet>();
        enemyList = new ArrayList<Enemy>();
        healthBarEnemyList = new ArrayList<EnemyHealthBar>();
        enemyStartTime = System.nanoTime();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();
        thread = new MainThread(getHolder(), this);
        gameDifficulty = - (4 + gameMode);
        bg.setVector(gameDifficulty);
        sound = new SoundPlayer(getContext());
        musicPlayer = new MusicPlayer(getContext());
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
        musicPlayer.stopGameMusic();
        musicPlayer.stopInGameMusic();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
//                player.setUp(true);
            }
            if (player.getPlaying()) {
                if (!started) started = true;
                reset = false;
                float scaleX = (float) getWidth() / (WIDTH * 1.f);
                float scaleY =(float) getHeight() / (HEIGHT * 1.f);
                //
                //up button position
                Rect up = new Rect(80, HEIGHT - 140, 140,HEIGHT - 80);
                if (up.contains((int) (touchX/scaleX), (int) (touchY/scaleY))) {
                    isFocusUpButton = true;
                    player.setUp(true);
                };
                Rect down = new Rect(80, HEIGHT - 70, 140,HEIGHT - 10);
                if (down.contains((int) (touchX/scaleX), (int) (touchY/scaleY))) {
                    isFocusDownButton = true;
                    player.setDown(true);
                };
                Rect left = new Rect(20, HEIGHT - 100, 80,HEIGHT - 40);
                if (left.contains((int) (touchX/scaleX), (int) (touchY/scaleY))) {
                    isFocusLeftButton = true;
                    player.setLeft(true);
                };
                Rect right = new Rect(140, HEIGHT - 100, 200,HEIGHT - 40);
                if (right.contains((int) (touchX/scaleX), (int) (touchY/scaleY))) {
                    isFocusRightButton = true;
                    player.setRight(true);
                };
                // aim button position
                Rect aim = new Rect((int) ((WIDTH - 125)*scaleX), (int) ((HEIGHT - 100)*scaleY),
                         (int) ((WIDTH - 55)*scaleX), (int) ((HEIGHT - 40)*scaleY));
//                System.out.println(touchX + " : " + touchY);
                if (aim.contains(touchX, touchY)){
                    isFocusAimButton = true;
                    bulletList.add(new Bullet(
                            BitmapFactory.decodeResource(getResources(), R.drawable.bullet20),
                            player));
                    System.out.println("Touched Aim Button");
                    sound.playBulletSound();
                }
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            player.setDown(false);
            player.setLeft(false);
            player.setRight(false);
            isFocusUpButton = false;
            isFocusDownButton = false;
            isFocusLeftButton = false;
            isFocusRightButton = false;
            isFocusAimButton = false;
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            player.update();
            musicPlayer.pauseGameMusic();
            musicPlayer.playInGameMusic();

            if (player.getY() > HEIGHT) {
                player.setHealthPoint(player.MAX_HEALTH_POINTS);
                player.setPlaying(false);
            }

            //thêm tên lửa vào timer
            long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
            if (missileElapsed > (2500 - player.getScore()/2)) {
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

            //kiem tra tat ca ten lua, check va cham va remove
            for (int i = 0; i < missiles.size(); i++) {
                //update missile
                missiles.get(i).update();
                // check va cham
                if (collision(missiles.get(i), player)) {
                    if (player.getHealthPoint() > 1) {
                        sound.playExplosionSound();
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), missiles.get(i).getX() - 25,
                                missiles.get(i).getY() - 20, 100, 100, 25, 2);
                        player.setHealthPoint(player.getHealthPoint() - 1);
                        missiles.remove(i);
                    } else {
                        sound.playExplosionSound();
                        missiles.remove(i);
                        player.setHealthPoint(player.MAX_HEALTH_POINTS);
                        player.setPlaying(false);    // end game
                    }
                    break;
                }
                //loai bo ta ca ten lua nam ngoai man hinh
                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i);
                    break;
                }
            }
            explosion.update();

            //enemy
            long enemyElapsed = (System.nanoTime() - enemyStartTime) / 1000000;
            if (enemyElapsed > (3000 - player.getScore()/9)) {
                int randomEnemyType = rand.nextInt(4) + 1;
                switch (randomEnemyType) {
                    case 1:
                        enemyType = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter8);
                        break;
                    case 2:
                        enemyType = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter3_96);
                        break;
                    case 3:
                        enemyType = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter9);
                        break;
                    case 4:
                        enemyType = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter10);
                        break;
                }
                enemyList.add(new Enemy(enemyType,
                        WIDTH + 10, 60 + (int) (rand.nextDouble() * (HEIGHT - 160)), 60, 60, player.getScore(), 2, 7, gameMode));   // min - ran*(max-min)
                enemyStartTime = System.nanoTime();
            }

            long enemyLv2Elapsed = (System.nanoTime() - enemyLv2StartTime) / 1000000;
            if (enemyLv2Elapsed > (8000 - player.getScore()/9)) {
                int randomEnemyType = rand.nextInt(3) + 1;
                switch (randomEnemyType) {
                    case 1:
                        enemyLv2Type = BitmapFactory.decodeResource(getResources(), R.drawable.aircraft3);
                        break;
                    case 2:
                        enemyLv2Type = BitmapFactory.decodeResource(getResources(), R.drawable.aircraft5);
                        break;
                    case 3:
                        enemyLv2Type = BitmapFactory.decodeResource(getResources(), R.drawable.aircraft6);
                        break;
                }
                enemyList.add(new Enemy(enemyLv2Type,
                        WIDTH + 10, 65 + (int) ( rand.nextDouble() * (HEIGHT - 315)), 65, 65, player.getScore(), 1, 12, gameMode));
                enemyLv2StartTime = System.nanoTime();
            }

            long enemyLv3Elapsed = (System.nanoTime() - enemyLv3StartTime) / 1000000;
            if (enemyLv3Elapsed > (19000 - player.getScore()/9)) {
                int randomEnemyType = rand.nextInt(2) + 1;
                switch (randomEnemyType) {
                    case 1:
                        enemyLv3Type = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter4);
                        break;
                    case 2:
                        enemyLv3Type = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter13);
                        break;
                }
                enemyList.add(new Enemy(enemyLv3Type,
                        WIDTH + 10, 100 + (int) (rand.nextDouble() * (HEIGHT - 200)), 80, 80, player.getScore(), 5, 4, gameMode));
                enemyLv3StartTime = System.nanoTime();
            }

            for (int i = 0; i < enemyList.size(); i++) {
                enemyList.get(i).update();
                healthBarEnemyList.add(new EnemyHealthBar(enemyList.get(i), 8, 1));
                // check va cham
                if (collision(enemyList.get(i), player)) {
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), enemyList.get(i).getX() -25,
                            enemyList.get(i).getY()-25, 100, 100, 25, 2);
                    sound.playExplosionSound();
                    if (player.getHealthPoint() > enemyList.get(i).getHealthPoint()) {
                        player.setHealthPoint(player.getHealthPoint() - enemyList.get(i).getHealthPoint());
                        enemyList.get(i).setHealthPoint(0);
                        enemyList.remove(i);
                    } else {
                        enemyList.get(i).setHealthPoint(0);
                        enemyList.remove(i);
                        player.setHealthPoint(player.MAX_HEALTH_POINTS);
                        player.setPlaying(false);    // end game
                    }
                    break;
                }
                if (enemyList.get(i).getX() < - 50) {
                    enemyList.get(i).setHealthPoint(0);
                    enemyList.remove(i);
                    break;
                }
                if (enemyList.get(i).getMaxHealthPoints() == 5){
                    long enemyBulletElapsed = (System.nanoTime() - enemyBulletStartTime) / 1000000;
                    if (enemyBulletElapsed > 3000) {
                        enemyBulletList.add(new EnemyBullet(
                                BitmapFactory.decodeResource(getResources(), R.drawable.bullet_trip), 50, 15,
                                player, enemyList.get(i)));
                        sound.playEnemyFireSound();
                        enemyBulletStartTime = System.nanoTime();
                    }
                }
            }
            explosion.update();

            //smoke
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));
                smokeStartTime = System.nanoTime();
            }
            for (int i = 0; i < smoke.size(); i++) {
                smoke.get(i).update();
                if (smoke.get(i).getX() < -10) {
                    smoke.remove(i);
                }
            }

            for (Bullet b: bulletList) {
                b.update();
            }

            for (int i = 0; i <  bulletList.size(); i++) {
                if ( bulletList.get(i).getX() > getWidth()) {
                    bulletList.remove(i);
                    break;
                }
                // bullet ban trung enemy
                for (int j = 0; j < enemyList.size(); j++) {
                    if (collision(enemyList.get(j), bulletList.get(i))) {
                        if (enemyList.get(j).getHealthPoint() > 1) {
                            enemyList.get(j).setHealthPoint(enemyList.get(j).getHealthPoint() - 1);
                            bulletList.remove(i);
                        } else {
                            player.setScore(player.getScore() + 5);
                            explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), enemyList.get(j).getX() -25,
                                enemyList.get(j).getY()-25, 100, 100, 25, 10);
                            sound.playExplosionSound2();
                            enemyList.get(j).setHealthPoint(enemyList.get(j).getHealthPoint() - 1);
                            bulletList.remove(i);
                            enemyList.remove(j);
                        }
                        break;
                    }
                }
            }
            explosion.update();

            for (EnemyBullet eb: enemyBulletList) {
                eb.update();
            }

            for (int i = 0; i <  enemyBulletList.size(); i++) {
                if ( enemyBulletList.get(i).getX() < - 100) {
                    enemyBulletList.remove(i);
                    break;
                }
                if (collision(enemyBulletList.get(i), player)) {
                    if (player.getHealthPoint() > 1) {
                        sound.playExplosionSound();
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), enemyBulletList.get(i).getX() - 25,
                                enemyBulletList.get(i).getY() - 20, 100, 100, 25, 2);
                        player.setHealthPoint(player.getHealthPoint() - 1);
                        enemyBulletList.remove(i);
                    } else {
                        sound.playExplosionSound();
                        enemyBulletList.remove(i);
                        player.setHealthPoint(player.MAX_HEALTH_POINTS);
                        player.setPlaying(false);
                    }
                    break;
                }
            }
            explosion.update();
        }
        else {
            player.resetDY();
            player.resetDX();
            musicPlayer.playGameMusic();
            musicPlayer.pauseInGameMusic();
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25, 10);
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
        enemyList.clear();
        healthBarEnemyList.clear();
        player.resetDY();
        player.resetDX();
        if(player.getScore() > best)
        {
            best = player.getScore();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highestScore", best);
            editor.apply();
        }
        player.resetScore();
        player.setY(HEIGHT/2);
        player.setX(90);
        newGameCreated = true;
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = (float) getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = (float) getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            healthStatus.draw(canvas);
            if(!disappear) {
                player.draw(canvas);
                healthBar.draw(canvas);
                largeHealthBar.draw(canvas);
                healthStatus.draw(canvas);
            }
//            for (Smokepuff sp : smoke) {
//                sp.draw(canvas);
//            }
            for (Missile m : missiles) {
                m.draw(canvas);
            }
            for (Enemy e : enemyList) {
                e.draw(canvas);
            }
            for (EnemyHealthBar h: healthBarEnemyList) {
                if(h.getEnemyHealth() > 0) {
                    h.draw(canvas);
                }
            }
            for (Bullet b: bulletList) {
                b.draw(canvas);
            }
            for (EnemyBullet eb: enemyBulletList) {
                eb.draw(canvas);
            }
            if(started){
                explosion.draw(canvas);
            }

            if(!disappear) {
                drawAimButton(canvas);
                drawControllerButton(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    private void drawControllerButton(Canvas canvas) {
        Paint paint = new Paint();
        Resources res = getResources();
        Bitmap up, down, left, right;
        if (!isFocusUpButton){
            up = BitmapFactory.decodeResource(res, R.drawable.up10);
        } else {
            up = BitmapFactory.decodeResource(res, R.drawable.up10a);
        }
        canvas.drawBitmap(up, 80, HEIGHT - 140, paint);

        if (!isFocusDownButton){
            down = BitmapFactory.decodeResource(res, R.drawable.down);
        } else {
            down = BitmapFactory.decodeResource(res, R.drawable.down_focus);
        }
        canvas.drawBitmap(down, 80, HEIGHT - 70, paint);

        if (!isFocusLeftButton){
            left = BitmapFactory.decodeResource(res, R.drawable.left);
        } else {
            left = BitmapFactory.decodeResource(res, R.drawable.left_focus);
        }
        canvas.drawBitmap(left, 20, HEIGHT - 100, paint);

        if (!isFocusRightButton){
            right = BitmapFactory.decodeResource(res, R.drawable.right);
        } else {
            right = BitmapFactory.decodeResource(res, R.drawable.right_focus);
        }
        canvas.drawBitmap(right, 140, HEIGHT - 100, paint);

//        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.parseColor("#ebf2ef"));
//        mPaint.setStrokeWidth(1);
//        mPaint.setStyle(Paint.Style.STROKE);
//        //up
//        canvas.drawRect(80, HEIGHT - 140, 140,HEIGHT - 80, mPaint);
//        // down
//        canvas.drawRect(80, HEIGHT - 70, 140,HEIGHT - 10, mPaint);
//        // left
//        canvas.drawRect(20, HEIGHT - 100, 80,HEIGHT - 40, mPaint);
//        // right
//        canvas.drawRect(140, HEIGHT - 100, 200,HEIGHT - 40, mPaint);

//        Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint2.setColor(Color.parseColor("#d1e0de"));
//        mPaint2.setStrokeWidth(1);
//        mPaint2.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(WIDTH - 125, HEIGHT - 100, WIDTH - 55,HEIGHT - 40, mPaint2);
    }

    private void drawAimButton(Canvas canvas) {
        Paint aimBtnStyle = new Paint();
        Resources res = getResources();
        Bitmap aim;
        if(!isFocusAimButton){
            aim = BitmapFactory.decodeResource(res, R.drawable.aim_12);
        } else {
            aim = BitmapFactory.decodeResource(res, R.drawable.aim_11);
        }
        canvas.drawBitmap(aim, WIDTH - 120, HEIGHT - 95, aimBtnStyle);
    }

    private void drawText(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(12);
        paint.setColor(Color.parseColor("#fdfffc"));
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Bold.otf"));
        canvas.drawText("SCORE", WIDTH/2, 20, paint);

        Paint scoreStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreStyle.setColor(Color.parseColor("#fdfffc"));
        scoreStyle.setTextSize(18);
        canvas.drawText(String.valueOf(player.getScore()), WIDTH/2 + 5, 50, scoreStyle);

        Paint paraStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paraStyle.setTextSize(16);
        paraStyle.setColor(Color.parseColor("#fdfffc"));
        paraStyle.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Semibold.otf"));
        canvas.drawText("FPS: " + Math.round(thread.getAverageFPS()), WIDTH - 100, 30, paraStyle);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint style = new Paint(Paint.ANTI_ALIAS_FLAG);
            style.setTextSize(30);
            style.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Black.otf"));
            style.setColor(Color.parseColor("#ffd52b"));
            canvas.drawText("BEST: "+ best, WIDTH/2-50, HEIGHT/2 - 40, style);

            Paint style2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            style2.setTextSize(25);
            style2.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Bold.otf"));
            style2.setColor(Color.parseColor("#01466e"));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, style2);

            Paint style3 = new Paint(Paint.ANTI_ALIAS_FLAG);
            style3.setTextSize(16);
            style3.setColor(Color.parseColor("#283000"));
            style3.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Regular.otf"));
            canvas.drawText("Press and hold the control button to move", WIDTH/2-50, HEIGHT/2 + 25, style3);
            canvas.drawText("Press the fire button to destroy the enemy", WIDTH/2-50, HEIGHT/2 + 45, style3);
        }
    }
}
