import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(a: Triple<Int, Int, Int>, b: Triple<Int, Int, Int>): Double {
    return sqrt((a.first - b.first).toDouble().pow(2) + (a.second - b.second).toDouble().pow(2) + (a.third - b.third).toDouble().pow(2))
}

fun calcDistances(boxes: List<Triple<Int, Int, Int>>): List<Triple<Triple<Int, Int, Int>, Triple<Int, Int, Int>, Double>> {
    val matrix = mutableListOf<Triple<Triple<Int, Int, Int>, Triple<Int, Int, Int>, Double>>()
    boxes.forEachIndexed { i, box ->
        for (j in i + 1..<boxes.size) {
            matrix.add(Triple(box, boxes[j], euclideanDistance(box, boxes[j])))
        }
    }
    matrix.sortBy { it.third }
    return matrix
}

fun MutableList<MutableSet<Triple<Int, Int, Int>>>.connectBoxes(boxA: Triple<Int, Int, Int>, boxB: Triple<Int, Int, Int>) {
    val foundA = this.findCircuit(boxA)
    val foundB = this.findCircuit(boxB)
    if (foundA < 0 && foundB < 0) {
        this.add(mutableSetOf(boxA, boxB))
    } else if (foundA >= 0 && foundB >= 0 && foundA != foundB) {
        this[foundA].addAll(this[foundB])
        this.removeAt(foundB)
    } else if (foundA < 0) {
        this[foundB].add(boxA)
    } else {
        this[foundA].add(boxB)
    }
}

fun MutableList<MutableSet<Triple<Int, Int, Int>>>.findCircuit(box: Triple<Int, Int, Int>): Int {
    for ((i, circuit) in this.withIndex()) {
        if (circuit.contains(box)) return i
    }
    return -1
}

fun main() {
    fun part1(input1: List<String>): Int {
        val input = parse(input1)
        val matrix = calcDistances(input)
        val circuits = mutableListOf<MutableSet<Triple<Int, Int, Int>>>()
        circuits.add(mutableSetOf(matrix[0].first, matrix[0].second))
        for (i in 1..1000) {
            val connection = matrix[i]
            var connected = false
            var circuitFound = -1
            val circuitsIterator = circuits.listIterator()
            while (circuitsIterator.hasNext()) {
                val c = circuitsIterator.nextIndex()
                val circuit = circuitsIterator.next()
                if (circuit.contains(connection.first) && circuit.contains(connection.second)) continue
                if (circuit.contains(connection.first) || circuit.contains(connection.second)) {
                    connected = true
                    circuit.addAll(listOf(connection.first, connection.second))
                    if (circuitFound != -1) {
                        circuits[circuitFound].addAll(circuit)
                        circuitsIterator.remove()
                    } else {
                        circuitFound = c
                    }
                }
            }
            if (!connected) {
                circuits.add(mutableSetOf(connection.first, connection.second))
            }
        }
        circuits.sortByDescending { it.size }
        return circuits[0].size * circuits[1].size * circuits[2].size
    }

    fun part2(input2: List<String>): Long {
        val input = parse(input2)
        val matrix = calcDistances(input)
        val circuits = mutableListOf<MutableSet<Triple<Int, Int, Int>>>()
        for (connection in matrix) {
            circuits.connectBoxes(connection.first, connection.second)
            if (circuits.size == 1 && circuits[0].size == input.size) return connection.first.first.toLong() * connection.second.first.toLong()
        }
        return -1
    }

    val input = readInput("Day08")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): List<Triple<Int, Int, Int>> {
    return inputData.map { it.split(",") }.map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
}