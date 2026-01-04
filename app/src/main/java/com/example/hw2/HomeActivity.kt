package com.example.hw2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonModeButton: Button = findViewById(R.id.home_BTN_button_mode)
        val sensorModeButton: Button = findViewById(R.id.home_BTN_sensor_mode)
        val topTenButton: Button = findViewById(R.id.home_BTN_top_ten)
        val fastModeSwitch: SwitchMaterial = findViewById(R.id.home_SW_fast_mode)

        fastModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            fastModeSwitch.text = if (isChecked) "On" else "Fast"
        }

        buttonModeButton.setOnClickListener {
            startGame(isSensorMode = false, isFastMode = fastModeSwitch.isChecked)
        }

        sensorModeButton.setOnClickListener {
            startGame(isSensorMode = true, isFastMode = fastModeSwitch.isChecked)
        }

        topTenButton.setOnClickListener {
            val intent = Intent(this, ScoresActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGame(isSensorMode: Boolean, isFastMode: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("IS_SENSOR_MODE", isSensorMode)
            putExtra("IS_FAST_MODE", isFastMode)
        }
        startActivity(intent)
    }
}
