package day_3

import java.io.File

const val isPartTwo = true

fun isSymbolNearby(grid: List<List<Int>>, i: Int, j: Int): Int {
    for (y in -1..1) for (x in -1..1) {
        val idx1 = i+y
        val idx2 = j+x
        if (idx1 in grid.indices && idx2 in grid[0].indices && grid[idx1][idx2] > 1) return if (isPartTwo) grid[idx1][idx2] else 2
    }
    return 1
}

fun main() {
    val grid = mutableListOf<List<Char>>()
    File(System.getProperty("user.dir")+ "\\src\\day_3\\data" + "").forEachLine { grid.add(it.toCharArray().toList()) }
    var gearId = 1
    val gridMask = grid.map{
        it.map {c ->
            if (isPartTwo) {
                if (c == '*') ++gearId else if (c.isDigit()) 1 else 0
            } else {
                if (c == '.') 0 else (if (c.isDigit()) 1 else 2)
            }
        }
    }
    val isValidNum = gridMask.mapIndexed { i, l ->
        l.mapIndexed { j, n -> if (n == 0 || n > 1) 0 else isSymbolNearby(gridMask, i, j) }
    }
    var startIndex = -1
    var endIndex = -1
    var nearbyNum = 0
    var sum = 0
    val gearMap = mutableMapOf<Int, List<Int>>()
    isValidNum.forEachIndexed { i, l ->
        fun evalNum() {
            if (startIndex > -1 && nearbyNum > 1) {
                if (isPartTwo) {
                    gearMap[nearbyNum] = gearMap.getOrDefault(nearbyNum, mutableListOf()) + grid[i].subList(startIndex, endIndex+1).joinToString("").toInt()
                } else sum += grid[i].subList(startIndex, endIndex+1).joinToString("").toInt()
            }
            startIndex = -1
            endIndex = -1
            nearbyNum = 0
        }
        l.forEachIndexed { j, c ->
            if (c > 0) {
                if (startIndex == -1) startIndex = j
                endIndex = j
                if (c > 1) nearbyNum = c
            } else evalNum()
        }
        evalNum()
    }
    if (isPartTwo) sum = gearMap.keys.filter { gearMap[it]?.size == 2 }.sumOf { gearMap[it]!![0] * gearMap[it]!![1] }
    println(sum)
}