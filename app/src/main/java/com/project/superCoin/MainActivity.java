package com.project.superCoin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageButton pauseWhilePlaying;
    ImageButton startAfterPaused;
    //declaration of all variables
    private long firstTimeExit = 0;
    private TextView scoreGaming;
    private TextView tapToStartLabel;
    private ImageView player;
    private ImageView coin;
    private ImageView diamond;
    private ImageView enemy1;
    private ImageView enemy2;
    private FrameLayout frameLb;

    private int frameHeight;
    private int frameWidth;
    private int playerSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int playerY;
    private int playerX;
    private int coinX;
    private int coinY;
    private int diamondX;
    private int diamondY;
    private int enemy1X;
    private int enemy1Y;
    private int enemy2X;
    private int enemy2Y;

    private SoundEffects sound;

    private int playerSpeed;
    private int coinSpeed;
    private int diamondSpeed;
    private int enemy1Speed;
    private int enemy2Speed;

    private int score = 0;
    private int increaseSpeed = 0;
    private int colorChange = 255;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //status
    private boolean action_flg = false;
    private boolean hit_flg = false;
    private boolean start_flg = false;
    private boolean pause_flg = false;

    //set FPS
    //60 frames/sec
    //millisecond/second
    private int time = 1000 / 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sound = new SoundEffects(this);

        scoreGaming = findViewById(R.id.score_gaming);
        tapToStartLabel = findViewById(R.id.tapToStart);
        pauseWhilePlaying = findViewById(R.id.pause_while_playing);
        startAfterPaused = findViewById(R.id.start_after_paused);

        frameLb = findViewById(R.id.frame_lb);

        player = findViewById(R.id.player);
        coin = findViewById(R.id.coin);
        diamond = findViewById(R.id.diamond);
        enemy1 = findViewById(R.id.enemy1);
        enemy2 = findViewById(R.id.enemy2);

        WindowManager wm = getWindowManager();
        Display dis = wm.getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);

        pauseWhilePlaying.setEnabled(false);
        pauseWhilePlaying.setVisibility(View.GONE);
        scoreGaming.setVisibility(View.GONE);
        frameLb.setVisibility(View.GONE);

        screenWidth = size.x;
        screenHeight = size.y;

        playerSpeed = Math.round(screenWidth / 100);
        coinSpeed = Math.round(screenWidth / 120);
        diamondSpeed = Math.round(screenWidth / 100);
        enemy1Speed = Math.round(screenWidth / 100);
        enemy2Speed = Math.round(screenWidth / 80);

        coin.setX(-80f);
        coin.setY(-80f);
        diamond.setX(-80f);
        diamond.setY(-80f);
        enemy1.setX(-80f);
        enemy1.setY(-80f);
        enemy2.setX(-80f);
        enemy2.setY(-80f);

        scoreGaming.setText("Score: 0");
    }

    public void position() {
        hit();

        //coin position
        coinX -= coinSpeed;
        if (coinX < 0) {
            coinX = screenWidth + 20;
            coinY = (int) Math.floor(Math.random() * (frameHeight - coin.getHeight()));
        }
        coin.setX(coinX);
        coin.setY(coinY);

        //enemy1
        enemy1X -= enemy1Speed;
        if (enemy1X < 0) {
            enemy1X = screenWidth + 5;
            enemy1Y = (int) Math.floor(Math.random() * (frameHeight - enemy1.getHeight()));
        }
        enemy1.setX(enemy1X);
        enemy1.setY(enemy1Y);

        //enemy2
        enemy2X -= enemy2Speed;
        if (enemy2X < 0) {
            enemy2X = screenWidth + 10;
            enemy2Y = (int) Math.floor(Math.random() * (frameHeight - enemy2.getHeight()));
        }
        enemy2.setX(enemy2X);
        enemy2.setY(enemy2Y);

        //diamond
        diamondX -= diamondSpeed;
        if (diamondX < 0) {
            diamondX = screenWidth + 5000;
            diamondY = (int) Math.floor(Math.random() * (frameHeight - diamond.getHeight()));
        }
        diamond.setX(diamondX);
        diamond.setY(diamondY);

        //player
        if (action_flg) {
            playerY -= playerSpeed;
            player.setImageResource(R.drawable.player0);
            playerX += 5;
        } else {
            playerY += playerSpeed;
            player.setImageResource(R.drawable.player1);
            playerX -= 5;
        }

        if (hit_flg) {
            player.setImageResource(R.drawable.player2);
        }

        if (playerY < 0) {
            playerY = 0;
        }

        if (playerX < 0) {
            playerX = 0;
        }

        if (playerY > frameHeight - playerSize) {
            playerY = frameHeight - playerSize;
        }

        player.setY(playerY);
        player.setX(playerX);

        scoreGaming.setText("Score: " + score);
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!start_flg) {
            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            playerY = (int) player.getY();

            playerSize = player.getHeight();

            tapToStartLabel.setVisibility(View.GONE);

            pauseWhilePlaying.setVisibility(View.VISIBLE);

            scoreGaming.setVisibility(View.VISIBLE);

            pauseWhilePlaying.setEnabled(true);

            setFPS(time);
        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    public void hit() {
        Rect playerR = new Rect(playerX + 10, playerY + 10, playerX + player.getWidth() - 20, playerY + player.getHeight() - 20);

        //coin hit
        Rect coinR = new Rect(coinX, coinY, coinX + coin.getWidth(), coinY + coin.getHeight());

        if (coinR.intersect(playerR)) {
            score += 1;
            coinX = -10;
            sound.collectSound();
            Log.d("Rect", "Coin hit");
        }

        //diamond hit
        Rect diamondR = new Rect(diamondX, diamondY, diamondX + diamond.getWidth(), diamondY + diamond.getHeight());

        if (diamondR.intersect(playerR)) {
            score += 3;
            diamondX = -10;
            sound.collectSound();
            Log.d("Rect", "Diamond hit");
        }

        //enemy1 hit
        Rect enemy1R = new Rect(enemy1X, enemy1Y, enemy1X + enemy1.getWidth(), enemy1Y + enemy1.getHeight());

        if (enemy1R.intersect(playerR)) {
            hit_flg = true;
            timer.cancel();
            timer = null;
            sound.loseSound();
            Log.d("Rect", "Enemy1 hit");

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        }

        //enemy2 hit
        Rect enemy2R = new Rect(enemy2X, enemy2Y, enemy2X + enemy2.getWidth(), enemy2Y + enemy2.getHeight());

        if (enemy2R.intersect(playerR)) {
            hit_flg = true;
            timer.cancel();
            timer = null;
            sound.loseSound();
            Log.d("Rect", "Enemy2 hit");

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        }
    }

    public void pauseGame(View view) {
        if (!pause_flg) {
            pause_flg = true;
            timer.cancel();
            timer = null;
            Drawable d = getResources().getDrawable((R.drawable.ic_action_paused));
            pauseWhilePlaying.setBackground(d);
            frameLb.setVisibility(View.VISIBLE);
        } else {
            pause_flg = false;
            Drawable d = getResources().getDrawable((R.drawable.ic_action_pause));
            pauseWhilePlaying.setBackground(d);
            frameLb.setVisibility(View.GONE);
            setFPS(time);
        }
    }

    public boolean setFPS(int time) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        position();
                        Log.d("timer coin", String.valueOf(coinSpeed));
                        Log.d("timer diamond", String.valueOf(diamondSpeed));
                        Log.d("timer enemy1", String.valueOf(enemy1Speed));
                        Log.d("timer enemy2", String.valueOf(enemy2Speed));
                        Log.d("timer increase Speed", String.valueOf(increaseSpeed));
                        checkTime();
                    }
                });
            }
        }, 0, time);
        return true;
    }

    public boolean checkTime() {
        increaseSpeed += 1;
        if (increaseSpeed == 300) {
            Math.round(coinSpeed += 1);
            Math.round(diamondSpeed += 1);
            Math.round(enemy1Speed += 1);
            Math.round(enemy2Speed += 1);
            increaseSpeed = 0;
            LinearLayout rootBg = findViewById(R.id.root_id);
            rootBg.setBackgroundColor(Color.rgb(85, colorChange -= 10, 200));
        }
        if (colorChange == 100) {
            colorChange = 255;
        }
        return true;
    }

    public void exitApp(View view) {
        startActivity(new Intent(getApplicationContext(), StartActivity.class));
        finish();
    }

    //clicks the Back button twice to exit the program
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTimeExit < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(MainActivity.this, "Press again to exit this awesome game", Toast.LENGTH_SHORT).show();
                firstTimeExit = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}