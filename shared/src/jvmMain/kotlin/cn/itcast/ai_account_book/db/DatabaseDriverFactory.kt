package cn.itcast.ai_account_book.db

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
  actual fun createDriver(): app.cash.sqldelight.db.SqlDriver {
    val dir = File(System.getProperty("user.home"), ".ai_account_book")
    if (!dir.exists()) dir.mkdirs()
    val dbPath = File(dir, "ai_account_book.db").absolutePath
    val driver = JdbcSqliteDriver("jdbc:sqlite:$dbPath")
    AppDatabase.Schema.create(driver)
    return driver
  }
}
