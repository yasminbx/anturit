import kotlin.random.Random

fun guessedNumbers(s: String?): List<Int>? {
    return s?.split(",")
        ?.mapNotNull { it.trim().toIntOrNull() }
        ?.takeIf { it.size == 7 }
}

fun isLegalGuess(guess: List<Int>?): Boolean {
    return guess?.let {
        it.size == 7 && it.distinct().size == 7 && it.all { num -> num in 1..40 }
    } ?: false
}

fun equalsCount(a: List<Int>?, b: List<Int>?): Int {
    return a?.intersect(b?.toSet() ?: emptySet())?.size ?: 0
}

fun generateLottoNumbers(): List<Int> {
    return (1..40).shuffled().take(7)
}

fun main() {
    val lottoNumbers = generateLottoNumbers()
    println("Lotto numerot on julkaistu. Arvaa numerot")

    while (true) {
        print("Syötä arvaus: ")
        val input = readlnOrNull()

        if (input == "Lopeta") return

        val guessed = guessedNumbers(input)
        if (!isLegalGuess(guessed)) {
            println("Virheellinen arvaus.Syötä numero 1..40 välistä.")
            continue
        }

        val matches = equalsCount(guessed, lottoNumbers)
        println("Oikein $matches numerot.")

        if (matches == 7) {
            println("Hyvä! Arvasint numerot oikein!")
            return
        }
    }
}
