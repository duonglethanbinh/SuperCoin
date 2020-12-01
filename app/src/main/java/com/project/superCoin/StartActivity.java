package com.project.superCoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    ImageButton play, more;
    private long firstTimeExit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //make fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        play = findViewById(R.id.play);
        more = findViewById(R.id.more);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(StartActivity.this, more);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Intent intent, chooser;
                        int id = menuItem.getItemId();
                        if (id == R.id.feedback) {
                            intent = new Intent(Intent.ACTION_SEND);
                            intent.setData(Uri.parse("mailto:"));

                            String[] to = {"17521305@gm.uit.edu.vn;17520279@gm.uit.edu.vn"};
                            intent.putExtra(Intent.EXTRA_EMAIL, to);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about SuperCoin");
                            intent.putExtra(Intent.EXTRA_TEXT, "I feel ...");

                            //need this to prompts email client only
                            intent.setType("message/rfc822");

                            startActivity(Intent.createChooser(intent, "Send Feedback by using: "));
                        }

                        if (id == R.id.share) {
                            intent = new Intent(Intent.ACTION_SEND);

                            String sAux = "\nLet me recommend you this Game:\n\nClick on this link: https://uit.edu.vn\n\n";
                            intent.putExtra(Intent.EXTRA_SUBJECT, "SuperCoin Share");
                            intent.putExtra(Intent.EXTRA_TEXT, sAux);

                            intent.setType("text/plain");

                            startActivity(Intent.createChooser(intent, "Share by using: "));
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    //clicks the Back button twice to exit the program
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTimeExit < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(StartActivity.this, "Press again to exit this awesome game", Toast.LENGTH_SHORT).show();
                firstTimeExit = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}