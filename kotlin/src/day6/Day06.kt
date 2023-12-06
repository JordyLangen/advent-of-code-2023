package day6

import println
import readInput

fun main() {

  data class Race(
    val time: Long,
    val distance: Long,
  )

  fun parseRaces(input: List<String>): List<Race> {
    val times = input.first().substringAfter(":")
      .split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }

    val distances = input.last().substringAfter(":")
      .split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }

    return times.zip(distances).map { Race(it.first, it.second) }
  }

  fun parseRace(input: List<String>): Race {
    val time = input.first().substringAfter(":")
      .replace(" ", "").trim().toLong()

    val distance = input.last().substringAfter(":")
      .replace(" ", "").trim().toLong()

    return Race(time, distance)
  }

  fun wins(race: Race): Long {
    val range = (race.distance / race.time until race.time - (race.distance / race.time))

    val first = range
      .first { time -> time * (race.time - time) > race.distance }

    val last = range.reversed()
      .first { time -> time * (race.time - time) > race.distance }

    return (last - first) + 1
  }

  fun part1(races: List<Race>): Long = races
    .map { race -> wins(race) }
    .reduce { sum, wins -> sum * wins }

  fun part2(race: Race): Long = wins(race)

  val exampleInput = readInput("day6/input_example")

  part1(parseRaces(exampleInput)).println()
  part2(parseRace(exampleInput)).println()

  val input = readInput("day6/input")

  part1(parseRaces(input)).println()
  part2(parseRace(input)).println()
}
