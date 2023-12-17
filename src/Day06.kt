fun main() {
    fun part1(input: List<String>): Int {
        val times = input.first().removePrefix("Time:").split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
        val distances = input.last().removePrefix("Distance:").split(' ').filter { it.isNotEmpty() }.map { it.toInt() }

        return times.zip(distances)
            .map { (raceTime, distanceRecord) ->
                val numberOfWays = (1..<raceTime)
                    .map { holdTime ->
                        val moveTime = raceTime - holdTime
                        val speed = holdTime
                        moveTime * speed
                    }
                    .count { it > distanceRecord }
                numberOfWays
            }
            .reduce { acc, current -> acc * current }
    }

    fun part2(input: List<String>): Int {
        val raceTime = input.first().filter { it.isDigit() }.toLong()
        val distanceRecord = input.last().filter { it.isDigit() }.toLong()

        val numberOfWays = (1..<raceTime)
            .map { holdTime ->
                val moveTime = raceTime - holdTime
                val speed = holdTime
                moveTime * speed
            }
            .count { it > distanceRecord }

        return numberOfWays
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
