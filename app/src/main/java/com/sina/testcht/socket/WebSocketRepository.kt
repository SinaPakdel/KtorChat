package com.sina.testcht.socket

import javax.inject.Inject

class WebSocketRepository @Inject constructor(private var webSocketClient: WebSocketClient) {
    suspend fun connectToWebSocket(url: String, messageCallback: (String) -> Unit) {
        webSocketClient = WebSocketClient(url, messageCallback)
        webSocketClient.connect()
    }

    suspend fun sendMessage(message: String) {
        webSocketClient.sendMessage(message)
    }

    suspend fun disconnectWebSocket() {
        webSocketClient.disconnect()
    }
}