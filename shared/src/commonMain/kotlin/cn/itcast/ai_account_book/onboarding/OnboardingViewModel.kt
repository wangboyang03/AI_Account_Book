package cn.itcast.ai_account_book.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_account_book.data.UserStore
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
  var currentPage by mutableIntStateOf(0)
    private set
  var name by mutableStateOf("")

  fun onPageChanged(page: Int) {
    currentPage = page
  }

  fun onGetStarted(onDone: () -> Unit) {
    viewModelScope.launch {
      UserStore.saveUserName(name.trim())
      onDone()
    }
  }
}
