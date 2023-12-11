package day_9

import java.io.File

const val isPartTwo = true
val datasets = mutableListOf<MutableList<MutableList<Int>>>()

fun generateDerivatives(i: Int) {
    while (!datasets[i].last().all { it == 0 }) {
        val lastLine = datasets[i].last()
        val derivatives = mutableListOf<Int>()
        for (j in 0..lastLine.size - 2) derivatives.add(lastLine[j+1] - lastLine[j])
        datasets[i].add(derivatives)
    }
}

fun extrapolate(i: Int) {
    generateDerivatives(i)
    for (j in datasets[i].size - 1 downTo 1) {
        if (isPartTwo) {
            datasets[i][j - 1].add(0, datasets[i][j - 1].first() - datasets[i][j].first())
        } else {
            datasets[i][j - 1].add(datasets[i][j - 1].last() + datasets[i][j].last())
        }
    }
}

fun main() {
    File(System.getProperty("user.dir")+ "\\src\\day_9\\data").forEachLine { line ->
      datasets.add(mutableListOf(line.split(" ").map { it.toInt() }.toMutableList()))
    }
    for (i in datasets.indices) extrapolate(i)
    datasets.forEach { println(it) }
    println(datasets.sumOf { if (isPartTwo) it[0].first() else it[0].last() })
}