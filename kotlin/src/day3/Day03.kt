package day3

import kotlin.math.*
import println
import readInput

fun main() {
  data class Location(val x: Int, val y: Int)

  data class EnginePart(val id: Int, val index: Int, val start: Int, val end: Int)

  val numbersRegex = "[0-9]+".toRegex()
  val symbolRegex = "[^\\d.]".toRegex()
  val gearRegex = "[*]".toRegex()

  fun withinBounds(upper: Int, value: Int): Int = max(min(upper, value), 0)

  fun findPartsAndBoundaries(input: List<String>): Map<EnginePart, List<Location>> {
    val xUpperBound = input.maxOf { it.length } - 1
    val yUpperBound = input.size - 1

    return input.flatMapIndexed { index, line ->
      numbersRegex.findAll(line).map {
        EnginePart(it.value.toInt(), index, it.range.first, it.range.last)
      }
    }.associateWith {
      sequence {
        for (x in it.start..it.end) {
          yield(Location(withinBounds(xUpperBound, x), withinBounds(yUpperBound, it.index - 1)))
          yield(Location(withinBounds(xUpperBound, x), withinBounds(yUpperBound, it.index + 1)))
        }

        for (y in it.index - 1..it.index + 1) {
          yield(Location(withinBounds(xUpperBound, it.start - 1), withinBounds(yUpperBound, y)))
          yield(Location(withinBounds(xUpperBound, it.end + 1), withinBounds(yUpperBound, y)))
        }
      }.toList()
    }
  }

  fun findGears(input: List<String>): List<Location> =
    input.flatMapIndexed { index, line ->
      gearRegex.findAll(line).map { Location(it.range.first, index) }
    }

  fun part1(input: List<String>): Int = findPartsAndBoundaries(input).filter { (_, boundary) ->
    boundary.any {
      symbolRegex.matches(input[it.y][it.x].toString())
    }
  }.keys.sumOf { it.id }

  fun part2(input: List<String>): Int {
    val partsAndBoundaries = findPartsAndBoundaries(input)
    val gears = findGears(input)

    return gears.sumOf { gear ->
      val adjacentParts = partsAndBoundaries.filter { (_, boundary) ->
        boundary.contains(gear)
      }

      when (adjacentParts.size) {
        2 -> adjacentParts.keys.first().id * adjacentParts.keys.last().id
        else -> 0
      }
    }
  }

  val exampleInput = readInput("day3/input_example")
  part1(exampleInput).println()
  part2(exampleInput).println()

  val input = readInput("day3/input")
  part1(input).println()
  part2(input).println()
}
