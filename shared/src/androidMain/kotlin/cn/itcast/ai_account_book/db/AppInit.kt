package cn.itcast.ai_account_book.db

import android.content.Context

actual fun initDatabase(context: Any?) {
  App.init(context as Context)
}
