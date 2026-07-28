package cn.itcast.ai_account_book

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import cn.itcast.ai_account_book.data.UserStore
import cn.itcast.ai_account_book.home.HomeScreen
import cn.itcast.ai_account_book.onboarding.OnboardingScreen

@Composable @Preview fun App() {
  MaterialTheme {
    var route by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
      val name = UserStore.getUserName()
      route = if (name.isNullOrBlank()) "onboarding" else "home"
    }

    when (route) {
      "onboarding" -> OnboardingScreen(onFinished = { route = "home" })
      "home" -> HomeScreen()
    }
  }
}
