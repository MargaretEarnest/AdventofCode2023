package day_6

import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

const val isPartTwo = true

fun quadForm(time: BigDecimal, dist: BigDecimal, isNeg: Boolean): BigDecimal {
    val a = -BigDecimal.ONE
    val mc = MathContext(100)
    return (-time + (if (isNeg) -BigDecimal.ONE else BigDecimal.ONE) * (time.pow(2) - BigDecimal.valueOf(4).multiply(a).multiply(-dist)).sqrt(mc)) / (BigDecimal.valueOf(2)*a)
}

fun main() {
    var times = listOf<BigDecimal>()
    var distances = listOf<BigDecimal>()
    File(System.getProperty("user.dir")+ "\\src\\day_6\\data").forEachLine { line ->
        if (line.contains("Time")) {
            times = if (isPartTwo) {
                listOf(line.substringAfter("Time: ").trimStart().replace(" ", "").toBigDecimal())
            } else {
                line.substringAfter("Time: ").trimStart().split(" ").filter { it != "" }.map { it.toBigDecimal() }
            }
        } else {
            distances = if (isPartTwo) {
                listOf(line.substringAfter("Distance: ").trimStart().replace(" ", "").toBigDecimal())
            } else {
                line.substringAfter("Distance: ").trimStart().split(" ").filter { it != "" }.map { it.toBigDecimal() }
            }
        }
    }

    var product = BigDecimal.ONE
    for (i in times.indices) {
        val lowerBound = (quadForm(times[i], distances[i], false) + BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.UP).round(MathContext(10))
        val upperBound = (quadForm(times[i], distances[i], true) - BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.DOWN).round(MathContext(10))
        product *= (upperBound - lowerBound + BigDecimal.ONE)
    }
    println(product)
}