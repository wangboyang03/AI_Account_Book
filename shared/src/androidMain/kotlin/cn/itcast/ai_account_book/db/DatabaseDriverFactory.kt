package cn.itcast.ai_account_book.db

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import android.content.Context

actual class DatabaseDriverFactory {
  actual fun createDriver(): app.cash.sqldelight.db.SqlDriver {
    val ctx = App.context
    return AndroidSqliteDriver(
      schema = AppDatabase.Schema,
      context = ctx,
      name = "ai_account_book.db"
    )
  }
}

object App {
  lateinit var context: Context
    private set

  fun init(context: Context) {
    this.context = context
  }
}
