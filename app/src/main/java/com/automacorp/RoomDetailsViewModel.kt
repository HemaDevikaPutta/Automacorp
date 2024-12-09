package com.automacorp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomDetailViewModel : ViewModel() {
    data class RoomDetailState(
        val room: RoomDto? = null,
        val error: String? = null,
        val isLoading: Boolean = false,
        val isUpdating: Boolean = false
    )

    private val _roomState = MutableStateFlow(RoomDetailState())
    val roomState = _roomState.asStateFlow()

    fun loadRoomDetails(roomId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _roomState.value = _roomState.value.copy(isLoading = true)
            runCatching {
                ApiServices.roomsApiService.findById(roomId).execute()
            }
                .onSuccess { response ->
                    val room = response.body()
                    _roomState.value = RoomDetailState(room = room)
                }
                .onFailure { error ->
                    _roomState.value = RoomDetailState(
                        error = error.localizedMessage ?: "Unknown error"
                    )
                }
        }
    }
    fun updateRoom(roomId: Long, roomDetails: RoomCommandDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _roomState.value = _roomState.value.copy(isUpdating = true)
            runCatching {
                ApiServices.roomsApiService.updateRoom(roomId, roomDetails).execute()
            }
                .onSuccess { response ->
                    val updatedRoom = response.body()
                    _roomState.value = RoomDetailState(room = updatedRoom)
                }
                .onFailure { error ->
                    _roomState.value = _roomState.value.copy(
                        error = error.localizedMessage ?: "Update failed",
                        isUpdating = false
                    )
                }
        }
    }
}

