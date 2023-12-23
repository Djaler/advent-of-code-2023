fun main() {
    fun <T> Sequence<T>.repeatForever() = generateSequence(this) { it }.flatten()

    fun String.toInstructionsSequence(): Sequence<String> {
        return this.splitToSequence("").filter { it.isNotEmpty() }.repeatForever()
    }

    fun Sequence<String>.toNodesMap(): Map<String, Pair<String, String>> {
        val regex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")

        return this
            .map { line ->
                val result = regex.find(line)!!

                val (_, from, left, right) = result.groupValues
                from to (left to right)
            }
            .toMap()
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first().toInstructionsSequence()
        val nodesMap = input.asSequence().drop(2).toNodesMap()

        var currentNode = "AAA"
        for ((step, instruction) in instructions.withIndex()) {
            val (left, right) = nodesMap[currentNode]!!

            currentNode = when (instruction) {
                "L" -> left
                else -> right
            }

            if (currentNode == "ZZZ") {
                return step + 1
            }
        }

        throw RuntimeException("Result not found")
    }

    fun gcd(a: Long, b: Long): Long {
        var num1 = a
        var num2 = b
        while (num2 != 0L) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        return num1
    }

    fun lcm(a: Long, b: Long): Long {
        return a / gcd(a, b) * b
    }

    fun lcm(numbers: List<Long>): Long {
        return numbers.reduce { acc, cur -> lcm(acc, cur) }
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first().toInstructionsSequence()
        val nodesMap = input.asSequence().drop(2).toNodesMap()

        val startNodes = nodesMap.keys.filter { it.endsWith('A') }

        val stepsToEnd = startNodes.map {
            var currentNode = it
            for ((step, instruction) in instructions.withIndex()) {
                val (left, right) = nodesMap[currentNode]!!

                currentNode = when (instruction) {
                    "L" -> left
                    else -> right
                }

                if (currentNode.endsWith('Z')) {
                    return@map step + 1
                }
            }

            throw RuntimeException("Result not found")
        }

        return lcm(stepsToEnd.map { it.toLong() })
    }

    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput1) == 6)
    check(part2(testInput2) == 6L)
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
