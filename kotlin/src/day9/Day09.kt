package day9

import println
import readInput

fun main() {

  fun parseReadings(input: List<String>): List<List<Int>> = input
    .map { line -> line.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() } }

  fun differences(reading: List<Int>): List<Int> = reading
    .zipWithNext()
    .map { (a, b) -> b - a }

  fun predictNextValue(reading: List<Int>): Int {
    val calculations = mutableListOf(reading)

    var foundEnd = false

    do {
      val differences = differences(calculations.last())

      if (differences.all { it == 0 }) {
        foundEnd = true
      } else {
        calculations.add(differences)
      }
    } while (!foundEnd)

    return calculations.sumOf { it.last() }
  }

  fun part1(input: List<String>): Int = parseReadings(input)
    .sumOf { reading -> predictNextValue(reading) }

  fun part2(input: List<String>): Int = parseReadings(input)
    .map { it.reversed() }
    .sumOf { reading -> predictNextValue(reading) }

  val exampleInput = readInput("day9/input_example")
  part1(exampleInput).println()
  part2(exampleInput).println()

  val input = readInput("day9/input")
  part1(input).println()
  part2(input).println()
}
