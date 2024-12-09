package com.automacorp
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.ui.theme.AutomacorpTheme


class HomeActivity : ComponentActivity() {
    companion object {
        const val ROOM_PARAM = "room_param"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.Center

                    )
                    {
                        val context = LocalContext.current // Access the context for Toast


                        Image(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "AutomaCrop Logo", modifier = Modifier.fillMaxWidth())
                        Greeting("Welcome on automacorp the app to manage building windows.")

                        var inputText by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.user_profile),
                                    contentDescription = "User Profile",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            },
                            label = { Text("Enter your Name") },
                            placeholder = { Text("Please enter your name") },
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
                                if (inputText.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Please enter your name",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else {
                                    val intent = Intent(context, RoomListActivity::class.java).apply {
                                        putExtra(ROOM_PARAM, inputText)
                                    }
                                    context.startActivity(intent)




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
                            Text("Submit")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name, textAlign = TextAlign.Center, modifier = Modifier.padding(30.dp), fontWeight = FontWeight.Bold)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutomacorpTheme {
        Greeting("Welcome on automacorp the app to manage building windows")
    }
}
