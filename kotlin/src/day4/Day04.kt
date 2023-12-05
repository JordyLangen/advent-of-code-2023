package day4

import println
import readInput

fun main() {

  data class Card(val id: String, val winning: List<Int>, val scratched: List<Int>)

  fun parseNumbers(input: String): List<Int> = input.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }

  fun parseCards(input: List<String>): List<Card> = input
    .map { line ->
      val (id, numbers) = line.split(":")
      val (winning, scratched) = numbers.split("|")

      Card(id, parseNumbers(winning), parseNumbers(scratched))
    }

  fun part1(input: List<String>): Int {
    return parseCards(input)
      .mapNotNull { card ->
        List(card.scratched.filter { card.winning.contains(it) }.size) { index -> index + 1 }
          .reduceOrNull { acc, _ -> acc * 2 }
      }
      .sum()
  }

  fun part2(input: List<String>): Int {
    val cards = parseCards(input).toMutableList()
    val cardCounts = mutableMapOf<Card, Int>()

    for (index in 0 until cards.size) {
      val card = cards[index]
      val cardCount = cardCounts.getOrDefault(card, 0) + 1
      cardCounts[card] = cardCount

      val wins = card.scratched.filter { card.winning.contains(it) }.size

      cards.drop(index + 1).take(wins).forEach {
        val currentCount = cardCounts.getOrDefault(it, 0)
        cardCounts[it] = currentCount + cardCount
      }
    }

    return cardCounts.values.sum()
  }

  val exampleInput = readInput("day4/input_example")
  part1(exampleInput).println()
  part2(exampleInput).println()

  val input = readInput("day4/input")
  part1(input).println()
  part2(input).println()
}
