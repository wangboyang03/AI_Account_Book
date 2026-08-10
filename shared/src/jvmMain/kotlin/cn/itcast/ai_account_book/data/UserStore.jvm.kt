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
    try {
      val f = file()
      f.writeText(name)
      cacheFlow.value = name
      println("UserStore: saved userName='$name' to ${f.absolutePath}")
    } catch (e: Exception) {
      println("UserStore: failed to save userName: $e")
      e.printStackTrace()
    }
  }

  actual suspend fun getUserName(): String? {
    ensureInit()
    val f = file()
    return try {
      val value = if (f.exists()) f.readText().trim().ifEmpty { null } else null
      println("UserStore: getUserName -> '$value' (file=${f.absolutePath}, exists=${f.exists()})")
      value
    } catch (e: Exception) {
      println("UserStore: failed to read userName: $e")
      e.printStackTrace()
      null
    }
  }

  actual suspend fun deleteUserName() {
    file().delete()
    cacheFlow.value = null
  }
}
