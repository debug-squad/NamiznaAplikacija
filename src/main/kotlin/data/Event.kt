package data

@kotlinx.serialization.Serializable
data class Event(
    val _id: String,
    val title: String,
    val description: String,
    val organization: String?,
    val contact: String?,
    val price: String?,
    val tags: List<String>,
    val image_url: String,
    val site_url: String?,
    val location: ILocation,
    val date_start: String,
    val date_end: String?,
    val attendace: Int,
    val created: String,
    val modified: String
)