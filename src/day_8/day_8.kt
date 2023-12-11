package day_8

import java.io.File
import java.util.Collections.max
import kotlin.math.floor
import kotlin.math.max

const val isPartTwo = true
var sequence = listOf<Long>()
val nodeMap = mutableMapOf<String, List<String>>()
val firstZValues = mutableMapOf<String, MutableList<Pair<String, Long>>>()
var seqIdx: Long = 0

fun runNodeToIndex(node: String, startIdx: Long, endIdx: Long): String {
    var currIdx = startIdx
    var currNode = node
    while (currIdx < endIdx) {
        currNode = nodeMap[currNode]!![sequence[(currIdx % sequence.size).toInt()].toInt()]
        currIdx++
    }
    return currNode
}

fun skipValForCycle(startNode: String, zNode: String): Long {
    if (firstZValues.containsKey(startNode)) {
        val nextToLastZ = firstZValues.getOrDefault(startNode, listOf()).getOrNull(firstZValues[startNode]!!.lastIndex - 1)
        val lastZ = firstZValues.getOrDefault(startNode, listOf()).getOrNull(firstZValues[startNode]!!.lastIndex)
        if (firstZValues[startNode]!!.size > 1 && lastZ?.first == nextToLastZ?.first && lastZ!!.second % sequence.size == nextToLastZ!!.second % sequence.size) {
//            println("Skipping ${lastZ.second - nextToLastZ.second}")
            return lastZ.second - nextToLastZ.second
        } else {
            firstZValues[startNode]!!.add(Pair(zNode, seqIdx))
        }
    } else {
        firstZValues[startNode] = mutableListOf(Pair(zNode, seqIdx))
    }
    return 0
}

fun main() {
    var isFirstLine = true

    File(System.getProperty("user.dir")+ "\\src\\day_8\\data").forEachLine { line ->
        if (isFirstLine) {
            sequence = line.split("").filter { it.isNotEmpty() }.map { if (it == "L") 0 else 1 }
            isFirstLine = false
        } else {
            if (line != "") {
                val data = line.split(" = ")
                val children = data[1].trim('(', ')').split(", ")
                nodeMap[data[0]] = listOf(children[0], children[1])
            }
        }
    }

    var repeats = 0
    if (isPartTwo) {
        val currNodes: MutableMap<String, Pair<Long, String>> = nodeMap.keys.filter { it.endsWith("A") }.associateWith { Pair(0.toLong(), it) }.toMutableMap()
        val nodeKeysMinusFirst = currNodes.keys.filter { it != currNodes.keys.first() }
        var skip: Long = 18113
        while (true) {
            val currNode = currNodes[currNodes.keys.first()]
            val nextNode = runNodeToIndex(currNode!!.second, seqIdx, seqIdx + 1)
            currNodes[currNodes.keys.first()] = Pair(seqIdx + 1, nextNode)
            val skipIdxs = mutableListOf<Pair<Long, String>>()
            if (nextNode.endsWith("Z")) {
                var broke = false
                for (n in nodeKeysMinusFirst) {
                    val node = currNodes[n]
                    val spedUpNode = runNodeToIndex(node!!.second, node.first, seqIdx+1)
                    currNodes[n] = Pair(seqIdx+1, spedUpNode)
//                    println("Who $spedUpNode, comes from $n")
                    if (!spedUpNode.endsWith("Z")) {
                        broke = true
                        break
                    }
//                    println("In loop $firstZValues")
                    val skipIdx = skipValForCycle(n, spedUpNode)
                    skipIdxs.add(Pair(skipIdx, spedUpNode))
                    println("Skip Id: " + skipIdx)
//                    seqIdx += skipIdx
                }
                if (!broke) break
                println("$currNodes")
//                println(skipIdxs)
                val firstSkip = skipValForCycle(currNodes.keys.first(), nextNode)
//                skipIdxs.add(Pair(firstSkip, currNodes.keys.first()))
                val max = skipIdxs.maxOfOrNull { it.first } ?: 0
                println("$max, $skip, $skipIdxs")
                if (max >= skip && skipIdxs.map { it.first }.all { it % skip == 0.toLong() }) {
                    println("$skip, $max")
                    skip = max
                }
//                println("$firstZValues")
                seqIdx += skip
            }
            seqIdx++
        }
        println(floor((seqIdx / sequence.size).toDouble()))
    } else {
        var currNode = "AAA"
        while (currNode != "ZZZ") {
            currNode = nodeMap[currNode]!![sequence[seqIdx.toInt()].toInt()]
            if (seqIdx < sequence.size - 1) seqIdx++
            else {
                seqIdx = 0
                repeats++
            }
        }
        println(repeats*sequence.size + seqIdx)
    }
}