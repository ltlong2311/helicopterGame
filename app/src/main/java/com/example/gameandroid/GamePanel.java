package com.example.gameandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
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

import java.io.InputStream;
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
    private HealthBar healthBar;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private Random rand = new Random();
    private boolean newGameCreated;
    private boolean isFocusUpButton;
    private boolean isFocusAimButton;
    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best = 0;

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
        healthBar = new HealthBar(player, 60, 12, 2);
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
                //up button position
                Rect up = new Rect((int) (60*scaleX), (int) ((HEIGHT - 110)*scaleY), (int) (110*scaleX),
                        (int) ((HEIGHT - 60)*scaleY));
                if (up.contains(touchX, touchY)) {
                    isFocusUpButton = true;
                    player.setUp(true);
                };
                // aim button position
                Rect aim = new Rect((int) ((WIDTH - 125)*scaleX), (int) ((HEIGHT - 110)*scaleY),
                         (int) ((WIDTH - 75)*scaleX), (int) ((HEIGHT - 60)*scaleY));
//                System.out.println(touchX + " : " + touchY);
                if (aim.contains(touchX, touchY)){
                    isFocusAimButton = true;
                    System.out.println("Touched Aim Button");
                } else {
                    isFocusUpButton = true;
                    player.setUp(true);
                }
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            isFocusUpButton = false;
            isFocusAimButton = false;
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

//                System.out.println("making missile");
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
                    missiles.remove(i);
                    if (player.getHealthPoint() > 1) {
                        player.setHealthPoint(player.getHealthPoint() - 1);
                    } else {
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


            //them smoke vao timer
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));   // smoker sau toa do player (may bay)
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

        if(player.getScore() > best)
        {
            best = player.getScore();
        }

        newGameCreated = true;
    }

    public boolean collision(GameObject a, GameObject b) {
        // sử dụng React.intersects tìm giao điểm của hình chữ nhật hiện tại và hình chữ nhật được chỉ định và lưu trữ kết quả dưới dạng hình chữ nhật hiện tại.
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
            if(!disappear) {
                player.draw(canvas);
                healthBar.draw(canvas);
                drawControllerButton(canvas);
                drawAimButton(canvas);
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

    private void drawControllerButton(Canvas canvas) {
        //
//        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.parseColor("#ebf2ef"));
//        mPaint.setStrokeWidth(1);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(60, HEIGHT - 110, 120,HEIGHT - 50, mPaint); // -5 tu icon den border

        Paint paint = new Paint();
        Resources res = getResources();
        Bitmap up;
        if (!isFocusUpButton){
            up = BitmapFactory.decodeResource(res, R.drawable.up10);
        } else {
            up = BitmapFactory.decodeResource(res, R.drawable.up10a);
        }
        canvas.drawBitmap(up, 55, HEIGHT - 115, paint);

        //
        Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(Color.parseColor("#d1e0de"));
        mPaint2.setStrokeWidth(1);
        mPaint2.setStyle(Paint.Style.STROKE);
        float radius = 45.0f;
        canvas.drawRect(WIDTH - 125, HEIGHT - 110, WIDTH - 75,HEIGHT - 60, mPaint2);
//        canvas.drawCircle(WIDTH - 100, HEIGHT - 95, radius, mPaint2);
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
        canvas.drawBitmap(aim, WIDTH - 125, HEIGHT - 110, aimBtnStyle);
    }

    private void drawText(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
        paint.setColor(Color.parseColor("#00235e"));
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Bold.otf"));
        canvas.drawText("DISTANCE: " + (player.getScore()*3), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 120, HEIGHT - 10, paint);
        paint.setTextSize(18);
        paint.setColor(Color.parseColor("#fdfffc"));
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Semibold.otf"));
        canvas.drawText("FPS: " + Math.round(thread.getAverageFPS()), WIDTH - 100, 30, paint);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint style = new Paint(Paint.ANTI_ALIAS_FLAG);
            style.setTextSize(30);
            style.setColor(Color.parseColor("#01466e"));
            style.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Bold.otf"));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, style);

            Paint style2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            style2.setTextSize(15);
            style2.setColor(Color.parseColor("#283000"));
            style2.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"SourceSansPro-Regular.otf"));
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 + 25, style2);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 45, style2);
        }
    }
}
