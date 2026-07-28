package cn.itcast.ai_account_book.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

actual object UserStore {
  private val KEY_NAME = stringPreferencesKey("user_name")

  private val dataStore: DataStore<Preferences> by lazy {
    val dir = File(System.getProperty("user.home"), ".ai_account_book")
    dir.mkdirs()
    PreferenceDataStoreFactory.create(
      produceFile = { File(dir, "user_prefs.preferences_pb") }
    )
  }

  actual fun init(context: Any?) { /* JVM侧lazy初始化, 无需context */ }

  actual val userNameFlow: Flow<String?>
    get() = dataStore.data.map { it[KEY_NAME] }

  actual suspend fun saveUserName(name: String) {
    dataStore.edit { it[KEY_NAME] = name }
  }

  actual suspend fun getUserName(): String? {
    return dataStore.data.map { it[KEY_NAME] }.first()
  }

  actual suspend fun deleteUserName() {
    dataStore.edit { it.remove(KEY_NAME) }
  }
}
