package com.example.gameandroid.Sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.gameandroid.R;
import com.example.gameandroid.Views.Home;

import java.util.ArrayList;
import java.util.List;

public class SoundPlayer {
    Boolean sound;
    SharedPreferences preferences;
    private static SoundPool soundPool;
    private static int explosionSound, explosionSound2;
    private static int bulletSound, enemyFire;
    List<Integer> streams = new ArrayList<Integer>();

    public SoundPlayer(Context context) {
        preferences = context.getSharedPreferences(Home.GAME_SETTINGS, Context.MODE_PRIVATE);
        sound = preferences.getBoolean("sound", true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        explosionSound = soundPool.load(context, R.raw.explosion, 2);
        explosionSound2 = soundPool.load(context, R.raw.tieng_dan_ban_vao_tuong, 1);
        bulletSound = soundPool.load(context, R.raw.tieng_sung, 1);
        enemyFire = soundPool.load(context, R.raw.enemylv3_fire, 1);
    }

    public void playExplosionSound() {
        if (sound) {
            int streamID = soundPool.play(explosionSound, 1.0f, 1.0f, 1, 0, 1);
            streams.add(streamID);
        }
    }

    public void playBulletSound() {
        if (sound) {
            int streamID = soundPool.play(bulletSound, 1.0f, 1.0f, 1, 0, 1);
            streams.add(streamID);
        }
    }

    public void playEnemyFireSound() {
        if (sound) {
            int streamID = soundPool.play(enemyFire, 1.0f, 1.0f, 1, 0, 1);
            streams.add(streamID);
        }
    }

    public void playExplosionSound2() {
        if (sound) {
            int streamID = soundPool.play(explosionSound2, 1.0f, 1.0f, 1, 0, 1);
            streams.add(streamID);
        }
    }

    public void setSoundPause() {
        for (Integer stream : streams) {
            soundPool.pause(stream);
        }
    }


    public void setSoundStop() {
        for (Integer stream : streams) {
            soundPool.stop(stream);
        }
        streams.clear();
    }
}
