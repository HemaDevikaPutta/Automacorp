package com.automacorp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    data class RoomListState(
        val rooms: List<RoomDto> = emptyList(),
        val error: String? = null
    )

    val roomsState = MutableStateFlow(RoomListState())

    fun findAll() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.findAll().execute()
            }
                .onSuccess { response ->
                    val rooms = response.body() ?: emptyList()
                    roomsState.value = RoomListState(rooms)
                }
                .onFailure { error ->
                    error.printStackTrace()
                    roomsState.value = RoomListState(
                        emptyList(),
                        error.stackTraceToString()
                    )
                }
        }
    }
}