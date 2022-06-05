import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.json.Json
import com.github.kittinunf.result.Result
import data.Event
import data.EventAdd
import data.Login
import data.Response
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

object Api {
    var token: String? = login("test123", "test123")

    fun login(username: String, password: String): String? {
        val (_, _, result) = Fuel.post(
            "https://mb-hub.herokuapp.com/client/login",
            listOf("client_name" to username, "password" to password)
        ).responseString()
        return if (result is Result.Success) {
            token = Json { ignoreUnknownKeys = true }.decodeFromString<Response<Login>>(result.get()).data.token
            token
        } else {
            println("login: ${result.get()}")
            token
        }
    }

    fun getEvents(): List<Event>? {
        return try {
            val (_, _, result) = Fuel.get("https://mb-hub.herokuapp.com/event").responseString()
            if (result is Result.Success) {
                Json { ignoreUnknownKeys = true }.decodeFromString<List<Event>>(result.get())
            } else {
                println("getEvents: ${result.get()}")
                emptyList()
            }
        } catch (e: Exception) {
            println("getEvents: ${e.localizedMessage}")
            null
        }
    }

    fun addEvent(event: EventAdd): Event? {
        return try {
            val (_, _, result) = Fuel.post("https://mb-hub.herokuapp.com/event")
                .header("Authorization" to "Bearer $token", "Content-type" to "application/json")
                .body(Json.encodeToString(event))
                .responseString()
            if (result is Result.Success) {
                Json { ignoreUnknownKeys = true }.decodeFromString<Event>(result.get())
            } else {
                println("addEvent: ${result.get()}")
                null
            }
        } catch (e: Exception) {
            println("addEvent: ${e.localizedMessage}")
            null
        }
    }

    fun updateEvent(event: Event): Boolean {
        return try {
            val (_, _, result) = Fuel.put("https://mb-hub.herokuapp.com/event/${event._id}")
                .header("Authorization" to "Bearer $token", "Content-type" to "application/json")
                .body(Json.encodeToString(event))
                .responseString()
            if(result is Result.Success) {
                true
            } else {
                println(result.get())
                false
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
            false
        }
    }
}