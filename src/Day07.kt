var inputToShow = mutableListOf<MutableList<String>>()

var memo = mutableListOf<Pair<Pair<Int, Int>, Long>>()

fun List<List<String>>.goDownSplits(start: Pair<Int, Int>, splitPositions: MutableSet<Pair<Int, Int>>): MutableSet<Pair<Int, Int>> {
    for (i in start.first..<this.size) {
        if (inputToShow[i][start.second] == ".")
            inputToShow[i][start.second] = "|"
        val row = this[i]
        if (row[start.second] == "^") {
            var toBreak = false
            val doContinue = splitPositions.add(Pair(i, start.second))
            if (!doContinue) break
            if (start.second - 1 >= 0) {
                splitPositions.addAll(this.goDownSplits(Pair(i, start.second - 1), splitPositions))
                toBreak = true
            }
            if (start.second + 1 < this[0].size) {
                splitPositions.addAll(this.goDownSplits(Pair(i, start.second + 1), splitPositions))
                toBreak = true
            }
            if (toBreak) {
                return splitPositions
            }
        }
    }
    return mutableSetOf()
}

fun List<List<String>>.goDownPaths(start: Pair<Int, Int>): Long {
    var splitCount = 0L
    for (i in start.first..<this.size) {
        val row = this[i]
        if (row[start.second] == "^") {
            var toBreak = false
            if (start.second - 1 >= 0) {
                memo.filter { it.first == Pair(i, start.second - 1) }.let {
                    if (it.isNotEmpty()) {
                        splitCount += it.last().second
                    } else {
                        val res = this.goDownPaths(Pair(i, start.second - 1))
                        memo.add(Pair(Pair(i, start.second - 1), res))
                        splitCount += res
                    }
                }
                toBreak = true
            }
            if (start.second + 1 < this[0].size) {
                memo.filter { it.first == Pair(i, start.second + 1) }.let {
                    if (it.isNotEmpty()) {
                        splitCount += it.last().second
                    } else {
                        val res = this.goDownPaths(Pair(i, start.second + 1))
                        memo.add(Pair(Pair(i, start.second + 1), res))
                        splitCount += res
                    }
                }
                toBreak = true
            }
            if (toBreak) {
                break
            }
        }
        if (i == this.size - 1) {
            splitCount++
        }
    }
    return splitCount
}

fun main() {
    fun part1(input1: List<String>): Int {
        val input = parse(input1)
        inputToShow = input.toMutableList().map { it.toMutableList() }.toMutableList()
        var start: Pair<Int, Int>
        val splitPositions = mutableSetOf<Pair<Int, Int>>()
        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, col ->
                if (col == "S") {
                    start = Pair(i, j)
                    splitPositions.addAll(input.goDownSplits(start, splitPositions))
                }
            }
        }
        return splitPositions.size
    }

    fun part2(input2: List<String>): Long {
        val input = parse(input2)
        var start: Pair<Int, Int>
        var splitCount = 0L
        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, col ->
                if (col == "S") {
                    start = Pair(i, j)
                    splitCount = input.goDownPaths(start)
                }
            }
        }
        return splitCount
    }

    val input = readInput("Day07")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): List<List<String>> {
    return inputData.map { row -> row.toCharArray().map{ it.toString() } }
}