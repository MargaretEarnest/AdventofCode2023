package day_7

import java.io.File

const val isPartTwo = true

fun is5OfKind(hand: String) = hand.toCharArray().toSet().size == 1

fun is4OfKind(hand: String): Boolean {
    val chars = hand.toCharArray().toMutableSet()
    val numOcc = hand.toCharArray().filter { it == chars.first() }
    return chars.size == 2 && (numOcc.size == 1 || numOcc.size == 4)
}

fun isFullHouse(hand: String): Boolean {
    val chars = hand.toCharArray().toMutableSet()
    val numOcc = hand.toCharArray().filter { it == chars.first() }
    return chars.size == 2 && (numOcc.size == 2 || numOcc.size == 3)
}

fun is3OfKind(hand: String) = hand.toCharArray().toSet().map { hand.count { c -> c == it } }.contains(3)

fun is2Pair(hand: String) = hand.toCharArray().toSet().size == 3 && !is3OfKind(hand)

fun is1Pair(hand: String) = hand.toCharArray().toSet().size == 4

fun isHighCard(hand: String) = hand.toCharArray().toSet().size == 5

fun main() {
    val hands = mutableMapOf<String, Int>()
    val sortedHands: MutableList<MutableList<String>> = MutableList(7) { mutableListOf() }
    val charOrder = if (isPartTwo) {
        listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J")
    } else {
        listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2")
    }
    File(System.getProperty("user.dir")+ "\\src\\day_7\\data").forEachLine { line ->
        val d = line.split(" ")
        hands[d[0]] = d[1].toInt()
    }
    hands.keys.forEach {
        if (isPartTwo && it.contains("J")) {
            val charsNoJ = it.split("").filter { c -> c != "J" && c != "" }
            val numDistinctChars = charsNoJ.toSet().size
            val highestGroup = it.toCharArray().toSet().maxOfOrNull { d -> it.count { c -> d == c } }!!
            if (numDistinctChars < 2) sortedHands[6].add(it)
            else if (numDistinctChars == 2 && highestGroup == charsNoJ.size - 1) sortedHands[5].add(it)
            else if (numDistinctChars == 2 && highestGroup >= 1) sortedHands[4].add(it)
            else if (numDistinctChars == 3 && highestGroup >= 1) sortedHands[3].add(it)
            else if (numDistinctChars == charsNoJ.size) sortedHands[1].add(it)
        } else {
            if (is5OfKind(it)) sortedHands[6].add(it)
            else if (is4OfKind(it)) sortedHands[5].add(it)
            else if (isFullHouse(it)) sortedHands[4].add(it)
            else if (is3OfKind(it)) sortedHands[3].add(it)
            else if (is2Pair(it)) sortedHands[2].add(it)
            else if (is1Pair(it)) sortedHands[1].add(it)
            else if (isHighCard(it)) sortedHands[0].add(it)
        }
    }

    sortedHands.forEach {
        it.sortByDescending { s -> s.split("").filter { c -> c != "" }.joinToString("") { c -> (charOrder.indexOf(c) + 1).toString().padStart(2, '0') }.toInt() }
    }

    println(sortedHands.flatten().mapIndexed { i, h -> hands.getOrDefault(h, 0) * (i+1) }.sum())
}