package com.weis.videoxie.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ShakeUtils implements SensorEventListener {
    public ShakeUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }

    public void onResume() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            //这里可以调节摇一摇的灵敏度
            if ((Math.abs(values[0]) > SENSOR_VALUE || Math.abs(values[1]) > SENSOR_VALUE || Math.abs(values[2]) > SENSOR_VALUE)) {
                counter++;
                if (counter == 3) {
                    System.out.println("sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
                    if (null != mOnShakeListener) {
                        if (vibrator.hasVibrator())
                            vibrator.vibrate(500);
                        mOnShakeListener.onShake();
                    }
                    counter = 0;
                }
            }
        }
    }

    public interface OnShakeListener {
        void onShake();
    }

    private int counter = 0;
    private Vibrator vibrator = null;
    private SensorManager mSensorManager = null;
    private OnShakeListener mOnShakeListener = null;
    private static final int SENSOR_VALUE = 14;
}