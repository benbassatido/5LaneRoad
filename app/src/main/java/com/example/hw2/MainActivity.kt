package com.example.hw2

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.interfaces.TiltCallback
import com.example.hw2.logic.GameManager
import com.example.hw2.utilities.ScoreManager
import com.example.hw2.utilities.SingleSoundPlayer
import com.example.hw2.utilities.TiltDetector

class MainActivity : AppCompatActivity(), TiltCallback {

    private lateinit var gameManager: GameManager
    private var tiltDetector: TiltDetector? = null
    private lateinit var scoreManager: ScoreManager
    private lateinit var soundPlayer: SingleSoundPlayer

    private lateinit var laneCars: Array<ImageView>
    private lateinit var nails: Array<ImageView>
    private lateinit var hearts: Array<ImageView>
    private lateinit var distanceLabel: TextView

    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton

    private var isFastMode = false
    private var isSensorMode = false

    private var didShowGameOverDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isFastMode = intent.getBooleanExtra("IS_FAST_MODE", false)
        isSensorMode = intent.getBooleanExtra("IS_SENSOR_MODE", false)

        scoreManager = ScoreManager(this)
        soundPlayer = SingleSoundPlayer(this)

        initViews()
        initGame()

        if (isSensorMode) {
            tiltDetector = TiltDetector(this, this)
            btnLeft.visibility = View.INVISIBLE
            btnRight.visibility = View.INVISIBLE
        } else {
            btnLeft.setOnClickListener { gameManager.moveCarLeft() }
            btnRight.setOnClickListener { gameManager.moveCarRight() }
        }

        gameManager.startGame(isFastMode)
    }

    override fun onResume() {
        super.onResume()
        tiltDetector?.start()
        gameManager.resumeGame(isFastMode)
    }

    override fun onPause() {
        super.onPause()
        tiltDetector?.stop()
        gameManager.pauseGame()
    }

    private fun initViews() {
        laneCars = arrayOf(
            findViewById(R.id.main_IMG_car0),
            findViewById(R.id.main_IMG_car1),
            findViewById(R.id.main_IMG_car2),
            findViewById(R.id.main_IMG_car3),
            findViewById(R.id.main_IMG_car4)
        )

        nails = arrayOf(
            findViewById(R.id.main_IMG_nail1),
            findViewById(R.id.main_IMG_nail2),
            findViewById(R.id.main_IMG_nail3),
            findViewById(R.id.main_IMG_nail4),
            findViewById(R.id.main_IMG_nail5),
            findViewById(R.id.main_IMG_nail6),
            findViewById(R.id.main_IMG_nail7),
            findViewById(R.id.main_IMG_nail8),
            findViewById(R.id.main_IMG_nail9),
            findViewById(R.id.main_IMG_nail10),
            findViewById(R.id.main_IMG_nail11),
            findViewById(R.id.main_IMG_nail12),
            findViewById(R.id.main_IMG_nail13),
            findViewById(R.id.main_IMG_nail14),
            findViewById(R.id.main_IMG_nail15),
            findViewById(R.id.main_IMG_nail16),
            findViewById(R.id.main_IMG_nail17),
            findViewById(R.id.main_IMG_nail18),
            findViewById(R.id.main_IMG_nail19),
            findViewById(R.id.main_IMG_nail20),
            findViewById(R.id.main_IMG_nail21),
            findViewById(R.id.main_IMG_nail22),
            findViewById(R.id.main_IMG_nail23),
            findViewById(R.id.main_IMG_nail24),
            findViewById(R.id.main_IMG_nail25),
            findViewById(R.id.main_IMG_nail26),
            findViewById(R.id.main_IMG_nail27),
            findViewById(R.id.main_IMG_nail28),
            findViewById(R.id.main_IMG_nail29),
            findViewById(R.id.main_IMG_nail30),
            findViewById(R.id.main_IMG_nail31),
            findViewById(R.id.main_IMG_nail32),
            findViewById(R.id.main_IMG_nail33),
            findViewById(R.id.main_IMG_nail34),
            findViewById(R.id.main_IMG_nail35),
            findViewById(R.id.main_IMG_nail36),
            findViewById(R.id.main_IMG_nail37),
            findViewById(R.id.main_IMG_nail38),
            findViewById(R.id.main_IMG_nail39),
            findViewById(R.id.main_IMG_nail40),
            findViewById(R.id.main_IMG_nail41),
            findViewById(R.id.main_IMG_nail42),
            findViewById(R.id.main_IMG_nail43),
            findViewById(R.id.main_IMG_nail44),
            findViewById(R.id.main_IMG_nail45),
            findViewById(R.id.main_IMG_nail46),
            findViewById(R.id.main_IMG_nail47),
            findViewById(R.id.main_IMG_nail48),
            findViewById(R.id.main_IMG_nail49),
            findViewById(R.id.main_IMG_nail50)
        )

        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        distanceLabel = findViewById(R.id.main_LBL_distance)

        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)
    }

    private fun initGame() {
        gameManager = GameManager()
        didShowGameOverDialog = false

        gameManager.onStateChanged = { runOnUiThread { refreshUI() } }

        gameManager.onCrash = {
            runOnUiThread {
                Toast.makeText(this, "Crash!", Toast.LENGTH_SHORT).show()
                soundPlayer.playSound(R.raw.boom)
            }
        }
    }

    private fun refreshUI() {

        // Distance
        distanceLabel.text = "${gameManager.distance}m"

        // Car
        for (i in laneCars.indices) {
            laneCars[i].visibility =
                if (i == gameManager.currentLane) View.VISIBLE else View.INVISIBLE
        }

        // Hearts
        for (i in hearts.indices) {
            val index = hearts.size - 1 - i
            hearts[index].visibility =
                if (i < gameManager.lives) View.VISIBLE else View.INVISIBLE
        }

        // Board
        for (i in gameManager.board.indices) {
            when (gameManager.board[i]) {
                GameManager.EMPTY -> {
                    nails[i].visibility = View.INVISIBLE
                }
                GameManager.NAIL -> {
                    nails[i].visibility = View.VISIBLE
                    nails[i].setImageResource(R.drawable.spike_nails)
                }
                GameManager.COIN -> {
                    nails[i].visibility = View.VISIBLE
                    nails[i].setImageResource(R.drawable.coin)
                }
            }
        }

        if (gameManager.isGameOver && !didShowGameOverDialog) {
            didShowGameOverDialog = true
            showSaveScoreDialog(gameManager.distance)
        }
    }

    private fun showSaveScoreDialog(finalScore: Int) {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Game Over!")
            .setMessage("Enter your name to save your score of $finalScore")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotBlank()) {
                    scoreManager.saveScoreWithCurrentLocation(
                        name = name,
                        points = finalScore
                    ) { finish() }
                } else finish()
            }
            .setNegativeButton("Cancel") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onTiltLeft() {
        gameManager.moveCarLeft()
    }

    override fun onTiltRight() {
        gameManager.moveCarRight()
    }
}
