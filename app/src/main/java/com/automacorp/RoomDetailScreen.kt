package com.automacorp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoomDetailScreen(

    roomId: Long,
    viewModel: RoomDetailViewModel,
    navigateBack: () -> Unit,
    roomIdOrName: Boolean
) {
    val roomState by viewModel.roomState.collectAsState()


    LaunchedEffect(roomId) {
        viewModel.loadRoomDetails(roomId)
    }
    LaunchedEffect(roomIdOrName) {
        Log.d("RoomDetailScreen", "RoomIdOrName is: $roomIdOrName")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Automacorp") },
                backgroundColor = colorResource(id = R.color.dark_blue),
                contentColor = colorResource(id = R.color.white),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(painter = painterResource(id = R.drawable.back_iv), contentDescription = "Back Press" )
                    }
                },
            )
        }
    ) { innerPadding ->
        when {
            roomState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
            roomState.error != null -> {
                ErrorDisplay(
                    message = roomState.error ?: "Unknown error",
                    onRetry = { viewModel.loadRoomDetails(roomId) }
                )
            }
            roomState.room != null -> {
                val room = roomState.room!!

                RoomDetailsView(room, roomIdOrName, viewModel, roomId)
            }
        }
    }
}

@Composable
fun RoomDetailsView(
    room: RoomDto,
    roomIdOrName: Boolean,
    viewModel: RoomDetailViewModel,
    roomId: Long
) {
    if (!roomIdOrName) {
        NoRoomsFoundView()
    } else {
        RoomDetailContent(room, viewModel, roomId)
    }
}

@Composable
fun NoRoomsFoundView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "No Rooms Found",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun RoomDetailContent(room: RoomDto, viewModel: RoomDetailViewModel, roomId: Long) {
    var targetTemperature by remember { mutableStateOf(room.targetTemperature?.toFloat() ?: 20f) }

    LaunchedEffect(targetTemperature) {
        val roomCommandDto = RoomCommandDto(
            name = room.name,
            currentTemperature = room.currentTemperature?.toDouble(),
            targetTemperature = targetTemperature.toDouble()
        )
        viewModel.updateRoom(roomId, roomCommandDto)
        println("New Target Temperature: $targetTemperature")
    }
    Column(modifier = Modifier.padding(4.dp)) {
        Spacer(modifier = Modifier.height(0.dp)) // Space at the top

        RoomDetailCard("Room name", room.name)
        RoomDetailText("Current temperature", "${room.currentTemperature}°C")
        RoomDetailSlider(
            title = "Target temperature",
            initialValue = room.targetTemperature?.toFloat() ?: 20f,
            onValueChange = { newValue ->
                targetTemperature = newValue // Update the slider value

                println("New Target Temperature: $newValue")
            }
        )
        Text(
            text = "${room.targetTemperature ?: "N/A"} °C",
            modifier = Modifier.padding(15.dp,0.dp,0.dp,0.dp),
        )
    }
}

@Composable
fun RoomDetailCard(title: String, content: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = 3.dp,
            border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_blue))
        ) {
            Text(content, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun RoomDetailText(title: String, content: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp))
        Spacer(modifier = Modifier.height(5.dp))
        Text(content)
    }
}

@Composable
fun RoomDetailSlider(
    title: String,
    initialValue: Float,
    onValueChange: (Float) -> Unit
) {
    // State to hold the current slider value, initialized with the provided default value
    var sliderValue by remember { mutableStateOf(initialValue) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Display the title of the slider
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Display the slider and allow updates
        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue  // Update the state with the new value
                onValueChange(newValue) // Notify the parent composable about the change
            },
            valueRange = 10f..99f, // Set the range of the slider
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.dark_blue),
                activeTrackColor = colorResource(id = R.color.dark_blue)
            )
        )
    }
}


@Composable
fun ErrorDisplay(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(message.equals("End of input")){
            Text(text = "No Rooms Found With this ID or Name", fontWeight = FontWeight.Bold)
        }
        else{
            Text(text = "Error: $message", fontWeight = FontWeight.Bold)

        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}
