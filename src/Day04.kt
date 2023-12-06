import kotlin.math.pow

fun main() {
    data class Card(val cardNumber: String, val winningNumbers: Set<String>, val numbers: List<String>) {
        val winningCount = numbers.count { it in winningNumbers }
    }

    fun parseCards(input: List<String>): List<Card> {
        return input.map { line ->
            val matchResult = Regex("Card\\s+(\\d+): (.+) \\| (.+)").find(line)!!
            val cardNumber = matchResult.groupValues[1]
            val winningNumbers = matchResult.groupValues[2].split(Regex("\\s+")).toSet()
            val numbers = matchResult.groupValues[3].split(Regex("\\s+"))

            Card(cardNumber, winningNumbers, numbers)
        }
    }

    fun part1(input: List<String>): Int {
        return parseCards(input)
            .sumOf { card ->
                when (card.winningCount) {
                    0 -> 0
                    else -> 2.0.pow(card.winningCount - 1).toInt()
                }
            }
    }

    fun part2(input: List<String>): Int {
        val cards = parseCards(input).toMutableList()

        val cardsNumberToCount = cards.associate { card -> card.cardNumber to 1 }.toMutableMap()

        cards.forEachIndexed { index, card ->
            val cardsCount = cardsNumberToCount[card.cardNumber]!!
            val winningCount = card.winningCount
            val nextCards = cards.slice(index + 1..(index + winningCount))
            nextCards.forEach {
                cardsNumberToCount[it.cardNumber] = cardsNumberToCount[it.cardNumber]!! + cardsCount
            }
        }

        return cardsNumberToCount.values.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
