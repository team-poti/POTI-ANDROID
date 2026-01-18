package com.poti.android.core.network.model

sealed class NetworkError(override val message: String?) : Throwable(message) {
    data class BadRequest(val code: Int?, val serverMsg: String) : NetworkError(serverMsg)

    data class Unauthorized(val serverMsg: String) : NetworkError(serverMsg)

    data class Forbidden(val serverMsg: String) : NetworkError(serverMsg)

    data class NotFound(val serverMsg: String) : NetworkError(serverMsg)

    data class Conflict(val serverMsg: String) : NetworkError(serverMsg)

    data class ServerError(val serverMsg: String) : NetworkError(serverMsg)

    object NetworkConnection : NetworkError("인터넷 연결을 확인해주세요.") {
        private fun readResolve(): Any = NetworkConnection
    }

    object Unknown : NetworkError("알 수 없는 에러가 발생했습니다.") {
        private fun readResolve(): Any = Unknown
    }
}
