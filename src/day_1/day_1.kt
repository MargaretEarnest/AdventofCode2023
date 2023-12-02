package day_1

import java.io.File

const val isPartTwo = true
val digits = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6",
        "seven" to "7", "eight" to "8", "nine" to "9")

fun getResult(line: String): Int {
    val target = if (isPartTwo) "one|two|three|four|five|six|seven|eight|nine" else ""
    val sep = if (isPartTwo) "|" else ""
    var firstNum = "($target$sep\\d)".toRegex().findAll(line).first().groupValues[0]
    var secondNum = "(${target.reversed()}$sep\\d)".toRegex().findAll(line.reversed()).first().groupValues[0].reversed()
    if (firstNum in digits) firstNum = digits.getOrDefault(firstNum, "")
    if (secondNum in digits) secondNum = digits.getOrDefault(secondNum, "")
    return (firstNum + secondNum).toInt()
}

fun main() {
    var sum = 0
    File(System.getProperty("user.dir")+ "\\src\\day_1\\data").forEachLine { sum += getResult(it) }
    println(sum)
}