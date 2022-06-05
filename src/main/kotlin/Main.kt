// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.Event
import data.EventAdd
import data.Location

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    MaterialTheme {
        var error by remember { mutableStateOf<String?>(null) }

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var date_start by remember { mutableStateOf("2022-06-05T13:37:09.376Z") }
        var date_end by remember { mutableStateOf("") }
        var latitude by remember { mutableStateOf("46.50735") }
        var longitude by remember { mutableStateOf("15.63816") }
        var organization by remember { mutableStateOf("") }
        var contact by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var tags by remember { mutableStateOf("") }
        var site_url by remember { mutableStateOf("") }
        var image_url by remember { mutableStateOf("https://www.visitmaribor.si/media/9867/dogodek-botanicni-vrt3.jpg?anchor=center&mode=crop&width=640&height=480&quality=95") }

        fun setValues(event: Event?) {
            title = event?.title ?: ""
            description = event?.description ?: ""
            date_start = event?.date_start ?: ""
            date_end = event?.date_end ?: ""
            latitude = (event?.location as? Location)?.coordinates?.get(0)?.toString() ?: ""
            longitude = (event?.location as? Location)?.coordinates?.get(1)?.toString() ?: ""
            organization = event?.organization ?: ""
            contact = event?.contact ?: ""
            price = event?.price ?: ""
            tags = event?.tags?.joinToString(",") ?: ""
            site_url = event?.site_url ?: ""
            image_url = event?.image_url ?: ""
        }

        fun toEventAdd(): EventAdd? {
            val latitude = try {
                latitude.toDouble()
            } catch (e: Exception) {
                error = "Latitude not a number"; return null
            }
            val longitude = try {
                longitude.toDouble()
            } catch (e: Exception) {
                error = "Longitude not a number"; return null
            }
            return EventAdd(
                title = title,
                description = description,
                date_start = date_start,
                date_end = date_end.ifEmpty { null },
                location = Location(coordinates = listOf(latitude, longitude)),
                organization = organization.ifEmpty { null },
                contact = contact.ifEmpty { null },
                price = price.ifEmpty { null },
                tags = tags.split(','),
                site_url = site_url.ifEmpty { null },
                image_url = image_url,
            )
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                    if (error != null) Text("Error: $error")


                    Button(
                        onClick = {
                            error = null
                            val event = toEventAdd()
                            if (event != null) {
                                val event = Api.addEvent(event)
                                if (event == null) {
                                    error = "Napaka pri vstavljanju"
                                }
                            }
                        }) {
                        Text("Kreiraj")
                    }

                    Row {
                        Button(
                            onClick = {
                                error = null
                                setValues(null)
                            }) {
                            Text("Resetiraj")
                        }
                    }
                }
            }
        }
    }
}

fun main() = singleWindowApplication(
    title = "Scrollbars",
    // state = WindowState(width = 250.dp, height = 400.dp)
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    App()
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }
}