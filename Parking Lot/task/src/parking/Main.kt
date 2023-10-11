package parking

import kotlin.system.exitProcess

data class Car(val registration: String, val color: String)

class ParkingLot(private val size: Int) {
    private val spots: MutableList<Car?> = MutableList(size) { null }

    fun park(car: Car): Int {
        val free: Int = spots.indexOfFirst { it == null }
        if (free >= 0) spots[free] = car
        return free + 1
    }

    fun leave(spot: Int) {
        spots[spot - 1] = null
    }

    fun printStatus() {
        if (spots.all { it == null }) {
            println("Parking lot is empty.")
        } else {
            spots.forEachIndexed { index, car ->
                if (car != null) println("${index + 1} ${car.registration} ${car.color}")
            }
        }
    }

    fun spotsByColor(color: String) = spots.indices.filter {
            index -> spots[index]?.color?.lowercase() == color.lowercase()
    }

    fun printSpotsByColor(color: String) {
        val spotsWithColor = spotsByColor(color)
        if (spotsWithColor.isNotEmpty()) {
            println(spotsWithColor.map { it + 1 }.joinToString())
        } else {
            println("No cars with color $color were found.")
        }
    }

    fun printSpotsByRegistration(registration: String) {
        val spotNr = spots.indexOfFirst { it?.registration == registration }
        if (spotNr >= 0) {
            println(spotNr + 1)
        } else {
            println("No cars with registration number $registration were found.")
        }
    }

    private fun registrationByColor(color: String) = spots.filter {
        it?.color?.lowercase() == color.lowercase()
    }

    fun printRegistrationByColor(color: String) {
        val found = registrationByColor(color)
        if (found.isNotEmpty()) {
            println(found.map { it?.registration }.joinToString())
        } else {
            println("No cars with color $color were found.")
        }
    }
}

fun main() {
    var parkingLot: ParkingLot? = null

    while (true) {
        val commandLine = readln().split("\\s+".toRegex())
        val command = commandLine.first()

        if (command == "exit") { exitProcess(0) }
        if (parkingLot != null) {
            when (command) {
                "create" -> {
                    val size = commandLine[1].toInt()
                    parkingLot = ParkingLot(size)
                    println("Created a parking lot with $size spots.")
                }

                "park" -> {
                    val registration = commandLine[1]
                    val color = commandLine[2]
                    val car = Car(registration, color)
                    val spot = parkingLot.park(car)

                    if (spot >= 1) {
                        println("${car.color} car parked in spot $spot.")
                    } else {
                        println("Sorry, the parking lot is full.")
                    }
                }

                "leave" -> {
                    val spotNr = commandLine[1].toInt()
                    parkingLot.leave(spotNr)
                    println("Spot $spotNr is free.")
                }

                "status" -> {
                    parkingLot.printStatus()
                }

                "spot_by_color" -> parkingLot.printSpotsByColor(commandLine[1])
                "spot_by_reg" -> parkingLot.printSpotsByRegistration(commandLine[1])
                "reg_by_color" -> parkingLot.printRegistrationByColor(commandLine[1])
            }
        } else {
            if (command == "create") {
                val size = commandLine[1].toInt()
                parkingLot = ParkingLot(size)
                println("Created a parking lot with $size spots.")
            } else {
                println("Sorry, a parking lot has not been created.")
            }
        }
    }
}
