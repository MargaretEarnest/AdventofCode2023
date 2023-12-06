package day_5

import java.io.File
import java.math.BigInteger

const val isPartTwo = true
var maps = mutableMapOf<Pair<String, String>, MutableList<List<BigInteger>>>()

class AffectedRange(val start: BigInteger, val end: BigInteger, val isAffected: Boolean) {
    override fun toString(): String {
        return "<$start, $end, $isAffected>"
    }
}

fun mendRange(start: BigInteger, end: BigInteger, rStart: BigInteger, rEnd: BigInteger): List<Pair<BigInteger, BigInteger>> {
    println("Split range input start=$start, end=$end, rStart=$rStart, rEnd=$rEnd")
    return if (start <= rStart && end > rEnd) {
        println("affected range entirely in test range")
        listOf(Pair(start, end))
    } else if (rStart <= start && rEnd > end) {
        println("test range entirely in affected range")
        listOf(Pair(rStart, rEnd))
    } else if (rStart <= start && rEnd > start && rEnd < end) {
        println("affected range starts before test range but ends inside")
        listOf(Pair(start, rEnd))
    } else if (rStart >= start && rStart < end && rEnd > end) {
        println("affected range starts in test range but ends after it")
        listOf(Pair(rStart, end))
    } else listOf(Pair(start, end), Pair(rStart, rEnd))
}

fun splitRange(start: BigInteger, end: BigInteger, rStart: BigInteger, rEnd: BigInteger): List<AffectedRange> {
    println("Split range input start=$start, end=$end, rStart=$rStart, rEnd=$rEnd")
    return if (start <= rStart && end > rEnd) {
        println("affected range entirely in test range")
        listOf(AffectedRange(start, rStart-BigInteger.ONE, false), AffectedRange(rStart, rEnd-BigInteger.ONE, true), AffectedRange(rEnd, end, false))
    } else if (rStart <= start && rEnd > end) {
        println("test range entirely in affected range")
        listOf(AffectedRange(start, end, true))
    } else if (rStart <= start && rEnd > start && rEnd < end) {
        println("affected range starts before test range but ends inside")
        listOf(AffectedRange(start, rEnd-BigInteger.ONE, true), AffectedRange(rEnd, end, false))
    } else if (rStart >= start && rStart < end && rEnd > end) {
        println("affected range starts in test range but ends after it")
        listOf(AffectedRange(start, rStart-BigInteger.ONE, false), AffectedRange(rStart, end, true))
    } else listOf(AffectedRange(start, end, false))
}

fun transform(value: BigInteger, range: List<BigInteger>): BigInteger {
    return range[0] + (value - range[1])
}

fun mendAllRanges(ranges: List<Pair<BigInteger, BigInteger>>, lastSize: Int = 0): List<Pair<BigInteger, BigInteger>> {
    var tempList = mutableListOf<Pair<BigInteger, BigInteger>>()
    for (i in 0..<ranges.size-1) {
        val combined = mendRange(ranges[i].first, ranges[i].second, ranges[i+1].first, ranges[i+1].second)
        println("Combined $combined")
        tempList.addAll(combined)
    }
    println(tempList.size.toString() + " ~ " + lastSize)
    if (tempList.size != lastSize) return mendAllRanges(tempList, tempList.size)
    return tempList
}

fun transformRange(start: BigInteger, size: BigInteger): List<Pair<BigInteger, BigInteger>> {
    var currDomain = "seed"
    // final list of ranges, updated after each domain change
    var ranges = mutableListOf(Pair(start, start+size-BigInteger.ONE))
//    println("Start range is $ranges")
    // domains being collected from the current domain change
    var tempRanges = mutableSetOf<Pair<BigInteger, BigInteger>>()
    var currRanges: List<Pair<BigInteger, BigInteger>>
    var dumbRanges: MutableList<Pair<BigInteger, BigInteger>> = mutableListOf()
    while (currDomain != "location") {
        currRanges = ranges
        val mapping = maps.keys.first { it.first == currDomain }
        for (range in maps[mapping]!!) {
            println("mapping $mapping, curr=$currRanges")
            currRanges.forEach {
                println("Current ranges $currRanges, range is $it, mapping with $range ($mapping)")
//                currRanges = listOf(it)
//                currRanges.forEach { r ->
                val splitRanges = splitRange(it.first, it.second, range[1], range[1] + range[2])
                println("Split ranges $splitRanges")
                dumbRanges.addAll(splitRanges.map {ar ->
                    if (ar.isAffected) Pair(transform(ar.start, range), transform(ar.end, range))
                    else Pair(ar.start, ar.end)
                }.sortedBy { it.first })
                println("dumb list $dumbRanges")
                if (dumbRanges.size > 1) {
                    dumbRanges = mendAllRanges(dumbRanges).toMutableList()
                    println("mended dumb list $dumbRanges")
                }


//                }
//                println("Current ranges after $currRanges")
            }
            println("dumbRanges=$dumbRanges")
            currRanges = dumbRanges.toList()
            println("currRanges=$currRanges")
            dumbRanges.clear()
        }
        println("Curr before temp $currRanges, $tempRanges")
        tempRanges.addAll(currRanges)
        dumbRanges.clear()
        currRanges = listOf()
//        println("New ranges: $tempRanges")
        ranges = tempRanges.toMutableList()
        println("New round, ranges are $ranges")
        tempRanges = mutableSetOf()
        currDomain = mapping.second
    }
//    println("Final ranges $ranges")
    return ranges
}

fun transformWithMap(seed: BigInteger): BigInteger {
    var currDomain = "seed"
    var value = seed
    while (currDomain != "location") {
       val mapping = maps.keys.first { it.first == currDomain }
        for (range in maps[mapping]!!) {
            if (value in range[1]..range[1]+range[2]) {
                value = range[0] + (value - range[1])
                break
            }
        }
        currDomain = mapping.second
    }
    return value
}

fun main() {
    var seeds: List<BigInteger> = emptyList()
    var currMap: Pair<String, String> = Pair("", "")
    File(System.getProperty("user.dir")+ "\\src\\day_5\\test").forEachLine { line ->
        if (line.contains("seeds")) {
            seeds = line.substringAfter("seeds: ").split(" ").map { it.toBigInteger() }
        }
        if (line.contains("map")) {
            val map = Pair(line.substringBefore("-to"), line.substringAfter("to-").substringBefore(" "))
            maps[map] = mutableListOf()
            currMap = map
        } else if (line != "") {
            maps[currMap]?.add(line.split(" ").map { it.toBigInteger() })
        }
    }
    if (isPartTwo) { // seeds.indices
        println((0..1 step 2).map { i -> transformRange(seeds[i], seeds[i + 1]).map { it.first }.minBy { it } }.minBy { it })
    } else {
        println(seeds.map { n -> transformWithMap(n) }.minBy { it })
    }
}