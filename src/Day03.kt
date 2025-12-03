fun main() {
    fun part1(input1: List<String>): Int {
        val input = parse(input1)
        var sumPower = 0
        for (bank in input) {
            var currentBatteryPos = 1
            var maxBankPower = 0
            for (i in 0..<bank.size) {
                val firstBattery = bank[i]
                for (j in currentBatteryPos..<bank.size) {
                    val secondBattery = bank[j]
                    val batteryPower = (firstBattery + secondBattery).toInt()
                    maxBankPower = maxOf(maxBankPower, batteryPower)
                }
                currentBatteryPos++
            }
            val oldSum = sumPower
            sumPower += maxBankPower
            println("sum = $oldSum + $maxBankPower = $sumPower")
        }
        return sumPower
    }

    fun part2(input2: List<String>): Long {
        val input = parse(input2)
        var sumPower = 0L
        for (bank in input) {
            var maxBankPower = ""
            for (battery in bank) {
                val batteryJoin = maxBankPower + battery
                if (batteryJoin.length <= 12) {
                    maxBankPower = batteryJoin
                } else {
                    var foundMax = 0L
                    for (i in batteryJoin.indices) {
                        foundMax = maxOf(foundMax, batteryJoin.removeRange(i, i + 1).toLong())
                    }
                    maxBankPower = foundMax.toString()
                }
            }
            sumPower += maxBankPower.toLong()
        }
        return sumPower
    }

    val input = readInput("Day03")
    doPartsWithTimes(input, ::part1, ::part2)

}

private fun parse(inputData: List<String>): List<List<String>> {
    return inputData.map { it.toCharArray().toList().map { it2 -> it2.toString() } }
}