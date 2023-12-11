package day_10

import java.io.File

const val isPartTwo = false
val pipeMap = mapOf(
        '|' to listOf(true, false, true, false),
        '-' to listOf(false, true, false, true),
        'L' to listOf(true, true, false, false),
        'J' to listOf(true, false, false, true),
        '7' to listOf(false, false, true, true),
        'F' to listOf(false, true, true, false),
        '.' to listOf(false, false, false, false),
        'S' to listOf(true, true, true, true)
)
val pipeStrings = mapOf(
    listOf(true, false, true, false) to "║",
    listOf(false, true, false, true) to "═",
    listOf(true, true, false, false) to "╚",
    listOf(true, false, false, true) to "╝",
    listOf(false, false, true, true) to "╗",
    listOf(false, true, true, false) to "╔",
    listOf(false, false, false, false) to " ",
    listOf(true, true, true, true) to "╬"
)
val pipes = mutableListOf<List<Node>>()
const val ANSI_RESET = "\u001B[0m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_YELLOW = "\u001B[33m"


class Node(val x: Int, val y: Int, char: Char) {
    var directions = listOf<Boolean>()
    private var isOrigin = char == 'S'
    var distFromStart = -1
    var isEnclosed = false

    init { directions = pipeMap[char]!! }

    override fun toString(): String {
        val color = if (isOrigin) ANSI_GREEN else if (distFromStart > 0) ANSI_RED else if (isEnclosed) ANSI_YELLOW else ""
        return color + if (isEnclosed) "■" else pipeStrings.getOrDefault(directions, "?") + ANSI_RESET
    }
}

val dirMap = mapOf(0 to Pair(0, -1), 1 to Pair(1, 0), 2 to Pair(0, 1), 3 to Pair(-1, 0))

fun filterPipes() {
    for (y in pipes.indices) {
        pipes[y].forEachIndexed { x, p ->
            val filteredDirs = p.directions.mapIndexed { i, d -> isConnected(x, y, i, d) }
            p.directions = if (filteredDirs.count { it } > 1) filteredDirs else listOf(false, false, false, false)
        }
    }
}

fun getNodeInDir(x: Int, y: Int, dirIdx: Int): Node? {
    val diffs = dirMap[dirIdx]
    val xDiff = x + diffs!!.first
    val yDiff = y + diffs.second
    return if (xDiff in pipes[0].indices && yDiff in pipes.indices) pipes[yDiff][xDiff] else null
}

fun isConnected(x: Int, y: Int, dirIdx: Int, dirValue: Boolean): Boolean {
    return dirValue && getNodeInDir(x, y, dirIdx) != null && getNodeInDir(x, y, dirIdx)!!.directions[(dirIdx + 2) % 4]
}

var start: Pair<Int, Int> = Pair(0, 0)

fun main() {
    File(System.getProperty("user.dir")+ "\\src\\day_10\\test2").forEachLine { line ->
        if (line.contains("S")) start = Pair(pipes.size, line.indexOf("S"))
        pipes.add(line.toCharArray().mapIndexed { i, c -> Node(i, pipes.size, c) })
    }

    for (i in 0..4) filterPipes() // this is just for better visualization, not relevant to the puzzle

    if (isPartTwo) {
//        pipes.forEach {
//            for (n in it) {
//                val neighbors = (0..3).map {d -> getNodeInDir(n.x, n.y, d)!! }
//            }
//        }
    } else {
        val startNode = pipes[start.first][start.second]
        startNode.distFromStart = 0
        var queue = listOf(startNode)
        while (queue.isNotEmpty()) {
            val tempQueue = mutableListOf<Node>()
            for (n in queue) {
                val children = n.directions.mapIndexed { i, b -> if (b) i else null }.filterNotNull().map {
                    getNodeInDir(n.x, n.y, it)!!
                }.filter { it.distFromStart == -1 }
                tempQueue.addAll(children)
                children.forEach { it.distFromStart = n.distFromStart + 1 }
            }
            queue = tempQueue
        }
        pipes.forEach { println(it.joinToString("")) }
        println(pipes.maxOf { it.maxOf { n -> n.distFromStart } })
    }
}