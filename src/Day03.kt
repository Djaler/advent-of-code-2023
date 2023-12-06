fun main() {
    fun isSymbol(char: Char): Boolean {
        return !char.isDigit() && char != '.'
    }

    fun neighboursCoordinates(matrix: List<String>, currentRow: Int, currentCol: Int): List<Pair<Int, Int>> {
        return listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1, 0 to 1,
            1 to -1, 1 to 0, 1 to 1
        )
            .map { (rowShift, colShift) -> currentRow + rowShift to currentCol + colShift }
            .filter { (row, col) -> row in matrix.indices && col in matrix[row].indices }
    }

    fun isAdjacentToSymbol(matrix: List<String>, currentRow: Int, currentCol: Int): Boolean {
        return neighboursCoordinates(matrix, currentRow, currentCol)
            .map { (row, col) -> matrix[row][col] }
            .any { isSymbol(it) }
    }

    fun walkNumbers(input: List<String>, onDigit: (Char, Int, Int) -> Unit, onNumber: (Int) -> Unit) {
        for ((rowIndex, row) in input.withIndex()) {
            var number: Int? = null
            for ((colIndex, char) in row.withIndex()) {
                if (char.isDigit()) {
                    if (number == null) {
                        number = char.digitToInt()
                    } else {
                        number = number * 10 + char.digitToInt()
                    }

                    onDigit(char, rowIndex, colIndex)
                } else if (number != null) {
                    onNumber(number)
                    number = null
                }
            }

            if (number != null) {
                onNumber(number)
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        var numberAdjacentToSymbol = false

        walkNumbers(
            input,
            onDigit = { _, row, col ->
                if (!numberAdjacentToSymbol) {
                    if (isAdjacentToSymbol(input, row, col)) {
                        numberAdjacentToSymbol = true
                    }
                }
            },
            onNumber = { number ->
                if (numberAdjacentToSymbol) {
                    sum += number
                }
                numberAdjacentToSymbol = false
            }
        )

        return sum
    }

    data class Gear(val row: Int, val col: Int)

    fun findAdjacentGears(matrix: List<String>, currentRow: Int, currentCol: Int): Set<Gear> {
        return neighboursCoordinates(matrix, currentRow, currentCol)
            .mapNotNull { (row, col) ->
                if (matrix[row][col] == '*') {
                    Gear(row, col)
                } else {
                    null
                }
            }
            .toSet()
    }

    fun part2(input: List<String>): Int {
        val gears = mutableMapOf<Gear, MutableSet<Int>>()
        val adjacentGears = mutableSetOf<Gear>()

        walkNumbers(
            input,
            onDigit = { _, row, col ->
                adjacentGears.addAll(findAdjacentGears(input, row, col))
            },
            onNumber = { number ->
                for (gear in adjacentGears) {
                    gears.getOrPut(gear) { mutableSetOf() }
                        .add(number)
                }
                adjacentGears.clear()
            }
        )

        return gears.values
            .filter { it.size == 2 }
            .sumOf { it.max() * it.min() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
