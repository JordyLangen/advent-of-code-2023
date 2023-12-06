package day5

import println
import read
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

suspend fun main() {

  data class MappingRange(
    val source: LongRange,
    val destination: LongRange,
  )

  data class Almanac(
    val seeds: List<Long>,
    val seedToSoil: List<MappingRange>,
    val soilToFertilizer: List<MappingRange>,
    val fertilizerToWater: List<MappingRange>,
    val waterToLight: List<MappingRange>,
    val lightToTemperature: List<MappingRange>,
    val temperatureToHumidity: List<MappingRange>,
    val humidityToLocation: List<MappingRange>,
  ) {
    val mappings = listOf(seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation)
  }

  fun parseSection(sections: List<String>, name: String): List<MappingRange> =
    sections.find { it.startsWith(name) }
      ?.split("\n")
      ?.drop(1)
      ?.map { line ->
        val (destination, source, range) = line.split(" ")
          .filter { it.isNotBlank() }
          .map { it.trim().toLong() }

        MappingRange(
          source until source + range,
          destination until destination + range
        )
      } ?: throw Exception("Invalid input")

  fun parseAlmanac(path: String): Almanac {
    val content = read(path)
    val sections = content.split("\n\n")

    return Almanac(
      sections.first()
        .split(":")
        .last()
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() },
      parseSection(sections, "seed-to-soil"),
      parseSection(sections, "soil-to-fertilizer"),
      parseSection(sections, "fertilizer-to-water"),
      parseSection(sections, "water-to-light"),
      parseSection(sections, "light-to-temperature"),
      parseSection(sections, "temperature-to-humidity"),
      parseSection(sections, "humidity-to-location"),
    );
  }

  fun map(mappingRanges: List<MappingRange>, target: Long): Long {
    val mapping = mappingRanges.find { mapping ->
      target >= mapping.source.first && target <= mapping.source.last
    }

    return if (mapping != null) {
      mapping.destination.first + (target - mapping.source.first)
    } else {
      target
    }
  }

  fun part1(almanac: Almanac): Long {
    val mappings = almanac.mappings;

    return almanac.seeds
      .map { seed -> mappings.fold(seed) { target, mappingRanges -> map(mappingRanges, target) } }
      .minOf { it }
  }

  fun part2(almanac: Almanac): Long {
    val mappings = almanac.mappings;

    val chunkedSeeds = almanac.seeds
      .asSequence()
      .chunked(2)
      .distinct()

    var smallestLocation = Long.MAX_VALUE

    for (seedAndRange in chunkedSeeds) {
      for (seed in seedAndRange[0] until seedAndRange[1] + seedAndRange[0]) {
        val location = mappings.fold(seed) { target, mappingRanges -> map(mappingRanges, target) }

        if (location < smallestLocation) {
          smallestLocation = location
        }
      }
    }

    return smallestLocation
  }

  val exampleAlmanac = parseAlmanac("day5/input_example")
  part1(exampleAlmanac).println()
  part2(exampleAlmanac).println()

  val almanac = parseAlmanac("day5/input")
  part1(almanac).println()
  part2(almanac).println()
}
