fun main() {
    fun part1(input1: List<String>): Long {
        val input = parse(input1)
        var sumId = 0L
        for (pair in input) {
            for (id in pair.first..pair.second) {
                if (id.toString().length % 2 == 0) {
                    var idFound = true
                    val i = id.toString().length / 2
                    val idString = id.toString().chunked(i)
                    for (j in 1..<idString.size) {
                        if (idString[j - 1] != idString[j]) {
                            idFound = false
                            break
                        }
                    }
                    if (idFound) {
                        sumId += id
                    }
                }
            }
        }
        return sumId
    }

    fun part2(input2: List<String>): Long {
        val input = parse(input2)
        var sumId = 0L
        for (pair in input) {
            for (id in pair.first..pair.second) {
                if (id.toString().length == 1) continue
                var idFound = true
                for (i in 1..10.coerceAtMost(id.toString().length / 2)) {
                    idFound = true
                    val idString = id.toString().chunked(i)
                    for (j in 1..<idString.size) {
                        if (idString[j - 1] != idString[j]) {
                            idFound = false
                            break
                        }
                    }
                    if (idFound) {
                        break
                    }
                }
                if (idFound) {
                    sumId += id
                }
            }
        }
        return sumId
    }

    val input = readInput("Day02")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): List<Pair<Long, Long>> {
    return inputData[0].split(",").map { it.split("-") }.map{ Pair(it[0].toLong(), it[1].toLong()) }.toList()
}