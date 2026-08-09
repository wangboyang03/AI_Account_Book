package cn.itcast.ai_account_book.db

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import kotlin.getValue

actual object Database {
  actual val db: AppDatabase by lazy {
    val dir = File(System.getProperty("user.home"), ".ai_account_book")
    if (!dir.exists()) dir.mkdirs()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${File(dir, "ai_account_book.db").absolutePath}")
    AppDatabase.Schema.migrate(driver, 0, AppDatabase.Schema.version)
    AppDatabase(driver)
  }
}
