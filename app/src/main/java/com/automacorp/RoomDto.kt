package com.automacorp

data class RoomDto(
    val id: Long,
    val name: String,
    val floor: String,
    val currentTemperature: Double?,
    val targetTemperature: Double?,
    val windows: List<WindowDto>
)
data class RoomCommandDto(
    val name: String,
    val currentTemperature: Double?,
    val targetTemperature: Double?,
    val floor: Int = 1,
    val buildingId: Long = -10
)



data class WindowDto(
    val id: Long,
    val name: String,
    val status: String? // Make status nullable
)