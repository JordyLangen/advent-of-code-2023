package day7

import println
import readInput
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

enum class HandType {
  HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
}

data class Hand(val cards: String, val bid: Int, val type: HandType, val value: String)

fun main() {

  val cards = "23456789TJQKA"
  val cardsWithWildcard = "J23456789TQKA"
  val wildcard = 'J'

  fun calculateHandType(dealtCards: String, supportWildcard: Boolean): HandType {
    val cardsToEvaluate = if (supportWildcard) {
      val mostCommonCard = dealtCards
        .filter { it != wildcard }
        .groupBy { it }
        .entries
        .maxByOrNull { it.value.size }
        ?.key ?: return HandType.FIVE_OF_A_KIND

      dealtCards.replace(wildcard, mostCommonCard)
    } else {
      dealtCards
    }

    val cardCounts = cardsToEvaluate
      .groupBy { it }
      .mapValues { it.value.size }
      .values
      .sortedDescending()

    return when (cardCounts) {
      listOf(5) -> HandType.FIVE_OF_A_KIND
      listOf(4, 1) -> HandType.FOUR_OF_A_KIND
      listOf(3, 2) -> HandType.FULL_HOUSE
      listOf(3, 1, 1) -> HandType.THREE_OF_A_KIND
      listOf(2, 2, 1) -> HandType.TWO_PAIR
      listOf(2, 1, 1, 1) -> HandType.ONE_PAIR
      else -> HandType.HIGH_CARD
    };
  }

  fun calculateValue(dealtCards: String, supportWildcard: Boolean): String = dealtCards
    .map { card -> (if (supportWildcard) cardsWithWildcard else cards).indexOf(card) }
    .joinToString(".") { cardValue ->
      cardValue.toString().padStart(2, '0')
    }

  fun parseHands(input: List<String>, supportWildcard: Boolean): List<Hand> = input
    .map { line ->
      val (dealtCards, bid) = line.split(" ")

      Hand(
        dealtCards,
        bid.toInt(),
        calculateHandType(dealtCards, supportWildcard),
        calculateValue(dealtCards, supportWildcard)
      )
    }

  fun part1(input: List<String>): Int = parseHands(input, false)
    .sortedBy { it.value }
    .sortedBy { it.type }
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .reduce { sum, score -> sum + score }

  fun part2(input: List<String>): Int = parseHands(input, true)
    .sortedBy { it.value }
    .sortedBy { it.type }
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .reduce { sum, score -> sum + score }

  val exampleInput = readInput("day7/input_example")
  part1(exampleInput).println()
  part2(exampleInput).println()

  val input = readInput("day7/input")
  part1(input).println()
  part2(input).println()
}
