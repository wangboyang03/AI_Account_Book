package cn.itcast.ai_account_book.network

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

expect fun createPlatformHttpClient(): HttpClient

sealed class ApiResult<out T> {
  data class Success<T>(val data: T) : ApiResult<T>()
  data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
}
