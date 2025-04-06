package com.example.threads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.threads.ui.theme.ThreadsTheme
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import java.util.concurrent.ThreadLocalRandom
class Lotto {

    private val lottoNumbers: MutableSet<Int> = mutableSetOf()
    private val stats: IntArray = IntArray(8) { 0 }

    init {
        generateLottoNumbers()
    }

    private fun generateLottoNumbers() {
        // Generate 7 distinct random numbers from 1 to 40
        val random = ThreadLocalRandom.current()
        while (lottoNumbers.size < 7) {
            lottoNumbers.add(random.nextInt(1, 41))
        }
    }

    @Synchronized
    fun check(numbers: List<Int>) {
        val correctGuesses = numbers.count { lottoNumbers.contains(it) }
        stats[correctGuesses]++
    }

    fun getStats(): IntArray {
        return stats
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val totalGuesses = 13_500_000
            val numThreads = Runtime.getRuntime().availableProcessors()
            val lotto = Lotto()

            val startTime = System.currentTimeMillis()

            val threads = mutableListOf<Thread>()
            val guessesPerThread = AtomicInteger(totalGuesses / numThreads)

            for (i in 0 until numThreads) {
                threads.add(thread(start = true) {
                    val threadLotto = Lotto()
                    repeat(guessesPerThread.get()) {
                        val guess = generateRandomGuess()
                        threadLotto.check(guess)
                    }
                })
            }


            threads.forEach { it.join() }

            val endTime = System.currentTimeMillis()
            val runningTime = endTime - startTime


            println("All joined")
            println("Running time $runningTime")


            val totalGuessesMade = lotto.getStats().sum()
            println("Checksum: $totalGuessesMade should be $totalGuesses")


            for (i in 0..7) {
                println("$i: ${lotto.getStats()[i]}")
            }
        }

        fun generateRandomGuess(): List<Int> {
            val guessSet = mutableSetOf<Int>()
            val random = ThreadLocalRandom.current()
            while (guessSet.size < 7) {
                guessSet.add(random.nextInt(1, 41))
            }
            return guessSet.toList()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

