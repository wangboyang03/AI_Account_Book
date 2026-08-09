package cn.itcast.ai_account_book.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

actual object UserStore {
  private val cacheFlow = MutableStateFlow<String?>(null)
  private var initialized = false

  private fun file(): File {
    val dir = File(System.getProperty("user.home"), ".ai_account_book")
    dir.mkdirs()
    return File(dir, "user_name.txt")
  }

  private fun ensureInit() {
    if (!initialized) {
      initialized = true
      val f = file()
      if (f.exists()) {
        val saved = f.readText().trim()
        if (saved.isNotEmpty()) cacheFlow.value = saved
      }
    }
  }

  actual fun init(context: Any?) {
    ensureInit()
  }

  actual val userNameFlow: Flow<String?>
    get() {
      ensureInit()
      return cacheFlow
    }

  actual suspend fun saveUserName(name: String) {
    file().writeText(name)
    cacheFlow.value = name
  }

  actual suspend fun getUserName(): String? {
    ensureInit()
    val f = file()
    return if (f.exists()) f.readText().trim().ifEmpty { null } else null
  }

  actual suspend fun deleteUserName() {
    file().delete()
    cacheFlow.value = null
  }
}
