package cn.itcast.ai_account_book

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.itcast.ai_account_book.data.UserStore

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    UserStore.init(this)
    App.init(this)

    setContent {
      App()
    }
  }
}

@Preview @Composable fun AppAndroidPreview() {
  App()
}
