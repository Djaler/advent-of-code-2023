fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstNumber = line.first { it.isDigit() }.digitToInt()
            val lastNumber = line.last { it.isDigit() }.digitToInt()
            return@sumOf firstNumber * 10 + lastNumber
        }
    }

    val digitWords = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.findAnyOf(digitWords)!!
            val lastDigit = line.findLastAnyOf(digitWords)!!

            val firstNumber = if (firstDigit.second.length == 1) {
                firstDigit.second.toInt()
            } else {
                digitWords.indexOf(firstDigit.second) + 1
            }
            val lastNumber = if (lastDigit.second.length == 1) {
                lastDigit.second.toInt()
            } else {
                digitWords.indexOf(lastDigit.second) + 1
            }

            return@sumOf firstNumber * 10 + lastNumber
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
