package cn.itcast.ai_account_book.db

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import kotlin.getValue

actual object Database {
  actual val db: AppDatabase by lazy {
    val dir = File(System.getProperty("user.home"), ".ai_account_book")
    if (!dir.exists()) dir.mkdirs()
    val path = File(dir, "ai_account_book.db").absolutePath
    val driver = JdbcSqliteDriver("jdbc:sqlite:$path")
    bootstrap(driver)
    AppDatabase(driver)
  }

  // Idempotent schema bootstrap: handles fresh DB, legacy DB without user_name,
  // and repairs the broken user_version tracking left by earlier builds.
  private fun bootstrap(driver: JdbcSqliteDriver) {
    try {
      if (!tableExists(driver, "TransactionEntity")) {
        AppDatabase.Schema.create(driver)
      } else if (!columnExists(driver, "TransactionEntity", "user_name")) {
        driver.execute(
          null,
          "ALTER TABLE TransactionEntity ADD COLUMN user_name TEXT NOT NULL DEFAULT ''",
          0
        )
      }
      driver.execute(null, "PRAGMA user_version = ${AppDatabase.Schema.version}", 0)
    } catch (e: Exception) {
      println("DB bootstrap error: $e")
      e.printStackTrace()
    }
  }

  private fun tableExists(driver: JdbcSqliteDriver, table: String): Boolean {
    val count = driver.executeQuery(
      identifier = null,
      sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='$table'",
      mapper = { cursor ->
        cursor.next()
        QueryResult.Value(cursor.getLong(0) ?: 0L)
      },
      parameters = 0
    ).value
    return count > 0
  }

  private fun columnExists(driver: JdbcSqliteDriver, table: String, column: String): Boolean {
    var found = false
    driver.executeQuery(
      identifier = null,
      sql = "PRAGMA table_info($table)",
      mapper = { cursor ->
        while (cursor.next().value) {
          if (cursor.getString(1) == column) found = true
        }
        QueryResult.Value(Unit)
      },
      parameters = 0
    )
    return found
  }
}
