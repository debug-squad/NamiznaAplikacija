package data

@kotlinx.serialization.Serializable
data class Response<T>(val success: Boolean, val data: T)