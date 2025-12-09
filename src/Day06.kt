fun main() {
    fun part1(input1: List<String>): Long {
        val input = parse1(input1)
        var grandTotal = 0L
        for (j in 0..<input[0].size) {
            val operation = input[input.size - 1][j]
            var columnTotal = input[0][j].toLong()
            for (i in 1..<input.size -1) {
                when (operation) {
                    "*" -> {
                        columnTotal *= input[i][j].toLong()
                    }
                    "+" -> {
                        columnTotal += input[i][j].toLong()
                    }
                }
            }
            grandTotal += columnTotal
        }
        return grandTotal
    }

    fun part2(input2: List<String>): Long {
        val input = parse2(input2)
        val matrix = mutableListOf<List<Long>>()
        val currentM = mutableListOf<Long>()
        for (i in input.first[0].size -1 downTo 0) {
            var thisNum = ""
            for (j in 0..<input.first.size) {
                thisNum += input.first[j][i].trim()
            }
            if (thisNum.isNotBlank()) {
                currentM.add(thisNum.toLong())
            }
            if (thisNum.isBlank() || i == 0) {
                matrix.add(currentM.toMutableList())
                currentM.clear()
            }
        }
        val operations = input.second.reversed()
        var grandTotal = 0L
        for (i in 0..<matrix.size) {
            val operation = operations[i]
            var columnTotal = matrix[i][0]
            for (j in 1..<matrix[i].size) {
                when (operation) {
                    "*" -> {
                        columnTotal *= matrix[i][j]
                    }
                    "+" -> {
                        columnTotal += matrix[i][j]
                    }
                }
            }
            grandTotal += columnTotal
        }
        return grandTotal
    }

    val input = readInput("Day06")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse1(inputData: List<String>): List<List<String>> {
    return inputData.map { row -> row.split(" ").filter { it.isNotBlank() } }
}

private fun parse2(inputData: List<String>): Pair<List<List<String>>, List<String>> {
    val rows = inputData.map { row -> row.toCharArray().map { it.toString() } }
    return Pair(rows.filterIndexed { i, _ -> i != rows.size -1 }, rows[rows.size - 1].filter { it.isNotBlank() })
}