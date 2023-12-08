package day8

import println
import readInput
import kotlin.math.pow

fun main() {

  data class Path(val left: String, val right: String)

  data class MapToEnd(val instructions: List<Char>, val nodes: Map<String, Path>)

  fun parseMap(input: List<String>): MapToEnd {
    val instructions = input.first().trim().toList()
    val nodes = input.drop(2).associate { line ->
      val (id, paths) = line.split("=").map { it.trim() }
      val (left, right) = paths.split(",").map { it.replace("(", "").replace(")", "").trim() }

      id to Path(left, right)
    }

    return MapToEnd(instructions, nodes)
  }

  fun stepsToReachEnd(map: MapToEnd, start: String, destinations: List<String>): Int {
    var currentNode = start
    var steps = 0;
    var reachedEnd = false

    while (!reachedEnd) {
      val paths = map.nodes[currentNode]!!

      currentNode = when (map.instructions[steps % map.instructions.size]) {
        'L' -> paths.left
        'R' -> paths.right
        else -> throw Exception("Unknown instruction")
      }

      reachedEnd = destinations.contains(currentNode)
      steps++
    }

    return steps
  }

  fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger

    while (lcm <= maxLcm) {
      if (lcm % a == 0L && lcm % b == 0L) {
        return lcm
      }
      lcm += larger
    }

    return maxLcm
  }

  fun part1(input: List<String>): Int = stepsToReachEnd(parseMap(input), "AAA", listOf("ZZZ"))

  fun part2(input: List<String>): Long {
    val map = parseMap(input);
    val destinations = map.nodes.keys.filter { it.endsWith("Z") }

    return map.nodes.keys.asSequence()
      .filter { it.endsWith("A") }
      .map { start -> stepsToReachEnd(map, start, destinations) }
      .map { it.toLong() }
      .reduce { gcd, steps -> lcm(gcd, steps) }
  }

  val part1Example = "RL\n" +
    "\n" +
    "AAA = (BBB, CCC)\n" +
    "BBB = (DDD, EEE)\n" +
    "CCC = (ZZZ, GGG)\n" +
    "DDD = (DDD, DDD)\n" +
    "EEE = (EEE, EEE)\n" +
    "GGG = (GGG, GGG)\n" +
    "ZZZ = (ZZZ, ZZZ)";

  part1(part1Example.split("\n")).println()

  val part2Example = "LR\n" +
    "\n" +
    "11A = (11B, XXX)\n" +
    "11B = (XXX, 11Z)\n" +
    "11Z = (11B, XXX)\n" +
    "22A = (22B, XXX)\n" +
    "22B = (22C, 22C)\n" +
    "22C = (22Z, 22Z)\n" +
    "22Z = (22B, 22B)\n" +
    "XXX = (XXX, XXX)"

  part2(part2Example.split("\n")).println()

  val input = readInput("day8/input")
  part1(input).println()
  part2(input).println()
}
