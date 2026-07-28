package cn.itcast.ai_account_book.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_account_book.data.UserStore
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel : ViewModel() {
  var uiState by mutableStateOf(HomeUiState())
    private set

  init {
    viewModelScope.launch {
      val name = UserStore.getUserName() ?: ""
      uiState = HomeUiState(
        userName = name,
        poem = poems[Random.nextInt(poems.size)],
        cards = listOf(
          OverviewCard("Total Salary", 1800.00),
          OverviewCard("Total Expense", 1800.00),
          OverviewCard("Monthly Expenses", 400.00)
        ),
        transactions = listOf(
          Transaction("1", "Food", "20 Feb 2020", 200.0, 1.0, "Google Pay"),
          Transaction("2", "Uber", "26 Feb 2020", 18.0, 0.8, "Cash"),
          Transaction("3", "Shopping", "13 Mar 2020", 400.0, 0.12, "Paytm")
        )
      )
    }
  }
}
