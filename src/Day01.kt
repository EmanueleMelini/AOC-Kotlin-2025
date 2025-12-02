import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        var timesAt0 = 0
        var currentPos = 50
        for (rotation in input) {
            val rotationValue = rotation.replace("[^0-9]".toRegex(), "").toInt() * if (rotation.replace("[0-9]".toRegex(), "") == "L") -1 else 1
            currentPos += rotationValue
            if (currentPos < -99) currentPos %= 100
            if (currentPos < 0) currentPos = 100 - abs(currentPos)
            if (currentPos > 99) currentPos %= 100
            if (currentPos == 0) timesAt0++
        }
        return timesAt0
    }

    fun part2(input: List<String>): Int {
        var timesAt0 = 0
        var currentPos = 50
        for (rotation in input) {
            val rotationValue = rotation.replace("[^0-9]".toRegex(), "").toInt()
            val rotationDir = rotation.replace("[0-9]".toRegex(), "") == "L"
            (1..rotationValue).forEach { _ ->
                if (rotationDir) currentPos-- else currentPos++
                if (currentPos < -99) currentPos %= 100
                if (currentPos < 0) currentPos = 100 - abs(currentPos)
                if (currentPos > 99) currentPos %= 100
                if (currentPos == 0) timesAt0++
            }
        }
        return timesAt0
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
