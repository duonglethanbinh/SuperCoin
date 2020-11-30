package com.project.superCoin;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundEffects {
    private static int collectSound;
    private static int loseSound;
    private static SoundPool sound;
    final int MAX = 2;

    public SoundEffects(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audio = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            sound = new SoundPool.Builder()
                    .setAudioAttributes(audio)
                    .setMaxStreams(MAX)
                    .build();
        } else {
            sound = new SoundPool(MAX, AudioManager.STREAM_MUSIC, 0);
        }

        collectSound = sound.load(context, R.raw.collected, 1);
        loseSound = sound.load(context, R.raw.loser, 1);
    }

    public void collectSound() {
        sound.play(collectSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void loseSound() {
        sound.play(loseSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
