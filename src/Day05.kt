fun rangesOverlap(firstRange: Pair<Long, Long>, secondRange: Pair<Long, Long>): Boolean {
    return !((firstRange.first < secondRange.first && firstRange.second < secondRange.first)
            || (firstRange.first > secondRange.second && firstRange.second > secondRange.second))
}

fun main() {
    fun part1(input1: List<String>): Int {
        val input = parse(input1)
        var freshCount = 0
        input.second.forEach { id ->
            freshCount += if (input.first.any { (a, b) -> id in a..b }) 1 else 0
        }
        return freshCount
    }

    fun part2(input2: List<String>): Long {
        val input = parse(input2)
        val freshRangeList = mutableListOf<Pair<Long, Long>>()
        input.first.forEach { (a, b) ->
            var updated = false
            freshRangeList.forEachIndexed { i, (fa, fb) ->
                if (rangesOverlap(a to b, fa to fb)) {
                    freshRangeList[i] = Pair(minOf(fa, a), maxOf(fb, b))
                    updated = true
                }
            }
            if (!updated) {
                freshRangeList.add(Pair(a, b))
            }
        }
        freshRangeList.sortBy { it.first }
        var reOrdered: Boolean
        do {
            reOrdered = true
            var i = 0
            var maxIndices = freshRangeList.size
            while (i < maxIndices) {
                val (fa, fb) = freshRangeList[i]
                var j = i + 1
                while (j < maxIndices) {
                    val (sa, sb) = freshRangeList[j]
                    if (rangesOverlap(fa to fb, sa to sb)) {
                        freshRangeList[i] = Pair(
                            minOf(fa, sa),
                            maxOf(fb, sb)
                        )
                        freshRangeList.removeAt(j)
                        reOrdered = false
                        maxIndices--
                    }
                    j++
                }
                i++
            }
        } while (!reOrdered)
        var allFreshCount = 0L
        freshRangeList.forEach { (a, b) -> allFreshCount += (b - a + 1) }
        return allFreshCount
    }

    val input = readInput("Day05")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): Pair<List<Pair<Long, Long>>, List<Long>> {
    val ranges = mutableListOf<Pair<Long, Long>>()
    val ids = mutableListOf<Long>()
    var switched = false
    inputData.forEach { line ->
        if (line.trim() == "") {
            switched = true
        } else {
            if (switched) {
                ids.add(line.toLong())
            } else {
                line.split("-").map { it.toLong() }.let {
                    ranges.add(Pair(it[0], it[1]))
                }
            }
        }
    }
    return Pair(ranges, ids)
}