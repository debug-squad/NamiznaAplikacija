// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Aggregates.*
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Projections.*
import com.mongodb.client.model.Sorts.*
import com.mongodb.client.model.Updates.*
import com.mongodb.reactivestreams.client.MongoClients
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.bson.Document

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.logging.Level
import java.util.logging.Logger

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App() {

    MaterialTheme {

        var title by remember { mutableStateOf("") };
        var description by remember { mutableStateOf("") };
        var date_start by remember { mutableStateOf("") };
        var date_end by remember { mutableStateOf("") };
        var latitude by remember { mutableStateOf("") };
        var longitude by remember { mutableStateOf("") };
        var organization by remember { mutableStateOf("") };
        var contact by remember { mutableStateOf("") };
        var price by remember { mutableStateOf("") };
        var tags by remember { mutableStateOf("") };
        var site_url by remember { mutableStateOf("") };
        var image_url by remember { mutableStateOf("") };
        var curent by remember { mutableStateOf(0) };

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )
                    OutlinedTextField(
                        value = date_start,
                        onValueChange = { date_start = it },
                        label = { Text("Start date") }
                    )
                    OutlinedTextField(
                        value = date_end,
                        onValueChange = { date_end = it },
                        label = { Text("End date") }
                    )
                    OutlinedTextField(
                        value = latitude,
                        onValueChange = { latitude = it },
                        label = { Text("Location: latitude") }
                    )
                    OutlinedTextField(
                        value = longitude,
                        onValueChange = { longitude = it },
                        label = { Text("Location: longitude") }
                    )
               // }
                //Column {
                    OutlinedTextField(
                        value = organization,
                        onValueChange = { organization = it },
                        label = { Text("Organization") }
                    )
                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it },
                        label = { Text("Contact") }
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") }
                    )
                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text("Tags") }
                    )
                    OutlinedTextField(
                        value = site_url,
                        onValueChange = { site_url = it },
                        label = { Text("Site url") }
                    )
                    OutlinedTextField(
                        value = image_url,
                        onValueChange = { image_url = it },
                        label = { Text("Image url") }
                    )
                    Button(
                        onClick = {
                            addEvent(title, description, date_start, date_end, latitude.toDouble(), longitude.toDouble(), organization, contact, price, tags, site_url, image_url)
                        }) {
                        Text("Dodaj")
                    }
                    Row {
                        Button(
                            onClick = {
                                val event = openWindows()
                                title = event[curent].title
                                description = event[curent].description
                                date_start = event[curent].date_start.toString()
                                date_end = event[curent].date_end.toString()
                                latitude = event[curent].location?.coordinates?.get(0).toString()
                                longitude = event[curent].location?.coordinates?.get(1).toString()
                                organization = event[curent].organization.toString()
                                contact = event[curent].contact.toString()
                                price = event[curent].price.toString()
                                tags = event[curent].tags.toString()
                                site_url = event[curent].site_url.toString()
                                image_url = event[curent].image_url.toString()
                                if (curent != 0) {
                                    curent--
                                }
                            }
                        ) {
                            Text("Nazaj ")
                        }
                        Button(
                            onClick = {
                                val event = openWindows()
                                title = event[curent].title
                                description = event[curent].description
                                date_start = event[curent].date_start.toString()
                                date_end = event[curent].date_end.toString()
                                latitude = event[curent].location?.coordinates?.get(0).toString()
                                longitude = event[curent].location?.coordinates?.get(1).toString()
                                organization = event[curent].organization.toString()
                                contact = event[curent].contact.toString()
                                price = event[curent].price.toString()
                                tags = event[curent].tags.toString()
                                site_url = event[curent].site_url.toString()
                                image_url = event[curent].image_url.toString()
                                if (curent != event.size - 1) {
                                    curent++
                                }
                            }
                        ) {
                            Text("Naslednji")
                        }

                    }
                    Button(
                        onClick = {
                            val event = openWindows()
                            updateEvent(event[curent]._id,title, description, date_start, date_end, latitude.toDouble(), longitude.toDouble(), organization, contact, price, tags, site_url, image_url)
                        }
                    ){
                        Text("Posodobi")
                    }
                }
            }


        }
    }

}

@kotlinx.serialization.Serializable
data class Event(
    val _id: String,
    val title: String,
    val description: String,
    val organization: String?,
    val contact: String?,
    val price: String?,
    val tags: List<String>?,
    val image_url: String?,
    val site_url: String?,
    val location: Location?,
    val date_start: String?,
    val date_end: String?,
    val attendace: Int?,
    val created: String,
    val modified: String

)
@kotlinx.serialization.Serializable
data class Location(
    val type: String,
    val coordinates: List<Double>
)

fun openWindows():List<Event>{
    val (_, _, result) = Fuel.get("https://mb-hub.herokuapp.com/event").responseString()
    if (result is Result.Success) {
        //val jj = Json.parseToJsonElement(result.get())
        val test = Json{ignoreUnknownKeys = true}.decodeFromString<List<Event>>(result.get())

        //val token = jj.jsonObject["data"]!!.jsonObject["token"]!!.jsonPrimitive!!.content
        return test
    } else {
        return emptyList()
    }
}

fun token(): String{
    val username = "test123"
    val password = "test123"
    val params = listOf("client_name" to username, "password" to password)
    val (_, _, result) = Fuel.post("https://mb-hub.herokuapp.com/client/login", params).responseString()
    return if (result is Result.Success) {
        val jj = Json.parseToJsonElement(result.get())
        val token = jj.jsonObject["data"]!!.jsonObject["token"]!!.jsonPrimitive!!.content
        token
    } else {
        "error"
    }
}

fun addEvent(title: String, description: String, date_start: String, date_end: String, latitude: Double, longitude: Double, organization: String, contact: String, price: String, tags: String, site_url: String, image_url: String){
    val token = token()

    val jsonEvent = buildJsonObject{
        put("title", title)
        put("description", description)
        put("date_start", date_start)
        put("date_end",date_end)
        putJsonObject("location"){
            put("type", "Point")
            putJsonArray("coordinates"){
                add(longitude)
                add(latitude)
            }
        }
        put("organization", organization)
        put("contact", contact)
        put("price", price)
        putJsonArray("tags"){
            add(tags)
        }
        put("site_url",site_url)
        put("image_url", image_url)
    }

    val params = listOf("client_name" to " ", "password" to " ")

    val (_, _, result) = Fuel.post("https://mb-hub.herokuapp.com/event").header("Authorization" to "Bearer $token", "Content-type" to "application/json" ).body(jsonEvent.toString()).responseString()
}

fun updateEvent(id: String, title: String, description: String, date_start: String, date_end: String, latitude: Double, longitude: Double, organization: String, contact: String, price: String, tags: String, site_url: String, image_url: String){
    val token = token()

    val jsonEvent = buildJsonObject{
        put("title", title)
        put("description", description)
        put("date_start", date_start)
        put("date_end",date_end)
        putJsonObject("location"){
            put("type", "Point")
            putJsonArray("coordinates"){
                add(longitude)
                add(latitude)
            }
        }
        put("organization", organization)
        put("contact", contact)
        put("price", price)
        putJsonArray("tags"){
            add(tags)
        }
        put("site_url",site_url)
        put("image_url", image_url)
    }

    val (_, _, result) = Fuel.put("https://mb-hub.herokuapp.com/event/${id}").header("Authorization" to "Bearer $token", "Content-type" to "application/json" ).body(jsonEvent.toString()).responseString()
}



@OptIn(InternalCoroutinesApi::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

