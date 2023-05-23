package ru.mirea.gladirap.mireaproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;


public class Camera_activity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private final String TAG = getClass().getSimpleName();
    private boolean isWork = false;
    private Uri imageUri;

    private ImageView photo;
    private Button back;
    private Button photo_button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        photo = findViewById(R.id.imageView);
        photo_button = findViewById(R.id.battonPhoto);
        back = findViewById(R.id.battonBack);

        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    photo.setImageURI(imageUri);
                }
            }
        };

        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);


        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(isWork){
                    Log.d(TAG, "on Click btn1");
                    try {
                        File photoFile = createImageFile();
                        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                        imageUri = FileProvider.getUriForFile(Camera_activity.this, authorities, photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraActivityResultLauncher.launch(cameraIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "back click");
                Intent intent = new Intent(Camera_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {

            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}