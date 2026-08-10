package cn.itcast.ai_account_book

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cn.itcast.ai_account_book.data.UserStore
import cn.itcast.ai_account_book.db.initDatabase
import cn.itcast.ai_account_book.di.appModule
import cn.itcast.ai_account_book.home.HomeScreen
import cn.itcast.ai_account_book.onboarding.OnboardingScreen
import org.koin.core.context.startKoin

object App {
  private var koinStarted = false

  fun init(context: Any?) {
    if (!koinStarted) {
      initDatabase(context)
      startKoin { modules(appModule) }
      koinStarted = true
    }
  }
}

@Composable
fun App() {
  MaterialTheme {
    var route by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
      try {
        val name = UserStore.getUserName()
        println("App startup: read userName='$name'")
        route = if (name.isNullOrBlank()) "onboarding" else "home"
      } catch (e: Exception) {
        println("App startup: failed to read userName: $e")
        e.printStackTrace()
        route = "onboarding"
      }
    }
    when (route) {
      "onboarding" -> OnboardingScreen(onFinished = { route = "home" })
      "home" -> HomeScreen()
    }
  }
}
