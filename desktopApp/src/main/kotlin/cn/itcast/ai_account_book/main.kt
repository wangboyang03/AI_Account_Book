package cn.itcast.ai_account_book

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
  App.init(null)
  Window(
    onCloseRequest = ::exitApplication,
    title = "AI_Account_Book",
  ) {
    App()
  }
}
