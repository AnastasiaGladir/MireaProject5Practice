package ru.mirea.gladirap.mireaproject;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import ru.mirea.gladirap.mireaproject.databinding.ActivityMainBinding;

public class Accelerometer_activity extends Activity implements SensorEventListener {

    private ActivityMainBinding binding;
    private TextView magnitX;
    private TextView magnitY;
    private TextView magnitZ;
    private SensorManager sensorManager;

    private Sensor accelerometerSensor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        magnitX = findViewById(R.id.textViewAzimuth);
        magnitY = findViewById(R.id.textViewPitch);
        magnitZ = findViewById(R.id.textViewRoll);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float valueX = event.values[0];
            float valueY = event.values[1];
            float valueZ = event.values[2];
            magnitX.setText("X: " + valueX);
            magnitY.setText("Y : " + valueY);
            magnitZ.setText("Z : " + valueZ);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
