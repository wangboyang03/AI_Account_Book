package cn.itcast.ai_account_book.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_account_book.transaction.AddTransactionScreen
import cn.itcast.ai_account_book.transaction.TransactionItem

object Routes {
  const val HOME = "home"
  const val CHART = "chart"
  const val CALENDAR = "calendar"
  const val SETTINGS = "settings"
}

@Composable
fun HomeScreen() {
  var selectedTab by remember { mutableStateOf(Routes.HOME) }
  var showAdd by remember { mutableStateOf(false) }
  var editingTransaction by remember { mutableStateOf<TransactionItem?>(null) }
  val primary = Color(0xFF7F3DFF)
  val vm = remember { HomeViewModel() }

  if (showAdd || editingTransaction != null) {
    AddTransactionScreen(
      editTransaction = editingTransaction,
      onBack = { showAdd = false; editingTransaction = null },
      onSaved = { vm.loadData(); showAdd = false; editingTransaction = null }
    )
    return
  }

  Scaffold(
    bottomBar = {
      NavigationBar(containerColor = Color.White) {
        listOf(
          Routes.HOME to "首页", Routes.CHART to "图表",
          Routes.CALENDAR to "日历", Routes.SETTINGS to "设置"
        ).forEach { (route, label) ->
          NavigationBarItem(
            selected = selectedTab == route,
            onClick = { selectedTab = route },
            label = { Text(label, fontSize = 11.sp) },
            icon = {},
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = primary, unselectedIconColor = Color(0xFFB0B0C8),
              selectedTextColor = primary, unselectedTextColor = Color(0xFFB0B0C8),
              indicatorColor = Color.Transparent
            )
          )
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAdd = true },
        containerColor = primary, contentColor = Color.White
      ) { Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
    }
  ) { padding ->
    Box(Modifier.padding(padding)) {
      when (selectedTab) {
        Routes.HOME -> HomeTabPage(vm, onEditTransaction = { editingTransaction = it })
        Routes.CHART -> ChartPage(vm)
        Routes.CALENDAR -> {
          Box(Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
            Text("日历视图（开发中）", color = Color(0xFFB0B0C8))
          }
        }
        Routes.SETTINGS -> SettingsPage()
      }
    }
  }
}
