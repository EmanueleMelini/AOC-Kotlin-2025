import java.io.FileOutputStream
import java.io.Serializable
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.TimeSource

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Quadruple class for Kotlin
 */
data class Quadruple<A,B,C,D>(var first: A, var second: B, var third: C, var fourth: D): java.io.Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"

    fun checkIsCopy(another: Quadruple<A, B, C, D>): Boolean {
        return setOf(first, second, third, fourth) == setOf(another.first, another.second, another.third, another.fourth)
    }
}

/**
 * Mutable Pair class for Kotlin
 */
data class MutablePair<A, B>(var first: A, var second: B): java.io.Serializable {
    override fun toString(): String = "($first, $second)"

    fun checkIsCopy(another: MutablePair<A, B>): Boolean {
        return setOf(first, second) == setOf(another.first, another.second)
    }
}

/**
 * Mutable Triple class for Kotlin
 */
data class MutableTriple<A, B, C>(var first: A, var second: B, var third: C): Serializable {
    override fun toString(): String = "($first, $second, $third)"

    fun checkIsCopy(another: MutableTriple<A, B, C>): Boolean {
        return setOf(first, second, third) == setOf(another.first, another.second, another.third)
    }
}

/**
 * Math expressions evaluation function
 */
fun evaluate(str: String): Double {

    data class Data(val rest: List<Char>, val value: Double)

    return object : Any() {

        fun parse(chars: List<Char>): Double {
            return getExpression(chars.filter { it != ' ' })
                .also { if (it.rest.isNotEmpty()) throw RuntimeException("Unexpected character: ${it.rest.first()}") }
                .value
        }

        private fun getExpression(chars: List<Char>): Data {
            var (rest, carry) = getTerm(chars)
            while (true) {
                when {
                    rest.firstOrNull() == '+' -> rest = getTerm(rest.drop(1)).also { carry += it.value }.rest
                    rest.firstOrNull() == '-' -> rest = getTerm(rest.drop(1)).also { carry -= it.value }.rest
                    else                      -> return Data(rest, carry)
                }
            }
        }

        private fun getTerm(chars: List<Char>): Data {
            var (rest, carry) = getFactor(chars)
            while (true) {
                when {
                    rest.firstOrNull() == '*' -> rest = getTerm(rest.drop(1)).also { carry *= it.value }.rest
                    rest.firstOrNull() == '/' -> rest = getTerm(rest.drop(1)).also { carry /= it.value }.rest
                    else                      -> return Data(rest, carry)
                }
            }
        }

        private fun getFactor(chars: List<Char>): Data {
            return when (val char = chars.firstOrNull()) {
                '+'              -> getFactor(chars.drop(1)).let { Data(it.rest, +it.value) }
                '-'              -> getFactor(chars.drop(1)).let { Data(it.rest, -it.value) }
                '('              -> getParenthesizedExpression(chars.drop(1))
                in '0'..'9', '.' -> getNumber(chars) // valid first characters of a number
                else             -> throw RuntimeException("Unexpected character: $char")
            }
        }

        private fun getParenthesizedExpression(chars: List<Char>): Data {
            return getExpression(chars)
                .also { if (it.rest.firstOrNull() != ')') throw RuntimeException("Missing closing parenthesis") }
                .let { Data(it.rest.drop(1), it.value) }
        }

        private fun getNumber(chars: List<Char>): Data {
            val s = chars.takeWhile { it.isDigit() || it == '.' }.joinToString("")
            return Data(chars.drop(s.length), s.toDouble())
        }

    }.parse(str.toList())

}

fun Int.toTernaryString(): String {
    var ternaryString = ""

    var number = this

    while (number != 0) {
        val module = number % 3
        number /= 3

        if (module < 0)
            number++

        val ternary = "${if (module < 0) module + (3 * -1) else module}"

        ternaryString += ternary

    }

    return ternaryString.reversed()

}

fun <A, B> doPartsWithTimes(input: A, part1: (A) -> B, part2: (A) -> B, initialTime: TimeSource.Monotonic.ValueTimeMark? = null) {

    val source = TimeSource.Monotonic
    val start = source.markNow()

    val part1Result = part1(input)
    println("Part 1 result: $part1Result")

    val part1Time = source.markNow()
    val part1Diff = part1Time - start

    println("Part 1 time: ${part1Diff.inWholeMilliseconds}ms")

    val part2Result = part2(input)
    println("Part 2 result: $part2Result")

    val part2Time = source.markNow()
    val part2Diff = part2Time - part1Time

    println("Part 2 time: ${part2Diff.inWholeMilliseconds}ms")

    val end = source.markNow()
    val diff = end - (initialTime ?: start)

    println("Total time: ${diff.inWholeMilliseconds}ms")

}

fun <A, B> parseAndDoPartsWithTimes(part1: (A) -> B, part2: (A) -> B, parseInput: () -> A) {

    val source = TimeSource.Monotonic
    val start = source.markNow()

    val parsedInput = parseInput()

    val parseTime = source.markNow()
    val parseDiff = parseTime - start

    println("Parse time: ${parseDiff.inWholeMilliseconds}ms")

    doPartsWithTimes(parsedInput, part1, part2, start)

}

fun getStaticDirXY(): List<String> {
    return listOf("UP", "RX", "DW", "LF")
}

fun String.getDirXY(): List<Int> {
    return when (this) {
        "UP" -> listOf(-1, 0)
        "RX" -> listOf(0, 1)
        "DW" -> listOf(1, 0)
        "LF" -> listOf(0, -1)
        else -> listOf(0, 0)
    }
}

fun String.switchDir(): String {
    return when (this) {
        "UP" -> "RX"
        "RX" -> "DW"
        "DW" -> "LF"
        "LF" -> "UP"
        else -> ""
    }
}

fun getStaticDiagonalDirXY(): List<String> {
    return listOf("UL", "UR", "DR", "DL")
}

fun String.getDiagonalDirXY(): List<Int> {
    return when (this) {
        "UL" -> listOf(-1, -1)
        "UR" -> listOf(-1, 1)
        "DR" -> listOf(1, 1)
        "DL" -> listOf(1, -1)
        else -> listOf(0, 0)
    }
}

fun String.switchDiagonalDir(): String {
    return when (this) {
        "UL" -> "UR"
        "UR" -> "DR"
        "DR" -> "DL"
        "DL" -> "UL"
        else -> ""
    }
}

fun String.getDirsFromDiagonal(): List<String> {
    return when (this) {
        "UL" -> listOf("LF", "UP")
        "UR" -> listOf("UP", "RX")
        "DR" -> listOf("RX", "DW")
        "DL" -> listOf("DW", "LF")
        else -> listOf("", "")
    }
}

fun getStaticArrowDirXY(): List<String> {
    return listOf("^", ">", "v", "<")
}

fun String.getArrowDirXY(): List<Int> {
    return when (this) {
        "^" -> listOf(-1, 0)
        ">" -> listOf(0, 1)
        "v" -> listOf(1, 0)
        "<" -> listOf(0, -1)
        else -> listOf(0, 0)
    }
}

fun String.switchArrowDir(): String {
    return when (this) {
        "^" -> ">"
        ">" -> "v"
        "v" -> "<"
        "<" -> "^"
        else -> ""
    }
}

fun printAndWrite(day: String, print: String) {
    val f = Path("src/${day}_dump.txt")
    FileOutputStream(f.toFile(), true).bufferedWriter().use {
        it.write(print)
        it.newLine()
        it.close()
    }
    println(print)
}

fun clearDump(day: String) {
    val f = Path("src/${day}_dump.txt")
    f.toFile().delete()
}