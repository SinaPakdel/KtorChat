package com.sina.testcht

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sina.testcht.socket.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebSocketViewModel @Inject constructor(private val repository: WebSocketRepository) :
    ViewModel() {
    private val _receivedMessage = MutableLiveData<String>()
    val receivedMessage: LiveData<String> get() = _receivedMessage

    fun connectToWebSocket(url: String) {
        viewModelScope.launch {
            repository.connectToWebSocket(url) { message ->
                _receivedMessage.postValue(message)
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            repository.sendMessage(message)
        }
    }

    fun disconnectWebSocket() {
        viewModelScope.launch {
            repository.disconnectWebSocket()
        }
    }
}