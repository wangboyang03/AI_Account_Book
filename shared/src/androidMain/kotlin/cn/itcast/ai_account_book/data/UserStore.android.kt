package cn.itcast.ai_account_book.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

actual object UserStore {
  private lateinit var appContext: Context
  private val cacheFlow = MutableStateFlow<String?>(null)

  private fun file(): File = File(appContext.filesDir, "user_name.txt")

  actual fun init(context: Any?) {
    appContext = context as Context
    val f = file()
    if (f.exists()) {
      val saved = f.readText().trim()
      if (saved.isNotEmpty()) cacheFlow.value = saved
    }
  }

  actual val userNameFlow: Flow<String?>
    get() = cacheFlow

  actual suspend fun saveUserName(name: String) {
    file().writeText(name)
    cacheFlow.value = name
  }

  actual suspend fun getUserName(): String? {
    val f = file()
    return if (f.exists()) f.readText().trim().ifEmpty { null } else null
  }

  actual suspend fun deleteUserName() {
    file().delete()
    cacheFlow.value = null
  }
}
