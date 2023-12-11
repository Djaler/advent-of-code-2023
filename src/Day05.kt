fun main() {
    fun parseSourceDestinationMappings(input: List<String>): List<List<Pair<LongRange, LongRange>>> {
        val sourceAndDestinationMappings = mutableListOf<MutableList<Pair<LongRange, LongRange>>>()

        for (line in input.subList(2, input.size)) {
            if (line.endsWith("map:")) {
                sourceAndDestinationMappings.add(mutableListOf())
                continue
            }
            if (line.isNotEmpty()) {
                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.split(' ').map { it.toLong() }

                val sourceRange = sourceRangeStart..<(sourceRangeStart + rangeLength)
                val destinationRange = destinationRangeStart..<(destinationRangeStart + rangeLength)

                sourceAndDestinationMappings.last().add(sourceRange to destinationRange)
            }
        }

        return sourceAndDestinationMappings
    }

    fun findMinSeedLocation(
        seeds: Sequence<Long>,
        sourceAndDestinationMappings: List<List<Pair<LongRange, LongRange>>>
    ): Long {
        return seeds
            .map { seed ->
                var currentValue = seed
                for (mappings in sourceAndDestinationMappings) {
                    mappings
                        .firstOrNull { (sourceRange, destinationRange) -> currentValue in sourceRange }
                        ?.let { (sourceRange, destinationRange) ->
                            currentValue = currentValue - sourceRange.first + destinationRange.first
                        }
                }
                return@map currentValue
            }.min()
    }

    fun part1(input: List<String>): Long {
        val seeds = input.first().removePrefix("seeds: ").splitToSequence(' ').map { it.toLong() }

        val sourceAndDestinationMappings = parseSourceDestinationMappings(input)

        return findMinSeedLocation(seeds, sourceAndDestinationMappings)
    }

    fun part2(input: List<String>): Long {
        val seeds = Regex("(\\d+) (\\d+)").findAll(input.first())
            .map { result ->
                val rangeStart = result.groupValues[1].toLong()
                val rangeLength = result.groupValues[2].toLong()
                rangeStart..<(rangeStart + rangeLength)
            }
            .flatMap { it }

        val sourceAndDestinationMappings = parseSourceDestinationMappings(input)

        return findMinSeedLocation(seeds, sourceAndDestinationMappings)
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
