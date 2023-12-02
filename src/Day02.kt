fun main() {
    data class Game(val gameId: Int, val sets: List<String>)

    fun part1(input: List<String>): Int {
        val cubesCount = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )

        return input
            .map { line ->
                val result = "Game (\\d+): (.+)".toRegex().find(line)!!
                val gameId = result.groups[1]!!.value.toInt()
                val sets = result.groups[2]!!.value.split("; ")

                Game(gameId, sets)
            }
            .filter { game ->
                game.sets
                    .flatMap { set -> set.split(", ") }
                    .all { cubesInfo ->
                        val (count, color) = cubesInfo.split(" ")
                        val maxCount = cubesCount[color]!!
                        val possible = maxCount >= count.toInt()
                        possible
                    }
            }
            .sumOf { game -> game.gameId }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line ->
                val result = "Game (\\d+): (.+)".toRegex().find(line)!!
                val gameId = result.groups[1]!!.value.toInt()
                val sets = result.groups[2]!!.value.split("; ")

                Game(gameId, sets)
            }
            .sumOf { game ->
                val maxNumbers = game.sets
                    .flatMap { set -> set.split(", ") }
                    .map { cubesInfo ->
                        val (count, color) = cubesInfo.split(" ")
                        color to count.toInt()
                    }
                    .groupBy({ it.first }, { it.second })
                    .mapValues { it.value.max() }
                maxNumbers["red"]!! * maxNumbers["green"]!! * maxNumbers["blue"]!!
            }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
