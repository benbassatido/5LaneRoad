package com.example.hw2.logic

import com.example.hw2.utilities.Timer

class GameManager {

    companion object {
        private const val ROWS = 10
        private const val COLS = 5

        private const val NORMAL_TICK_DELAY = 800L
        private const val FAST_TICK_DELAY = 400L

        private const val START_LANE = 2
        private const val MAX_LIVES = 3

        private const val COIN_TICK_INTERVAL = 10
        private const val COIN_BONUS_DISTANCE = 10

        // Board values
        const val EMPTY = 0
        const val NAIL = 1
        const val COIN = 2
    }

    var onStateChanged: (() -> Unit)? = null
    var onCrash: (() -> Unit)? = null

    // State
    val board = IntArray(ROWS * COLS) { EMPTY }
    var currentLane = START_LANE
        private set
    var lives = MAX_LIVES
        private set
    var distance = 0
        private set
    var isGameOver = false
        private set

    private var tickCount = 0
    private lateinit var timer: Timer

    private fun indexOf(row: Int, col: Int) = row * COLS + col

    fun startGame(isFastMode: Boolean) {
        val delay = if (isFastMode) FAST_TICK_DELAY else NORMAL_TICK_DELAY
        timer = Timer(delay) { gameTick() }

        isGameOver = false
        currentLane = START_LANE
        lives = MAX_LIVES
        distance = 0
        tickCount = 0

        clearBoard()
        spawnRandomTop(2, NAIL)

        notifyChange()
        timer.start()
    }

    fun moveCarLeft() {
        if (isGameOver) return
        if (currentLane > 0) {
            currentLane--
            notifyChange()
        }
    }

    fun moveCarRight() {
        if (isGameOver) return
        if (currentLane < COLS - 1) {
            currentLane++
            notifyChange()
        }
    }

    private fun gameTick() {
        if (isGameOver) return

        tickCount++
        distance++

        clearBottomRow()
        stepDown()

        if (tickCount % 2 == 0) spawnInTop(NAIL)
        if (tickCount % COIN_TICK_INTERVAL == 0) spawnInTop(COIN)

        notifyChange()
    }

    private fun clearBoard() {
        for (i in board.indices) board[i] = EMPTY
    }

    private fun clearBottomRow() {
        val row = ROWS - 1
        for (col in 0 until COLS) {
            board[indexOf(row, col)] = EMPTY
        }
    }

    private fun spawnRandomTop(count: Int, type: Int) {
        val cols = (0 until COLS).shuffled().take(count)
        for (c in cols) board[indexOf(0, c)] = type
    }

    private fun spawnInTop(type: Int) {
        val emptyCols = (0 until COLS).filter { board[indexOf(0, it)] == EMPTY }
        if (emptyCols.isNotEmpty()) {
            val col = emptyCols.random()
            board[indexOf(0, col)] = type
        }
    }

    private fun stepDown() {
        for (row in ROWS - 2 downTo 0) {
            for (col in 0 until COLS) {
                val idx = indexOf(row, col)
                val type = board[idx]
                if (type == EMPTY) continue

                val nextRow = row + 1
                val nextIdx = indexOf(nextRow, col)

                // collision with car
                if (nextRow == ROWS - 1 && col == currentLane) {
                    board[idx] = EMPTY
                    if (type == COIN) collectCoin() else hitCar()
                    continue
                }

                if (board[nextIdx] == EMPTY) {
                    board[nextIdx] = type
                    board[idx] = EMPTY
                }
            }
        }
    }

    private fun hitCar() {
        lives--

        onCrash?.invoke()

        if (lives <= 0) {
            isGameOver = true
            timer.stop()
        }
    }

    private fun collectCoin() {
        distance += COIN_BONUS_DISTANCE
    }

    private fun notifyChange() {
        onStateChanged?.invoke()
    }
}
