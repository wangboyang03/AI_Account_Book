package cn.itcast.ai_account_book.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_account_book.data.UserStore
import cn.itcast.ai_account_book.db.Database
import cn.itcast.ai_account_book.transaction.TransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState

  private val poems = listOf(
    "山重水复疑无路，柳暗花明又一村",
    "长风破浪会有时，直挂云帆济沧海",
    "不畏浮云遮望眼，自缘身在最高层",
    "千磨万击还坚劲，任尔东西南北风",
    "人生自古谁无死，留取丹心照汗青",
    "会当凌绝顶，一览众山小",
    "路漫漫其修远兮，吾将上下而求索",
    "行到水穷处，坐看云起时",
    "天生我材必有用，千金散尽还复来",
    "沉舟侧畔千帆过，病树前头万木春"
  )
  private val poem = poems.random()

  init {
    loadData()
  }

  fun loadData() {
    viewModelScope.launch {
      try {
        val name = UserStore.getUserName() ?: ""
        // One-time migration: claim transactions created before per-user isolation.
        if (name.isNotBlank()) {
          Database.db.transactionQueries.assignOrphans(name)
        }
        val entities = Database.db.transactionQueries.selectAll(name).executeAsList()
        val items = entities.map {
          TransactionItem(it.id, it.amount, it.type, it.category, it.note, it.date, it.created_at)
        }
        val income = items.filter { it.type == "income" }.sumOf { it.amount }
        val expense = items.filter { it.type == "expense" }.sumOf { it.amount }
        val balance = income - expense
        _uiState.value = HomeUiState(
          userName = name,
          poem = poem,
          cards = listOf(
            OverviewCard("余额", "¥${"%.2f".format(balance)}"),
            OverviewCard("收入", "¥${"%.2f".format(income)}"),
            OverviewCard("支出", "¥${"%.2f".format(expense)}")
          ),
          transactions = items
        )
      } catch (e: Exception) {
        println("loadData error: $e")
        e.printStackTrace()
      }
    }
  }

  fun deleteTransaction(id: Long) {
    viewModelScope.launch {
      Database.db.transactionQueries.delete(id)
      loadData()
    }
  }
}
