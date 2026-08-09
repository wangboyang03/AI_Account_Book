package cn.itcast.ai_account_book.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_account_book.db.Database
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn

class AddTransactionViewModel(
  private val editTransaction: TransactionItem? = null
) : ViewModel() {
  var type by mutableStateOf(editTransaction?.type ?: "expense")
    private set
  var amount by mutableStateOf(editTransaction?.amount?.toString() ?: "")
    private set
  var note by mutableStateOf(editTransaction?.note ?: "")
    private set
  var selectedCategory by mutableStateOf(
    editTransaction?.let { tx ->
      val cats = if (tx.type == "expense") expenseCategories else incomeCategories
      cats.indexOfFirst { it.name == tx.category }.coerceAtLeast(0)
    } ?: 0
  )
    private set
  var dateMs by mutableStateOf(
    editTransaction?.date ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
      .atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
  )

  val categories get() = if (type == "expense") expenseCategories else incomeCategories

  fun updateType(t: String) {
    type = t
    selectedCategory = 0
  }
  fun updateAmount(a: String) { amount = a }
  fun updateNote(n: String) { note = n }
  fun updateCategory(i: Int) { selectedCategory = i }
  fun updateDate(ms: Long) { dateMs = ms }

  fun save(onDone: () -> Unit) {
    val amt = amount.toDoubleOrNull()
    if (amt == null || amt <= 0) return
    val cat = categories[selectedCategory].name
    viewModelScope.launch {
      if (editTransaction != null) {
        Database.db.transactionQueries.update(
          amount = amt, type = type, category = cat,
          note = note, date = dateMs,
          id = editTransaction.id
        )
      } else {
        Database.db.transactionQueries.insert(
          amount = amt, type = type, category = cat,
          note = note, date = dateMs,
          created_at = Clock.System.now().toEpochMilliseconds()
        )
      }
      onDone()
    }
  }
}
