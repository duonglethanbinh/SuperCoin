package com.project.superCoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    int hightScore;
    private long firstTimeExit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //make fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView hightScoreLabel = findViewById(R.id.hightScoreLabel);
        TextView gamesPlayedLabel = findViewById(R.id.gamesPlayedLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText("" + score);

        SharedPreferences preferencesScore = getSharedPreferences("HIGHTSCORE", Context.MODE_PRIVATE);
        hightScore = preferencesScore.getInt("HIGHTSCORE", 0);

        if (score > hightScore) {
            hightScoreLabel.setText("Hight Score: " + score);

            SharedPreferences.Editor editor = preferencesScore.edit();
            editor.putInt("HIGHTSCORE", score);
            editor.commit();
        } else {
            hightScoreLabel.setText("Hight Score: " + hightScore);
        }

        SharedPreferences preferencesGames = getSharedPreferences("GAMES", Context.MODE_PRIVATE);
        int games = preferencesGames.getInt("GAMES", 0);

        gamesPlayedLabel.setText("Attempts: " + (games + 1));

        SharedPreferences.Editor editor = preferencesGames.edit();
        editor.putInt("GAMES", (games + 1));
        editor.commit();
    }

    public void tryAgain(View view) {
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
                Toast.makeText(ResultActivity.this, "Press again to exit this awesome game", Toast.LENGTH_SHORT).show();
                firstTimeExit = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}