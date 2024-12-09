package com.automacorp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoomListScreen(
    rooms: List<RoomDto>,
    navigateBack: () -> Unit,
    openRoom: (id: Long) -> Unit

) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val googleIntent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HemaDevikaPutta")) }
    val sheetStateRooms = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // State to hold the entered recipient email
    var recipientEmail by remember { mutableStateOf("") }
    var recipientIdOrName by remember { mutableStateOf("") }


    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Enter Recipient Email",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = recipientEmail,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    onValueChange = { recipientEmail = it },
                    label = { Text("Enter your Email") },
                    placeholder = { Text("Please enter your email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.dark_blue),  // Change color when focused
                        unfocusedBorderColor = Color.Gray,  // Change color when unfocused
                        focusedLabelColor = colorResource(id = R.color.dark_blue),  // Change color of the label when focused
                        unfocusedLabelColor = Color.Gray,  // Change color of the label when unfocused
                        cursorColor = colorResource(id = R.color.dark_blue)  // Change the cursor color
                    )
                )


                Button(
                    onClick = {
                        if (recipientEmail.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter your email",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        else {
                            scope.launch {
                                sheetState.hide()
                                sendEmail(context ,recipientEmail)
                            }





//                        navController.navigate("second screen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.dark_blue),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    elevation = ButtonDefaults.elevation(8.dp)
                ) {
                    Text("Send Email")
                }
            }
        }
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetStateRooms,
            sheetContent = {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Recipient Id or Name",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    OutlinedTextField(
                        value = recipientIdOrName,
                        onValueChange = { recipientIdOrName = it },
                        label = { Text("Enter Id or Name") },
                        placeholder = { Text("Please enter id or name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.dark_blue),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = colorResource(id = R.color.dark_blue),
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = colorResource(id = R.color.dark_blue)
                        )
                    )

                    Button(
                        onClick = {
                            val roomId = rooms.firstOrNull { it.id.toString() == recipientIdOrName || it.name == recipientIdOrName }?.id

                            if (recipientIdOrName.isEmpty()) {
                                Toast.makeText(context, "Please enter the recipient's name or ID", Toast.LENGTH_SHORT).show()
                            } else {

                                scope.launch {
                                    sheetStateRooms.hide()
                                    if (isFieldPresentInList(rooms, recipientIdOrName)) {
                                        context.startActivity(RoomDetailActivity.createIntent1(context, true, roomId))
                                    } else {
                                        context.startActivity(RoomDetailActivity.createIntent1(context, false, roomId))
                                    }

                                }


                                // Perform actions for recipientIdOrName

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.dark_blue),
                            contentColor = colorResource(id = R.color.white)
                        ),
                        elevation = ButtonDefaults.elevation(8.dp)
                    ) {
                        Text("Submit")
                    }
                }
            }
        )
        {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("AutomaCrop") },
                        backgroundColor = colorResource(id = R.color.dark_blue),
                        contentColor = colorResource(id = R.color.white),
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(painter = painterResource(id = R.drawable.back_iv), contentDescription = "Back Press" )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch { sheetStateRooms.show() }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.apartment),
                                    contentDescription = "Send Email",
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )
                            }
                            IconButton(onClick = {
                                scope.launch { sheetState.show() }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.mail),
                                    contentDescription = "Send Email",
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )
                            }
                            IconButton(onClick = { context.startActivity(googleIntent) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.social),
                                    contentDescription = "Send Email",
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                if (rooms.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text(
                            text = "No Rooms Found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.h5
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(rooms, key = { it.id }) { room ->
                            RoomItem(
                                room = room,
                            )
                        }
                    }
                }
            }
        }
    }
}
fun isFieldPresentInList(rooms: List<RoomDto>, fieldValue: String): Boolean {
    for (room in rooms) {
        if (room.id.toString() == fieldValue || room.name == fieldValue) {
            return true
        }
    }
    return false
}
fun sendEmail(context: android.content.Context, recipient: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, "AutomaCrop Login")
        putExtra(Intent.EXTRA_TEXT, "You have been successfully logged in!")
        setPackage("com.google.android.gm")
    }

    try {
        context.startActivity(emailIntent)
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(context, "Gmail is not installed.", Toast.LENGTH_SHORT).show()
    }
}
@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = 3.dp,
        border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_blue))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Target temperature: ${room.targetTemperature}°C",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${room.currentTemperature}°",
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_blue)
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

