package cn.itcast.ai_account_book.home

data class OverviewCard(val label: String, val display: String)

data class HomeUiState(
  val userName: String = "",
  val poem: String = "",
  val cards: List<OverviewCard> = emptyList(),
  val transactions: List<cn.itcast.ai_account_book.transaction.TransactionItem> = emptyList()
)
