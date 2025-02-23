package com.example.musicplayerapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewSong);

        // Request storage permissions
        requestPermissions();
    }

    public void requestPermissions() {
        // Check for Android version & request the correct permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33)
            Dexter.withContext(this).withPermission(Manifest.permission.READ_MEDIA_AUDIO).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    displaySongs();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
        } else { // Android 12 and below
            Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    displaySongs();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
        }
    }

    // Use MediaStore to get all audio files
    public ArrayList<File> getAllAudioFiles() {
        ArrayList<File> audioList = new ArrayList<>();
        String[] projection = { MediaStore.Audio.Media.DATA };

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(0);
                File file = new File(filePath);
                if (file.exists() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav"))) {
                    audioList.add(file);
                }
            }
            cursor.close();
        }
        return audioList;
    }

    // Display found songs in ListView
    void displaySongs() {
        final ArrayList<File> mySongs = getAllAudioFiles();

        if (mySongs.isEmpty()) {
            Toast.makeText(this, "No songs found on device", Toast.LENGTH_SHORT).show();
            return;
        }

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, items);
        listView.setAdapter(myAdapter);
    }
}
