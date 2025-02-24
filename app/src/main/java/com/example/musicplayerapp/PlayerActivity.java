package com.example.musicplayerapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnnext, btnprev, btnff, btnfr, btnplay;
    TextView txtsname, txtsstart, txtsstop;
    SeekBar seekmusic;
    ImageView imageView;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    Handler seekbarHandler = new Handler();
    Runnable updateSeekBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Now Playing");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.playbtn);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        txtsname = findViewById(R.id.txtsongname);
        txtsstart = findViewById(R.id.txtsstart);
        txtsstop = findViewById(R.id.txtsstop);
        seekmusic = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.imageview);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        ArrayList<String> songPaths = bundle != null ? bundle.getStringArrayList("songs") : null;
        position = bundle != null ? bundle.getInt("pos", 0) : 0;

        if (songPaths == null || songPaths.isEmpty()) {
            Toast.makeText(this, "No songs received!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity to prevent crash
            return;
        }

// Convert String paths back to File objects
        mySongs = new ArrayList<>();
        for (String path : songPaths) {
            File songFile = new File(path);
            if (songFile.exists()) {
                mySongs.add(songFile);
            }
        }


        txtsname.setSelected(true);
        playSong(position);

        // Update SeekBar with Handler
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekmusic.setProgress(currentPosition);
                    txtsstart.setText(createTime(currentPosition)); // ✅ Update current time
                    seekbarHandler.postDelayed(this, 500);
                }
            }
        };

        seekbarHandler.postDelayed(updateSeekBar, 500);

        // Apply Color to SeekBar
        seekmusic.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY
        );
        seekmusic.getThumb().setColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
        );

        // SeekBar Change Listener
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btnplay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                btnplay.post(() -> btnplay.setForeground(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_play)));
                mediaPlayer.pause();
            } else {
                btnplay.post(() -> btnplay.setForeground(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_pause)));
                mediaPlayer.start();
            }
        });

        btnnext.setOnClickListener(v -> changeSong(1));
        btnprev.setOnClickListener(v -> changeSong(-1));

        mediaPlayer.setOnCompletionListener(mp -> btnnext.performClick());

        btnff.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            }
        });

        btnfr.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }
        });
    }

    private void playSong(int pos) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Uri uri = Uri.parse(mySongs.get(pos).toString());
        sname = mySongs.get(pos).getName();
        txtsname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        seekmusic.setMax(mediaPlayer.getDuration());
        seekmusic.setProgress(0);

        String endTime = createTime(mediaPlayer.getDuration());
        txtsstop.setText(endTime);
        txtsstart.setText("0:00"); // ✅ Reset current time to 0:00 at the start

        seekbarHandler.postDelayed(updateSeekBar, 500);

        btnplay.post(() -> btnplay.setForeground(ContextCompat.getDrawable(this, R.drawable.ic_pause)));
        startAnimation(imageView);
    }

    private void changeSong(int direction) {
        position = (position + direction) % mySongs.size();
        if (position < 0) {
            position = mySongs.size() - 1;
        }
        playSong(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        seekbarHandler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration) {
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        return min + ":" + (sec < 10 ? "0" : "") + sec;
    }
}
