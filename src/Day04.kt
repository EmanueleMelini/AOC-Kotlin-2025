val directions = listOf(
    Pair(-1, -1),
    Pair(0, -1),
    Pair(1, -1),
    Pair(-1, 0),
    Pair(1, 0),
    Pair(-1, 1),
    Pair(0, 1),
    Pair(1, 1)
)

fun paperSolution(input: List<List<String>>, doAfterCount: (i: Int, j: Int) -> Unit) {
    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, col ->
            if (col == "@") {
                var countAdjacent = 0
                for (dir in directions) {
                    val nx = i + dir.first
                    val ny = j + dir.second
                    if (nx !in input.indices || ny !in input[0].indices) continue
                    if (input[nx][ny] == "@") countAdjacent++
                }
                if (countAdjacent in 0..3) doAfterCount(i, j)
            }
        }
    }
}

fun main() {
    fun part1(input1: List<String>): Int {
        val input = parse(input1)
        var sumPapers = 0
        paperSolution(input) { _, _ -> sumPapers ++ }
        return sumPapers
    }

    fun part2(input2: List<String>): Int {
        val input = parse(input2).map { it.toMutableList() }
        var sumPapers: Int
        val removedPapers = mutableListOf<Pair<Int, Int>>()
        var removedPapersCount = 0
        do {
            sumPapers = 0
            paperSolution(input) { i, j ->
                sumPapers++
                removedPapers.add(Pair(i, j))
            }
            removedPapers.forEach {
                input[it.first][it.second] = "."
            }
            removedPapersCount += removedPapers.size
            removedPapers.clear()
        } while (sumPapers > 0)
        return removedPapersCount
    }

    val input = readInput("Day04")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): List<List<String>> {
    return inputData.map { it.toCharArray().map { it2 -> it2.toString()} }
}