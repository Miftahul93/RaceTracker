package com.athaya.racetracker.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * This class represents a state holder for race participant
 */

class RaceParticipants(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMills: Long = 500L,
    private val progressIncrement: Int = 1,
    private val initialProgress: Int = 0
) {
    init {
        require(maxProgress > 0) {
            "maxProgress=$maxProgress; must be > 0"
        }
        require(progressIncrement > 0) {
            "progressIncrement=$progressIncrement; must be > 0"
        }
    }

    /**
     * indicate the race participant's current progress
     */
    var currentProgress by mutableStateOf(initialProgress)
        private set

    /**
     * update the value of [currentProgress] by value progress [progressIncrement] until it reaches
     * [maxProgress], there is a delay of [progressDelayMills] between each update.
     */
    suspend fun run() {
        while (currentProgress < maxProgress) {
            delay(progressDelayMills)
            currentProgress += progressIncrement

            /**
             * Untuk mengetahui cara membatalkan coroutine saat pengguna mengklik tombol Reset,
             */
      /*  try {
            while (currentProgress < maxProgress) {
                delay(progressDelayMills)
                currentProgress += progressIncrement
            }
        } catch (e: CancellationException) {
            Log.e("RaceParticipant", "$name: ${e.message}")
            throw e // Always re-throw CancellationException.*/
        }
    }

    /**
     * Regardless of the value of [initialProgress] the reset function will reset the [currentProgress] to 0
     */
    fun reset() {
        currentProgress = 0
    }
}

val RaceParticipants.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()