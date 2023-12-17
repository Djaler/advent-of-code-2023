enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

fun main() {
    data class HandBid(val hand: String, val bid: Int, val handType: HandType)

    fun handComparator(cardsOrdering: List<Char>): Comparator<HandBid> {
        return Comparator.comparing<HandBid, HandType> { it.handType }
            .thenComparing { hand1, hand2 ->
                hand1.hand.zip(hand2.hand).forEach { (card1, card2) ->
                    val cardComparing = cardsOrdering.indexOf(card1).compareTo(cardsOrdering.indexOf(card2))
                    if (cardComparing != 0) {
                        return@thenComparing cardComparing
                    }
                }
                return@thenComparing 0
            }
    }

    fun part1(input: List<String>): Int {
        val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')

        fun getHandType(hand: String): HandType {
            val cardsByCount = hand.groupingBy { it }.eachCount().entries.groupBy({ it.value }, { it.key })

            if (5 in cardsByCount) {
                return HandType.FIVE_OF_A_KIND
            }
            if (4 in cardsByCount) {
                return HandType.FOUR_OF_A_KIND
            }
            if (3 in cardsByCount) {
                if (2 in cardsByCount) {
                    return HandType.FULL_HOUSE
                }
                return HandType.THREE_OF_A_KIND
            }
            if (2 in cardsByCount) {
                if (cardsByCount[2]!!.size == 2) {
                    return HandType.TWO_PAIR
                }
                return HandType.ONE_PAIR
            }
            return HandType.HIGH_CARD
        }

        return input
            .map { line ->
                val (hand, bid) = line.split(' ')

                HandBid(hand, bid.toInt(), getHandType(hand))
            }
            .sortedWith(handComparator(cards).reversed())
            .mapIndexed { index, (_, bid) ->
                (index + 1) * bid
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

        fun getHandType(hand: String): HandType {
            val jokersCount = hand.count { it == 'J' }

            val cardsByCount =
                hand.filter { it != 'J' }.groupingBy { it }.eachCount().entries.groupBy({ it.value }, { it.key })

            if (5 in cardsByCount) {
                return HandType.FIVE_OF_A_KIND
            }
            if (4 in cardsByCount) {
                if (jokersCount == 1) {
                    return HandType.FIVE_OF_A_KIND
                }
                return HandType.FOUR_OF_A_KIND
            }
            if (3 in cardsByCount) {
                when (jokersCount) {
                    2 -> return HandType.FIVE_OF_A_KIND
                    1 -> return HandType.FOUR_OF_A_KIND
                }
                if (2 in cardsByCount) {
                    return HandType.FULL_HOUSE
                }
                return HandType.THREE_OF_A_KIND
            }
            if (2 in cardsByCount) {
                val anotherPairExists = cardsByCount[2]!!.size == 2

                when (jokersCount) {
                    3 -> return HandType.FIVE_OF_A_KIND
                    2 -> return HandType.FOUR_OF_A_KIND
                    1 -> {
                        if (anotherPairExists) {
                            return HandType.FULL_HOUSE
                        }
                        return HandType.THREE_OF_A_KIND
                    }
                }
                if (anotherPairExists) {
                    return HandType.TWO_PAIR
                }
                return HandType.ONE_PAIR
            }

            when (jokersCount) {
                5, 4 -> return HandType.FIVE_OF_A_KIND
                3 -> return HandType.FOUR_OF_A_KIND
                2 -> return HandType.THREE_OF_A_KIND
                1 -> return HandType.ONE_PAIR
            }

            return HandType.HIGH_CARD
        }

        return input
            .map { line ->
                val (hand, bid) = line.split(' ')

                HandBid(hand, bid.toInt(), getHandType(hand))
            }
            .sortedWith(handComparator(cards).reversed())
            .mapIndexed { index, (_, bid) ->
                (index + 1) * bid
            }
            .sum()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
