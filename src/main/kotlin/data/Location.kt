package data

@kotlinx.serialization.Serializable
@kotlinx.serialization.SerialName("Point")
data class Location(val coordinates: List<Double>) : ILocation()