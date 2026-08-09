package cn.itcast.ai_account_book.db

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
  fun createDriver(): SqlDriver
}
