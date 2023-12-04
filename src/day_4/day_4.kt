package day_4

import java.io.File
import kotlin.math.pow

const val isPartTwo = true
var scratchMap = mutableMapOf<Int, Int>()
var sum = 0.0

fun findWinningNums(card: String): Int {
    val winning = card.substringAfter(": ").substringBefore(" |").split(" ").filter { it != "" }
    val actual = card.substringAfter("| ").split(" ").filter { it != "" }
    return winning.intersect(actual).size
}

fun countScorecards(id: Int) {
    val winnerTotal = scratchMap.getOrDefault(id, 0)
    sum += winnerTotal
    for (i in id+1..id + winnerTotal) countScorecards(i)
}

fun main() {
    File(System.getProperty("user.dir")+ "\\src\\day_4\\data").forEachLine { line ->
        if (isPartTwo) {
            val id = line.substringAfter("Card ").substringBefore(":").trimStart().toInt()
            scratchMap[id] = findWinningNums(line)
        } else {
            val winningNums = findWinningNums(line)
            sum +=  if (winningNums == 0) 0.0 else 2.0.pow(winningNums.toDouble() - 1)
        }
    }
    if (isPartTwo) sum = scratchMap.keys.size.toDouble()
    scratchMap.keys.forEach { countScorecards(it) }
    println(sum.toInt())
}