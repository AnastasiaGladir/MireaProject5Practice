package ru.mirea.gladirap.mireaproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class Audio_activity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork = false;
    private Button recordButton = null;
    private Button playButton = null;
    private String recordFilePath = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        recordButton = findViewById(R.id.buttonRecord);
        playButton = findViewById(R.id.buttonPlay);
        playButton.setEnabled(false);
        recordFilePath = (new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/audioRecordTest.3gp")).getAbsolutePath();


        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStartRecording) {
                    recordButton.setText("Stop");
                    playButton.setEnabled(false);
                    startRecording();
                } else {
                    recordButton.setText("Start");
                    playButton.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStartPlaying) {
                    playButton.setText("Stop");
                    recordButton.setEnabled(false);
                    startPlaying();
                } else {
                    playButton.setText("Resume");
                    recordButton.setEnabled(true);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isWork ) finish();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "Playing failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Recording failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}