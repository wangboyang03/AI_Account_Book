package cn.itcast.ai_account_book.data

import kotlinx.coroutines.flow.Flow

expect object UserStore {
  fun init(context: Any?)
  val userNameFlow: Flow<String?>
  suspend fun saveUserName(name: String)
  suspend fun getUserName(): String?
  suspend fun deleteUserName()
}
