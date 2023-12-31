package day_2

import java.io.File
import kotlin.math.max

const val isPartTwo = false

fun main() {
    var sum = 0
    File(System.getProperty("user.dir")+ "\\src\\day_2\\data").forEachLine { line ->
        val idAndData = line.split(": ")
        val id = idAndData[0].substringAfter("Game ").toInt()
        val colors = idAndData[1].split("; ")
        sum += if (isPartTwo) {
            val minCubes = mutableMapOf<String, Int>()
            colors.map {
                it.split(", ").forEach { c ->
                    val d = c.split(" ")
                    minCubes[d[1]] = max(minCubes.getOrDefault(d[1], 0), d[0].toInt())
                }
            }
            minCubes.values.reduce { acc, el -> acc * el}
        } else {
            val limits = mapOf("red" to 12, "blue" to 14, "green" to 13)
            if (colors.map {
                it.split(", ").map { c ->
                    val d = c.split(" ")
                    limits.getOrDefault(d[1], 0) < d[0].toInt()
                }
            }.map { true in it }.any { it }) 0 else id
        }
    }
    println(sum)
}