package cn.itcast.ai_account_book.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

actual object UserStore {
  private lateinit var dataStore: DataStore<Preferences>
  private val KEY_NAME = stringPreferencesKey("user_name")

  actual fun init(context: Any?) {
    val ctx = context as Context
    dataStore = PreferenceDataStoreFactory.create(
      produceFile = { ctx.filesDir.resolve("user_prefs.preferences_pb") }
    )
  }

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
