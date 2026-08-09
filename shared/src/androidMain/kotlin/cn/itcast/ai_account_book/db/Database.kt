package cn.itcast.ai_account_book.db

actual object Database {
  actual val db: AppDatabase by lazy {
    val driver = DatabaseDriverFactory().createDriver()
    AppDatabase(driver)
  }
}
