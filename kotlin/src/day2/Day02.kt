package day2

import println
import readInput

fun main() {
  data class RevealedHand(val red: Int, val green: Int, val blue: Int)

  data class Game(val id: Int, val revealedHands: List<RevealedHand>) {
    val red = revealedHands.maxOf { it.red }
    val green = revealedHands.maxOf { it.green }
    val blue = revealedHands.maxOf { it.blue }
  }

  fun parseGame(input: String): Game {
    val id = input.split(":").firstOrNull()?.split(" ")?.last()?.toInt() ?: throw Exception("Invalid input")

    val revealedHands = input.split(":").lastOrNull()?.split(";")?.map { it ->
      val dealtCubes = it.split(", ")
        .map { it.trim() }
        .associate { it.split(" ").last() to it.split(" ").first().toInt() }

      RevealedHand(
        dealtCubes.getOrDefault("red", 0),
        dealtCubes.getOrDefault("green", 0),
        dealtCubes.getOrDefault("blue", 0)
      )
    } ?: throw Exception("Invalid input")

    return Game(id, revealedHands)
  }

  fun part1(input: List<String>): Int =
    input.map { parseGame(it) }.filter { it.red <= 12 && it.green <= 13 && it.blue <= 14 }.sumOf { it.id }

  fun part2(input: List<String>): Int =
    input.map { parseGame(it) }.sumOf { it.red * it.green * it.blue }

  val exampleInput = readInput("day2/input_example")
  part1(exampleInput).println()
  part2(exampleInput).println()

  val input = readInput("day2/input")
  part1(input).println()
  part2(input).println()
}
