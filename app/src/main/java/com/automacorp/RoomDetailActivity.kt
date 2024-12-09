package com.automacorp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class RoomDetailActivity : ComponentActivity() {
    companion object {
        private const val EXTRA_ROOM_ID = "room_id"
        private const val EXTRA_ROOM_ID_Name = "isPresentNameorId"

        fun createIntent(context: Context, roomId: Long): Intent {
            return Intent(context, RoomDetailActivity::class.java).apply {
                putExtra(EXTRA_ROOM_ID, roomId)
            }
        }
        fun createIntent1(context: Context, isPresent: Boolean, roomId: Long?): Intent {
            return Intent(context, RoomDetailActivity::class.java).apply {
                putExtra(EXTRA_ROOM_ID, roomId)
                putExtra(EXTRA_ROOM_ID_Name, isPresent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getLongExtra(EXTRA_ROOM_ID, -1)
        val isPresent = intent.getBooleanExtra(EXTRA_ROOM_ID_Name,false)
        val viewModel: RoomDetailViewModel by viewModels()

        setContent {
            val isRoomPresent = remember { mutableStateOf(isPresent) }

            RoomDetailScreen(
                roomId = roomId,
                roomIdOrName = isRoomPresent.value,
                viewModel = viewModel,
                navigateBack = { finish() }
            )
        }
    }
}
