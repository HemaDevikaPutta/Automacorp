package com.automacorp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: RoomViewModel by viewModels()

        setContent {
            val roomsState by viewModel.roomsState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.findAll()
            }

            if (roomsState.error != null) {
                Log.e("RoomState",roomsState.error.toString())
                Toast.makeText(
                    applicationContext,

                    "Error loading rooms: ${roomsState.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

            RoomListScreen(
                rooms = roomsState.rooms,
                navigateBack = { finish() },
                openRoom = { roomId ->
                    startActivity(RoomDetailActivity.createIntent(this, roomId)) }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val viewModel: RoomViewModel by viewModels()
        viewModel.findAll()

    }
}