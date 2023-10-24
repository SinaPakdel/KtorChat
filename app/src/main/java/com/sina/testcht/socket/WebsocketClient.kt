package com.sina.testcht.socket

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WebSocketClient(
    private val url: String,
    private var messageCallback: (String) -> Unit
) {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var session: WebSocketSession? = null

    suspend fun connect() {
        withContext(Dispatchers.IO) {
            client.ws(method = HttpMethod.Get, host = url) {
                val webSocketSession = this
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()

                    withContext(Dispatchers.Main) {
                        messageCallback(receivedText)
                    }
                }
                webSocketSession.close()
            }
        }
    }

    suspend fun sendMessage(message: String, session: WebSocketSession? = this.session) {
        session?.send(Frame.Text(message))
    }

    suspend fun disconnect(session: WebSocketSession? = this.session) {
        session?.close()
        client.close()
    }
}