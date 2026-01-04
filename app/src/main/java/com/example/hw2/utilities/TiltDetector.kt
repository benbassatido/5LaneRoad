package com.example.hw2.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.hw2.interfaces.TiltCallback

class TiltDetector(context: Context, private val tiltCallback: TiltCallback) {

    private val sensorManager = context.getSystemService(
        Context.SENSOR_SERVICE
    ) as SensorManager

    private val sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_ACCELEROMETER
    )

    private var timestamp: Long = 0L

    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            calculateTilt(x)
        }
    }

    private fun calculateTilt(x: Float) {
        if (System.currentTimeMillis() - timestamp < 500) return
        timestamp = System.currentTimeMillis()

        if (x > 3.0) {
            tiltCallback.onTiltLeft()
        } else if (x < -3.0) {
            tiltCallback.onTiltRight()
        }
    }

    fun start() {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
