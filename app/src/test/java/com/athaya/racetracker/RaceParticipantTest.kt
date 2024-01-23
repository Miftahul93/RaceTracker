package com.athaya.racetracker

import com.athaya.racetracker.ui.RaceParticipants
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RaceParticipantTest {
    private val raceParticipant = RaceParticipants(
        name = "Test",
        maxProgress = 100,
        progressDelayMills = 500L,
        initialProgress = 0,
        progressIncrement = 1
    )

    @Test
    fun raceParticipant_RaceStarted_ProgressUpdated() = runTest {
        val expectedProgress = 1
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.progressDelayMills)
        runCurrent()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_RaceFinished_ProgressUpdated() = runTest {
        launch { raceParticipant.run() }

        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMills)
        runCurrent()
        assertEquals(100, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_RaePaused_ProgressUpdated() = runTest {
        val expectedProgress = 5
        val racerJob = launch { raceParticipant.run() }
        advanceTimeBy(expectedProgress * raceParticipant.progressDelayMills)
        runCurrent()
        racerJob.cancelAndJoin()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_RacePausedAndResumed_ProgressUpdated() = runTest {
        val expectedProgress = 5

        repeat(2) {
            val racerJob = launch { raceParticipant.run() }
            advanceTimeBy(expectedProgress * raceParticipant.progressDelayMills)
            runCurrent()
            racerJob.cancelAndJoin()
        }
        assertEquals(expectedProgress * 2, raceParticipant.currentProgress)
    }

    @Test(expected = IllegalArgumentException::class)
    fun raceParticipant_ProgressIncrementZero_ExceptionThrown() = runTest {
        RaceParticipants(name = "Progress Test", progressIncrement = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun raceParticipant_MaxProgressZero_ExceptionThrown() {
        RaceParticipants(name = "Progress Test", maxProgress = 0)
    }
}